package my.app.Library;

import Packet.GPSPacket;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GPSListener {

	private Context ctx;
	private final String provider;
	private final LocationManager mlocManager;
	private final LocationListener listener;
	private final int channel;
	private GPSPacket packet;

	public GPSListener(LocationListener c, String prov, int chan) {
		this.listener = c;
		this.provider = prov;
		this.channel = chan;

		this.packet = new GPSPacket();

		this.mlocManager = (LocationManager) ((Context) c)
			.getSystemService(Context.LOCATION_SERVICE);
		this.mlocManager.requestLocationUpdates(prov, 0, 0, this.listener);
		// mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,
		// 0, 0, listener);
	}

	public byte[] encode(Location loc) {
		this.packet = new GPSPacket(loc.getLatitude(), loc.getLongitude(), loc.getAltitude(),
			loc.getSpeed(), loc.getAccuracy());
		return this.packet.build();
		/*
		 * ByteBuffer b = ByteBuffer.allocate(32);
		 * b.putDouble(loc.getLongitude()); b.putDouble(loc.getLatitude());
		 * b.putDouble(loc.getAltitude()); b.putFloat(loc.getAccuracy());
		 * b.putFloat(loc.getSpeed()); return b.array();
		 */
	}

	public int getChannel() {
		return this.channel;
	}

	public void stop() {
		if (this.mlocManager != null)
			this.mlocManager.removeUpdates(this.listener);
	}

}