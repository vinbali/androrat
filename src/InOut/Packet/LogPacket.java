package Packet;

import java.nio.ByteBuffer;


public class LogPacket implements Packet {

	long date;
	byte type; // 0 ok / 1 Error
	String message;

	public LogPacket() {

	}

	public LogPacket(long date, byte type, String message) {
		this.date = date;
		this.type = type;
		this.message = message;
	}

	public byte[] build() {
		final ByteBuffer b = ByteBuffer.allocate(9 + this.message.length());
		b.putLong(this.date);
		b.put(this.type);
		b.put(this.message.getBytes());
		return b.array();
	}

	public long getDate() {
		return this.date;
	}

	public String getMessage() {
		return this.message;
	}

	public byte getType() {
		return this.type;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);
		this.date = b.getLong();
		this.type = b.get();
		final byte[] tmp = new byte[b.remaining()];
		b.get(tmp);
		this.message = new String(tmp);
	}

}
