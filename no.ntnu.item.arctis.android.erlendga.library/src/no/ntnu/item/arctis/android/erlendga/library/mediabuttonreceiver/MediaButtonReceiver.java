package no.ntnu.item.arctis.android.erlendga.library.mediabuttonreceiver;

import no.ntnu.item.arctis.runtime.android.AndroidBlock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

public class MediaButtonReceiver extends AndroidBlock {

	private Context context = (Context) getProperty("Android");
	private BroadcastReceiver broadcastReceiver;
	// Instance parameter. Edit only in overview page.
	public final int priority;
	
	public void initializeReceiver() {	
		broadcastReceiver = new BroadcastReceiver() {
			
			public void onReceive(Context context, Intent intent) {
				String intentAction = intent.getAction();		
					
				if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
					KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
					
					if (keyEvent == null) {
						return;
					}
					int keyEventAction = keyEvent.getAction();
					//TODO Should react on long-press instead of ACTION_UP
					if (keyEventAction == KeyEvent.ACTION_UP) {
							sendToBlock("MEDIA_BUTTON");
					}
					abortBroadcast();
		        }
			}
		};
		
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
		intentFilter.setPriority(priority);
		//TODO Check out the registerMediaButtonReceiver method from the API
		context.registerReceiver(broadcastReceiver, intentFilter);
	}

	public void removeRegistration() {
		context.unregisterReceiver(broadcastReceiver);
	}

	// Do not edit this constructor.
	public MediaButtonReceiver(int priority) {
	    this.priority = priority;
	}
}
