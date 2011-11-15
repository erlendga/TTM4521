package no.ntnu.item.arctis.android.erlendga.library.endofspeechalert;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import no.ntnu.item.arctis.android.R;
import no.ntnu.item.arctis.runtime.Block;

public class EndOfSpeechAlert extends Block {

	public void createMediaPlayer() {
		Context context = (Context) getProperty("Android");
		MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alert_sound_ideal_for_software_systems_etc_ver_15);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				mp = null;
			}
		});
		mediaPlayer.start();
	}

}
