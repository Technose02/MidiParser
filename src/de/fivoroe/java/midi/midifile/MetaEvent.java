package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MetaEvent extends Event {

	@Override
	public int getEventType() {
		return MidiFileUtils.META_EVENT;
	}
	
	private MetaEvent() {
		super();
	}
	
	protected static MetaEvent createFromStream(InputStream is) throws IOException {
		MetaEvent e = new MetaEvent();
		MidiFileUtils.logWriter().println("Event ist Meta-Event");
		e.init(is);
		return e;
	}
	
	private void init(InputStream is) throws IOException {
		byte[] buff = new byte[1024];
		byte cmd = (byte)is.read();
		m_bytesRead++;
		MidiFileUtils.logWriter().println("  Command ist 0x" + MidiFileUtils.getHexString(cmd));
		switch (cmd) {
			default:
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

	@Override
	public void writeToStream(OutputStream os) {
		// TODO Auto-generated method stub
	}

}
