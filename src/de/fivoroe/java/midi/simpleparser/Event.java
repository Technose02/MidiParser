package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;

public abstract class Event {

	protected static Event s_lastEvent = null;
	
	protected int  m_bytesRead;
	protected long m_deltaTime;
	
	protected Event() {
		m_bytesRead = 0;
	}
	
	final public static Event fromStream(InputStream is) throws IOException {
		
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
	
    protected final int getBytesRead() {
    	return m_bytesRead;
    }
    
    protected final void setBytesRead(int bytesRead) {
    	m_bytesRead = bytesRead;
    }
    
	public abstract int getEventType();
	
}
