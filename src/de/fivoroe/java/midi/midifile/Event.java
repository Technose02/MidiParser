package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;

public abstract class Event extends MidiFilePart {

	protected static Event s_lastEvent = null;
	
	protected long m_deltaTime;
	
	protected Event() {
		super();
	}
	
	protected static Event createFromStream(InputStream is) throws IOException {
		
		VariableLengthQuantity dt = VariableLengthQuantity.fromStream(is, true);		
		int bytesRead = dt.getByteCount(true);

		MidiFileUtils.logWriter().println("DeltaTime: " + dt.getAsLong());
		
		byte evt_start = (byte)is.read();
		bytesRead++;
		
		if ( (byte)(0xff) == evt_start) {
			s_lastEvent = MetaEvent.createFromStream(is);
		} else if ((byte)(0xf0) == MidiFileUtils.getUpper(evt_start)) {
			s_lastEvent = SysexEvent.createFromStream(evt_start, is);
		} else {
			s_lastEvent = MidiEvent.createFromStream(evt_start, is);
		}
		s_lastEvent.m_deltaTime = dt.getAsLong();
		bytesRead += s_lastEvent.getBytesRead();
		s_lastEvent.setBytesRead(bytesRead);
		return s_lastEvent;
	}
	
	public abstract int getEventType();
	
}
