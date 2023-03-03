package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import item.Match;
import item.Player;
import run.Runner;

public class MainUI {

    public JFrame window;

    private JPanel basePanel;
    private JPanel playersPanel;
    private JPanel attendanceListPanel;
    private JPanel playerListPanel;
    private JPanel rankingsPanel;
    private JPanel matchesPanel;
    private JPanel activeMatchesPanel;
    private JPanel previousMatchesPanel;
    private JPanel controlPanel;
    private JPanel playerControlPanel;
    private JPanel matchControlPanel;

    private JScrollPane attendanceScrollPane;
    private JScrollPane playerListScrollPane;
    private JScrollPane rankingsScrollPane;
    private JScrollPane activeMatchesScrollPane;
    private JScrollPane previousMatchesScrollPane;

    private JLabel attendanceListLabel;
    private JLabel playerListLabel;
    private JLabel rankingsLabel;
    private JLabel activeMatchesLabel;
    private JLabel previousMatchesLabel;

    private JList<Player> attendanceList;
    private JList<Player> playerList;
    private JList<Player> rankingsList;
    private JList<Match> activeMatchesList;
    private JList<Match> previousMatchesList;

    private JButton newArrivalButton;
    private JButton returningArrivalButton;
    private JButton retirePlayerButton;
    private JButton editPlayerButton;
    private JButton findMatchButton;
    private JButton createMatchButton;
    private JButton recordMatchButton;
    private JButton discardMatchButton;

    private static final Player dummy =
            new Player("---------- ----------", "", 0, false);

    private MainUIListener l;

    /**
     * 
     */
    public MainUI()  {
        createElements();
        createLayout();
        update();
        createListener();
        createSettings();
    }

    /**
     * 
     */
    private void createElements() {
        window = new JFrame();

        basePanel = new JPanel();
        playersPanel = new JPanel();
        attendanceListPanel = new JPanel();
        playerListPanel = new JPanel();
        rankingsPanel = new JPanel();
        matchesPanel = new JPanel();
        activeMatchesPanel = new JPanel();
        previousMatchesPanel = new JPanel();
        controlPanel = new JPanel();
        playerControlPanel = new JPanel();
        matchControlPanel = new JPanel();

        attendanceListLabel = new JLabel("Attendance List");
        playerListLabel = new JLabel("Players");
        rankingsLabel = new JLabel("Rankings");
        activeMatchesLabel = new JLabel("Active Matches");
        previousMatchesLabel = new JLabel("Previous Matches");

        attendanceList = new JList<>();
        playerList = new JList<>();
        rankingsList = new JList<>();
        activeMatchesList = new JList<>();
        previousMatchesList = new JList<>();

        attendanceScrollPane = new JScrollPane(attendanceList);
        playerListScrollPane = new JScrollPane(playerList);
        rankingsScrollPane = new JScrollPane(rankingsList);
        activeMatchesScrollPane = new JScrollPane(activeMatchesList);
        previousMatchesScrollPane = new JScrollPane(previousMatchesList);

        newArrivalButton = new JButton("New Arrival");
        returningArrivalButton = new JButton("Returning Arrival");
        retirePlayerButton = new JButton("Retire Player");
        editPlayerButton = new JButton("Edit Player");
        findMatchButton = new JButton("Find Match");
        createMatchButton = new JButton("Create Match");
        recordMatchButton = new JButton("Record Match");
        discardMatchButton = new JButton("Discard Match");
    }

    /**
     * 
     */
    private void createLayout() {
        basePanel           .setLayout(new BorderLayout());
        playersPanel        .setLayout(new BorderLayout());
        attendanceListPanel .setLayout(new BorderLayout());
        playerListPanel     .setLayout(new BorderLayout());
        rankingsPanel       .setLayout(new BorderLayout());
        matchesPanel        .setLayout(new BorderLayout());
        activeMatchesPanel  .setLayout(new BorderLayout());
        previousMatchesPanel.setLayout(new BorderLayout());
        controlPanel        .setLayout(new BorderLayout());
        playerControlPanel  .setLayout(
                new BoxLayout(playerControlPanel, BoxLayout.Y_AXIS));
        matchControlPanel   .setLayout(
                new BoxLayout(matchControlPanel, BoxLayout.Y_AXIS));

        String N = BorderLayout.NORTH,
                S = BorderLayout.SOUTH,
                E = BorderLayout.EAST,
                W = BorderLayout.WEST,
                C = BorderLayout.CENTER;

        window.setContentPane(basePanel);
        {
            basePanel.add(playersPanel, W);
            {
                playersPanel.add(attendanceListPanel, W);
                {
                    attendanceListPanel.add(attendanceListLabel, N);
                    attendanceListPanel.add(attendanceScrollPane, C);
                }

                playersPanel.add(playerListPanel, C);
                {
                    playerListPanel.add(playerListLabel, N);
                    playerListPanel.add(playerListScrollPane, C);
                }

                playersPanel.add(rankingsPanel, E);
                {
                    rankingsPanel.add(rankingsLabel, N);
                    rankingsPanel.add(rankingsScrollPane, C);
                }
            }

            basePanel.add(matchesPanel, E);
            {
                matchesPanel.add(activeMatchesPanel, W);
                {
                    activeMatchesPanel.add(activeMatchesLabel, N);
                    activeMatchesPanel.add(activeMatchesScrollPane,C);
                }

                matchesPanel.add(previousMatchesPanel, E);
                {
                    previousMatchesPanel.add(previousMatchesLabel, N);
                    previousMatchesPanel.add(previousMatchesScrollPane,C);
                }
            }

            basePanel.add(controlPanel, C);
            {
                controlPanel.add(playerControlPanel, N);
                {
                    newArrivalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    returningArrivalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    retirePlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    editPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                    playerControlPanel.add(newArrivalButton);
                    playerControlPanel.add(returningArrivalButton);
                    playerControlPanel.add(retirePlayerButton);
                    playerControlPanel.add(editPlayerButton);
                }

                controlPanel.add(matchControlPanel, S);
                {
                    findMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    createMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    recordMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    discardMatchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                    matchControlPanel.add(findMatchButton);
                    matchControlPanel.add(createMatchButton);
                    matchControlPanel.add(recordMatchButton);
                    matchControlPanel.add(discardMatchButton);
                }
            }
        }
    }

    /**
     * 
     */
    private void createListener() {
        l = new MainUIListener();

        newArrivalButton.addActionListener(l);
        returningArrivalButton.addActionListener(l);
        retirePlayerButton.addActionListener(l);
        editPlayerButton.addActionListener(l);
        findMatchButton.addActionListener(l);
        createMatchButton.addActionListener(l);
        recordMatchButton.addActionListener(l);
        discardMatchButton.addActionListener(l);
        attendanceList.addListSelectionListener(l);
        playerList.addListSelectionListener(l);
        activeMatchesList.addListSelectionListener(l);
        previousMatchesList.addListSelectionListener(l);
    }

    /**
     * 
     */
    private void createSettings() {
        newArrivalButton.setName("MainUI.Button.newArrival");
        returningArrivalButton.setName("MainUI.Button.retArrival");
        retirePlayerButton.setName("MainUI.Button.retire");
        editPlayerButton.setName("MainUI.Button.editPlayer");
        findMatchButton.setName("MainUI.Button.findMatch");
        createMatchButton.setName("MainUI.Button.createMatch");
        recordMatchButton.setName("MainUI.Button.recordMatch");
        discardMatchButton.setName("MainUI.Button.discardMatch");

        attendanceList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        playerList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        rankingsList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        activeMatchesList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        previousMatchesList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        attendanceScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        playerListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rankingsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        activeMatchesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        previousMatchesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        attendanceScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        playerListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        activeMatchesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        previousMatchesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        attendanceList.setCellRenderer(new PlayerCellRenderer());
        playerList.setCellRenderer(new PlayerCellRenderer());
        rankingsList.setCellRenderer(new RankingsCellRenderer());

        checkEnabledButtons();

        window.setSize(1200, 500);
        window.setTitle("Title Placeholder");
        window.setMinimumSize(new Dimension(600,400));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }

    private void populatePlayers() {
        ArrayList<Player> playersByName =
                new ArrayList<>(Runner.getRunner().getAllPlayers());
        playersByName.sort(new Player.PlayerNameComparator());
        if(playersByName.isEmpty()) {
            playersByName.add(dummy);
        }
        playerList.setListData(playersByName.toArray(new Player[0]));

        ArrayList<Player> playersByRank =
                new ArrayList<>(Runner.getRunner().getAllPlayers());
        playersByRank.sort(new Player.PlayerRankComparator());
        if(playersByRank.isEmpty()) {
            playersByRank.add(dummy);
        }
        rankingsList.setListData(playersByRank.toArray(new Player[0]));
    }

    private void populateAttendance() {
        ArrayList<Player> players =
                new ArrayList<>(Runner.getRunner().getAttendance());
        if(players.isEmpty()) {
            players.add(dummy);
        }
        players.sort(new Player.PlayerMatchComparator());
        attendanceList.setListData(players.toArray(new Player[0]));
    }

    private void populateMatches() {
        ArrayList<Match> past =
                new ArrayList<Match>(Runner.getRunner().getPastMatches());
        ArrayList<Match> curr =
                new ArrayList<Match>(Runner.getRunner().getCurrMatches());
        past.sort(new Match.InverseMatchComparator());
        curr.sort(null);
        previousMatchesList.setListData(past.toArray(new Match[0]));
        activeMatchesList.setListData(curr.toArray(new Match[0]));
    }

    public void update() {
        populatePlayers();
        populateAttendance();
        populateMatches();
    }

    private void checkEnabledButtons() {
        checkEnableReturningPlayerButton();
        checkEnableRetirePlayerButton();
        checkEnableEditPlayerButton();
        checkEnableFindMatchButton();
        checkEnableRecordMatchButton();
        checkEnableDiscardMatchButton();
    }

    private void checkEnableReturningPlayerButton() {
        returningArrivalButton.setEnabled(
                playerList.getSelectedValue() != null &&
                !playerList.getSelectedValue().equals(dummy));
    }

    private void checkEnableRetirePlayerButton() {
        retirePlayerButton.setEnabled(
                attendanceList.getSelectedValue() != null &&
                !attendanceList.getSelectedValue().equals(dummy));
    }

    private void checkEnableEditPlayerButton() {
        editPlayerButton.setEnabled(
                playerList.getSelectedValue() != null &&
                !playerList.getSelectedValue().equals(dummy));
    }

    private void checkEnableFindMatchButton() {
        findMatchButton.setEnabled(attendanceList.getSelectedValue() != null &&
                Runner.getRunner().getAttendance().size() > 1);
    }

    private void checkEnableRecordMatchButton() {
        recordMatchButton.setEnabled(
                activeMatchesList.getSelectedValue() != null);
    }

    private void checkEnableDiscardMatchButton() {
        discardMatchButton.setEnabled(
                activeMatchesList.getSelectedValue() != null);
    }

    private void newArrival() {
        new PlayerAdder(this);
    }

    private void returningPlayer() {
        Runner.getRunner().playerArrived(playerList.getSelectedValue());
        update();
    }

    private void retiringPlayer() {
        Runner.getRunner().retirePlayer(attendanceList.getSelectedValue());
        update();
    }

    private void editPlayer() {
        new PlayerEditor(this, playerList.getSelectedValue());
    }

    private void findMatch() {
        Player findMatchFor = attendanceList.getSelectedValue();
        Player matchedPlayer = Runner.getRunner().matchPlayer(findMatchFor);
        Runner.getRunner().createMatch(findMatchFor, matchedPlayer);
    }

    private void createManualMatch() {
        new MatchCreator(this);
    }

    private void openMatchRecorder() {
        new MatchRecorder(this, activeMatchesList.getSelectedValue());
    }

    private void discardMatch() {
        Match m = activeMatchesList.getSelectedValue();
        String discardMatchMessage = "";
        discardMatchMessage += "Are you sure you would like to discard this match:\n";
        discardMatchMessage += m;
        int result = JOptionPane.showConfirmDialog(window, discardMatchMessage,
                "Confirm Discard", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Runner.getRunner().discardMatch(m);
        }

        update();
    }

    private class MainUIListener implements ActionListener, ListSelectionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                switch(((Component) e.getSource()).getName()) {
                    case "MainUI.Button.newArrival" :
                        newArrival();
                        break;

                    case "MainUI.Button.retArrival" :
                        returningPlayer();
                        break;

                    case "MainUI.Button.retire" :
                        retiringPlayer();
                        break;

                    case "MainUI.Button.editPlayer" :
                        editPlayer();
                        break;

                    case "MainUI.Button.findMatch" :
                        findMatch();
                        break;

                    case "MainUI.Button.createMatch" :
                        createManualMatch();
                        break;

                    case "MainUI.Button.recordMatch" :
                        openMatchRecorder();
                        break;

                    case "MainUI.Button.discardMatch" :
                        discardMatch();
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            checkEnabledButtons();
        }
    }
}

class RankingsCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    public RankingsCellRenderer() {
        setOpaque(true);
    }

    @Override public Component getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Player player = (Player) value;

        value = "" + Runner.getRunner().getPlayerRank(player)
                + ": " + player.toString();

        Color background = Color.WHITE;
        Color foreground =
                player.getNumMatchesPlayed() < 5 ? Color.MAGENTA :
                    !player.isConfirmed() ? Color.GREEN :
                        Color.BLACK;

        Component c = super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        c.setForeground(foreground);
        c.setBackground(background);

        return c;
    }
}

class PlayerCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    public PlayerCellRenderer() {
        setOpaque(true);
    }

    @Override public Component getListCellRendererComponent(JList<?> list,
            Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Player player = (Player) value;

        value = player.toString();

        Color background = Color.WHITE;
        Color foreground =
                player.getNumMatchesPlayed() < 5 ? Color.MAGENTA :
                    !player.isConfirmed() ? Color.GREEN :
                        Color.BLACK;

        Component c = super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);

        c.setForeground(foreground);
        c.setBackground(background);

        return c;
    }
}

