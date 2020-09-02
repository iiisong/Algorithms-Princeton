import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class BaseballElimination {
	private final int numTeams; // number of teams
	private final String[] teamNames; // array of team names; index --> names
	private final HashMap<String, Integer> teamNameIndex; // map names --> index
	private final int[][] stats; // stats

	private boolean[][] elimination;  // if elimination[i][j] is true, then team j eliminated team i
 
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In file = new In(filename);

		numTeams = file.readInt(); // number of teams

		// initialize name containers for conversion between int to string and vice versa
		teamNames = new String[numTeams];
		teamNameIndex = new HashMap<>(numTeams);

		// wins, losses, remaining, games; team id as index
		stats = new int[numTeams][3 + numTeams];

		elimination = new boolean[numTeams][numTeams + 1]; // last spot for elimination status

		// iterate throught teams
		for (int t = 0; t < numTeams; t++) {
			String name = file.readString(); // store names
			teamNames[t] = name;
			teamNameIndex.put(name, t);

			for (int i = 0; i < numTeams + 3; i++) {
				stats[t][i] = file.readInt();
			}
		} 

		for (int t = 0; t < numTeams; t++) {
			Iterable<Integer> elimList = eliminationHelper(t);

			if (elimList == null) {
				continue;
			}

			for (Integer idx : elimList) {
				elimination[t][numTeams] = true;
				elimination[t][idx] = true;
			}
		}
	}

	// number of teams
	public int numberOfTeams() {
		return numTeams;
	}

	// all teams
	public Iterable<String> teams() {
		return new LinkedList<String>(Arrays.asList(teamNames));
	}

	// number of wins for given team
	public int wins(String team) {
		return stats[teamToIndex(team)][0];
	}

	// number of losses for given team
	public int losses(String team) {
		return stats[teamToIndex(team)][1];
	}

	// number of remaining games for given team
	public int remaining(String team) {
		return stats[teamToIndex(team)][2];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		int t1 = teamToIndex(team1);
		int t2 = teamToIndex(team2);

		return stats[t1][t2 + 3];
	}

	private Iterable<Integer> eliminationHelper(int t) {
		// max wins possible by target team
		int mwins = stats[t][0] + stats[t][2];

		// num of possible games (without the target team)
		int possGames = numTeams * (numTeams - 1) / 2;

		// all possible combo of games  + teams + s and t nodes
			// starting node is second to last node, termination node is last node
		int nNodes = possGames + numTeams + 2;
		FlowNetwork fn = new FlowNetwork(nNodes);

		int count = numTeams;

		for (int i = 0; i < numTeams; i++) {
			// skip target team
			if (i == t) {
				continue;
			}

			for (int j = i + 1; j < numTeams; j++) {
				// skip target team
				if (j == t) {
					continue;
				}

				// numbers of games between the two teamss
				int cap = stats[i][j + 3];

				// starting node to game
				fn.addEdge(new FlowEdge(nNodes - 2, count, cap));

				// game to the two teams
				fn.addEdge(new FlowEdge(count, i, Double.POSITIVE_INFINITY));
				fn.addEdge(new FlowEdge(count, j, Double.POSITIVE_INFINITY));
				count++;
			}

			// term node is the last node
			int diff = mwins - stats[i][0];

			// already eliminated, impossible to catch up
			if (diff < 0) {
				elimination[t][i] = true;
				elimination[t][numTeams] = true;
				return null;
			}

			fn.addEdge(new FlowEdge(i, nNodes - 1, diff));
		}

		FordFulkerson ff = new FordFulkerson(fn, nNodes - 2, nNodes - 1);

		LinkedList<Integer> result = new LinkedList<>();

		// if no items are eliminated, no items would return true in inCut()
		for (int i = 0; i < numTeams; i++) {
			if (i == t) {
				continue;
			}

			// correct team for offset
			if (ff.inCut(i)) {
				result.add(i);
			}
		}

		return result;
	}

	// is given team eliminated?
	public boolean isEliminated(String team) {
		return elimination[teamToIndex(team)][numTeams];
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		int t = teamToIndex(team);

		LinkedList<String> result = new LinkedList<>();

    if (!elimination[t][numTeams]) {
      return null;
    }

		for (int i = 0; i < numTeams; i++) {
			if (elimination[t][i]) {
				result.add(teamNames[i]);
			}
		}

		return result;
	}

  private int teamToIndex(String team) {
    Integer i = teamNameIndex.get(team);
    if (i == null) {
      throw new IllegalArgumentException();
    }

    return (int) i;
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
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}