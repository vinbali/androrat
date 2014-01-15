package in;

import inout.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class Receiver {

	private final Socket socket;
	private final byte[] received_data;
	private ByteBuffer buffer;
	private final InputStream is;

	public Receiver(Socket s) throws IOException {
		this.socket = s;
		this.is = this.socket.getInputStream();

		this.received_data = new byte[Protocol.MAX_PACKET_SIZE];
		this.buffer = ByteBuffer.allocate(Protocol.MAX_PACKET_SIZE);
	}

	public ByteBuffer read() throws IOException, SocketException { // A
																	// supprimer
																	// !
		int n = 0;

		n = this.is.read(this.received_data);

		this.buffer.clear();
		this.buffer = ByteBuffer.wrap(this.received_data, 0, n);
		// System.out.println("data has been read:" + buffer.limit());

		return this.buffer;
	}

	public ByteBuffer read(ByteBuffer b) throws IOException, SocketException {
		int n = 0;

		byte[] theRest = null;

		if (b.position() > 0 && b.position() < Protocol.HEADER_LENGTH_DATA) {
			theRest = new byte[b.position()];
			b.flip();
			b.get(theRest, 0, b.limit());
			System.arraycopy(theRest, 0, this.received_data, 0, theRest.length);
			// for(int i = 0; i<theRest.length;i++)
			// received_data[i] = theRest[i];

			// System.out.println("theRest len = "+theRest.length);
			n = this.is.read(this.received_data, theRest.length, Protocol.MAX_PACKET_SIZE
				- theRest.length);
			n += theRest.length;
		} else
			n = this.is.read(this.received_data);

		// buffer.clear();
		this.buffer = ByteBuffer.wrap(this.received_data, 0, n);
		// System.out.println("data has been read:" + n);

		return this.buffer;
	}

}
