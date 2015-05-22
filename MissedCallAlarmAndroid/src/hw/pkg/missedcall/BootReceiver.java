package hw.pkg.missedcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

			Boolean on = pref.getBoolean("setOn", false);
			Boolean set = pref.getBoolean("notifSet", false);

			if(on == true) {
				context.startService(new Intent(context, SmsCheckService.class));
				if (set == true) {
					NotificationControl.notifOn(context);
				}
			}
		}

		if (intent.getAction().equals("ACTION.RESTART.SmsCheckService")) {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

			Boolean on = pref.getBoolean("setOn", false);
			Boolean set = pref.getBoolean("notifSet", false);

			if(on == true) {
				context.startService(new Intent(context, SmsCheckService.class));
				if (set == true) {
					NotificationControl.notifOn(context);
				}
			}
		}
	}

}
