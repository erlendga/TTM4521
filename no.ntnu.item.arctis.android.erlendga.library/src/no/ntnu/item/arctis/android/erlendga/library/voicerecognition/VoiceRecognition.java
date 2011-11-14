package no.ntnu.item.arctis.android.erlendga.library.voicerecognition;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import no.ntnu.item.arctis.android.R;
import no.ntnu.item.arctis.runtime.Block;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.speech.RecognizerIntent;

import com.bitreactive.library.android.core.startinvisibleactivity.InvisibleActivity;
import com.bitreactive.library.android.core.startinvisibleactivity.InvisibleActivity.IActivityResultListener;

public class VoiceRecognition extends Block {

	private boolean wasNotFound;
	public java.util.ArrayList<java.lang.String> results;
	// Instance parameter. Edit only in overview page.
	public final java.lang.String hint;
	// Instance parameter. Edit only in overview page.
	public final int requestCode;
	// Instance parameter. Edit only in overview page.
	public final int maxResults;
	public com.bitreactive.library.android.core.startinvisibleactivity.InvisibleActivity activity;
	public android.os.PowerManager.WakeLock wakeLock;
	public java.util.ArrayList<java.lang.String> result;
	private MediaPlayer mediaPlayerStart = null;
	private MediaPlayer mediaPlayerResult = null;
	
	public void startActivity() {
		Intent intent = new Intent(getContext(), InvisibleActivity.class);
		intent.putExtra(InvisibleActivity.BLOCK_ID_EXTRA_KEY, blockID);
		intent.putExtra(InvisibleActivity.SESSION_ID_EXTRA_KEY, sessionID);
		String wrapping = (String) getProperty("wrapping");
		
		if(wrapping!=null && wrapping.equalsIgnoreCase("service")) {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		
		Context context = (Context) getProperty("Android");
		
		if(context!=null) {
			context.startActivity(intent);
		} else {
			log("Could not find a context to start the invisible activity.", null);
		}
	}
	
	public PowerManager.WakeLock acquireWakeLock() {
		PowerManager powerManager = (PowerManager) getContext().getSystemService(getContext().POWER_SERVICE);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Voice Recognition");
		wakeLock.acquire();
		return wakeLock;
	}
	
	public void startVoiceRecognitionActivity() {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, hint);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
			activity.startActivityForResult(intent, requestCode);
			sendToBlock("STARTED");
		}catch (ActivityNotFoundException e) {
			wasNotFound = true;
			sendToBlock("NOT_FOUND");
		}
	}
	
	public void playNotificationStartSound() {
		mediaPlayerStart = MediaPlayer.create(getContext(), R.raw.alert_sound_ideal_for_software_systems_etc_ver_3);
		mediaPlayerStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				sendToBlock("START_SOUND_COMPLETED");
			}
		});
		mediaPlayerStart.start();
	}
	
	public void addListener() {
		mediaPlayerStart.release();
		mediaPlayerStart = null;
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			public void run() {
				sendToBlock("ERROR");
			}
		}, 5000);
		
		activity.registerOnActivityResultListener(requestCode, new IActivityResultListener() {

			public void onActivityResult(int resultCode, Intent data) {
				timer.cancel();
				
				if (resultCode == android.app.Activity.RESULT_OK) {
		            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		            sendToBlock("RESULT", matches);
		        }
				else {
		        	if(!wasNotFound) {
		        		sendToBlock("ERROR");
		        	}
		        }
			}
		});
	}
	
	public void playNotificationResultSound() {
		mediaPlayerResult = MediaPlayer.create(getContext(), R.raw.alert_sound_ideal_for_software_systems_etc_ver_15);
		mediaPlayerResult.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				sendToBlock("RESULT_SOUND_COMPLETED");
			}
		});
		mediaPlayerResult.start();
	}
	
	public void releaseMediaPlayerResult() {
		mediaPlayerResult.release();
		mediaPlayerResult = null;
	}
	
	public void releaseWakeLock() {
		wakeLock.release();
	}
	
	private Context getContext() {
		return (Context) getProperty("Android");
	}

	// Do not edit this constructor.
	public VoiceRecognition(java.lang.String hint, int requestCode, int maxResults) {
	    this.hint = hint;
	    this.requestCode = requestCode;
	    this.maxResults = maxResults;
	}
}
