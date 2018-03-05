package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;

public class SysexEvent extends Event {

	@Override
	public int getEventType() {
		return MidiFileUtils.SYSEX_EVENT;
	}
	
	private SysexEvent() {
		super();
	}
	
	protected static SysexEvent createFromStream(byte lower, InputStream is) throws IOException {
		SysexEvent e = new SysexEvent();
		MidiFileUtils.logWriter().println("Event ist Sysex-Event");
		e.init(lower, is);
		return e;
	}
	
	private void init(byte lower, InputStream is) throws IOException {
		MidiFileUtils.logWriter().println("  [Identifier 0x" + MidiFileUtils.getHexString(lower) + "]");
		byte[] buff = new byte[255];
		VariableLengthQuantity tmp = VariableLengthQuantity.fromStream(is, true);
		m_bytesRead += tmp.getByteCount(true);
		long l_ = tmp.getAsLong();
		
		MidiFileUtils.logWriter().println("  Length ist " + l_ + " [0x" + tmp.getHexString(true) + " -> 0x" + tmp.getHexString(false) + "]");

		if (buff.length < (int)l_) {
			buff = new byte[(int)l_];
		}
		int d = is.read(buff, 0, (int)l_);
		m_bytesRead += d;
		if (l_ > 0) {
			MidiFileUtils.logWriter().println("  Data ist 0x" + MidiFileUtils.getHexString(buff, 0, d));
		}
	}
}
