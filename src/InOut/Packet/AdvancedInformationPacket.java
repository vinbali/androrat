package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class AdvancedInformationPacket implements Packet, Serializable {

	private static final long serialVersionUID = 44346671562310318L;
	String phoneNumber;
	String IMEI;
	String softwareVersion;
	String countryCode;
	String operatorCode;
	String operatorName;
	String simOperatorCode;
	String simOperatorName;
	String simCountryCode;
	String simSerial;

	boolean wifiAvailable;
	boolean wifiConnectedOrConnecting;
	String wifiExtraInfos;
	String wifiReason;

	String mobileNetworkName;
	boolean mobileNetworkAvailable;
	boolean mobileNetworkConnectedOrConnecting;
	String mobileNetworkExtraInfos;
	String mobileNetworkReason;

	String androidVersion;
	int androidSdk;

	ArrayList<String> sensors;

	int batteryHealth;
	int batteryLevel;
	int batteryPlugged;
	boolean batteryPresent;
	int batteryScale;
	int batteryStatus;
	String batteryTechnology;
	int batteryTemperature;
	int batteryVoltage;

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

	public int getAndroidSdk() {
		return this.androidSdk;
	}

	public String getAndroidVersion() {
		return this.androidVersion;
	}

	public int getBatteryHealth() {
		return this.batteryHealth;
	}

	public int getBatteryLevel() {
		return this.batteryLevel;
	}

	public int getBatteryPlugged() {
		return this.batteryPlugged;
	}

	public int getBatteryScale() {
		return this.batteryScale;
	}

	public int getBatteryStatus() {
		return this.batteryStatus;
	}

	public String getBatteryTechnology() {
		return this.batteryTechnology;
	}

	public int getBatteryTemperature() {
		return this.batteryTemperature;
	}

	public int getBatteryVoltage() {
		return this.batteryVoltage;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public String getIMEI() {
		return this.IMEI;
	}

	public String getMobileNetworkExtraInfos() {
		return this.mobileNetworkExtraInfos;
	}

	public String getMobileNetworkName() {
		return this.mobileNetworkName;
	}

	public String getMobileNetworkReason() {
		return this.mobileNetworkReason;
	}

	public String getOperatorCode() {
		return this.operatorCode;
	}

	public String getOperatorName() {
		return this.operatorName;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public ArrayList<String> getSensors() {
		return this.sensors;
	}

	public String getSimCountryCode() {
		return this.simCountryCode;
	}

	public String getSimOperatorCode() {
		return this.simOperatorCode;
	}

	public String getSimOperatorName() {
		return this.simOperatorName;
	}

	public String getSimSerial() {
		return this.simSerial;
	}

	public String getSoftwareVersion() {
		return this.softwareVersion;
	}

	public String getWifiExtraInfos() {
		return this.wifiExtraInfos;
	}

	public String getWifiReason() {
		return this.wifiReason;
	}

	public boolean isBatteryPresent() {
		return this.batteryPresent;
	}

	public boolean isMobileNetworkAvailable() {
		return this.mobileNetworkAvailable;
	}

	public boolean isMobileNetworkConnectedOrConnecting() {
		return this.mobileNetworkConnectedOrConnecting;
	}

	public boolean isWifiAvailable() {
		return this.wifiAvailable;
	}

	public boolean isWifiConnectedOrConnecting() {
		return this.wifiConnectedOrConnecting;
	}

	public void parse(byte[] packet) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			final AdvancedInformationPacket adv = (AdvancedInformationPacket) in.readObject();
			this.setPhoneNumber(adv.getPhoneNumber());
			this.setIMEI(adv.getIMEI());
			this.setSoftwareVersion(adv.getSoftwareVersion());
			this.setCountryCode(adv.getCountryCode());
			this.setOperatorCode(adv.getOperatorCode());
			this.setOperatorName(adv.getOperatorName());
			this.setSimOperatorCode(adv.getSimOperatorCode());
			this.setSimOperatorName(adv.getSimOperatorName());
			this.setSimCountryCode(adv.getSimCountryCode());
			this.setSimSerial(adv.getSimSerial());
			this.setWifiAvailable(adv.isWifiAvailable());
			this.setWifiConnectedOrConnecting(adv.isWifiConnectedOrConnecting());
			this.setWifiExtraInfos(adv.getWifiExtraInfos());
			this.setWifiReason(adv.getWifiReason());
			this.setMobileNetworkName(adv.getMobileNetworkName());
			this.setMobileNetworkAvailable(adv.isMobileNetworkAvailable());
			this.setMobileNetworkConnectedOrConnecting(adv.isMobileNetworkConnectedOrConnecting());
			this.setMobileNetworkExtraInfos(adv.getMobileNetworkExtraInfos());
			this.setMobileNetworkReason(adv.getMobileNetworkReason());
			this.setAndroidVersion(adv.getAndroidVersion());
			this.setAndroidSdk(adv.getAndroidSdk());
			this.setSensors(adv.getSensors());
			this.setBatteryHealth(adv.getBatteryHealth());
			this.setBatteryLevel(adv.getBatteryLevel());
			this.setBatteryPlugged(adv.getBatteryPlugged());
			this.setBatteryPresent(adv.isBatteryPresent());
			this.setBatteryScale(adv.getBatteryScale());
			this.setBatteryStatus(adv.getBatteryStatus());
			this.setBatteryTechnology(adv.getBatteryTechnology());
			this.setBatteryTemperature(adv.getBatteryTemperature());
			this.setBatteryVoltage(adv.getBatteryVoltage());
		} catch (final Exception e) {}
	}

	public void setAndroidSdk(int androidSdk) {
		this.androidSdk = androidSdk;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

	public void setBatteryHealth(int batteryHealth) {
		this.batteryHealth = batteryHealth;
	}

	public void setBatteryLevel(int batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public void setBatteryPlugged(int batteryPlugged) {
		this.batteryPlugged = batteryPlugged;
	}

	public void setBatteryPresent(boolean batteryPresent) {
		this.batteryPresent = batteryPresent;
	}

	public void setBatteryScale(int batteryScale) {
		this.batteryScale = batteryScale;
	}

	public void setBatteryStatus(int batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

	public void setBatteryTechnology(String batteryTechnology) {
		this.batteryTechnology = batteryTechnology;
	}

	public void setBatteryTemperature(int batteryTemperature) {
		this.batteryTemperature = batteryTemperature;
	}

	public void setBatteryVoltage(int batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setIMEI(String iMEI) {
		this.IMEI = iMEI;
	}

	public void setMobileNetworkAvailable(boolean mobileNetworkAvailable) {
		this.mobileNetworkAvailable = mobileNetworkAvailable;
	}

	public void setMobileNetworkConnectedOrConnecting(boolean mobileNetworkConnectedOrConnecting) {
		this.mobileNetworkConnectedOrConnecting = mobileNetworkConnectedOrConnecting;
	}

	public void setMobileNetworkExtraInfos(String mobileNetworkExtraInfos) {
		this.mobileNetworkExtraInfos = mobileNetworkExtraInfos;
	}

	public void setMobileNetworkName(String mobileNetworkName) {
		this.mobileNetworkName = mobileNetworkName;
	}

	public void setMobileNetworkReason(String mobileNetworkReason) {
		this.mobileNetworkReason = mobileNetworkReason;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setSensors(ArrayList<String> sensors) {
		this.sensors = sensors;
	}

	public void setSimCountryCode(String code) {
		this.simCountryCode = code;
	}

	public void setSimOperatorCode(String simOperatorCode) {
		this.simOperatorCode = simOperatorCode;
	}

	public void setSimOperatorName(String simOperatorName) {
		this.simOperatorName = simOperatorName;
	}

	public void setSimSerial(String simSerial) {
		this.simSerial = simSerial;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public void setWifiAvailable(boolean wifiAvailable) {
		this.wifiAvailable = wifiAvailable;
	}

	public void setWifiConnectedOrConnecting(boolean wifiConnectedOrConnecting) {
		this.wifiConnectedOrConnecting = wifiConnectedOrConnecting;
	}

	public void setWifiExtraInfos(String wifiExtraInfos) {
		this.wifiExtraInfos = wifiExtraInfos;
	}

	public void setWifiReason(String wifiReason) {
		this.wifiReason = wifiReason;
	}

}
