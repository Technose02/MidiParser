package de.fivoroe.java.midi.simpleparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * http://www.music.mcgill.ca/~ich/classes/mumt306/StandardMIDIfileformat.html#BMA1_
 * 
 * https://www.midikits.net/midi_analyser/running_status.htm
 * 
 */

public class MidiFile {
	
	MidiHeader      m_header;
	List<MidiTrack> m_tracks;
	
	public MidiFile(String fullFilename) {

		try ( InputStream is = new FileInputStream(fullFilename) ) {
			
			// Parse Midi-Header
			m_header = new MidiHeader(is);			
			short cnt = m_header.getNumberOfTracks();
			
			// Parse Tracks
			m_tracks = new ArrayList<MidiTrack>(cnt);
			for (int i = 0; i < cnt; i++) {
				MidiFileUtils.logWriter().println("parsing track " + (i + 1) + " of " + cnt);
				MidiTrack tmp = new MidiTrack(is);
				m_tracks.add(tmp);
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
