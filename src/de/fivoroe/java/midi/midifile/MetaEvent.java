package de.fivoroe.java.midi.midifile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MetaEvent extends Event {

	private MetaEvent() {
		super();
	}
	
	protected static MetaEvent createFromStream(InputStream is) throws IOException {
		MetaEvent e = new MetaEvent();
		MidiFileUtils.logWriter().println("Event ist Meta-Event");
		e.readFromStream(is);
		return e;
	}
	
	@Override
	protected void readFromStream(InputStream is) throws IOException {
		byte[] buff = new byte[MidiFileUtils.READ_BUFFER_SIZE];
		byte cmd = (byte)is.read();
		setBytesRead(getBytesRead() + 1);
		MidiFileUtils.logWriter().println("  Command ist 0x" + MidiFileUtils.getHexString(cmd));
		switch (cmd) {
			default:
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
	}
	
	@Override
	public void writeToStream(OutputStream os) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getEventType() {
		return MidiFileUtils.META_EVENT;
	}

}
