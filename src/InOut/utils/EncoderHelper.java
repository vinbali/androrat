package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class EncoderHelper {

	public static ArrayList<String> decodeArrayList(byte[] data) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			return (ArrayList<String>) in.readObject();
		} catch (final Exception e) {
			return null;
		}
	}

	public static HashMap<String, String> decodeHashMap(byte[] data) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			return (HashMap<String, String>) in.readObject();
		} catch (final Exception e) {
			return null;
		}
	}

	public static HashSet<String> decodeHashSet(byte[] data) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			return (HashSet<String>) in.readObject();
		} catch (final Exception e) {
			return null;
		}
	}

	public static byte[] encodeArrayList(ArrayList<String> l) {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(l);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public static byte[] encodeHashMap(HashMap<String, String> h) {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(h);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public static byte[] encodeHashSet(HashSet<String> l) {
		if (l == null)
			return null;
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(l);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public static byte[] encodeLong(long l) {
		final ByteBuffer b = ByteBuffer.allocate(8);
		b.putLong(l);
		return b.array();
	}
}
