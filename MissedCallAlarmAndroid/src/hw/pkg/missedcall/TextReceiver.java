package hw.pkg.missedcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class TextReceiver extends BroadcastReceiver {

	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";

	public static String msg;
	public static String to;

	@Override
	public void onReceive(Context context, Intent intent) {
		// intent�� Action �˻�.....
		/* ���� ���϶� �˶����� �ٽ� ���� ��� */

		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			context.startService(new Intent(context, CheckCurrentApp.class));

			Log.e("SCREEN", "in ACTION_SCREEN_ON...  start service");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			context.stopService(new Intent(context, CheckCurrentApp.class));

			Log.e("SCREEN", "in ACTION_SCREEN_OFF...  stop service");
		}

		else if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle bundle = intent.getExtras();
			intent.removeExtra("pdus");

			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

				for (SmsMessage message : messages) {
					msg = message.getMessageBody();
					to = message.getOriginatingAddress();

					context.startService(new Intent(context,
							CheckCodeService.class));
				}
			}// end of inner-if
		}// end of outer-if
	}
}
