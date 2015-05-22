package hw.pkg.missedcall;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;

public class ContactInfo {
	public static int searchContacts(Context context, String name) {
		String[] arrProjection = { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String[] arrPhoneProjection = { ContactsContract.CommonDataKinds.Phone.NUMBER };

		Cursor clsCursor = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, arrProjection,
				ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null);
		
		while (clsCursor.moveToNext()) {
			if(!clsCursor.getString(1).equals(name)) {
				continue;
			}
			
			String strContactId = clsCursor.getString(0);

			Cursor clsPhoneCursor = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					arrPhoneProjection,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ strContactId, null, null);
			while (clsPhoneCursor.moveToNext()) {
				// �̸��� ��ȭ ��ȣ�� ������ ����Ʈ�� �����Ѵ�.
				Log.e("Pnumber",clsPhoneCursor.getString(0));
				String phoneNum = clsPhoneCursor.getString(0);
				String smsCont = name+": "+ phoneNum;
				
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(TextReceiver.to, null, smsCont, null, null);
				return 0;
			}

			clsPhoneCursor.close();
		}
		clsCursor.close();

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(TextReceiver.to, null, name+"(��)�� ��ϵ� ��ȭ��ȣ�� �����ϴ�.", null, null);
		return 0;
	}
}