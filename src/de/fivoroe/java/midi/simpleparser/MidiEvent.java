package de.fivoroe.java.midi.simpleparser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MidiEvent extends Event {
	
	private byte    m_statusByte;

	private void process(byte firstDataByte, InputStream is) {
		try {
			byte[] buff = new byte[1024];
			
			switch (getUpper(m_statusByte)) {
				case (byte)0xB0:
					GlobalWriter.getWriter().println("  [Kontrolländerung Kanal 0x"
				                      + MidiFile.getHexString(getLower(m_statusByte))
				                      + "]");
					byte ctrlnr = firstDataByte;
					GlobalWriter.getWriter().println("  Kontrollnummer ist 0x" + MidiFile.getHexString(ctrlnr));
					byte val = (byte)is.read();
					m_bytesRead++;
					GlobalWriter.getWriter().println("  Neuer Wert ist 0x" + MidiFile.getHexString(val));
					break;
				case (byte)0x90:
					GlobalWriter.getWriter().println("  [Note an Kanal 0x"
									  + MidiFile.getHexString(getLower(m_statusByte))
									  + "]");
					byte note = firstDataByte;
					GlobalWriter.getWriter().println("  Note ist 0x" + MidiFile.getHexString(note));
					byte vel = (byte)is.read();
					m_bytesRead++;
					GlobalWriter.getWriter().println("  Geschwindigkeit ist 0x" + MidiFile.getHexString(vel));
					break;
				default:
					GlobalWriter.getWriter().println("  Unbekannt: 0x" + MidiFile.getHexString(getUpper(m_statusByte)));
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					byte cur = firstDataByte;
					bao.write(new byte[] {cur});
					while (!isDataByte(cur)) {
						cur = (byte)is.read();
						m_bytesRead++;
						bao.write(new byte[] {cur});
					}
					bao.flush();
					VariableLengthQuantity tmp = VariableLengthQuantity.fromByteArray(bao.toByteArray(), true);
					int l_ = tmp.getAsInt();
					GlobalWriter.getWriter().println("  Length ist " + l_ + " [0x" + tmp.getHexString(true) + " -> 0x" + tmp.getHexString(false) + "]");

					if (buff.length < l_) {
						buff = new byte[l_];
					}
					int d = is.read(buff, 0, l_);
					m_bytesRead += d;
					if (l_ > 0) {
						GlobalWriter.getWriter().println("  Data ist 0x" + MidiFile.getHexString(buff, 0, d));
					}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected MidiEvent(byte first, InputStream is) {
		super();
		GlobalWriter.getWriter().println("Event ist Midi-Event");
		try {
			if (isDataByte(first)) {
				// running mode, Status nicht wiederholt.
				if (Event.s_lastEvent.getEventType() == Event.MIDI_EVENT) {
					m_statusByte = ((MidiEvent)s_lastEvent).m_statusByte;
					process(first, is);
				} else {
					System.err.println("  DatenByte gelesen, Statusbyte erwartet!");
					// Fehler!!!
				}
			} else {
				// first ist Status, kein running mode
				m_statusByte = first;
				byte data = (byte)is.read();
				m_bytesRead++;
				process(data, is);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int getEventType() {
		
		return Event.MIDI_EVENT;
	}

}
