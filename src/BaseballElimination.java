import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);

    }

    // number of teams
    public int numberOfTeams() {
        return -1;
    }

    // all teams
    public Iterable<String> teams() {
        return null;
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeamArgumentNull(team, "wins");
        return -1;
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeamArgumentNull(team, "losses");
        return -1;
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeamArgumentNull(team, "remaining");
        return -1;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeamArgumentNull(team1, "against 1");
        checkTeamArgumentNull(team2, "against 2");
        return -1;
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

    private void checkTeamArgumentNull(String team, String method) {
        if (team == null) {
            throw new IllegalArgumentException("Argument team should not be null for method: " + method);
        }
    }

}
