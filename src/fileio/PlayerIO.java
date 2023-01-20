package fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import item.Player;
import run.Util;

public class PlayerIO {

    /**
     * Saves the given Player to file
     * @param player
     * @return If the write was successful
     */
    public static boolean savePlayer(Player player) {
        File playersDir = new File("players/");

        if(!playersDir.exists()) {
            playersDir.mkdir();
        }

        File playerFile = new File("players/" + player.getEid() + ".player");

        if (playerFile.exists()) {
            playerFile.delete();
        }

        try {
            playerFile.createNewFile();

            Util.p("Attempting to save Player: " + player.toString());
            Util.p("As data:\n" + player.dataString());

            FileWriter writer = new FileWriter(playerFile);
            writer.write(player.dataString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads all Players from the players/ directory
     * If the expected directory does not exist, it will be created, preventing
     * this function from failing.
     * @return A HashMap of all players stored in file with eids as keys
     */
    public static HashMap<String, Player> loadAllPlayers() {
        HashMap<String, Player> players = new HashMap<>();

        File playersDir = new File("players/");
        if(!playersDir.exists()) {
            playersDir.mkdir();
        }

        for (File playerFile : playersDir.listFiles()) {
            try {
                Player p = loadPlayerFile(playerFile);
                Util.debug("loaded Player:" + p.toString());
                players.put(p.getEid(), p);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return players;
    }

    /**
     * Loads a Player from file give the Player's eid or null if a save file
     * does not exist
     * @param eid - The Player's eid
     * @return Player from file
     */
    public static Player loadPlayer(String eid) {
        try {
            return loadPlayerFile(new File("players/" + eid + ".player"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a Player from the given file
     * @param playerFile - The file containing the Player's data
     * @return The loaded Player
     * @throws FileNotFoundException - If a save file does not exist
     */
    public static Player loadPlayerFile(File playerFile)
            throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(playerFile));
        ArrayList<String> info = new ArrayList<>();

        try {

            for (String line = reader.readLine(); line != null;
                    line = reader.readLine()) {
                if (line.equals("START"))
                    continue;
                if (line.equals("END"))
                    break;

                info.add(line);
            }

            reader.close();
        }
        catch (IOException e) { e.printStackTrace(); }

        if(info.size() < 4)
            throw new FileNotFoundException(
                    "The given Player file is incomplete: "
                            + playerFile.getAbsolutePath());

        return new Player(info.get(1), info.get(2), info.get(0),
                Double.parseDouble(info.get(3)));
    }
}
