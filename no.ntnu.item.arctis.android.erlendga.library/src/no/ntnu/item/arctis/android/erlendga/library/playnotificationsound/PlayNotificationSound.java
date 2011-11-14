package no.ntnu.item.arctis.android.erlendga.library.playnotificationsound;

import no.ntnu.item.arctis.android.R;
import no.ntnu.item.arctis.runtime.Block;
import android.content.Context;
import android.media.MediaPlayer;

public class PlayNotificationSound extends Block {

	private Context context = (Context) getProperty("Android");
	public void play() {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alert_sound_ideal_for_software_systems_etc_ver_3);
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				sendToBlock("COMPLETED");
			}
		});
		mediaPlayer.start();
	}

}
