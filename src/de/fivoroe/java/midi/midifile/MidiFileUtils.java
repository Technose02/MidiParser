package de.fivoroe.java.midi.midifile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class MidiFileUtils {
	
	public static final int   READ_BUFFER_SIZE = 1024;

	public static final int   MIDI_HEADER_LENGTH = 6;
	
	public static final short MIDI_EVENT  = 0;
	public static final short META_EVENT  = 1;
	public static final short SYSEX_EVENT = 2;
	
	public static final short MIDI_FORMAT_SINGLETRACK      = 0;
	public static final short MIDI_FORMAT_MULTITRACK_SYNC  = 1;
	public static final short MIDI_FORMAT_MULTITRACK_ASYNC = 2;
	
	public static final String MTHD = "MThd";
	public static final String MTRK = "MTrk";
	
	
	private static PrintWriter m_logWriter;


	public static boolean isDataByte(byte b) {
		return Byte.toUnsignedInt(b) < 128;
	}
	
	public static byte getUpper(byte b) {
		return (byte)(b & ((byte)0xF0));
	}
	
	public static byte getLower(byte b) {
		return (byte)(b & ((byte)0x0F));
	}
	
	public static String getHexString(byte[] data, int start, int l) {
		if (start == 0 && l == data.length) {
			return javax.xml.bind.DatatypeConverter.printHexBinary(data);
		}
		byte[] sub = new byte[l];
		int k = 0;
		while (k < l) {
			sub[k] = data[start + k];
			k++;
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(sub);
	}
	
	public static String getHexString(byte data) {
		return javax.xml.bind.DatatypeConverter.printHexBinary(new byte[]{data});
	}
	
	public static void reverseByteArray(byte[] ba) {
		int L = ba.length;
		for (int i = 0; i < L / 2; i++) {
			byte b = ba[ L - i - 1];
			ba[ L - i - 1] = ba[i];
			ba[i] = b;
		}
	}
	
	public static void setLogWriter(String filename) {
		MidiFileUtils.disposeLogWriter();
		try {
			m_logWriter = new PrintWriter(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PrintWriter logWriter() {
		return m_logWriter;
	}
	
	public static void disposeLogWriter() {
		if (m_logWriter != null) {
			m_logWriter.flush();
			m_logWriter.close();
		}
	}
	
}
