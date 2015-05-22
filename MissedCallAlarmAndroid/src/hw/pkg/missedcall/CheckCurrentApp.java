package hw.pkg.missedcall;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class CheckCurrentApp extends Service {
	
	boolean isRunning = false;
	
	@Override
	public void onCreate() {
		// search top activity
		Thread background = new Thread(new Runnable() {
			public void run(){
				ActivityManager mActivityManager;
				while(isRunning) {
					ActivityManager localActivityManager = 
							(ActivityManager)getApplicationContext().getSystemService("activity");
				    mActivityManager = localActivityManager;
					
					String str1 = ((ActivityManager.RunningTaskInfo)mActivityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
					
					if(str1.equals("com.android.contacts")) {
						//clear missed call count
						try {
							SharedPreferences missedCallCount = getSharedPreferences("hw.pkg.missedcall", MODE_PRIVATE);
							SharedPreferences.Editor editor = missedCallCount.edit();
							editor.putInt("callCount", MissingCall.callLog(getApplicationContext()));
							editor.commit();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
				
		});
		isRunning = true;
		background.start();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		
		isRunning = false;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
