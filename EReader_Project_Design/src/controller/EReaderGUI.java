package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.EReader;

/**
 * EReaderGUI is a JFrame that displays and controls 
 * the graphical user inteface of the EReader model
 */
@SuppressWarnings("serial")
public class EReaderGUI extends JFrame implements Observer{

	public static final int WIDTH = 500;
	public static final int HEIGHT = 800;

	private EReader eReader;
	private JTextArea pageArea;
	private JLabel pageLabel;
	private JPanel controlPanel;
	private JTextField jumpField;
	JCheckBoxMenuItem darkMode;
	private KeyListener keyListener;

	// Creates and launches the JFrame
	public static void main(String[] args) {
		EReaderGUI window = new EReaderGUI();
		window.setVisible(true);
	}

	// Contructor: Lays out all graphical elements
	public EReaderGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(550, 720);
		this.setLocation(100, 40);
		this.setTitle("E-Reader");
		this.setLayout(new FlowLayout());

		// Add KeyListener for page turning
		keyListener = new ArrowKeyListener();
		this.addKeyListener(keyListener);
		this.setFocusable(true);
		this.requestFocus();

		eReader = new EReader("./Alice.txt", 36);

		setupMenu();
		pageArea = new JTextArea(eReader.getCurrPage());
		pageArea.setPreferredSize(new Dimension(500, 600));
		pageArea.setEditable(false);

		setupControlPanel();

		pageArea.addKeyListener(keyListener);
		controlPanel.addKeyListener(keyListener);
		this.add(pageArea);
		this.add(controlPanel);

		eReader.addObserver(this);
	}

	// Sets up the on-sceen controls
	private void setupControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 3, 0 , 0));
		controlPanel.addKeyListener(keyListener);

		ActionListener buttonListener = new ButtonListener();
		ActionListener textListener = new TextListener();

		JButton nextButton = new JButton("<");
		nextButton.addActionListener(buttonListener);
		nextButton.addKeyListener(keyListener);

		pageLabel = new JLabel("Page " + eReader.getCurrPageNumber() + 
				" of " + eReader.getTotalPages());

		JButton prevButton = new JButton(">");
		prevButton.addActionListener(buttonListener);
		prevButton.addKeyListener(keyListener);


		JLabel jumpLabel = new JLabel("Jump to Page: ");
		jumpLabel.addKeyListener(keyListener);

		jumpField = new JTextField();
		jumpField.setHorizontalAlignment(SwingConstants.RIGHT);
		jumpField.addActionListener(textListener);
		jumpField.addKeyListener(keyListener);

		JButton jumpButton = new JButton("Go");
		jumpButton.addActionListener(buttonListener);
		jumpButton.addKeyListener(keyListener);

		controlPanel.add(nextButton);
		controlPanel.add(pageLabel);
		controlPanel.add(prevButton);
		controlPanel.add(jumpLabel);
		controlPanel.add(jumpField);
		controlPanel.add(jumpButton);

	}

	// Sets up the menu bar elements
	private void setupMenu() {
		JMenuItem menu = new JMenu("Options");
		darkMode = new JCheckBoxMenuItem("Dark Mode");
		darkMode.setState(false);
		menu.add(darkMode);

		// Set the menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(menu);

		// Add the same listener to all menu items requiring action
		MenuItemListener menuListener = new MenuItemListener();
		darkMode.addActionListener(menuListener);
	}

	// Attemps page jump
	private void goToPage(int pageNumber) {
		if (eReader.jumpToPage(pageNumber))
			jumpField.setText("");
		else
			JOptionPane.showMessageDialog(null, "Page out of range");
	}

	// Updates the text area and page status 
	// when the observable class in changed
	@Override
	public void update(Observable o, Object arg) {
		pageArea.setText(eReader.getCurrPage());
		pageLabel.setText("Page " + eReader.getCurrPageNumber() + 
				" of " + eReader.getTotalPages());
	}

	// Handles the page turns from the left/right keys
	public class ArrowKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent key) {
			if (key.getKeyCode() == KeyEvent.VK_LEFT)
				eReader.prevPage();

			if (key.getKeyCode() == KeyEvent.VK_RIGHT) 
				eReader.nextPage();

		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

	}

	// Handles page turns from the on-screen arrow buttons
	// and page jumps from the onscreen jump button
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String buttonLabel = ((JButton)arg0.getSource()).getText();

			if (buttonLabel.equals("<"))
				eReader.prevPage();
			else if (buttonLabel.equals(">"))
				eReader.nextPage();
			else
				try {
					int pageNumber = Integer.parseInt(jumpField.getText());
					goToPage(pageNumber);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid input");
				}
		}

	}

	// Hadles page jump from pressing the enter key in the jump field
	private class TextListener implements ActionListener {

		// Converts a Celsius value into Fahrenheit 
		// and updates the opposite text field.
		public void actionPerformed(ActionEvent arg0) {
			try {
				int pageNumber = Integer.parseInt(jumpField.getText());
				goToPage(pageNumber);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid input");
			}
		}
	}

	// Handles the Dark Mode toggle in the menu bar
	private class MenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Find out the text of the JMenuItem that was just clicked
			String text = ((JMenuItem) e.getSource()).getText();

			if (text.equals("Dark Mode"))
				if (darkMode.getState()) {
					pageArea.setBackground(Color.BLACK);
					pageArea.setForeground(Color.WHITE);
					darkMode.setState(true);
				}
				else {
					pageArea.setBackground(Color.WHITE);
					pageArea.setForeground(Color.BLACK);
					darkMode.setState(false);
				}
		}
	}

}
