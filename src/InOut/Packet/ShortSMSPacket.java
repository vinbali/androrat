package Packet;

import java.nio.ByteBuffer;


public class ShortSMSPacket implements Packet {

	private int address_size;
	private String address;
	private long date;
	private int body_size;
	private String body;

	public ShortSMSPacket() {

	}

	public ShortSMSPacket(String ad, long dat, String body) {
		this.address = ad;
		this.address_size = ad.length();
		this.date = dat;
		this.body = body;
		this.body_size = this.body.length();
	}

	public byte[] build() {
		final ByteBuffer b = ByteBuffer.allocate(4 + 4 + this.address.length() + 4 + 4 + 8 + 4
			+ this.body.length() + 4);
		b.putInt(this.address_size);
		b.put(this.address.getBytes());
		b.putLong(this.date);
		b.putInt(this.body_size);
		b.put(this.body.getBytes());
		return b.array();
	}

	public String getAddress() {
		return this.address;
	}

	public String getBody() {
		return this.body;
	}

	public long getDate() {
		return this.date;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);
		this.address_size = b.getInt();
		byte[] tmp = new byte[this.address_size];
		b.get(tmp);
		this.address = new String(tmp);
		this.date = b.getLong();
		this.body_size = b.getInt();
		tmp = new byte[this.body_size];
		b.get(tmp);
		this.body = new String(tmp);
	}
}