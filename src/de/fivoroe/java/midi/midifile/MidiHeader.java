package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MidiHeader extends MidiFilePart {
	
	private short  m_numberOfTracks;
	private short  m_format;
	private byte[] m_dd;
	
	private MidiHeader() {
		super();
	}
	
	public static MidiHeader createFromStream(InputStream is) throws IOException {
		MidiHeader ret = new MidiHeader();
		ret.readFromStream(is);
		return ret;
	}

	@Override
	protected void readFromStream(InputStream is) throws IOException {
		byte[] buff = new byte[MidiFileUtils.READ_BUFFER_SIZE];
		
		// read MThd
		int d = is.read(buff, 0, MidiFileUtils.MTHD.length());
		setBytesRead(getBytesRead() + d);
		String mthd = new String(buff, 0, MidiFileUtils.MTHD.length());
		if (!mthd.equals(MidiFileUtils.MTHD)) {
			MidiFileUtils.logWriter().println("Not a valid Midi file (MThd not found)");
			return;
		}
		
		// read header length
		d = is.read(buff, 0, Integer.BYTES);
		setBytesRead(getBytesRead() + d);
		int l = ByteBuffer.wrap(buff).getInt();
		if (l != MidiFileUtils.MIDI_HEADER_LENGTH) {
			System.err.println("Not a valid Midi file (unexpected header length of " + l + ")");
			return;
		}
		
		// read type
		d = is.read(buff, 0, Short.BYTES);
		setBytesRead(getBytesRead() + d);
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
		d = is.read(buff, 0, Short.BYTES);
		setBytesRead(getBytesRead() + d);
		m_numberOfTracks = ByteBuffer.wrap(buff).getShort();
		MidiFileUtils.logWriter().println(m_numberOfTracks + " tracks found");
		
		// read dd
		m_dd = new byte[Short.BYTES];
		d = is.read(m_dd, 0, Short.BYTES);
		setBytesRead(getBytesRead() + d);
	}

	@Override
	public void writeToStream(OutputStream os) {
		// TODO Auto-generated method stub
		
	}

	short getNumberOfTracks() {
		return m_numberOfTracks;
	}
	
}
