package de.fivoroe.java.midi.simpleparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SimpleParser {

	
	public static void Test1(String[] args) {
		
		// 1354358762349876799
		//
		// ‭      12       CB       A6       1E       60       4D       56       3F
		// ‭00010010 11001011 10100110 00011110 01100000 01001101 01010110 00111111
		//
		//
		//       92       E5       E9       C3       E6       82       B5       AC
		// 10010010 11100101 11101001 11000011 11100110 10000010 10110101 00101100
		
//		byte[] input = new byte[]{(byte)0x92, (byte)0xe5, (byte)0xe9, (byte)0xc3, (byte)0xe6, (byte)0x82, (byte)0xb5, (byte)0xac};
		
		byte[] input = new byte[]{(byte)0xBA, (byte)0xEF, (byte)0x9A, (byte)0x15};
		
		VariableLengthQuantity mdt = VariableLengthQuantity.fromByteArray(input, true);
		System.out.println("Hex (decoded)   : " + mdt.getHexString(false));
		System.out.println("Binary (decoded): " + mdt.getBinaryString(false));
		System.out.println("");
		System.out.println("Hex (encoded)   : " + mdt.getHexString(true));
		System.out.println("Binary (encoded): " + mdt.getBinaryString(true));
		System.out.println("");
		System.out.println("Long            : " + mdt.getAsLong());
	}
	
	public static void Test2(String[] args) {
		VariableLengthQuantity mdt = VariableLengthQuantity.fromLong(123456789);
		System.out.println("Hex (decoded)   : " + mdt.getHexString(false));
		System.out.println("Binary (decoded): " + mdt.getBinaryString(false));
		System.out.println("");
		System.out.println("Hex (encoded)   : " + mdt.getHexString(true));
		System.out.println("Binary (encoded): " + mdt.getBinaryString(true));
		System.out.println("");
		System.out.println("Long            : " + mdt.getAsLong());
	}
	
	public static void Test3(String[] args) {
		long v1 = 123456;
		long v2 = 789000000;
		
		VariableLengthQuantity mdt1 = VariableLengthQuantity.fromLong(v1);
		VariableLengthQuantity mdt2 = VariableLengthQuantity.fromLong(v2);
		System.out.println(mdt1.getAsLong());
		System.out.println("Binary (decoded): " + mdt1.getBinaryString(false));
		System.out.println("Binary (encoded): " + mdt1.getBinaryString(true));
		System.out.println(mdt2.getAsLong());
		System.out.println("Binary (decoded): " + mdt2.getBinaryString(false));
		System.out.println("Binary (encoded): " + mdt2.getBinaryString(true));
		
		try (ByteArrayOutputStream bao = new ByteArrayOutputStream(1024)) {
			mdt1.writeToStream(bao, true);
			mdt2.writeToStream(bao, true);
			
			byte[] a = bao.toByteArray();
			ByteArrayInputStream bai = new ByteArrayInputStream(a);
			mdt1 = VariableLengthQuantity.fromStream(bai, true);
			mdt2 = VariableLengthQuantity.fromStream(bai, true);
			System.out.println(mdt1.getAsLong());
			System.out.println("Binary (decoded): " + mdt1.getBinaryString(false));
			System.out.println("Binary (encoded): " + mdt1.getBinaryString(true));
			System.out.println(mdt2.getAsLong());
			System.out.println("Binary (decoded): " + mdt2.getBinaryString(false));
			System.out.println("Binary (encoded): " + mdt2.getBinaryString(true));
			bai.close();
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static void main(String[] args) {
		
//		Test3(args);
		MidiFileUtils.setLogWriter("midi.txt");
		new MidiFile(args[0]);
		MidiFileUtils.disposeLogWriter();
		System.out.println("done!");
	}

}
