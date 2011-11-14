package no.ntnu.item.arctis.android.erlendga.library.validateresult;

import java.util.ArrayList;

import no.ntnu.item.arctis.runtime.android.AndroidBlock;

public class ValidateResult extends AndroidBlock {

	public java.lang.String address;
	public java.util.ArrayList<java.lang.String> result;
	
	public void validateResult(ArrayList<String> result) {
		String signal = "NOT_FOUND";
		for (String sentence : result) {
        	if (sentence.equals("call")) {
        		signal = "CALL";
        	}
        	if (sentence.contains("call") && sentence.contains(" ")) {
        		String address = sentence.substring(sentence.indexOf(" ") + 1, sentence.length());
        		if (address.equals("boat")) {
					signal = "CALL_ADDRESS";
					this.address = address.toUpperCase();
					break;
				}
				else if (address.equals("house")) {
					signal = "CALL_ADDRESS";
					this.address = address.toUpperCase();
					break;
				}
				else if (address.equals("job")) {
					signal = "CALL_ADDRESS";
					this.address = address.toUpperCase();
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
		sendToBlock(signal);	
	}
}
