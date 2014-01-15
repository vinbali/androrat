//package InOut;

package out;

import in.Demux;
import in.Receiver;
import inout.Controler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Connection {
	Socket s;
	String ip = "localhost";
	int port = 5555;
	DataOutputStream out;
	DataInputStream in;

	boolean stop = false;
	ByteBuffer readInstruction;
	Mux m;
	Demux dem;
	Controler controler;
	Receiver receive;

	public Connection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public Connection(String ip, int port, Controler ctrl) {
		this.ip = ip;
		this.port = port;
		this.controler = ctrl;
	}

	public boolean accept(ServerSocket ss) {
		try {
			this.s = ss.accept();

			this.in = new DataInputStream(this.s.getInputStream());
			this.out = new DataOutputStream(this.s.getOutputStream());
			this.m = new Mux(this.out);
			return true;

		} catch (final IOException e) {

			e.printStackTrace();
			return false;
		}
	}

	public boolean connect() {
		try {
			this.s = new Socket(this.ip, this.port);
			this.in = new DataInputStream(this.s.getInputStream());
			this.out = new DataOutputStream(this.s.getOutputStream());
			this.m = new Mux(this.out);
			this.dem = new Demux(this.controler, "moi");
			this.receive = new Receiver(this.s);
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ByteBuffer getInstruction() throws Exception {
		this.readInstruction = this.receive.read();

		if (this.dem.receive(this.readInstruction))
			this.readInstruction.compact();
		else
			this.readInstruction.clear();

		return this.readInstruction;
	}

	public boolean reconnect() {
		return this.connect();
	}

	public void sendData(int chan, byte[] packet) {
		this.m.send(chan, packet);
	}

	public void stop() {
		if (null == s)
			return;
		try {
			this.s.close();
		} catch (final IOException e) {

		}
	}
}