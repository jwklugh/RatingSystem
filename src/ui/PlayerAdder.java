package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import item.Player;
import run.Runner;

public class PlayerAdder {

    public MainUI parent;
    private JFrame window;
    private JPanel basePanel;
    private JPanel fieldPanel;
    private JPanel namePanel;
    private JPanel eidPanel;
    private JPanel buttonPanel;
    private JLabel nameLabel;
    private JLabel eidLabel;
    private JTextField nameField;
    private JTextField eidField;
    private JButton addButton;
    private JButton cancelButton;
    private AddPlayerListener l;

    public PlayerAdder(MainUI par) {
        parent = par;
        createElements();
        createLayout();
        createListener();
        createSettings();
    }

    private void createElements() {
        window = new JFrame();
        basePanel = new JPanel();
        fieldPanel = new JPanel();
        namePanel = new JPanel();
        eidPanel = new JPanel();
        buttonPanel = new JPanel();
        nameLabel = new JLabel("Name: ");
        eidLabel = new JLabel("EID: ");
        nameField = new JTextField(15);
        eidField = new JTextField(15);
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
    }

    /**
     * 
     */
    private void createLayout() {
        basePanel.setLayout(new BorderLayout());
        fieldPanel.setLayout(new BorderLayout());
        namePanel.setLayout(new BorderLayout());
        eidPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());

        @SuppressWarnings("unused")
        String N = BorderLayout.NORTH,
        S = BorderLayout.SOUTH,
        E = BorderLayout.EAST,
        W = BorderLayout.WEST,
        C = BorderLayout.CENTER;

        window.setContentPane(basePanel);
        {
            basePanel.add(fieldPanel, N);
            {
                fieldPanel.add(namePanel, N);
                {
                    namePanel.add(nameLabel, W);
                    namePanel.add(nameField, E);
                }

                fieldPanel.add(eidPanel, S);
                {
                    eidPanel.add(eidLabel, W);
                    eidPanel.add(eidField, E);
                }
            }

            basePanel.add(buttonPanel, S);
            {
                buttonPanel.add(addButton, E);
                buttonPanel.add(cancelButton, W);
            }
        }
    }

    /**
     * 
     */
    private void createListener() {
        l = new AddPlayerListener();
        nameField.getDocument().addDocumentListener(l);
        eidField.getDocument().addDocumentListener(l);
        addButton.addActionListener(l);
        cancelButton.addActionListener(l);
        window.addWindowListener(l);
    }

    /**
     * 
     */
    private void createSettings() {
        addButton.setName("PlayerAdder.Button.Add");
        cancelButton.setName("PlayerAdder.Button.Cancel");
        nameField.getDocument().addDocumentListener(l);

        window.setSize(450, 400);
        window.setTitle("Add Player");
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
    private void updateAddButtonEnabled() {
        String[] names = nameField.getText().trim().split(" ");
        boolean nameValid = names.length == 2;
        boolean eidValid = false;
        if (nameValid) {
            String eid = eidField.getText();
            eidValid = eid.length() > 0 && Runner.getRunner().checkEidFree(eid);
        }
        addButton.setEnabled(nameValid && eidValid);
    }

    /**
     * 
     */
    private void addButtonPressed() {
        Player newPlayer;

        newPlayer = Runner.getRunner().AddPlayer(
                nameField.getText(), eidField.getText());

        Runner.getRunner().playerArrived(newPlayer);

        window.dispatchEvent(
                new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

        parent.update();
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
    private class AddPlayerListener implements ActionListener,
    DocumentListener, WindowListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                switch(((Component) e.getSource()).getName()) {
                    case "PlayerAdder.Button.Add" :
                        addButtonPressed();
                        break;
                    case "PlayerAdder.Button.Cancel" :
                        cancelButtonPressed();
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateAddButtonEnabled();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateAddButtonEnabled();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateAddButtonEnabled();
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
