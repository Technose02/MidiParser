package de.fivoroe.java.midi.simpleparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;

public class VariableLengthQuantity {

	private byte[] m_encodedValue = null;
	private byte[] m_decodedValue = null;
	
	private void decode() {
		if (m_encodedValue.length == 1) {
			m_decodedValue = new byte[]{ m_encodedValue[0] };
			return;
		}
		
		try {
			MidiFileUtils.reverseByteArray(m_encodedValue);
			
			BitSet bsDec = new BitSet(Math.max(8, m_encodedValue.length * 7));
			BitSet bsIn = BitSet.valueOf(m_encodedValue);
			
			int kDec = 0;
			int kIn = 0;
			for (int i = 0; i < m_encodedValue.length; i++) {
				for (int j = 0; j < 7; j++) {
					if (bsIn.get(kIn++)) {
						bsDec.set(kDec);
					}
					kDec++;
				}
				kIn++;
			}
			m_decodedValue = bsDec.toByteArray();			
			MidiFileUtils.reverseByteArray(m_decodedValue);
		} finally {
			MidiFileUtils.reverseByteArray(m_encodedValue);
		}
	}
	
	private void updateValue(byte[] newValue, boolean encoded) {
		if (encoded) {
			m_encodedValue = newValue;
			decode();
			return;
		}
		m_decodedValue = newValue;
		encode();
	}
	
	public int getByteCount(boolean encoded) {
		if (encoded) {
			return m_encodedValue.length;
		}
		return m_decodedValue.length;
	}
	
	public String getHexString(boolean encoded) {
		String hex = null;
		if (encoded) {
			hex = javax.xml.bind.DatatypeConverter.printHexBinary(m_encodedValue);
		} else {
			hex = javax.xml.bind.DatatypeConverter.printHexBinary(m_decodedValue);
		}
		String ret = "";
		int i = 0;
		while (i < hex.length()) {
			ret += hex.substring(i, i + 2) + " ";
			i += 2;
		}
		return ret.trim();
	}

	public String getBinaryString(boolean encoded) {
		StringBuilder sb = new StringBuilder();
		
		byte[] src = m_decodedValue;
		if (encoded) {
			src = m_encodedValue;
		}
		
		try {
			MidiFileUtils.reverseByteArray(src);
			int L = src.length;
			BitSet b_ = BitSet.valueOf(src);
			for (int i = 0; i < L; i++)
			{
				sb.append(" ");
				for (int j = 0; j < 8; j++) {
					if (b_.get(i * 8 + j)) {
						sb.append("1");
					} else {
						sb.append("0");
					}
				}
			}
		} finally {
			MidiFileUtils.reverseByteArray(src);
		}
		
		return sb.reverse().toString().trim();
	}
	
	private VariableLengthQuantity() {
	}
	
	public void writeToStream(OutputStream os, boolean encoded) {
		try {
			if (encoded) {
				os.write(m_encodedValue);
			} else {
				os.write(m_decodedValue);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getAsLong() {
		long ret = (long)(m_decodedValue[0] & 0xff);
		for (int i = 1; i < m_decodedValue.length; i++) {
			int cur = Byte.toUnsignedInt(m_decodedValue[i]);
			ret = 256 * ret + cur;
		}
		return ret;
	}
	
	public int getAsInt() {
		return (int)getAsLong();
	}
	
	public static VariableLengthQuantity fromByteArray(byte[] value, boolean encoded) {
		VariableLengthQuantity ret = new VariableLengthQuantity();
		ret.updateValue(value, encoded);
		return ret;
	}
	
	private void encode() {
		try {
			
			// Ermittle die Anzahl der verwendeten Bits im dekodierten
		    // Array:
			int nbits = m_decodedValue.length * 8;
			int first = Byte.toUnsignedInt(m_decodedValue[0]);
			int cmp = 128;
			while (first < cmp && cmp > 1) {
				nbits--;
				cmp /= 2;
			}

			// Encoding
			MidiFileUtils.reverseByteArray(m_decodedValue);
			
			BitSet bsEnc = new BitSet(nbits);
			BitSet bsIn = BitSet.valueOf(m_decodedValue);
			
			int kEnc = 0;
			int kIn = 0;
			boolean zero = true;
			while (kIn <= nbits) {
				for (int j = 0; j < 7; j++) {
					if (bsIn.get(kIn++)) {
						bsEnc.set(kEnc);
					}
					kEnc++;
				}
				if (zero) {
					kEnc++;
					zero = false;
				} else {
					bsEnc.set(kEnc++);
				}
			}
			m_encodedValue = bsEnc.toByteArray();			
			MidiFileUtils.reverseByteArray(m_encodedValue);
		} finally {
			MidiFileUtils.reverseByteArray(m_decodedValue);
		}
	}
	
	public static VariableLengthQuantity fromStream(InputStream is, boolean encoded) {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		VariableLengthQuantity ret = null;
		try {
			byte cur;
			do {
				cur = (byte)is.read();
				bytes.add(cur);
			} while (!MidiFileUtils.isDataByte(cur));
			
			byte[] a = new byte[bytes.size()];
			for (int i = 0; i < bytes.size(); i++) {
				a[i] = bytes.get(i);
			}
			ret = VariableLengthQuantity.fromByteArray(a, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public static VariableLengthQuantity fromLong(long value) {
		ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
		bb.putLong(value);
		VariableLengthQuantity ret = new VariableLengthQuantity();
		byte[] a = bb.array();
		int k = 0;
		while (a[k++] == 0x0);
		k--;
		byte[] a_ = new byte[a.length - k];
		for (int i = 0; i < a.length - k; i ++) {
			a_[i] = a[i + k];
		}
			
		ret.updateValue(a_, false);
		return ret;
	}
}
