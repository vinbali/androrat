package in;

import inout.Controler;
import inout.Protocol;

import java.nio.ByteBuffer;

import Packet.TransportPacket;

public class Demux {
	// acces au controler
	private final Controler controler;

	// un packet
	private TransportPacket p;

	// l'identifiant du client
	private String imei;

	// le buffer de lecture
	private ByteBuffer buffer;

	// variables de controle
	private boolean partialDataExpected, reading;

	public Demux(Controler s, String i) {
		this.imei = i;
		this.controler = s;
		this.reading = true;
		this.partialDataExpected = false;

	}

	public boolean receive(ByteBuffer buffer) throws Exception {

		while (this.reading) {

			if (!this.partialDataExpected)
				// si la taille du buffer est insuffisante
				if ((buffer.limit() - buffer.position()) < Protocol.HEADER_LENGTH_DATA)
					return true;

			// dans le cas d'un paquet partiellement recue
			if (this.partialDataExpected)
				this.partialDataExpected = this.p.parseCompleter(buffer);
			else {
				this.p = new TransportPacket();
				this.partialDataExpected = this.p.parse(buffer);
			}

			if (this.partialDataExpected)
				return true;
			else
				this.controler.Storage(this.p, this.imei);

		}

		this.reading = true;
		return true;
	}

	public void setImei(String i) {
		this.imei = i;
	}

}
