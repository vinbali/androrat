package Packet;

import java.nio.ByteBuffer;


public class CommandPacket implements Packet {
	private short commande;
	private int targetChannel;
	private byte[] argument;

	public CommandPacket() {

	}

	public CommandPacket(short cmd, int targetChannel, byte[] arg) {
		this.commande = cmd;
		this.argument = arg;
		this.targetChannel = targetChannel;
	}

	public byte[] build() {
		final byte[] byteCmd = ByteBuffer.allocate(2).putShort(this.commande).array();
		final byte[] byteTargChan = ByteBuffer.allocate(4).putInt(this.targetChannel).array();
		final byte[] cmdToSend = new byte[byteCmd.length + byteTargChan.length
			+ this.argument.length];

		System.arraycopy(byteCmd, 0, cmdToSend, 0, byteCmd.length);
		System.arraycopy(byteTargChan, 0, cmdToSend, byteCmd.length, byteTargChan.length);
		System.arraycopy(this.argument, 0, cmdToSend, byteCmd.length + byteTargChan.length,
			this.argument.length);

		return cmdToSend;
	}

	public byte[] getArguments() {
		return this.argument;
	}

	public short getCommand() {
		return this.commande;
	}

	public int getTargetChannel() {
		return this.targetChannel;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);
		this.commande = b.getShort();
		this.targetChannel = b.getInt();
		this.argument = new byte[b.remaining()];
		b.get(this.argument, 0, b.remaining());
	}

	public void parse(ByteBuffer b) {
		this.commande = b.getShort();
		this.targetChannel = b.getInt();
		this.argument = new byte[b.remaining()];
		b.get(this.argument, 0, b.remaining());
	}

}
