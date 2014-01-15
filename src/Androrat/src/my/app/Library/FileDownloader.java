package my.app.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import my.app.client.ClientListener;
import Packet.FilePacket;

public class FileDownloader {

	ClientListener ctx;
	byte[] finalData;
	InputStream in;
	File f;
	int channel;
	FilePacket packet;
	byte[] buffer;
	short numseq = 0;
	int BUFF_SIZE = 4096;

	public FileDownloader(ClientListener c) {
		this.ctx = c;
	}

	public boolean downloadFile(String s, int chan) {
		this.channel = chan;
		this.f = new File(s);
		try {
			this.in = new FileInputStream(this.f);
		} catch (final FileNotFoundException e) {
			return false;
		}

		final Thread loadf = new Thread(new Runnable() {
			public void run() {
				FileDownloader.this.load();
			}
		});
		loadf.start();

		return true;
	}

	public void load() {
		try {
			while (true) {
				this.buffer = new byte[this.BUFF_SIZE];
				final int read = this.in.read(this.buffer);
				if (read == -1)
					break;
				if (read == this.BUFF_SIZE) {
					this.packet = new FilePacket(this.numseq, (byte) 1, this.buffer);
					this.ctx.handleData(this.channel, this.packet.build());
					this.numseq++;
				} else {// C'était le dernier paquet
					final byte[] tmp = new byte[read];
					System.arraycopy(this.buffer, 0, tmp, 0, read);
					this.packet = new FilePacket(this.numseq, (byte) 0, tmp);
					this.ctx.handleData(this.channel, this.packet.build());
					break;
				}
			}
			this.in.close();
		} catch (final IOException e) {
			this.ctx.sendError("IOException loading file");
		}
	}
}
