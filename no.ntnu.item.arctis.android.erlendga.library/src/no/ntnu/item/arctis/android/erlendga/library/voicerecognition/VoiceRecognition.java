package no.ntnu.item.arctis.android.erlendga.library.voicerecognition;

import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.Block;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
	
	private Context getContext() {
		return (Context) getProperty("Android");
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

	// Do not edit this constructor.
	public VoiceRecognition(java.lang.String hint, int requestCode, int maxResults) {
	    this.hint = hint;
	    this.requestCode = requestCode;
	    this.maxResults = maxResults;
	}

	public void addListener() {
		activity.registerOnActivityResultListener(requestCode, new IActivityResultListener() {
			
			public void onActivityResult(int resultCode, Intent data) {
				if (resultCode == android.app.Activity.RESULT_OK) {
		            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		            sendToBlock("RESULT", matches);
		        }
				else {
		        	if(!wasNotFound) {
		        		sendToBlock("CANCEL");
		        	}
		        }
			}
		});
	}
}
