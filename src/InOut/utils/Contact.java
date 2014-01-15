package utils;

import java.io.Serializable;
import java.util.ArrayList;


public class Contact implements Serializable {

	private static final long serialVersionUID = -744071613945933264L;
	long id;
	int times_contacted;
	long last_time_contacted;
	String display_name;
	int starred;
	ArrayList<String> phones;
	ArrayList<String> emails;
	ArrayList<String> notes;
	String street;
	String city;
	String region;
	String postalcode;
	String country;
	int type_addr;
	ArrayList<String> messaging;
	String OrganisationName;
	String OrganisationStatus; // manager ..
	byte[] photo;

	public Contact() {

	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public String getDisplay_name() {
		return this.display_name;
	}

	public ArrayList<String> getEmails() {
		return this.emails;
	}

	public long getId() {
		return this.id;
	}

	public long getLast_time_contacted() {
		return this.last_time_contacted;
	}

	public ArrayList<String> getMessaging() {
		return this.messaging;
	}

	public ArrayList<String> getNotes() {
		return this.notes;
	}

	public String getOrganisationName() {
		return this.OrganisationName;
	}

	public String getOrganisationStatus() {
		return this.OrganisationStatus;
	}

	public ArrayList<String> getPhones() {
		return this.phones;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public String getPostalcode() {
		return this.postalcode;
	}

	public String getRegion() {
		return this.region;
	}

	public int getStarred() {
		return this.starred;
	}

	public String getStreet() {
		return this.street;
	}

	public int getTimes_contacted() {
		return this.times_contacted;
	}

	public int getType_addr() {
		return this.type_addr;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLast_time_contacted(long last_time_contacted) {
		this.last_time_contacted = last_time_contacted;
	}

	public void setMessaging(ArrayList<String> messaging) {
		this.messaging = messaging;
	}

	public void setNotes(ArrayList<String> notes) {
		this.notes = notes;
	}

	public void setOrganisationName(String organisationName) {
		this.OrganisationName = organisationName;
	}

	public void setOrganisationStatus(String organisationStatus) {
		this.OrganisationStatus = organisationStatus;
	}

	public void setPhones(ArrayList<String> phones) {
		this.phones = phones;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public void setRegion(String reg) {
		this.region = reg;
	}

	public void setStarred(int starred) {
		this.starred = starred;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setTimes_contacted(int times_contacted) {
		this.times_contacted = times_contacted;
	}

	public void setType_addr(int type_addr) {
		this.type_addr = type_addr;
	}
}
