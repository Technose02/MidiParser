package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MidiHeader {
	
	private short  m_numberOfTracks;
	private short  m_format;
	private byte[] m_dd;
	
	public MidiHeader(InputStream is) {
		try {
			byte[] buff = new byte[255];
			
			// read MThd
			is.read(buff, 0, 4);
			String mthd = new String(buff, 0, 4);
			if (!mthd.equals(MidiFileUtils.MTHD)) {
				MidiFileUtils.logWriter().println("Not a valid Midi file (MThd not found)");
				return;
			}
			
			// read header length
			is.read(buff, 0, 4);
			int l = ByteBuffer.wrap(buff).getInt();
			if (l != 6) {
				System.err.println("Not a valid Midi file (unexpected header length of " + l + ")");
				return;
			}
			
			// read type
			is.read(buff, 0, 2);
			m_format = ByteBuffer.wrap(buff).getShort();
			
			switch(m_format) {
				case MidiFileUtils.MIDI_FORMAT_SINGLETRACK:
					MidiFileUtils.logWriter().println("type of Midifile is MIDI_FORMAT_SINGLETRACK.");
		    		break;
		    	case MidiFileUtils.MIDI_FORMAT_MULTITRACK_SYNC:
					MidiFileUtils.logWriter().println("type of Midifile is MIDI_FORMAT_MULTITRACK_SYNC.");
		    		break;
		    	case MidiFileUtils.MIDI_FORMAT_MULTITRACK_ASYNC:
					MidiFileUtils.logWriter().println("type of Midifile is MIDI_FORMAT_SINGLETRACK.");
		    		break;			
				default:
					System.err.println("Not a valid Midi file (unknown type of " + m_format + ")");
					return;
			}
			
			// read Track count
			is.read(buff, 0, 2);
			m_numberOfTracks = ByteBuffer.wrap(buff).getShort();
			MidiFileUtils.logWriter().println(m_numberOfTracks + " tracks found");
			
			// read dd
			m_dd = new byte[2];
			is.read(m_dd, 0, 2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	short getNumberOfTracks() {
		return m_numberOfTracks;
	}
	
	

}
