package Packet;

import inout.Protocol;

import java.nio.ByteBuffer;

public class TransportPacket implements Packet {

	private int totalLength;
	private int awaitedLength;
	private int localLength;
	private boolean last;
	private short NumSeq;
	private int channel;
	private byte data[];

	private int fillingPosition;

	public TransportPacket() {
		this.awaitedLength = 0;
		this.fillingPosition = 0;

	}

	public TransportPacket(int tdl, int ll, int channel, boolean last, short nums, byte[] data) {
		this.totalLength = tdl;
		this.channel = channel;
		this.last = last;
		this.data = data;
		this.localLength = ll;
		this.NumSeq = nums;
	}

	public byte[] build() {
		final byte[] cmdToSend = new byte[Protocol.HEADER_LENGTH_DATA + this.data.length];
		final byte[] header = Protocol.dataHeaderGenerator(this.totalLength, this.localLength,
			this.last, this.NumSeq, this.channel);
		System.arraycopy(header, 0, cmdToSend, 0, header.length);
		System.arraycopy(this.data, 0, cmdToSend, header.length, this.data.length);

		return cmdToSend;
	}

	public void dataFilling(ByteBuffer buffer, int length) {
		/*
		 * System.out.println("Taille buffer.remaining : "+buffer.remaining());
		 * System.out.println("Taille buffer.limit : "+buffer.limit());
		 * System.out.println("Taille buffer.pos : "+buffer.position());
		 * System.out.println("Taille fillig : "+fillingPosition);
		 * System.out.println("Taille partialData : "+partialData.length);
		 * System.out.println("Taille length : "+length);
		 */
		if (this.data == null)
			this.data = new byte[this.localLength];

		buffer.get(this.data, this.fillingPosition, length);
		this.fillingPosition += length;
		this.awaitedLength = this.localLength - this.fillingPosition;

	}

	public int getChannel() {
		return this.channel;
	}

	public byte[] getData() {
		return this.data;
	}

	public int getLocalLength() {
		return this.localLength;
	}

	public short getNumSeq() {
		return this.NumSeq;
	}

	public int getTotalLength() {
		return this.totalLength;
	}

	public boolean isLast() {
		return this.last;
	}

	public void parse(byte[] packet) {
		final ByteBuffer b = ByteBuffer.wrap(packet);

		this.totalLength = b.getInt();
		this.localLength = b.getInt();

		final byte checkLast = b.get();
		if (checkLast == (byte) 1)
			this.last = true;
		else
			this.last = false;

		this.NumSeq = b.getShort();
		this.channel = b.getInt();
		this.data = new byte[b.remaining()];
		b.get(this.data, 0, b.remaining());
	}

	public boolean parse(ByteBuffer buffer) throws Exception {

		this.totalLength = buffer.getInt();
		this.localLength = buffer.getInt();

		final byte lst = buffer.get();
		if (lst == 1)
			this.last = true;
		else
			this.last = false;

		this.NumSeq = buffer.getShort();
		this.channel = buffer.getInt();
		/*
		 * System.out.println("Taille totale de la donn�e : " + totalLength);
		 * System.out.println("Taille des donn�es du paquet : " + localLength);
		 * System.out.println("Dernier paquet : " + last);
		 * System.out.println("Position du paquet : " + NumSeq);
		 * System.out.println("Canal:" + channel);
		 * System.out.println("Recuperation de la donnee");
		 */
		// si la place restante dans le buffer est insuffisante
		if ((buffer.limit() - buffer.position()) < this.localLength) {

			this.dataFilling(buffer, buffer.limit() - buffer.position());
			// System.out.println("une partie du packet a ete sauvegarde");
			return true;

		} else {
			// s'il y a assez de place, on sauvegarde tout le paquet
			this.data = new byte[this.localLength];
			buffer.get(this.data, 0, this.data.length);
			return false;

		}

	}

	public boolean parseCompleter(ByteBuffer buffer) throws Exception {
		// System.out.println("les donnees attendues sont de taille = " +
		// awaitedLength);

		// si la taille des donnees attendues depasse celle du buffer
		if (buffer.limit() - buffer.position() < this.awaitedLength) {

			// on en recupere autant que l'on peut (taille du buffer)
			this.dataFilling(buffer, buffer.limit() - buffer.position());
			return true;
		} else {

			// sinon on recupere la totalite
			this.dataFilling(buffer, this.awaitedLength);
			return false;
		}

	}

}
