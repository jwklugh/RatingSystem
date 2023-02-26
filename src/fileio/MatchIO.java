package fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import item.Match;

public class MatchIO {

    /**
     * Saves the given Match to file
     * @param match - The match to write to file
     * @return If the write was successful
     */
    public static boolean saveMatch(Match match) {
        File matchesDir = new File("matches/");

        if(!matchesDir.exists()) {
            matchesDir.mkdir();
        }

        File matchFile = new File("matches/" + match.id() + ".match");

        if (matchFile.exists()) {
            matchFile.delete();
        }

        try {
            matchFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter(matchFile));
            writer.write(match.dataString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads all Matches from the matches/ directory
     * If the expected directory does not exist, it will be created, preventing
     * this function from failing.
     * @return A HashMap of all Matches stored in file with ids as keys
     */
    public static HashMap<Long, Match> loadAllMatches() {
        HashMap<Long, Match> matches = new HashMap<>();

        File matchesDir = new File("matches/");
        if(!matchesDir.exists()) {
            matchesDir.mkdir();
        }

        for (File matchFile : matchesDir.listFiles()) {
            try {
                Match m = loadMatchFile(matchFile);
                matches.put(m.id(), m);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return matches;
    }

    /**
     * Loads a Match from the given file
     * @param matchFile - The file containing the Match's data
     * @return The loaded Match
     * @throws FileNotFoundException - If a save file does not exist
     */
    public static Match loadMatchFile(File matchFile)
            throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(matchFile));

        String[] info = new String[5];
        for (int i=0; i < info.length; i++) {
            try { info[i] = reader.readLine(); }
            catch (IOException e) { e.printStackTrace(); }
        }
        try { reader.close(); } catch (IOException e) { e.printStackTrace(); }

        return new Match(info[1], info[2], info[3], info[4]);
    }

    /**
     * Deletes the file for the given match
     * @param match - The match to delete the file of
     * @return If the file was successfully deleted
     */
    public static void deleteMatch(Match match) {
        File matchFile = new File("matches/" + match.id() + ".match");
        File archiveDir = new File("archive/");

        if(!archiveDir.exists()) {
            archiveDir.mkdir();
        }

        archiveDir = new File("archive/" + match.id() + ".match");

        try {
            Files.move(matchFile.toPath(), archiveDir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
