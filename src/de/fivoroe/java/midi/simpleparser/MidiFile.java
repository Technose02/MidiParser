package de.fivoroe.java.midi.simpleparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/*
 * http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html#BMA1_
 * 
 * https://www.midikits.net/midi_analyser/running_status.htm
 * 
 */

public class MidiFile {
	
	final static short MIDI_FORMAT_SINGLETRACK      = 0;
	final static short MIDI_FORMAT_MULTITRACK_SYNC  = 1;
	final static short MIDI_FORMAT_MULTITRACK_ASYNC = 2;
	
	final static String MTHD = "MThd";
	final static String MTRK = "MTrk";
	
	static String getHexString(byte[] data, int start, int l) {
		if (start == 0 && l == data.length) {
			return javax.xml.bind.DatatypeConverter.printHexBinary(data);
		}
		byte[] sub = new byte[l];
		int k = 0;
		while (k < l) {
			sub[k] = data[start + k];
			k++;
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(sub);
	}
	
	static String getHexString(byte data) {
		return javax.xml.bind.DatatypeConverter.printHexBinary(new byte[]{data});
	}
	
	List<MidiTrack> m_tracks;
	
	public MidiFile(String fullFilename) {

		try (PrintWriter pw =  new PrintWriter( new FileOutputStream("midi.txt"))) {
			try ( InputStream is = new FileInputStream(fullFilename) ) {
				
				byte[] buff = new byte[255];
				
				// read MThd
				is.read(buff, 0, 4);
				String mthd = new String(buff, 0, 4);
				if (!mthd.equals(MidiFile.MTHD)) {
					GlobalWriter.getWriter().println("Not a valid Midi file (MThd not found)");
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
				short type = ByteBuffer.wrap(buff).getShort();
				switch(type) {
	    			case MIDI_FORMAT_SINGLETRACK:
		    			GlobalWriter.getWriter().println("type of Midifile is MIDI_FORMAT_SINGLETRACK.");
			    		break;
			    	case MIDI_FORMAT_MULTITRACK_SYNC:
		    			GlobalWriter.getWriter().println("type of Midifile is MIDI_FORMAT_MULTITRACK_SYNC.");
			    		break;
			    	case MIDI_FORMAT_MULTITRACK_ASYNC:
		    			GlobalWriter.getWriter().println("type of Midifile is MIDI_FORMAT_SINGLETRACK.");
			    		break;			
		    		default:
						System.err.println("Not a valid Midi file (unknown type of " + type + ")");
				}
				
				// read Track count
				is.read(buff, 0, 2);
				short c = ByteBuffer.wrap(buff).getShort();
				GlobalWriter.getWriter().println(c + " tracks found");
				
				// read dd
				byte[] dd = new byte[2];
				is.read(dd, 0, 2);
				
				m_tracks = new ArrayList<MidiTrack>(c);
				for (int i = 0; i < c; i++) {
					GlobalWriter.getWriter().println("parsing track " + (i + 1) + " of " + c);
					MidiTrack tmp = new MidiTrack(is);
					m_tracks.add(tmp);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
