package hw.pkg.missedcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class MissedCall_alarmActivity extends PreferenceActivity {
	/** Called when the activity is first created. */

	public SharedPreferences missedCallCount = null;
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		SharedPreferences prefOn = PreferenceManager.getDefaultSharedPreferences(
				getApplicationContext());

		SharedPreferences.Editor edit = prefOn.edit();
		edit.putBoolean("setOn", false);
		edit.putBoolean("notifSet", false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.setting);
		
		//"�˸��ٿ� ǥ��"�޴� disable��ų�� ���� ����
		Preference setupPref = (Preference) findPreference("setOn");
		Preference notiPref = (Preference) findPreference("notifSet");
		if(setupPref.getSharedPreferences().getBoolean("setOn", false) == true)
			notiPref.setEnabled(true);
		else
			notiPref.setEnabled(false);
		
		Log.e("Error", "I'm in Setting");

		
		try {
			missedCallCount = getSharedPreferences("hw.pkg.missedcall", MODE_PRIVATE);
			SharedPreferences.Editor editor = missedCallCount.edit();
			editor.putInt("callCount", MissingCall.callLog(getApplicationContext()));
			editor.commit();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setupPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				// open browser or intent here
				if(preference.getSharedPreferences().getBoolean("setOn", false) == true) {
					getApplicationContext().startService(
							new Intent(getApplicationContext(), SmsCheckService.class));

					Preference notPref = (Preference) findPreference("notifSet");
					//�˸��� ǥ��
					if(notPref.getSharedPreferences().getBoolean("notifSet", false) == true)
						NotificationControl.notifOn(getApplicationContext());
					
					//"�˸��ٿ� ǥ��" ������� Ȱ��ȭ
					notPref.setEnabled(true);
				}
				else {
					getApplicationContext().stopService(
							new Intent(getApplicationContext(), SmsCheckService.class));
					
					Preference notPref = (Preference) findPreference("notifSet");
					
					//�˸��� ǥ�� ����
					if(notPref.getSharedPreferences().getBoolean("notifSet", false) == true)
						NotificationControl.notifOff(getApplicationContext());
					
					//"�˸��ٿ� ǥ��" ������� ��Ȱ��ȭ
					notPref.setEnabled(false);
				}
				
				return true;
			}
		});
		
		Preference notifPref = (Preference) findPreference("notifSet");
		
		notifPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				if(preference.getSharedPreferences().getBoolean("notifSet", false) == true)
					NotificationControl.notifOn(getApplicationContext());
				
				else
					NotificationControl.notifOff(getApplicationContext());	
				
				return true;
			}
		});
		
		Preference linkPref = (Preference) findPreference("link");
		
		linkPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				Uri uri = Uri.parse("http://blog.naver.com/sky0867/20153124749");
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		        startActivity(intent);
				return true;
			}
		});
	}
}