package Packet;

import java.nio.ByteBuffer;


public class FilePacket implements Packet {

	byte[] data;
	byte mf;
	short numSeq;

	public FilePacket() {

	}

	public FilePacket(short num, byte mf, byte[] data) {
		this.data = data;
		this.numSeq = num;
		this.mf = mf;
	}

	public byte[] build() {
		final ByteBuffer b = ByteBuffer.allocate(this.data.length + 3);
		b.putShort(this.numSeq);
		b.put(this.mf);
		b.put(this.data);
		return b.array();
	}

	public byte[] getData() {
		return this.data;
	}

	public byte getMf() {
		return this.mf;
	}

	public short getNumSeq() {
		return this.numSeq;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);

		this.numSeq = b.getShort();
		this.mf = b.get();
		this.data = new byte[b.remaining()];
		b.get(this.data, 0, b.remaining());
	}

}
