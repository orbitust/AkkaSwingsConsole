package com.akka.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class AkkaActorSystemTestConsole extends JFrame {
	/**
	 * The text area which is used for displaying logging information.
	 */
	private JTextArea textArea;

	private JButton buttonStart = new JButton("Start Actor System");
	private JButton buttonClear = new JButton("Clear Screen");

	private PrintStream standardOut;

	public AkkaActorSystemTestConsole() {
		super("Akka Actor System Console");

		textArea = new JTextArea(50, 10);
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));

		// keeps reference of standard output stream
		standardOut = System.out;

		// re-assigns standard output stream and error output stream
		System.setOut(printStream);
		System.setErr(printStream);

		// creates the GUI
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.anchor = GridBagConstraints.WEST;

		add(buttonStart, constraints);

		constraints.gridx = 1;
		add(buttonClear, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		add(new JScrollPane(textArea), constraints);

		// adds event handler for button Start
		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
			//  Fire up actor systems on each port
		    	startup(2250);
		      
		    	startup(2251);
		    	
		    	startup(2252);
			}
		});

		// adds event handler for button Clear
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// clears the text area
				try {
					textArea.getDocument().remove(0,
							textArea.getDocument().getLength());
					standardOut.println("Text area cleared");
				} catch (BadLocationException ex) {
					ex.printStackTrace();
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 800);
		setLocationRelativeTo(null);	// centers on screen
		setVisible(true);
	}

	/**
	 * Prints log statements for testing in a thread
	 *//*
	private void printLog() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					System.out.println("Time now is " + (new Date()));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}*/

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AkkaActorSystemTestConsole testConsole = new AkkaActorSystemTestConsole();
				testConsole.setVisible(true);
			}
		});
	}
	
	private static Behavior<Void> rootBehavior() {
	    return Behaviors.setup(context -> {

	      // Create an actor that handles cluster domain events
	      context.spawn(ClusterListener.create(), "ClusterListener");

	      return Behaviors.empty();
	    });
	  }

	  private static void startup(int port) {
	    // Override the configuration of the port
	    // Override the configuration of the port
	    Map<String, Object> overrides = new HashMap<>();
	    overrides.put("akka.remote.artery.canonical.port", port);
	    overrides.put("akka.cluster.jmx.multi-mbeans-in-same-jvm", true);

	    Config config = ConfigFactory.parseMap(overrides)
	        .withFallback(ConfigFactory.load());

	    // Create an Akka system
	    ActorSystem<Void> system = ActorSystem.create(rootBehavior(), "ClusterSystem", config);
	  }
}
