package de.fivoroe.java.midi.simpleparser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class GlobalWriter {

	private static PrintWriter m_pw;
	
	public static void setOutput(String filename) {
		try {
			m_pw = new PrintWriter(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PrintWriter getWriter() {
		return m_pw;
	}
	
	public static void dispose() {
		m_pw.flush();
		m_pw.close();
	}
}
