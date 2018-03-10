package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MidiTrack extends MidiFilePart {

	private List<Event> m_events = new ArrayList<Event>();
	
	private MidiTrack() {
		super();
	}
	
	protected static MidiTrack createFromStream(InputStream is) throws IOException {
		MidiTrack t = new MidiTrack();
		t.readFromStream(is);
		return t;
	}
	
	@Override
	protected void readFromStream(InputStream is) throws IOException {
		byte[] buff = new byte[MidiFileUtils.READ_BUFFER_SIZE];
		
		int d = is.read(buff, 0, MidiFileUtils.MTRK.length());
		setBytesRead(getBytesRead() + d);
		String mtrk = new String(buff, 0, MidiFileUtils.MTRK.length());
		
		if (!mtrk.equals(MidiFileUtils.MTRK)) {
			System.err.println("  Not a valid Midi file: MTRK of second track not found");
			return;
		}
		
		// read length
		d = is.read(buff, 0, Integer.BYTES);
		setBytesRead(getBytesRead() + d);
		int l = ByteBuffer.wrap(buff).getInt();
		MidiFileUtils.logWriter().println("  Length of Trackdata: " + l + "byte(s)");
		
		int r = 0;
		while (r < l) {
			Event e = Event.createFromStream(is);
			setBytesRead(getBytesRead() + e.getBytesRead());
			r += e.getBytesRead();
			m_events.add(e);
			
		}
	}

	@Override
	public void writeToStream(OutputStream os) throws IOException {
		// TODO Auto-generated method stub
	}
	
}
