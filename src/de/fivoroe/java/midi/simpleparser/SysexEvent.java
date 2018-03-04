package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;

public class SysexEvent extends Event {

	@Override
	public int getEventType() {
		return Event.SYSEX_EVENT;
	}
	
	protected SysexEvent(byte lower, InputStream is) {
		super();
		GlobalWriter.getWriter().println("Event ist Sysex-Event");
		GlobalWriter.getWriter().println("  [Identifier 0x" + MidiFile.getHexString(lower) + "]");
		try {
			byte[] buff = new byte[1024];
			VariableLengthQuantity tmp = VariableLengthQuantity.fromStream(is, true);
			m_bytesRead += tmp.getByteCount(true);
			long l_ = tmp.getAsLong();
			
			GlobalWriter.getWriter().println("  Length ist " + l_ + " [0x" + tmp.getHexString(true) + " -> 0x" + tmp.getHexString(false) + "]");

			if (buff.length < (int)l_) {
				buff = new byte[(int)l_];
			}
			int d = is.read(buff, 0, (int)l_);
			m_bytesRead += d;
			if (l_ > 0) {
				GlobalWriter.getWriter().println("  Data ist 0x" + MidiFile.getHexString(buff, 0, d));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
