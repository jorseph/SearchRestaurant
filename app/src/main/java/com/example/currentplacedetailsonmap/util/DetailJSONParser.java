package com.example.currentplacedetailsonmap.util;

import android.util.Log;

import com.example.currentplacedetailsonmap.Activity.DataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailJSONParser {
	private static final String TAG = DetailJSONParser.class.getSimpleName();
	/** Receives a JSONObject and returns a list */
	public HashMap<String,String> parse(JSONObject jObject){

        JSONObject jDetails = null;
		String phone = "-NA-";
		HashMap<String, String> details = new HashMap<String, String>();

		try {			
			/** Retrieves all the elements in the 'details' array */
			jDetails = jObject.getJSONObject("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			/** Retrieves all the elements in the 'details' array */
			if(!jDetails.isNull("formatted_phone_number")) {
				phone = jDetails.getString("formatted_phone_number");
				Log.v(TAG,phone);
			}
			details.put("phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return details;
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		//return getDetails(jDetails);
	}

	/** Parsing the Place JSON object
	private HashMap<String, String> getDetails(JSONObject jPhone){

		HashMap<String, String> details = new HashMap<String, String>();
		String phone = "-NA-";

		try {
			// Extracting Phone name, if available
			if(!jPhone.isNull("formatted_phone_number")){
				phone = jPhone.getString("formatted_phone_number");
				Log.v(TAG,phone);
			}
			details.put("phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return details;
	}*/
}
