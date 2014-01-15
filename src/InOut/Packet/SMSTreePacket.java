package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class SMSTreePacket implements Packet {

	ArrayList<SMSPacket> list;

	public SMSTreePacket() {

	}

	public SMSTreePacket(ArrayList<SMSPacket> ar) {
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

	public ArrayList<SMSPacket> getList() {
		return this.list;
	}

	public void parse(byte[] packet) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			this.list = (ArrayList<SMSPacket>) in.readObject();
		} catch (final Exception e) {}
	}
}