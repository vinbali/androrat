package my.app.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class AlarmListener extends BroadcastReceiver {

	public final String TAG = AlarmListener.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(this.TAG, "Alarm received !");
		try {
			final Bundle bundle = intent.getExtras();
			final String message = bundle.getString("alarm_message");
			if (message != null) {
				Log.i(this.TAG, "Message received: " + message);

				final Intent serviceIntent = new Intent(context, Client.class);
				serviceIntent.setAction(AlarmListener.class.getSimpleName());// By
																				// this
																				// way
																				// the
																				// Client
																				// will
																				// know
																				// that
																				// it
																				// was
																				// AlarmListener
																				// that
																				// launched
																				// it
				context.startService(serviceIntent);

			}
			// Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} catch (final Exception e) {
			Log.e(this.TAG, "Error in Alarm received !" + e.getMessage());
		}
	}
}