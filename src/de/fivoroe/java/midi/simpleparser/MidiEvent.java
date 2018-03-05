package de.fivoroe.java.midi.simpleparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MidiEvent extends Event {
	
	private byte    m_statusByte;

	private void init(byte firstDataByte, InputStream is) throws IOException {
		byte[] buff = new byte[1024];
		
		switch (MidiFileUtils.getUpper(m_statusByte)) {
			case (byte)0xB0:
				MidiFileUtils.logWriter().println("  [Kontrolländerung Kanal 0x"
			                      + MidiFileUtils.getHexString(MidiFileUtils.getLower(m_statusByte))
			                      + "]");
				byte ctrlnr = firstDataByte;
				MidiFileUtils.logWriter().println("  Kontrollnummer ist 0x" + MidiFileUtils.getHexString(ctrlnr));
				byte val = (byte)is.read();
				m_bytesRead++;
				MidiFileUtils.logWriter().println("  Neuer Wert ist 0x" + MidiFileUtils.getHexString(val));
				break;
			case (byte)0x90:
				MidiFileUtils.logWriter().println("  [Note an Kanal 0x"
								  + MidiFileUtils.getHexString(MidiFileUtils.getLower(m_statusByte))
								  + "]");
				byte note = firstDataByte;
				MidiFileUtils.logWriter().println("  Note ist 0x" + MidiFileUtils.getHexString(note));
				byte vel = (byte)is.read();
				m_bytesRead++;
				MidiFileUtils.logWriter().println("  Geschwindigkeit ist 0x" + MidiFileUtils.getHexString(vel));
				break;
			default:
				MidiFileUtils.logWriter().println("  Unbekannt: 0x" + MidiFileUtils.getHexString(MidiFileUtils.getUpper(m_statusByte)));
				ByteArrayOutputStream bao = new ByteArrayOutputStream();
				byte cur = firstDataByte;
				bao.write(new byte[] {cur});
				while (!MidiFileUtils.isDataByte(cur)) {
					cur = (byte)is.read();
					m_bytesRead++;
					bao.write(new byte[] {cur});
				}
				bao.flush();
				VariableLengthQuantity tmp = VariableLengthQuantity.fromByteArray(bao.toByteArray(), true);
				int l_ = tmp.getAsInt();
				MidiFileUtils.logWriter().println("  Length ist " + l_ + " [0x" + tmp.getHexString(true) + " -> 0x" + tmp.getHexString(false) + "]");

				if (buff.length < l_) {
					buff = new byte[l_];
				}
				int d = is.read(buff, 0, l_);
				m_bytesRead += d;
				if (l_ > 0) {
					MidiFileUtils.logWriter().println("  Data ist 0x" + MidiFileUtils.getHexString(buff, 0, d));
				}
		}
	}
	
	private MidiEvent() {
		super();
	}
	
	protected static MidiEvent createFromStream(byte first, InputStream is) throws IOException {
		MidiEvent e = new MidiEvent();
		MidiFileUtils.logWriter().println("Event ist Midi-Event");
		if (MidiFileUtils.isDataByte(first)) {
			// running mode, Status nicht wiederholt.
			if (Event.s_lastEvent.getEventType() == MidiFileUtils.MIDI_EVENT) {
				e.m_statusByte = ((MidiEvent)s_lastEvent).m_statusByte;
				e.init(first, is);
			} else {
				System.err.println("  DatenByte gelesen, Statusbyte erwartet!");
				// Fehler!!!
			}
		} else {
			// first ist Status, kein running mode
			e.m_statusByte = first;
			byte data = (byte)is.read();
			e.m_bytesRead++;
			e.init(data, is);
		}
		return e;
	}
	
	@Override
	public int getEventType() {		
		return MidiFileUtils.MIDI_EVENT;
	}

}
