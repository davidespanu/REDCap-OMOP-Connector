package builders;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class BaseBuilder {
	
	
	public static JSONObject linearSearch(JSONArray array, String id) {
		for(int i = 0; i < array.length(); i++) {
			if(array.getJSONObject(i).getString("record_id").equals(id)) {
				return array.getJSONObject(i);
			}
		}
			
		return null;
	}

	
}
