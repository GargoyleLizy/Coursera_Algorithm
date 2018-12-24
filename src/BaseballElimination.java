import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BaseballElimination {
    private final int numberOfTeams;
    private final int[] win, lose, remain;
    private final String[] teamNames;
    private final int[][] remainingGames;

    private HashMap<String, Set<String>> eliminated = new HashMap<>();

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        // read in file for certain format
        In in = new In(filename);
        numberOfTeams = in.readInt();
        win = new int[numberOfTeams];
        lose = new int[numberOfTeams];
        remain = new int[numberOfTeams];
        teamNames = new String[numberOfTeams];
        remainingGames = new int[numberOfTeams][numberOfTeams];
        
        for (int i = 0; i < numberOfTeams; i++) {
            teamNames[i] = in.readString();
            win[i] = in.readInt();
            lose[i] = in.readInt();
            remain[i] = in.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                remainingGames[i][j] = in.readInt();
            }
        }
        // eliminated the trivial
        checkTrivialEliminated();
        checkNonTrivialEliminated();
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return () -> new Iterator<String>() {
            private int next = 0;

            @Override
            public boolean hasNext() {
                return next < teamNames.length;
            }

            @Override
            public String next() {
                String value = teamNames[next];
                next++;
                return value;
            }
        };
    }

    // number of wins for given team
    public int wins(String team) {
        validTeamNameArgument(team, "wins");
        int index = findTeamIndex(team);
        return win[index];
    }

    // number of losses for given team
    public int losses(String team) {
        validTeamNameArgument(team, "losses");
        int index = findTeamIndex(team);
        return lose[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validTeamNameArgument(team, "remaining");
        int index = findTeamIndex(team);
        return remain[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        validTeamNameArgument(team1, "against 1");
        validTeamNameArgument(team2, "against 2");
        int index1 = findTeamIndex(team1);
        int index2 = findTeamIndex(team2);
        return remainingGames[index1][index2];
    }

    // is given team eliminated?
    // TODO this could be optimized if we use a boolean array.
    public boolean isEliminated(String team) {
        validTeamNameArgument(team, "isEliminated");
        return eliminated.get(team) != null;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validTeamNameArgument(team, "certificateOfElimination");
        if (eliminated.get(team) != null) {
            return () -> eliminated.get(team).iterator();
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void validTeamNameArgument(String team, String method){
        validTeamArgumentNull(team,method);
        validTeamNameArgumentLegal(team);
    }

    private void validTeamArgumentNull(String team, String method) {
        if (team == null) {
            throw new IllegalArgumentException("Argument team should not be null for method: " + method);
        }
    }

    private void validTeamNameArgumentLegal(String targetTeam){
        for(String teamName:teamNames){
            if(teamName.equals(targetTeam)){
                return;
            }
        }
        throw new IllegalArgumentException("Team " + targetTeam + " does not exist");
    }

    // this could be optimized
    private int findTeamIndex(String teamName) {
        for (int i = 0; i < numberOfTeams; i++) {
            if (teamNames[i].equals(teamName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("teamName " + teamName + " is not recorded");
    }

    // We eliminated the teams that has no chance to be first.
    private void checkTrivialEliminated() {
        int most = 0;
        int mostIndex = 0;
        for (int i = 0; i < numberOfTeams; i++) {
            if (most < win[i]) {
                most = win[i];
                mostIndex = i;
            }
        }
        for (int i = 0; i < numberOfTeams; i++) {
            if (win[i] + remain[i] < most) {
                Set<String> correspondingSet = new TreeSet<>();
                correspondingSet.add(teamNames[mostIndex]);
                eliminated.put(teamNames[i], correspondingSet);
            }
        }
    }

    // Check each team, if it is nontrivial eliminated, put it into the data set.
    private void checkNonTrivialEliminated() {
        if (numberOfTeams <= 2) {
            // skip if there is not enough teams.
            return;
        }
        for (int i = 0; i < numberOfTeams; i++) {
            if (!isEliminated(teamNames[i])) {
                Set<String> eliminatedSubset = checkTeamNonTrivialEliminated(i);
                if (!eliminatedSubset.isEmpty()) {
                    eliminated.put(teamNames[i], eliminatedSubset);
                }
            }
        }
    }

    // Check team is nontrivial eliminated.
    private Set<String> checkTeamNonTrivialEliminated(int teamIndex) {
        FlowNetwork flowNetwork = constructFlowNetwork(teamIndex);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
        // we check which vertex is not on source side,
        // if it is not eliminated, then all the game vertex should be on the source side.
        Set<String> isOnSourceSide = new TreeSet<>();
        for (int i = 0; i < numberOfTeams - 1; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (i != teamIndex && j != teamIndex) {
                    int gamesBetijVertexIndex = convertGameToVertexIndex(i, j, teamIndex);
                    // if the game vertex is on source side, then it is not full.
                    if (fordFulkerson.inCut(gamesBetijVertexIndex)) {
                        isOnSourceSide.add(teamNames[i]);
                        isOnSourceSide.add(teamNames[j]);
                    }
                }
            }
        }
        return isOnSourceSide;
    }

    private FlowNetwork constructFlowNetwork(int targetTeamIndex) {
        int gameVs = (numberOfTeams - 1) * (numberOfTeams - 2) / 2;
        // s -> games -> teams -> t
        FlowNetwork flowNetwork = new FlowNetwork(1 + gameVs + numberOfTeams - 1 + 1);
        // start vertex is 0, and the next vertex are game
        for (int i = 0; i < numberOfTeams - 1; i++) {
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (i != targetTeamIndex && j != targetTeamIndex) {
                    int gamesBetij = remainingGames[i][j];
                    int gamesBetijVertexIndex = convertGameToVertexIndex(i, j, targetTeamIndex);
                    flowNetwork.addEdge(new FlowEdge(0, gamesBetijVertexIndex, gamesBetij));
                    // then games link to team
                    flowNetwork.addEdge(new FlowEdge(gamesBetijVertexIndex, convertTeamIndexToVertexIndex(i, targetTeamIndex), Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gamesBetijVertexIndex, convertTeamIndexToVertexIndex(j, targetTeamIndex), Double.POSITIVE_INFINITY));
                }
            }
        }
        // then link all team to t
        int tIndex = flowNetwork.V() - 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i != targetTeamIndex) {
                int capacity = win[targetTeamIndex] + remain[targetTeamIndex] - win[i];
                int teamVertexIndex = convertTeamIndexToVertexIndex(i, targetTeamIndex);
                flowNetwork.addEdge(new FlowEdge(teamVertexIndex, tIndex, capacity));
            }
        }
        return flowNetwork;
    }

    private int convertGameToVertexIndex(int i, int j, int removedTeamIndex) {
        if (i >= removedTeamIndex) {
            i = i - 1;
        }
        if (j >= removedTeamIndex) {
            j = j - 1;
        }
        int count = 0;
        int restTeamNumber = numberOfTeams - 1;
        for (int k = 0; k < i; k++) {
            count = count + (restTeamNumber - 1 - k);
        }
        count += (j - i);
        return count;
    }

    private int convertTeamIndexToVertexIndex(int i, int removedTeamIndex) {
        if (i >= removedTeamIndex) {
            i -= 1;
        }
        return 1 + (numberOfTeams - 1) * (numberOfTeams - 2) / 2 + i;
    }
}
