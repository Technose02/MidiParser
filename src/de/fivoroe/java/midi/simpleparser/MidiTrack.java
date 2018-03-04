package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MidiTrack {

	private List<Event> m_events = new ArrayList<Event>();
	
	public MidiTrack(InputStream is) {
		try {
			byte[] buff = new byte[255];
			
			is.read(buff, 0, 4);
			String mtrk = new String(buff, 0, 4);
			
			if (!mtrk.equals(MidiFile.MTRK)) {
				System.err.println("  Not a valid Midi file: MTRK of second track not found");
				return;
			}
			
			// read length
			is.read(buff, 0, 4);
			int l = ByteBuffer.wrap(buff).getInt();
			GlobalWriter.getWriter().println("  Length of Trackdata: " + l + "byte(s)");
			
			int r = 0;
			while (r < l) {
				Event e = Event.fromStream(is);
				m_events.add(e);
				r += e.getBytesRead();
				System.out.println("r ist jetzt " + r);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
}
