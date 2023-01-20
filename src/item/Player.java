package item;

import java.util.HashSet;

public class Player implements Comparable<Player> {

    private String name1;
    private String name2;
    private String eid;
    private HashSet<Match> matches;
    private double rating;

    public Player(String name, String studentID, double rate) {
        String[] names = name.split(" ");
        if (names.length > 1)
            name2 = names[1];
        name1 = names[0];
        eid = studentID;
        rating = rate;
        matches = new HashSet<>();
    }

    public Player(String firstname, String lastname, String studentID,
            double rate) {
        name1 = firstname;
        name2 = lastname;
        eid = studentID;

        rating = rate;
        matches = new HashSet<>();
    }

    public void edit(String name, String studentID, double rate) {
        String[] names = name.split(" ");
        if (names.length > 1)
            name2 = names[1];
        name1 = names[0];
        eid = studentID;
        rating = rate;
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

    /**
     * 
     * @param match
     */
    public void addMatch(Match match) {
        matches.add(match);
    }

    /**
     * Changes the Player's rating value by the given amount
     * @param change - The amount to change by
     */
    public void adjustRating(double change) {
        rating += change;
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
                + rating + "\n";

        // Note: Matches are stored on a player's file,
        // but aren't relevant for loading
        for (Match m : matches) {
            result += m.id() + "\n";
        }
        result += "END";

        return result;
    }
}
