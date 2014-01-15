package Packet;

import java.io.Serializable;
import java.nio.ByteBuffer;


public class CallPacket implements Packet, Serializable {

	private static final long serialVersionUID = 3972539952673409279L;

	private int id;
	private int type;
	private long date;
	private long duration;
	private int contact_id;
	private int phoneNumberSize;
	private String phoneNumber;
	private int nameSize;
	private String name;

	public CallPacket() {}

	public CallPacket(int id, int type, long date, long duration, int contact_id, String number,
		String name) {
		this.id = id;
		this.type = type;
		this.date = date;
		this.duration = duration;
		this.contact_id = contact_id;
		this.phoneNumber = number;
		if (this.phoneNumber != null)
			this.phoneNumberSize = number.length();
		else
			this.phoneNumberSize = 0;
		this.name = name;
		if (name != null)
			this.nameSize = name.length();
		else
			this.nameSize = 0;
	}

	public byte[] build() {
		final ByteBuffer b = ByteBuffer.allocate(4 * 5 + 8 * 2 + this.phoneNumberSize
			+ this.nameSize);
		b.putInt(this.id);
		b.putInt(this.type);
		b.putLong(this.date);
		b.putLong(this.duration);
		b.putInt(this.contact_id);
		b.putInt(this.phoneNumberSize);
		b.put(this.phoneNumber.getBytes());
		b.putInt(this.nameSize);
		b.put(this.name.getBytes());
		return b.array();
	}

	public int getContact_id() {
		return this.contact_id;
	}

	public long getDate() {
		return this.date;
	}

	public long getDuration() {
		return this.duration;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getNameSize() {
		return this.nameSize;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public int getPhoneNumberSize() {
		return this.phoneNumberSize;
	}

	public int getType() {
		return this.type;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);
		this.id = b.getInt();
		this.type = b.getInt();
		this.date = b.getLong();
		this.duration = b.getLong();
		this.contact_id = b.getInt();
		this.phoneNumberSize = b.getInt();
		byte[] tmp = new byte[this.phoneNumberSize];
		b.get(tmp);
		this.phoneNumber = new String(tmp);
		this.nameSize = b.getInt();
		tmp = new byte[this.nameSize];
		b.get(tmp);
		this.name = new String(tmp);
	}
}
