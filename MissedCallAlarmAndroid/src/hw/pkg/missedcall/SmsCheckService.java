package hw.pkg.missedcall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class SmsCheckService extends Service {

	final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	
	final String SCREEN_ON = "android.intent.action.SCREEN_ON";
	final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";
	
	private BroadcastReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.e("Error","I'm in SmsCheckService");
		
		unregisterRestartAlarm();

		IntentFilter filter = new IntentFilter(SMS_RECEIVED);
		filter.addAction(SCREEN_ON);
		filter.addAction(SCREEN_OFF);
		
		receiver = new TextReceiver();
		registerReceiver(receiver, filter);
		
		getApplicationContext().startService(
				new Intent(getApplicationContext(), CheckCurrentApp.class));
		
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		SharedPreferences prefOn = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext());
				
		boolean setOn = prefOn.getBoolean("setOn", false);
		
		Log.e("Error",setOn+" : value when onDestroy()");
		
		if(setOn == true) {
			registerRestartAlarm();
			NotificationControl.notifOn(getApplicationContext());
		}
		
		unregisterReceiver(receiver);
		
		getApplicationContext().stopService(
				new Intent(getApplicationContext(), CheckCurrentApp.class));
		
		super.onDestroy();
	}

	// support persistent of Service
	public void registerRestartAlarm() {
		Log.d("PersistentService", "registerRestartAlarm");
		Intent intent = new Intent(SmsCheckService.this, BootReceiver.class);
		intent.setAction("ACTION.RESTART.SmsCheckService");
		PendingIntent sender = PendingIntent.getBroadcast(SmsCheckService.this, 0, intent, 0);
		
		long firstTime = SystemClock.elapsedRealtime();
		firstTime += 10 * 1000; // 10초 후에 알람이벤트 발생
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				10 * 1000, sender);
	}

	public void unregisterRestartAlarm() {
		Log.d("PersistentService", "unregisterRestartAlarm");
		Intent intent = new Intent(SmsCheckService.this, BootReceiver.class);
		intent.setAction("ACTION.RESTART.SmsCheckService");
		PendingIntent sender = PendingIntent.getBroadcast(SmsCheckService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
	
}
