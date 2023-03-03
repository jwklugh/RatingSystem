package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import item.Player;
import run.Runner;

public class PlayerEditor {

    public MainUI parent;
    private JFrame window;
    private JPanel basePanel;
    private JPanel fieldPanel;
    private JPanel field2Panel;
    private JPanel idFieldPanel;       ///
    private JPanel namePanel;
    private JPanel eidPanel;
    private JPanel ratingPanel;        ///
    private JPanel showRatingPanel;    ///
    private JPanel matchesPlayedPanel;
    private JPanel confirmedPanel;
    private JPanel buttonPanel;
    private JLabel nameLabel;
    private JLabel eidLabel;
    private JLabel ratingLabel;        ///
    private JLabel showRatingLabel;    ///
    private JLabel matchesPlayedLabel;
    private JLabel confirmedLabel;
    private JTextField nameField;
    private JTextField eidField;
    private JSpinner ratingSpinner;    ///
    private JButton confirmButton;
    private JButton cancelButton;
    private JCheckBox showRatingCB;    ///
    private JCheckBox confirmedCB;
    private EditPlayerListener l;

    private Player player;

    public PlayerEditor(MainUI par, Player p) {
        parent = par;
        player = p;
        createElements();
        createLayout();
        createListener();
        createSettings();
    }

    private void createElements() {
        window = new JFrame();
        basePanel = new JPanel();
        fieldPanel = new JPanel();
        field2Panel = new JPanel();
        idFieldPanel = new JPanel();
        namePanel = new JPanel();
        eidPanel = new JPanel();
        ratingPanel = new JPanel();
        showRatingPanel = new JPanel();
        buttonPanel = new JPanel();
        matchesPlayedPanel = new JPanel();
        confirmedPanel = new JPanel();
        nameLabel = new JLabel(" Name:  ");
        eidLabel = new JLabel(" EID:  ");
        ratingLabel = new JLabel(" Rating:  ");
        showRatingLabel = new JLabel(" show");
        matchesPlayedLabel = new JLabel(" Matches Played: " +
                player.getNumMatchesPlayed());
        confirmedLabel = new JLabel("Confirmed?  ");
        nameField = new JTextField(player.toString(), 15);
        eidField = new JTextField(player.getEid(), 15);
        ratingSpinner = new JSpinner(
                new SpinnerNumberModel(player.getRating(), 0, 1000000, 1));
        showRatingCB = new JCheckBox();
        confirmedCB = new JCheckBox();
        confirmButton = new JButton("Confirm");
        cancelButton = new JButton("Cancel");
    }

    /**
     * 
     */
    private void createLayout() {
        basePanel.setLayout(new BorderLayout());
        fieldPanel.setLayout(new BorderLayout());
        field2Panel.setLayout(new BorderLayout());
        matchesPlayedPanel.setLayout(new BorderLayout());
        confirmedPanel.setLayout(new BorderLayout());
        idFieldPanel.setLayout(new BorderLayout());
        namePanel.setLayout(new BorderLayout());
        eidPanel.setLayout(new BorderLayout());
        ratingPanel.setLayout(new BorderLayout());
        showRatingPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());

        String N = BorderLayout.NORTH,
                S = BorderLayout.SOUTH,
                E = BorderLayout.EAST,
                W = BorderLayout.WEST,
                C = BorderLayout.CENTER;

        window.setContentPane(basePanel);
        {
            basePanel.add(fieldPanel, N);
            {
                fieldPanel.add(idFieldPanel, N);
                {
                    idFieldPanel.add(namePanel, N);
                    {
                        namePanel.add(nameLabel, W);
                        namePanel.add(nameField, C);
                    }

                    idFieldPanel.add(eidPanel, S);
                    {
                        eidPanel.add(eidLabel, W);
                        eidPanel.add(eidField, C);
                    }
                }


                fieldPanel.add(field2Panel, S);
                {
                    field2Panel.add(ratingPanel, N);
                    {
                        ratingPanel.add(ratingLabel, W);
                        ratingPanel.add(ratingSpinner, C);

                        ratingPanel.add(showRatingPanel, E);
                        {
                            showRatingPanel.add(showRatingCB, W);
                            showRatingPanel.add(showRatingLabel, E);
                        }
                    }

                    field2Panel.add(matchesPlayedPanel, S);
                    {
                        matchesPlayedPanel.add(matchesPlayedLabel, W);
                        matchesPlayedPanel.add(confirmedPanel, E);
                        {
                            confirmedPanel.add(confirmedLabel, W);
                            confirmedPanel.add(confirmedCB, E);
                        }
                    }
                }
            }

            basePanel.add(buttonPanel, S);
            {
                buttonPanel.add(confirmButton, E);
                buttonPanel.add(cancelButton, W);
            }
        }
    }

    /**
     * 
     */
    private void createListener() {
        l = new EditPlayerListener();
        nameField.getDocument().addDocumentListener(l);
        eidField.getDocument().addDocumentListener(l);
        ratingSpinner.addChangeListener(l);
        confirmButton.addActionListener(l);
        cancelButton.addActionListener(l);
        confirmedCB.addActionListener(l);
        window.addWindowListener(l);
    }

    /**
     * 
     */
    private void createSettings() {
        confirmButton.setName("PlayerEditor.Button.Confirm");
        cancelButton.setName("PlayerEditor.Button.Cancel");
        confirmedCB.setName("PlayerEditor.Checkbox.Confirmed");
        nameField.getDocument().addDocumentListener(l);
        confirmedCB.setSelected(player.isConfirmed());

        window.setSize(450, 400);
        window.setTitle("Add Player");
        window.setMinimumSize(new Dimension(100, 200));
        window.setAlwaysOnTop(true);
        window.setLocationRelativeTo(parent.window);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setVisible(true);

        updateConfirmButtonEnabled();

        parent.window.setEnabled(false);
    }

    /**
     * 
     */
    private void updateConfirmButtonEnabled() {
        String[] names = nameField.getText().trim().split(" ");
        boolean nameValid = names.length == 2;
        boolean eidValid = false;
        boolean changeMade =
                !nameField.getText().equals(player.toString()) ||
                !eidField.getText().equals(player.getEid()) ||
                !ratingSpinner.getValue().equals(player.getRating()) ||
                !(confirmedCB.isSelected() == player.isConfirmed());

        if (nameValid) {
            String eid = eidField.getText();
            eidValid = eid.length() > 0 &&
                    (Runner.getRunner().checkEidFree(eid) ||
                            eid.equals(player.getEid()));
        }

        confirmButton.setEnabled(nameValid && eidValid && changeMade);
    }

    /**
     * 
     */
    private void confirmButtonPressed() {
        Runner.getRunner().editPlayer(player, nameField.getText(),
                eidField.getText(), (double)ratingSpinner.getValue(),
                confirmedCB.isSelected());

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

    private void focusParentWindow() {
        parent.window.setEnabled(true);
        parent.window.requestFocus();
    }

    /**
     * 
     * @author Jason
     *
     */
    private class EditPlayerListener implements ActionListener,
    DocumentListener, ChangeListener, WindowListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                switch(((Component) e.getSource()).getName()) {
                    case "PlayerEditor.Button.Confirm" :
                        confirmButtonPressed();
                        break;
                    case "PlayerEditor.Button.Cancel" :
                        cancelButtonPressed();
                        break;
                    case "PlayerEditor.Checkbox.Confirmed" :
                        updateConfirmButtonEnabled();
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateConfirmButtonEnabled();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateConfirmButtonEnabled();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateConfirmButtonEnabled();
        }

        @Override
        public void stateChanged(ChangeEvent arg0) {
            updateConfirmButtonEnabled();
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

