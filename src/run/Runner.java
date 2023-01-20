package run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import fileio.MatchIO;
import fileio.PlayerIO;
import item.Match;
import item.Player;

/**
 * 
 * @author Jason
 *
 */
public class Runner {

    private static Runner theRunner;
    private HashSet<Player> attendence;
    private HashMap<String, Player> players;
    private HashMap<Long, Match> matches;

    /**
     * Private constructor to designate Singleton status
     */
    private Runner() {}

    /**
     * Initial loading of the Runner Object
     */
    private void populateRunner() {
        players =  PlayerIO.loadAllPlayers();
        matches = MatchIO.loadAllMatches();
        attendence = new HashSet<>();
        populatePlayerMatches();
    }

    /**
     * Gets the Singleton Runner Object
     * @return The Singleton Runner
     */
    public static Runner getRunner() {
        if (theRunner == null) {
            theRunner = new Runner();
            theRunner.populateRunner();
        }
        return theRunner;
    }

    /**
     * Populates the matches of players on initial load
     */
    public void populatePlayerMatches() {
        for (Match match : matches.values()) {
            match.player1().addMatch(match);
            match.player2().addMatch(match);
        }
    }

    /**
     * Gets a specific Player by their eid
     * @param eid - The eid of the player to get
     * @return The requested Player
     */
    public Player getPlayer(String eid) {
        Player p = players.get(eid);
        return p;
    }

    /**
     * Gets all players in the rankings
     * @return All players in the rankings
     */
    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    /**
     * Adds a new Player to the rankings
     * @param name - The name of the Player to add
     * @param eid - The eid of the Player to add
     * @return The new Player
     */
    public Player AddPlayer(String name, String eid) {
        Util.debug("Adding Player: " + name + " - " + eid);
        Player newPlayer = new Player(name, eid, 800);
        PlayerIO.savePlayer(newPlayer);
        players.put(newPlayer.getEid(), newPlayer);
        RatingSystemMain.ui.update();

        return newPlayer;
    }

    /**
     * Get all players currently in attendance
     * @return All players currently in attendance
     */
    public Collection<Player> getAttendance() {
        Collection<Player> attendingPlayers = new HashSet<Player>(attendence);
        return attendingPlayers;
    }

    /**
     * Creates a new Match with the given two Players
     * @param p1 - The first Player in the Match
     * @param p2 - The second Player in the Match
     * @return The created match
     */
    public Match createMatch(Player p1, Player p2) {
        Util.debug("Creating match:" + p1.toString() + " vs " + p2.toString());
        Match match = new Match(p1, p2);
        MatchIO.saveMatch(match);
        matches.put(match.id(), match);
        RatingSystemMain.ui.update();

        return match;
    }

    /**
     * Gets all played Matches (Matches with status != 0)
     * @return All played Matches
     */
    public Collection<Match> getPastMatches() {
        Collection<Match> playedMatches = new HashSet<Match>(matches.values());
        playedMatches.removeIf(m -> (m.getStatus() == 0));
        return playedMatches;
    }

    /**
     * Gets all unplayed Matches (Matches with status 0)
     * @return All unplayed Matches
     */
    public Collection<Match> getCurrMatches() {
        Collection<Match> currentMatches = new HashSet<Match>(matches.values());
        currentMatches.removeIf(m -> (m.getStatus() != 0));
        return currentMatches;
    }

    /**
     * Gets all matches in the system, played or not
     * @return All Matches
     */
    public Collection<Match> getAllMatches() {
        return matches.values();
    }

    /**
     * Checks if an eid already exists
     * @param eid - The eid to check
     * @return If a player already had the eid
     */
    public boolean checkEidFree(String eid) {
        return !players.containsKey(eid);
    }

    /**
     * Adds an existing player to the attendance list
     * @param p - The arriving player
     * @return If the player was added to the list
     */
    public boolean playerArrived(Player p) {
        return attendence.add(p);
    }

    /**
     * Removes a Player from the attendance without removing them from the rankings
     * @param p - The player to retire
     * @return If the player was successfully retired
     */
    public boolean retirePlayer(Player p) {
        return attendence.remove(p);
    }

    /**
     * Deletes a Player. TODO delete the player file.
     * @param p - The player to delete
     * @return If the player was successfully deleted
     */
    public boolean deletePlayer(Player p) {
        if(attendence.contains(p))
            if(!attendence.remove(p))
                return false;

        return players.remove(p.getEid(), p);
    }

    /**
     * Edits the information of the given Player. Updates Matches in accordance
     * with the change
     * @param p - The player to update
     * @param name - The name to update to
     * @param eid - The eid to edit to
     * @param rate - The rating to give the Player
     */
    public void editPlayer(Player p, String name, String eid, double rate) {

        p.edit(name, eid, rate);
        PlayerIO.savePlayer(p);
        RatingSystemMain.ui.update();

        // TODO - edit matches to match player id if edited
        for(Match m : matches.values()) {
            if (m.player1().equals(p) || m.player2().equals(p)) {
                MatchIO.saveMatch(m);
            }
        }
    }

    /**
     * Matches a Player with another random player in attendance. If enough
     * player are in attendance, randomness will be weighted to be closer to the
     * given Player
     * @param p - The player to match
     * @return A random player in attendance
     */
    public Player matchPlayer(Player p) {
        HashSet<Player> candidates = new HashSet<>(attendence);

        candidates.remove(p);
        for (Match m : getCurrMatches()) {
            candidates.remove(m.player1());
            candidates.remove(m.player2());
        }

        // If we don't meet some minimum threshold of candidates,
        // just pick one at random
        if (candidates.size() <= 10) {
            return candidates.toArray(new Player[0])
                    [(int) (Math.random() * candidates.size())];
        }

        return weightedPlayerSelector(p, candidates.toArray(new Player[0]));
    }

    /**
     * Selects a random Player from an array of candidates based on the given
     * Player's rating (weights Players closer in rating higher)
     * @param player - The Player to select by
     * @param candidates - The list of candidate Players to select from
     * @return A random Player weighted on the given Player's rating
     */
    Player weightedPlayerSelector(Player player, Player[] candidates) {

        double[] absScores = new double[candidates.length];
        double[] invertedScores = new double[candidates.length];
        double[] resultScores = new double[candidates.length];
        long[] entrees = new long[candidates.length];
        ArrayList<Player> entreeList = new ArrayList<>();

        double max = 0;

        for(int i = 0; i < candidates.length; i++) {
            absScores[i] = Math.abs(player.getRating() -
                    candidates[i].getRating());
            if (absScores[i]>max) {
                max=absScores[i];
            }
        }

        max = max * 2.5;

        double sum = 0;

        for(int i = 0; i < candidates.length; i++) {
            invertedScores[i] = max - absScores[i];
            sum += invertedScores[i];
        }

        for(int i = 0; i < candidates.length; i++) {
            resultScores[i] = invertedScores[i]/sum;
            entrees[i] = Math.round((int)(resultScores[i] * 1000) / 10.0);

            for (int j=0; j<entrees[i]; j++) {
                entreeList.add(candidates[i]);
            }
        }

        return entreeList.get((int) (Math.random() * entreeList.size()));
    }

    /**
     * Sets the result of a Match and changes the rating of Players according
     * to the result
     * @param match - The Match to record
     * @param p1Won - The result of the match (true if player1 won, false if player2 won
     */
    public void recordMatch(Match match, boolean p1Won) {
        double Rat1, Rat2, expectedScore;
        double k = 25;

        match.setResult(p1Won);
        Rat1 = match.player1().getRating();
        Rat2 = match.player2().getRating();
        expectedScore = 1 / (1 + Math.pow(10, (Rat2 - Rat1)/400));

        if(match.getStatus() == 1) {
            match.player1().adjustRating(Rat1 + k * (1 - expectedScore));
            match.player2().adjustRating(Rat2 - k * (1 - expectedScore));
        } else if (match.getStatus() == 2) {
            match.player1().setRating(Rat1 - k * expectedScore);
            match.player2().setRating(Rat2 + k * expectedScore);
        }

        MatchIO.saveMatch(match);
        PlayerIO.savePlayer(match.player1());
        PlayerIO.savePlayer(match.player2());
    }

    public boolean discardMatch(Match match) {
        if (match.getStatus() == 0) {
            return matches.remove(match.id(), match);
        }

        // TODO work out how to retroactively remove a match and adjust ratings
        return false;
    }
}
