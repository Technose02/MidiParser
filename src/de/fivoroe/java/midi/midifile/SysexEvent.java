package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SysexEvent extends Event {
	
	private byte m_lower;

	private SysexEvent() {
		super();
	}
	
	protected static SysexEvent createFromStream(byte lower, InputStream is) throws IOException {
		SysexEvent e = new SysexEvent();
		MidiFileUtils.logWriter().println("Event ist Sysex-Event");
		e.m_lower = lower;
		e.readFromStream(is);
		return e;
	}
	
	@Override
	protected void readFromStream(InputStream is) throws IOException {
		MidiFileUtils.logWriter().println("  [Identifier 0x" + MidiFileUtils.getHexString(m_lower) + "]");
		byte[] buff = new byte[MidiFileUtils.READ_BUFFER_SIZE];
		VariableLengthQuantity tmp = VariableLengthQuantity.fromStream(is, true);
		setBytesRead(getBytesRead() + tmp.getByteCount(true));
		long l_ = tmp.getAsLong();
		
		MidiFileUtils.logWriter().println("  Length ist " + l_ + " [0x" + tmp.getHexString(true) + " -> 0x" + tmp.getHexString(false) + "]");

		if (buff.length < (int)l_) {
			buff = new byte[(int)l_];
		}
		int d = is.read(buff, 0, (int)l_);
		setBytesRead(getBytesRead() + d);
		if (l_ > 0) {
			MidiFileUtils.logWriter().println("  Data ist 0x" + MidiFileUtils.getHexString(buff, 0, d));
		}
	}
	
	@Override
	public void writeToStream(OutputStream os) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getEventType() {
		return MidiFileUtils.SYSEX_EVENT;
	}
	
}
