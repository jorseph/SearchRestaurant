package com.example.currentplacedetailsonmap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {
	
	/** Receives a JSONObject and returns a list */
	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		JSONArray jPlaces = null;
		try {			
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("results");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return getPlaces(jPlaces);
	}

	/** Receives a JSONObject and returns a stringc */
	public String getPageToken(JSONObject jObject){

		String PageToken = null;
		try {
			/** Retrieves all the elements in the 'places' array */
			PageToken = jObject.getString("next_page_token");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		 */
		return PageToken;
	}
	
	
	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> place = null;	

		/** Taking each place, parses and adds to list object */
		for(int i=0; i<placesCount;i++){
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject)jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}
	
	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace){

		HashMap<String, String> place = new HashMap<String, String>();
		String placeName = "-NA-";
		String vicinity="-NA-";
		String latitude="";
		String longitude="";
		String photo_reference = "-NA-";
		String now_open = "";
		StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_PHOTO_API);
		
		try {
			// Extracting Place name, if available
			if(!jPlace.isNull("name")){
				placeName = jPlace.getString("name");
			}
			
			// Extracting Place Vicinity, if available
			if(!jPlace.isNull("vicinity")){
				vicinity = jPlace.getString("vicinity");
			}

			// Extracting Place Photo, if available
			if(!jPlace.isNull("photos")){
				JSONArray photos = jPlace.getJSONArray("photos");
				//Run for loop for getting photo_reference string in each object
				for (int i=0; i < 1; i++){
					JSONObject getPhtotos = photos.getJSONObject(i);
					photo_reference = getPhtotos.getString("photo_reference");
					sb.append(photo_reference);
					sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);
				}
			}

			latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
			longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
			if(!jPlace.isNull("opening_hours")) {
				now_open = jPlace.getJSONObject("opening_hours").getString("open_now");
			}
			
			place.put("place_name", placeName);
			place.put("vicinity", vicinity);
			place.put("lat", latitude);
			place.put("lng", longitude);
			//a string builder here to create the url
			place.put("photo", sb.toString());
			place.put("nowopen", now_open);
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return place;
	}
}
