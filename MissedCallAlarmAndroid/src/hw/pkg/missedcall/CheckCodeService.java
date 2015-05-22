package hw.pkg.missedcall;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;

public class CheckCodeService extends Service {

	private Context context;
	private int pwLeng;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		checking(this);
		
		this.stopSelf();
	}

	public boolean checking(Context context) {
		this.context = context;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String passwd = pref.getString("passwd", "");
		
		pwLeng = passwd.length();
		
		try{
			String pw = TextReceiver.msg.substring(0, pwLeng);
			Log.e("Error","pw:"+passwd+"// I got:"+pw);
			
			//비밀번호가 일치 할 경우.
			if(passwd.compareTo(pw) == 0) {
				//부재중전화 조회인 경우.
				if(pw.length() == TextReceiver.msg.length()){
					return missedCallProc();
				}
				String command = TextReceiver.msg.substring(pwLeng+1, pwLeng+4);
				if(command.compareTo("부재중") == 0) {
					return missedCallProc();
				}
				
				command = TextReceiver.msg.substring(pwLeng+1, pwLeng+5);
				//전화번호 검색요청인 경우
				if(command.compareTo("전화번호") == 0) {
					return contactProc();
				}
				
			}
			else
				return false;
		}catch(Exception e){
			Log.e("Error",e.toString());
			
			return false;
		}

		return true;
	}
	
	private boolean missedCallProc(){
		int preCount = 0;
		SharedPreferences missedCallCount = context.getSharedPreferences("hw.pkg.missedcall", Activity.MODE_PRIVATE);
		preCount = missedCallCount.getInt("callCount", -1);
	
		int curCount = 0;
		try {
			//How many missed calls in calllog now.
			curCount = MissingCall.callLog(context);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(preCount == -1 || preCount == curCount) {
			Log.e("Error","no missed call now." + preCount +", " + curCount);
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(TextReceiver.to, null, "부재중 전화가 없습니다.", null, null);
			return false;
		}
		
		SharedPreferences.Editor editor = missedCallCount.edit();
		editor.putInt("missedCount", (curCount - preCount));
		editor.putInt("callCount", curCount);
		editor.commit();
		
		MissingCall.getCallLog(context);
		
		return true;
	}
	private boolean contactProc(){
		String name = TextReceiver.msg.substring(pwLeng+6, TextReceiver.msg.length());
		
		ContactInfo.searchContacts(context, name);
		return true;
	}
}