package hw.pkg.missedcall;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationControl {
	public static final int NOTI_ID = 55;
	
	NotificationControl(){
		
	}
	
	
	public static void notifOn(Context context) {
		NotificationManager notMng = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		CharSequence ticker = "������ ��ȭ ����Ȯ��";
		Notification notif = new Notification(R.drawable.misscall_icon, 
				ticker, System.currentTimeMillis());
		
		notif.flags = Notification.FLAG_ONGOING_EVENT;
		
		//Context context = getApplicationContext();
		CharSequence contentTitle = "������ ��ȭ ����Ȯ��";
		CharSequence contentText = "����Ȯ�� ���񽺰� �۵��� �Դϴ�.";
		Intent notifIntent = new Intent(context, MissedCall_alarmActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifIntent, 0);
		
		notif.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		notMng.notify(NOTI_ID, notif);
	}
	public static void notifOff(Context context) {
		NotificationManager notMng = 
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notMng.cancel(NOTI_ID);
	}

}
