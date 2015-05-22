package hw.pkg.missedcall;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.util.Log;

public class MissingCall {

	static ContentResolver ctx;
	public static Cursor cursor;

	public static final String MESSAGE_TYPE_INBOX = "1";
	public static final String MESSAGE_TYPE_SENT = "2";
	public static final String MESSAGE_TYPE_CONVERSATIONS = "3";
	public static final String MESSAGE_TYPE_NEW = "new";

	final static private String[] CALL_PROJECTION = { CallLog.Calls.TYPE,
			CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
			CallLog.Calls.DATE, CallLog.Calls.DURATION };

	public static Cursor getCallHistoryCursor(Context context) {
		Cursor cursor = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, CALL_PROJECTION, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);

		return cursor;
	}

	public static int callLog(Context context) throws InterruptedException {
		// String[] projection = { CallLog.Calls.CACHED_NAME,
		// CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE };

		String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE;
		Cursor c = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, where, null, null);
		c.moveToFirst();

		Log.d("CALL", "" + c.getCount()); // do some other operation
		// if (c.getCount() == SOME_VALUE_TO_START_APP_ONE)// ...etc etc

		return c.getCount();
	}

	public static void getCallLog(Context context) {
		Cursor curCallLog = getCallHistoryCursor(context);
		// Logger.i( TAG , "curCallLog: " + curCallLog.getCount());
		SharedPreferences missedCallCount = context.getSharedPreferences("hw.pkg.missedcall", Activity.MODE_PRIVATE);
		
		if (curCallLog.moveToFirst() && curCallLog.getCount() > 0) {
			int missedCount = missedCallCount.getInt("missedCount", -1);
			Log.e("COUNT",""+missedCount);
			
			for(; missedCount > 0; missedCount--) {
				String smsCont = "";
				String callname = "";
				
				if (curCallLog.getString(
						curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(
						MESSAGE_TYPE_CONVERSATIONS)) {
				}

				if (curCallLog.getString(curCallLog
						.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null) {
					callname = "NoName";
				} else {
					callname = curCallLog.getString(curCallLog
							.getColumnIndex(CallLog.Calls.CACHED_NAME));
				}

				smsCont += (timeToString(curCallLog.getLong(curCallLog
						.getColumnIndex(CallLog.Calls.DATE))));
				smsCont += "\n" + callname;
				smsCont += ": "	+ curCallLog.getString(curCallLog
								.getColumnIndex(CallLog.Calls.NUMBER));
				
				curCallLog.moveToNext();
				
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(TextReceiver.to, null, smsCont, null, null);
				Log.e("Send",smsCont);
			}
		}
		
		SharedPreferences.Editor editor = missedCallCount.edit();
    	editor.putInt("missedCount", 0);
    	editor.commit();
	}

	public static String timeToString(Long time) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH½Ã mmºÐ");
		String date = simpleFormat.format(new Date(time));
		return date;
	}
}
