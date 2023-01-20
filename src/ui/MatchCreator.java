package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import item.Player;
import run.Runner;

/**
 * 
 * @author Jason
 *
 */
public class MatchCreator {

    public MainUI parent;
    private JFrame window;
    private JPanel basePanel;
    private JPanel player1ListPanel;
    private JPanel player2ListPanel;
    private JPanel buttonPanel;
    private JList<Player> player1List;
    private JList<Player> player2List;
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel vsLabel;
    private JButton cancelButton;
    private JButton createButton;

    private CreateMatchListener l;

    /**
     * 
     * @param par
     */
    public MatchCreator(MainUI par) {
        parent = par;
        createElements();
        createLayout();
        createListener();
        createSettings();
        populatePlayerLists();
    }

    /**
     * 
     */
    private void createElements() {
        window = new JFrame();
        basePanel = new JPanel();
        player1ListPanel = new JPanel();
        player2ListPanel = new JPanel();
        buttonPanel = new JPanel();
        player1Label = new JLabel("Player 1");
        player2Label = new JLabel("Player 2");
        vsLabel = new JLabel(" VS ");
        player1List = new JList<>();
        player2List = new JList<>();
        cancelButton = new JButton("Cancel");
        createButton = new JButton("Create");
    }

    /**
     * 
     */
    private void createLayout() {
        basePanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());
        player1ListPanel.setLayout(new BorderLayout());
        player2ListPanel.setLayout(new BorderLayout());

        @SuppressWarnings("unused")
        String N = BorderLayout.NORTH,
        S = BorderLayout.SOUTH,
        E = BorderLayout.EAST,
        W = BorderLayout.WEST,
        C = BorderLayout.CENTER;

        window.setContentPane(basePanel);
        {
            basePanel.add(player1ListPanel, W);
            {
                player1ListPanel.add(player1Label, N);
                player1ListPanel.add(player1List, C);
            }

            basePanel.add(buttonPanel, S);
            {
                buttonPanel.add(createButton, E);
                buttonPanel.add(cancelButton, W);
            }

            basePanel.add(player2ListPanel, E);
            {
                player2ListPanel.add(player2Label, N);
                player2ListPanel.add(player2List, C);
            }

            basePanel.add(vsLabel, C);
        }
    }

    /**
     * 
     */
    private void createListener() {
        l = new CreateMatchListener();
        player1List.addListSelectionListener(l);
        player2List.addListSelectionListener(l);
        createButton.addActionListener(l);
        cancelButton.addActionListener(l);
        window.addWindowListener(l);
    }

    /**
     * 
     */
    private void createSettings() {
        createButton.setName("MatchCreator.Button.Create");
        cancelButton.setName("MatchCreator.Button.Cancel");
        checkEnableCreateButton();

        player1List.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        player2List.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        window.setSize(450, 400);
        window.setTitle("Create Match");
        window.setMinimumSize(new Dimension(100, 200));
        window.setAlwaysOnTop(true);
        window.setLocationRelativeTo(parent.window);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);

        parent.window.setEnabled(false);
    }

    /**
     * 
     */
    private void populatePlayerLists() {
        ArrayList<Player> players =
                new ArrayList<>(Runner.getRunner().getAllPlayers());
        players.sort(null);
        player1List.setListData(players.toArray(new Player[0]));
        player2List.setListData(players.toArray(new Player[0]));
    }

    /**
     * 
     */
    private void checkEnableCreateButton() {
        createButton.setEnabled(!player1List.isSelectionEmpty() &&
                !player2List.isSelectionEmpty() &&
                !player1List.getSelectedValue().equals(
                        player2List.getSelectedValue()));
    }

    /**
     * 
     */
    private void createButtonPressed() {
        Runner.getRunner().createMatch(player1List.getSelectedValue(),
                player2List.getSelectedValue());
        window.dispatchEvent(
                new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * 
     */
    private void cancelButtonPressed() {
        window.dispatchEvent(
                new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * 
     */
    private void focusParentWindow() {
        parent.window.setEnabled(true);
        parent.window.requestFocus();
    }

    /**
     * 
     * @author Jason
     *
     */
    private class CreateMatchListener implements ActionListener,
    WindowListener, ListSelectionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                switch(((Component) e.getSource()).getName()) {
                    case "MatchCreator.Button.Create" :
                        createButtonPressed();
                        break;
                    case "MatchCreator.Button.Cancel" :
                        cancelButtonPressed();
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent arg0) {
            checkEnableCreateButton();
        }

        @Override
        public void windowClosing(WindowEvent arg0) {
            focusParentWindow();
        }

        @Override
        public void windowActivated(WindowEvent arg0) {}
        @Override
        public void windowClosed(WindowEvent arg0) {}
        @Override
        public void windowDeactivated(WindowEvent arg0) {}
        @Override
        public void windowDeiconified(WindowEvent arg0) {}
        @Override
        public void windowIconified(WindowEvent arg0) {}
        @Override
        public void windowOpened(WindowEvent arg0) {}

    }

}
