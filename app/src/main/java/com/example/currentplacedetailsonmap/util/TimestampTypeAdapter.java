package com.example.currentplacedetailsonmap.util;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TimestampTypeAdapter implements JsonSerializer<Timestamp>,
		JsonDeserializer<Timestamp> {
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

	@Override
	public Timestamp deserialize(JsonElement json, Type typeOfSrc,
			JsonDeserializationContext context) throws JsonParseException {
		try {
			Date date = dateFormat.parse(json.getAsString());
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Timestamp src, Type typeOfSrc,
			JsonSerializationContext context) {
		String dateFormatString = dateFormat.format(new Date(src.getTime()));
		return new JsonPrimitive(dateFormatString);
	}
}
