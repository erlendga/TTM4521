package no.ntnu.item.arctis.android.erlendga.library.speechinput;

import no.ntnu.item.arctis.android.R;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.bitreactive.library.android.core.activity.ArctisAndroidActivity;

public class SpeechInputActivity extends ArctisAndroidActivity {
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sendSignalToBuildingBlockWithObject("ACTIVITY_RESULT", resultCode);
	}
}
