package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html#BMA1_
 * 
 * https://www.midikits.net/midi_analyser/running_status.htm
 * 
 */

public class MidiFile extends MidiFilePart {
	
	MidiHeader      m_header;
	List<MidiTrack> m_tracks;
	
	private MidiFile() {
		super();
	}
	
	public static MidiFile createFromStream(InputStream is) throws IOException {
		MidiFile ret = new MidiFile();
		ret.readFromStream(is);
		return ret;
	}
	
	@Override
	protected void readFromStream(InputStream is) throws IOException {
		// Parse Midi-Header
		m_header = MidiHeader.createFromStream(is);
		setBytesRead(getBytesRead() + m_header.getBytesRead());
		if (m_header == null) {
			return;
		}
		short cnt = m_header.getNumberOfTracks();
		
		// Parse Tracks
		m_tracks = new ArrayList<MidiTrack>(cnt);
		for (int i = 0; i < cnt; i++) {
			MidiFileUtils.logWriter().println("parsing track " + (i + 1) + " of " + cnt);
			MidiTrack tmp = MidiTrack.createFromStream(is);
			if (tmp == null) {
				return;
			}
			setBytesRead(getBytesRead() + tmp.getBytesRead());
			m_tracks.add(tmp);
		}
	}

	@Override
	public void writeToStream(OutputStream os) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
