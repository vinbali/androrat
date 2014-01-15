package Packet;

import java.nio.ByteBuffer;


public class CallStatusPacket implements Packet {

	int type;
	/*
	 * 1 -> Incoming call 2 -> Missed call 3 -> Call accepted 4 -> Call send 5
	 * -> Hang Up
	 */
	String phonenumber;

	public CallStatusPacket() {

	}

	public CallStatusPacket(int type, String phone) {
		this.type = type;
		this.phonenumber = phone;
	}

	public byte[] build() {
		ByteBuffer b;
		if (this.phonenumber == null) {
			b = ByteBuffer.allocate(4);
			b.putInt(this.type);
		} else {
			b = ByteBuffer.allocate(4 + this.phonenumber.length());
			b.putInt(this.type);
			b.put(this.phonenumber.getBytes());
		}
		return b.array();
	}

	public String getPhonenumber() {
		return this.phonenumber;
	}

	public int getType() {
		return this.type;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);
		this.type = b.getInt();
		if (b.hasRemaining()) {
			final byte[] tmp = new byte[b.remaining()];
			b.get(tmp);
			this.phonenumber = new String(tmp);
		} else
			this.phonenumber = null;
	}
}
