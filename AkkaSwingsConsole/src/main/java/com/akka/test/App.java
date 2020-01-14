package com.akka.test;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class App {
	
	private static JButton buttonStart = new JButton("Start");
	private static JButton buttonClear = new JButton("Clear");

	
	private static PrintStream standardOut;
	private static JTextArea textArea;

  public static void main(String[] args) 
  {
		//  Fire up actor systems on each port
    	startup(2251);
      
    	startup(2252);
    	
    	startup(0);
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

    Config config = ConfigFactory.parseMap(overrides)
        .withFallback(ConfigFactory.load());

    // Create an Akka system
    ActorSystem<Void> system = ActorSystem.create(rootBehavior(), "ClusterSystem", config);
  }
}