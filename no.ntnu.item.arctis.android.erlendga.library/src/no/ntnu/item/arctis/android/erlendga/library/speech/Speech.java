package no.ntnu.item.arctis.android.erlendga.library.speech;

import java.util.HashMap;

import com.bitreactive.library.android.core.startinvisibleactivity.InvisibleActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import no.ntnu.item.arctis.runtime.Block;

import com.bitreactive.library.android.core.startinvisibleactivity.InvisibleActivity.IActivityResultListener;

public class Speech extends Block {

	private TextToSpeech textToSpeech;
	// Instance parameter. Edit only in overview page.
	public final int requestCode;
	// Instance parameter. Edit only in overview page.
	public final java.lang.String ID;
	
	public void textToSpeech(String text) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ID);
		textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params);
	}
	
	private Context getContext() {
		return (Context) getProperty("Android");
	}

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

	public void startTextToSpeechActivity(InvisibleActivity activity) {
		activity.registerOnActivityResultListener(requestCode, new IActivityResultListener() {
			
			public void onActivityResult(int resultCode, Intent data) {
				if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
		            textToSpeech = new TextToSpeech(getContext(), new OnInitListener() {
						
						public void onInit(int status) {
							
							textToSpeech.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
								
								public void onUtteranceCompleted(String utteranceId) {
									if (ID.equals(utteranceId)) {
										sendToBlock("COMPLETED");
									}
								}
							});

							if (status == TextToSpeech.SUCCESS) {
								sendToBlock("SUCCESS");
							}
							else {
								sendToBlock("FAILED");
							}
						}
					});
		        } 
		        else {
		            Intent installIntent = new Intent();
		            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
		            getContext().startActivity(installIntent);
		        }
			}
		});
		
		try {
			Intent intent = new Intent();
			intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			activity.startActivityForResult(intent, requestCode);
		}catch (ActivityNotFoundException e) {
			sendToBlock("NOT_FOUND");
		}
	}

	public void release() {
		textToSpeech.shutdown();
	}

	// Do not edit this constructor.
	public Speech(int requestCode, java.lang.String ID) {
	    this.requestCode = requestCode;
	    this.ID = ID;
	}

}
