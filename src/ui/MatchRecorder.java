package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import item.Match;
import item.Player;
import run.Runner;

public class MatchRecorder {

    private MainUI parent;
    private JFrame window;
    private JPanel basePanel;
    private JPanel buttonPanel;
    private JComboBox<Player> selectionBox;
    private JButton confirmButton;
    private JButton cancelButton;
    private JLabel promptLabel;

    private RecordMatchListener l;

    private Match match;
    private static final Player makeSelectionPlayer =
            new Player("<Select Player>", "", 0);

    /**
     * 
     * @param par
     */
    public MatchRecorder(MainUI par, Match m) {
        parent = par;
        match = m;
        createElements();
        createLayout();
        createListener();
        createSettings();
        populateSelectionBox();
    }

    private void createElements() {
        window = new JFrame();
        basePanel = new JPanel();
        buttonPanel = new JPanel();
        selectionBox = new JComboBox<>();
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");
        promptLabel = new JLabel("Please select the winner of the match:");
    }

    private void createLayout() {
        basePanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());

        @SuppressWarnings("unused")
        String N = BorderLayout.NORTH,
        S = BorderLayout.SOUTH,
        E = BorderLayout.EAST,
        W = BorderLayout.WEST,
        C = BorderLayout.CENTER;

        window.setContentPane(basePanel);
        {
            basePanel.add(promptLabel, N);
            basePanel.add(selectionBox, C);
            basePanel.add(buttonPanel, S);
            {
                buttonPanel.add(cancelButton, W);
                buttonPanel.add(confirmButton, E);
            }
        }
    }

    private void createListener() {
        l = new RecordMatchListener();
        window.addWindowListener(l);
        confirmButton.addActionListener(l);
        cancelButton.addActionListener(l);
        selectionBox.addItemListener(l);
    }

    private void createSettings() {
        confirmButton.setName("MatchRecorder.Button.Confirm");
        cancelButton.setName("MatchRecorder.Button.Cancel");
        selectionBox.setName("MatchRecorder.ComboBox.Selection");

        selectionBox.setEditable(false);
        checkEnableConfirmButton();

        window.setSize(450, 400);
        window.setTitle("Create Match");
        window.setMinimumSize(new Dimension(100, 200));
        window.setAlwaysOnTop(true);
        window.setLocationRelativeTo(parent.window);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);

        parent.window.setEnabled(false);
    }

    private void populateSelectionBox() {
        selectionBox.addItem(makeSelectionPlayer);
        selectionBox.addItem(match.player1());
        selectionBox.addItem(match.player2());
    }

    private void checkEnableConfirmButton() {
        Object selection = selectionBox.getSelectedItem();
        confirmButton.setEnabled(selection != null
                && !selection.equals(makeSelectionPlayer));
    }

    /**
     * 
     */
    private void confirmButtonPressed() {
        Runner.getRunner().recordMatch(match,
                selectionBox.getSelectedItem().equals(match.player1()));
        parent.update();
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

    private class RecordMatchListener implements ActionListener,
    WindowListener, ItemListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                switch(((Component) e.getSource()).getName()) {
                    case "MatchRecorder.Button.Confirm" :
                        confirmButtonPressed();
                        break;
                    case "MatchRecorder.Button.Cancel" :
                        cancelButtonPressed();
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            checkEnableConfirmButton();
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
