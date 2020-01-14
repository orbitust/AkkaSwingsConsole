package com.akka.test;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class CustomOutputStream extends OutputStream {
	private JTextArea textArea;

	public CustomOutputStream(JTextArea textArea) {
		this.textArea = textArea;
	}
	
	private static int lineNum = 0;
	private static int beginlineNum = 0;
	private static int endlineNum = 0;

	@Override
	public void write(int b) throws IOException 
	{
		// redirects data to the text area
		String s = String.valueOf((char)b);
		textArea.setForeground(Color.BLUE);
	    textArea.append(String.valueOf((char)b));
	    // scrolls the text area to the end of data
	    textArea.setCaretPosition(textArea.getDocument().getLength());
		
	}
}
