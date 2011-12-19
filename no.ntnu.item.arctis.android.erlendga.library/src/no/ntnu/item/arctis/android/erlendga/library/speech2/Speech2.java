package no.ntnu.item.arctis.android.erlendga.library.speech2;

import java.util.HashMap;

import no.ntnu.item.arctis.runtime.Block;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;

public class Speech2 extends Block {

	private TextToSpeech textToSpeech;
	// Instance parameter. Edit only in overview page.
	public final int requestCode;
	// Instance parameter. Edit only in overview page.
	public final java.lang.String iD;
	
	public void initializeTTS() {
        textToSpeech = new TextToSpeech((Context) getProperty("Android"), new OnInitListener() {
			
			public void onInit(int status) {
				
				textToSpeech.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
					
					public void onUtteranceCompleted(String utteranceId) {
						if (iD.equals(utteranceId)) {
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
	
	public void textToSpeech(String text) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, iD);
		textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params);
	}

	//TODO Move this functionality in the onUtteranceCompleted method
	public void release() {
		textToSpeech.shutdown();
	}
	
	// Do not edit this constructor.
	public Speech2(int requestCode, java.lang.String iD) {
	    this.requestCode = requestCode;
	    this.iD = iD;
	}
}
