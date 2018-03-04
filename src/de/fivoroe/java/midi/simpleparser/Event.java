package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;

public abstract class Event {

	public static final int MIDI_EVENT  = 0;
	public static final int META_EVENT  = 1;
	public static final int SYSEX_EVENT = 2;

	protected static Event s_lastEvent = null;
	
	protected int  m_bytesRead;
	protected long m_deltaTime;
	
	protected Event() {
		m_bytesRead = 0;
	}
	
	public static boolean isDataByte(byte b) {
		return Byte.toUnsignedInt(b) < 128;
	}
	
	public static byte getUpper(byte b) {
		return (byte)(b & ((byte)0xF0));
	}
	
	public static byte getLower(byte b) {
		return (byte)(b & ((byte)0x0F));
	}
	
	public static Event fromStream(InputStream is) {
		VariableLengthQuantity dt = VariableLengthQuantity.fromStream(is, true);		
		int bytesRead = dt.getByteCount(true);

		GlobalWriter.getWriter().println("DeltaTime: " + dt.getAsLong());
		
		try {
			byte evt_start = (byte)is.read();
			bytesRead++;
			
			if ( (byte)(0xff) == evt_start) {
				s_lastEvent = new MetaEvent(is);
			} else if ((byte)(0xf0) == getUpper(evt_start)) {
				s_lastEvent = new SysexEvent(evt_start, is);
			} else {
				s_lastEvent = new MidiEvent(evt_start, is);
			}
			s_lastEvent.m_deltaTime = dt.getAsLong();
			bytesRead += s_lastEvent.getBytesRead();
			s_lastEvent.setBytesRead(bytesRead);
			return s_lastEvent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
    protected final int getBytesRead() {
    	return m_bytesRead;
    }
    
    protected final void setBytesRead(int bytesRead) {
    	m_bytesRead = bytesRead;
    }
    
	public abstract int getEventType();
	
}
