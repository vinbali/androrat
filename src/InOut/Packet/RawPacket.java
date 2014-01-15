package Packet;

public class RawPacket implements Packet {

	private byte[] data;

	public RawPacket() {
		// Nothing
	}

	public RawPacket(byte[] data) {
		this.data = data;
	}

	public byte[] build() {
		return this.data;
	}

	public byte[] getData() {
		return this.data;
	}

	public void parse(byte[] packet) {
		this.data = packet;
	}
}
