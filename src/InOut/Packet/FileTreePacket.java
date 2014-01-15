package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utils.MyFile;

public class FileTreePacket implements Packet {

	private ArrayList<MyFile> list;

	public FileTreePacket() {

	}

	public FileTreePacket(ArrayList<MyFile> ar) {
		this.list = ar;
	}

	public byte[] build() {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this.list);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public ArrayList<MyFile> getList() {
		return this.list;
	}

	public void parse(byte[] packet) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			this.list = (ArrayList<MyFile>) in.readObject();
		} catch (final Exception e) {}
	}
}
