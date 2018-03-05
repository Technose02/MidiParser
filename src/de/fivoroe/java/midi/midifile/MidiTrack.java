package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MidiTrack {

	private List<Event> m_events = new ArrayList<Event>();
	
	private MidiTrack() {
	}
	
	protected static MidiTrack createFromStream(InputStream is) throws IOException {
		MidiTrack t = new MidiTrack();
		t.init(is);
		return t;
	}
	
	private void init(InputStream is) throws IOException {
		byte[] buff = new byte[255];
		
		is.read(buff, 0, 4);
		String mtrk = new String(buff, 0, 4);
		
		if (!mtrk.equals(MidiFileUtils.MTRK)) {
			System.err.println("  Not a valid Midi file: MTRK of second track not found");
			return;
		}
		
		// read length
		is.read(buff, 0, 4);
		int l = ByteBuffer.wrap(buff).getInt();
		MidiFileUtils.logWriter().println("  Length of Trackdata: " + l + "byte(s)");
		
		int r = 0;
		while (r < l) {
			Event e = Event.fromStream(is);
			m_events.add(e);
			r += e.getBytesRead();
		}
	}
}
