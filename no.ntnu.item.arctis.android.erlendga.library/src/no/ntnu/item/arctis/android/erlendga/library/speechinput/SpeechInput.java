package no.ntnu.item.arctis.android.erlendga.library.speechinput;

import no.ntnu.item.arctis.android.R;
import no.ntnu.item.arctis.runtime.Block;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PowerManager;

public class SpeechInput extends Block {

	public java.lang.String message;
	public java.lang.String address;
	public java.util.ArrayList<java.lang.String> result;
	public no.ntnu.item.arctis.android.erlendga.library.speechinput.SpeechInputActivity activity;
	public void addListeners() {
		Runnable r = new Runnable() {
			
			public void run() {
				activity.registerButton(R.id.exitButton, "EXIT", blockID);
				
			}
		};
		activity.runOnUiThread(r);
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public void setMessageCallAddress(String address) {
		message = "Connecting to " + address.toLowerCase() + ".";
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
	
	public void setMessageCallback() {
		message = "Connecting to address.";
	}
	public void setMessageRunning() {
		message = "Running";
	}

	public void moveTaskToBack() {
		activity.moveTaskToBack(true);
	}

	public void playMessage() {
		sendToBlock("MESSAGE", message);
	}

	public void moveTaskToFront() {
		activity.moveTaskToBack(false);
	}

	public void setMessageError() {
		message = "An error occurred. Please try again.";
	}
}
