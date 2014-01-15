package out;

import java.io.DataOutputStream;
import java.io.IOException;


public class Sender {

	DataOutputStream out;

	public Sender(DataOutputStream out) {
		this.out = out;
	}

	public void send(byte[] data) {
		try {
			this.out.write(data);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
