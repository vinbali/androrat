package my.app.Library;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import android.content.Context;
import android.telephony.TelephonyManager;


public class SystemInfo {

	Context ctx;
	TelephonyManager tm;

	public SystemInfo(Context c) {
		this.ctx = c;
		this.tm = (TelephonyManager) this.ctx.getSystemService(Context.TELEPHONY_SERVICE);
	}

	public byte[] getBasicInfos() {
		final Hashtable<String, String> h = new Hashtable<String, String>();
		String res;
		res = this.getIMEI();
		if (res != null)
			h.put("IMEI", res);
		res = this.getPhoneNumber();
		if (res != null)
			h.put("PhoneNumber", res);
		res = this.getCountryCode();
		if (res != null)
			h.put("Country", res);
		res = this.getOperatorName();
		if (res != null)
			h.put("Operator", res);
		res = this.getSimCountryCode();
		if (res != null)
			h.put("SimCountry", res);
		res = this.getSimOperatorName();
		if (res != null)
			h.put("SimOperator", res);
		res = this.getSimSerial();
		if (res != null)
			h.put("SimSerial", res);

		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(h);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public String getCountryCode() {
		return this.tm.getNetworkCountryIso();
	}

	public String getIMEI() {
		return this.tm.getDeviceId();
	}

	public String getOperatorName() {
		return this.tm.getNetworkOperatorName();
	}

	public String getPhoneNumber() {
		return this.tm.getLine1Number();
	}

	public String getSimCountryCode() {
		return this.tm.getSimCountryIso();
	}

	public String getSimOperatorName() {
		return this.tm.getSimOperatorName();
	}

	public String getSimSerial() {
		return this.tm.getSimSerialNumber();
	}

}
