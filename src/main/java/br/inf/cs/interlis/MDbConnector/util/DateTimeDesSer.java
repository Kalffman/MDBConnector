package br.inf.cs.interlis.MDbConnector.util;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeDesSer implements JsonDeserializer<Date>, JsonSerializer<Date> {
	
	private static final String MONGO_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	
	@Override
	public JsonElement serialize(Date src, Type type, JsonSerializationContext context) {
		if (src == null) {
			return null;
		} else {
			SimpleDateFormat format = new SimpleDateFormat(MONGO_UTC_FORMAT);
			JsonObject jo = new JsonObject();
			jo.addProperty("$date", format.format(src));
			return jo;
		}
	}

	@Override
	public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		Date date = null;
		date = new Date(json.getAsJsonObject().get("$date").getAsLong());
//		SimpleDateFormat format = new SimpleDateFormat(MONGO_UTC_FORMAT);
//		try {
//			date = format.parse(json.getAsJsonObject().get("$date").getAsString());
//		} catch (ParseException e) {
//			e.printStackTrace();
//			date = null;
//		}
		return date;
	}
}