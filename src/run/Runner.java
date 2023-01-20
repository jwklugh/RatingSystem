package run;

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
     * 
     */
    private Runner() {

    }

    private void populateRunner() {
        players =  PlayerIO.loadAllPlayers();
        matches = MatchIO.loadAllMatches();
        attendence = new HashSet<>();
        populatePlayerMatches();
    }

    /**
     * 
     * @return
     */
    public static Runner getRunner() {
        if (theRunner == null) {
            theRunner = new Runner();
            theRunner.populateRunner();
        }
        return theRunner;
    }

    public void populatePlayerMatches() {
        for (Match match : matches.values()) {
            match.player1().addMatch(match);
            match.player2().addMatch(match);
        }
    }

    /**
     * 
     * @param eid
     * @return
     */
    public Player getPlayer(String eid) {
        Player p = players.get(eid);
        return p;
    }

    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    public Player AddPlayer(String name, String eid) {
        Util.debug("Adding Player: " + name + " - " + eid);
        Player newPlayer = new Player(name, eid, 600);
        PlayerIO.savePlayer(newPlayer);
        players.put(newPlayer.getEid(), newPlayer);
        RatingSystemMain.ui.update();

        return newPlayer;
    }

    public Collection<Player> getAttendance() {
        Collection<Player> attendingPlayers = new HashSet<Player>(attendence);
        return attendingPlayers;
    }

    public Match createMatch(Player p1, Player p2) {
        Util.debug("Creating match:" + p1.toString() + " vs " + p2.toString());
        Match match = new Match(p1, p2);
        MatchIO.saveMatch(match);
        matches.put(match.id(), match);
        RatingSystemMain.ui.update();

        return match;
    }

    public Collection<Match> getPastMatches() {
        Collection<Match> playedMatches = new HashSet<Match>(matches.values());
        playedMatches.removeIf(m -> (m.getStatus() == 0));
        return playedMatches;
    }

    public Collection<Match> getCurrMatches() {
        Collection<Match> currentMatches = new HashSet<Match>(matches.values());
        currentMatches.removeIf(m -> (m.getStatus() != 0));
        return currentMatches;
    }

    public Collection<Match> getAllMatches() {
        return matches.values();
    }

    public boolean checkEidFree(String eid) {
        return !players.containsKey(eid);
    }

    public boolean playerArrived(Player p) {
        return attendence.add(p);
    }

    public boolean retirePlayer(Player p) {
        return attendence.remove(p);
    }

    public boolean deletePlayer(Player p) {
        if(attendence.contains(p))
            if(!attendence.remove(p))
                return false;

        return players.remove(p.getEid(), p);
    }

    public void editPlayer(Player p, String name, String eid, double rate) {

        p.edit(name, eid, rate);
        PlayerIO.savePlayer(p);
        RatingSystemMain.ui.update();

        // TODO - edit matches to match player id if edited
        for(Match m : matches.values()) {

        }
    }

    public Player matchPlayer(Player p) {
        HashSet<Player> candidates = new HashSet<>(attendence);

        candidates.remove(p);
        for (Match m : getCurrMatches()) {
            candidates.remove(m.player1());
            candidates.remove(m.player2());
        }

        return candidates.toArray(new Player[0])
                [(int) (Math.random() * players.size())];
    }

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
            match.player1().adjustRating(Rat1 - k * expectedScore);
            match.player2().adjustRating(Rat2 + k * expectedScore);
        }

        MatchIO.saveMatch(match);
        PlayerIO.savePlayer(match.player1());
        PlayerIO.savePlayer(match.player2());
    }
}
