package no.ntnu.arctis.android.erlendga.speechinput.speechinput;

import java.util.ArrayList;

import no.ntnu.item.arctis.android.R;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitreactive.library.android.core.activity.ArctisAndroidActivity;

public class SpeechInputActivity extends ArctisAndroidActivity{
	
private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	
	private ListView mList;
	
	public void onCreate(Bundle savedInstanceState) {	
//		setTheme(android.R.style.Theme_Dialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.voice_recognition);
        mList = (ListView) findViewById(R.id.list);
        super.onCreate(savedInstanceState);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, matches));
            sendSignalToBuildingBlockWithObject("MATCHES", matches);
        }
    	super.onActivityResult(requestCode, resultCode, data);
	}

}
