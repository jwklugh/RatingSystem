package item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class Player implements Comparable<Player> {

    private String name1;
    private String name2;
    private String eid;
    private HashSet<Match> matches;
    private double rating;
    private boolean confirmed;

    public Player(String name, String studentID, double rate, boolean conf) {
        String[] names = name.split(" ");
        if (names.length > 1)
            name2 = names[1];
        name1 = names[0];
        eid = studentID;
        rating = rate;
        matches = new HashSet<>();
        confirmed = conf;
    }

    public Player(String firstname, String lastname, String studentID,
            double rate, boolean conf) {
        name1 = firstname;
        name2 = lastname;
        eid = studentID;

        rating = rate;
        matches = new HashSet<>();
        confirmed = conf;
    }

    public void edit(String name, String studentID, double rate, boolean conf) {
        String[] names = name.split(" ");
        if (names.length > 1)
            name2 = names[1];
        name1 = names[0];
        eid = studentID;
        rating = rate;
        confirmed = conf;
    }

    public String firstname() {
        return name1;
    }

    public String lastname() {
        return name2;
    }

    public String getEid() {
        return eid;
    }

    public double getRating() {
        return rating;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Adds the given Match the this Players list of matches
     * @param match - The Match to add
     */
    public void addMatch(Match match) {
        matches.add(match);
    }

    /**
     * Gets the number of Matches this Player has played
     * @return The number of matches this player has played
     */
    public int getNumMatchesPlayed() {
        return matches.size();
    }

    /**
     * Changes the Player's rating value by the given amount
     * @param change - The amount to change by
     */
    public void adjustRating(double change) {
        rating += change;
    }

    /**
     * Sets the Player's rating directly to a new value
     * @param newRating - The rating to change to
     */
    public void setRating(double newRating) {
        rating = newRating;
    }

    /**
     * Sets the Player's confirmed value
     * @param conf - The value to set to
     */
    public void setConfirmed(boolean conf) {
        confirmed = conf;
    }

    /**
     * Determines if another Player is the same as this Player
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Player)
            return this.eid == ((Player) other).eid;
        return false;
    }

    /**
     * Compares this player to another player, returning > 0 if this Player has
     * a higher rating
     */
    @Override
    public int compareTo(Player o) {
        double diff = this.rating - o.rating;
        return (int)(diff < 0 ? Math.floor(diff) : Math.ceil(diff));
    }

    /**
     * Returns a String representation of this Player
     */
    @Override
    public String toString() {
        return name1 + " " + name2;
    }

    /**
     * Creates a String for saving player data to file
     * @return A String containing all player data
     */
    public String dataString() {
        String result = "START\n"
                + eid + "\n"
                + name1 + "\n"
                + name2 + "\n"
                + rating + "\n"
                + confirmed + "\n";

        // Note: Matches are stored on a player's file,
        // but aren't relevant for loading
        for (Match m : matches) {
            result += m.id() + "\n";
        }
        result += "END";

        return result;
    }

    /**
     * Comparator for sorting players by name (first then last)
     * @author Jason
     */
    public static class PlayerNameComparator implements Comparator<Player> {
        @Override
        public int compare(Player arg0, Player arg1) {
            int first = arg0.name1.compareTo(arg1.name1);
            return first != 0 ? first : arg0.name2.compareTo(arg1.name2);
        }
    }

    /**
     * Comparator for sorting players by rank
     * @author Jason
     */
    public static class PlayerRankComparator implements Comparator<Player> {
        @Override
        public int compare(Player arg0, Player arg1) {
            double diff = arg1.rating - arg0.rating;
            return (int)(diff < 0 ? Math.floor(diff) : Math.ceil(diff));
        }
    }

    /**
     * Comparator for sorting players by last match played (oldest first)
     * @author Jason
     */
    public static class PlayerMatchComparator implements Comparator<Player> {
        @Override
        public int compare(Player arg0, Player arg1) {

            if(arg0.matches.size() == 0 && arg1.matches.size() == 0)
                return 0;

            if(arg0.matches.size() == 0)
                return -1;

            if(arg1.matches.size() == 0)
                return 1;

            Match match0;
            Match match1;
            ArrayList<Match> matches;

            matches = new ArrayList<>(arg0.matches);
            matches.sort(new Match.InverseMatchComparator());
            match0 = matches.get(0);

            matches = new ArrayList<>(arg1.matches);
            matches.sort(new Match.InverseMatchComparator());
            match1 = matches.get(0);

            return match0.compareTo(match1);
        }
    }
}
