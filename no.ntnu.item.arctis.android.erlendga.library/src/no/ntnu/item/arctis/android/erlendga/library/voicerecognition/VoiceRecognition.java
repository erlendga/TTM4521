package no.ntnu.item.arctis.android.erlendga.library.voicerecognition;

import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.Block;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class VoiceRecognition extends Block {

	// Instance parameter. Edit only in overview page.
	public final int maxResults;
	
	public void createSpeechRecognizer(Activity activity) {
		activity.runOnUiThread(new Runnable() {
			
			public void run() {
				Context context = (Context) getProperty("Android");
				SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(context);
				RecognitionListener listener = new RecognitionListener() {
					
					public void onRmsChanged(float rmsdB) {
						// TODO Auto-generated method stub
						
					}
					
					public void onResults(Bundle results) {
						ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			            sendToBlock("RESULT", matches);
					}
					
					public void onReadyForSpeech(Bundle params) {
						sendToBlock("READY_FOR_SPEECH");
					}
					
					public void onPartialResults(Bundle partialResults) {
						// TODO Auto-generated method stub
						
					}
					
					public void onEvent(int eventType, Bundle params) {
						// TODO Auto-generated method stub
						
					}
					
					//TODO Make the error strings as instance parameters to make the block more generic
					public void onError(int error) {
						final String ERROR = "ERROR";
						switch (error) {
							case SpeechRecognizer.ERROR_AUDIO:
								sendToBlock(ERROR, "Audio recording error.");
								break;
							case SpeechRecognizer.ERROR_CLIENT:
								sendToBlock(ERROR, "Other client side errors.");
								break;
							case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
								sendToBlock(ERROR, "Insufficient permessions.");
								break;
							case SpeechRecognizer.ERROR_NETWORK:
								sendToBlock(ERROR, "Other network related errors.");
								break;
							case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
								sendToBlock(ERROR, "Network operation timed out.");
								break;
							case SpeechRecognizer.ERROR_NO_MATCH:
								sendToBlock(ERROR, "No recognition result matched.");
								break;
							case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
								sendToBlock(ERROR, "Recognition service busy.");
								break;
							case SpeechRecognizer.ERROR_SERVER:
								sendToBlock(ERROR, "Server sends error status.");
								break;
							case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
								sendToBlock(ERROR, "No speech input.");
								break;
							default:
								sendToBlock(ERROR, "Unknown error.");
								break;
						}
					}
					
					public void onEndOfSpeech() {
						sendToBlock("END_OF_SPEECH");
					}
					
					public void onBufferReceived(byte[] buffer) {
						// TODO Auto-generated method stub
						
					}
					
					public void onBeginningOfSpeech() {
						// TODO Auto-generated method stub
						
					}
				};
				
				recognizer.setRecognitionListener(listener);
			
				Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, maxResults);
		        
				recognizer.startListening(intent);	
			}
		});
	}
	// Do not edit this constructor.
	public VoiceRecognition(int maxResults) {
	    this.maxResults = maxResults;
	}
}
