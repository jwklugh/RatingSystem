package item;

import java.util.Comparator;
import java.util.Date;

import run.Runner;

public class Match implements Comparable<Match> {

    private long id; // The id number of this match
    private Player p1; // The first player in this match
    private Player p2; // The second player in this match
    private int status; // Match winner: 0 -> unplayed, 1 -> p1, 2 -> p2
    private Date timestamp; // The timestamp at which this match is recorded

    /**
     * Primary constructor
     * @param player1 - First player in the match
     * @param player2 - Second player in the match
     * @param currStatus - winner or unplayed status
     */
    public Match(Player player1, Player player2) {
        timestamp = new Date();
        p1 = player1;
        p2 = player2;
        id = generateId();
        status = 0;
    }

    /**
     * Load constructor
     * @param player1 - First player in the match
     * @param player2 - Second player in the match
     * @param currStatus - winner or unplayed status
     * @param loadedTimestamp - timestamp of this match's creation
     */
    public Match(String player1, String player2, String currStatus,
            String loadedTimestamp) {
        try {
            timestamp = new Date(Long.parseLong(loadedTimestamp));
            p1 = Runner.getRunner().getPlayer(player1);
            p2 = Runner.getRunner().getPlayer(player2);
            id = generateId();
            status = Integer.parseInt(currStatus);
        } catch (NumberFormatException e) {
            System.out.println("Error: Non-number data in number expected "
                    + "field.\nRecommend checking saved data for errors and/or "
                    + "corruption...\n" + e.getMessage());
        }
    }

    public long id() {
        return id;
    }

    public Player player1() {
        return p1;
    }

    public Player player2() {
        return p2;
    }

    /**
     * Gets the status of this match
     * @return 0 if unplayed, 1 if p1 won, 2 if p2 won
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status of this match
     * @param winner - true -> p1 won, false -> p2 won
     */
    public void setResult(boolean player1Won) {
        status = player1Won ? 1 : 2;
    }

    /**
     * Sets the player specified by player1 if the match has not yet been played
     * @param p - The player to set to
     * @param player1 - Whether to set to player 1 or 2
     */
    public boolean setPlayer(Player p, boolean player1) {
        if (status == 0) {
            if(player1) {
                p1 = p;
            } else {
                p2 = p;
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if another Player is the same as this Player
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Match)
            return this.id == ((Match) other).id;
        return false;
    }

    /**
     * Compares this Match to another Match, returning > 0 if this Match has
     * a later timestamp
     */
    @Override
    public int compareTo(Match o) {
        return this.timestamp.compareTo(o.timestamp);
    }

    @Override
    public String toString() {
        return "<html>"
                + p1.toString()
                + "<br>" + "vs"
                + "<br>" + p2.toString()
                + "<br>" + timestamp.toString();
    }

    public String dataString() {
        return "START\n"
                + p1.getEid() + "\n"
                + p2.getEid() + "\n"
                + status + "\n"
                + timestamp.getTime() + "\n"
                + "END";
    }

    private long generateId() {
        return (""
                + timestamp.getTime() + ""
                + p1.getEid()
                + p2.getEid()).hashCode();
    }

    /**
     * Comparator for sorting Matches newest first
     * @author Jason
     */
    public static class InverseMatchComparator implements Comparator<Match> {
        @Override
        public int compare(Match arg0, Match arg1) {
            return -arg0.compareTo(arg1);
        }
    }
}
