package no.ntnu.arctis.android.erlendga.speechinput.speechinput;

import java.util.ArrayList;

import no.ntnu.item.arctis.android.R;
import no.ntnu.item.arctis.runtime.android.AndroidBlock;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.Toast;

public class SpeechInput extends AndroidBlock {

	public SpeechInputActivity activity;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	public java.lang.String message;
	private CheckBox checkBox;
	private boolean idle = true;

	public Activity getActivity() {
		return activity;
	}

	public void addListeners() {
		Runnable r = new Runnable() {
			public void run() {
				activity.setParentID(blockID);
				activity.registerButton(R.id.btn_speak, "BUTTON", blockID);
				activity.setEnabled(R.id.btn_speak, true);		
				checkBox = (CheckBox) activity.findViewById(R.id.checkBox1);	

				BroadcastReceiver boadcastReceiver = new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
							String intentAction = intent.getAction();
					        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
					            return;
					        }
					        KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
					        if (event == null) {
					            return;
					        }
					        int action = event.getAction();
					        if (action == KeyEvent.ACTION_DOWN) {
						    	sendToBlock("MEDIA_BUTTON");
					        }
					        abortBroadcast();
				        }
					}
				};		
				IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
				filter.setPriority(1000000);
				activity.registerReceiver(boadcastReceiver, filter);
			}
		};
		activity.runOnUiThread(r);
	}
	
	public void setMessageStatement() {	 
		message = "Recording";
		idle = false;
	}

	public void startVoiceRecognitionActivity() {
		idle = true;
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a statement");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		activity.startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	public String keywordRecognition(ArrayList<String> matches) {
        String signal = "NOT_FOUND";
		if (checkBox.isChecked()) {
			for (String sentence : matches) {
	        	if (sentence.equals("call")) {
	        		signal = "CALL";
	        	}
	        	if (sentence.contains("call") && sentence.contains(" ")) {
	        		String address = sentence.substring(sentence.indexOf(" ") + 1, sentence.length());
	        		if (address.equals("boat")) {
						signal = "CALL_BOAT";
						break;
					}
					else if (address.equals("house")) {
						signal = "CALL_HOUSE";
						break;
					}
					else if (address.equals("job")) {
						signal = "CALL_JOB";
						break;
					}
					else if (address.equals("back")) {
						signal = "CALLBACK";
						break;
					}
	        	}
	        	else if (sentence.equals("replay")) {
					signal = "REPLAY";
				}
	        	else if (sentence.equals("emergency")) {
					signal = "EMERGENCY";
				}
				else if (sentence.equals("callback")) {
					signal = "CALLBACK";
				}
			}
		}
		else {
			String sentence = matches.get(0);
			if (sentence.equals("call")) {
        		signal = "CALL";
        	}
        	else if (sentence.equals("replay")) {
        		signal = "REPLAY";
        	}
        	else if (sentence.equals("emergency")) {
        		signal = "EMERGENCY";
        	}
        	else if (sentence.equals("call boat")) {
        		signal = "CALL_BOAT";
        	}
        	else if (sentence.equals("call house")) {
        		signal = "CALL_HOUSE";
        	}
        	else if (sentence.equals("call job")) {
        		signal = "CALL_JOB";
        	}
        	else if (sentence.equals("callback") || (sentence.equals("call back"))) {
        		signal = "CALLBACK";
        	}
		}
		return signal;
	}

	public void setMessageCallBoat() {
		message = "Connecting to boat.";
	}

	public void setMessageReplay() {
		message = "Replaying last message.";
	}

	public void setMessageEmergency() {
		message = "Connecting to nearest doctor.";
	}

	public void setMessageNotFound() {
		message = "Not recognizable. Please try again.";
	}

	public void setMessageCall() {
		message = "Unrecognized address. Please try again.";
	}

	public void setMessageCallHouse() {
		message = "Connecting to house.";
	}

	public void setMessageCallJob() {
		message = "Connecting to job.";
	}
	
	public void setMessageCallback() {
		message = "Connecting to address.";
	}

	public void makeToast(final String message) {
		Runnable r = new Runnable() {
			
			public void run() {
				Context context = activity.getApplicationContext();
				CharSequence text = message;
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				sendToBlock("TOASTED");
			}
		};
		activity.runOnUiThread(r);
	}

	public boolean getIdle() {
		return idle;
	}
}
