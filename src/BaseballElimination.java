import edu.princeton.cs.algs4.In;

import java.util.Iterator;

public class BaseballElimination {
    private int numberOfTeams;
    private int[] win, lose, remain;
    private String[] teamNames;
    private int[][] remainingGames;


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
            for(int j=0;j<numberOfTeams;j++){
                remainingGames[i][j]=in.readInt();
            }
        }
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
        checkTeamArgumentNull(team, "wins");
        int index = findTeamIndex(team);
        return win[index];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeamArgumentNull(team, "losses");
        int index = findTeamIndex(team);
        return lose[index];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeamArgumentNull(team, "remaining");
        int index = findTeamIndex(team);
        return remain[index];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeamArgumentNull(team1, "against 1");
        checkTeamArgumentNull(team2, "against 2");
        int index1 = findTeamIndex(team1);
        int index2 = findTeamIndex(team2);
        return remainingGames[index1][index2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeamArgumentNull(team, "isEliminated");
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeamArgumentNull(team, "certificateOfElimination");
        return null;
    }

    public static void main(String[] args){
        BaseballElimination division = new BaseballElimination(args[0]);
        System.out.println("number: " + division.numberOfTeams());
        for(String name: division.teams()){
            System.out.println("teamName: " + name);
        }
    }

    private void checkTeamArgumentNull(String team, String method) {
        if (team == null) {
            throw new IllegalArgumentException("Argument team should not be null for method: " + method);
        }
    }

    // this could be optimized
    private int findTeamIndex(String teamName){
        for(int i=0;i<numberOfTeams;i++){
            if(teamNames[i].equals(teamName)){
                return i;
            }
        }
        throw new IllegalArgumentException("teamName " + teamName + " is not recorded");
    }
}
