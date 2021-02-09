package meme;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class APIParser { //because im bored imma make my own
	private String thing;
	Map<String, String> apiList = new HashMap<String, String>();

	public APIParser(String api) {
		Log.d("TAG", "APIParser: in instantiation: api is good");
		thing = api;
	}

	public Map give() {
		return apiList;
	}


	private void makeMap() {
		String api[] = thing.split("\"");
		for (int i = 0; i < api.length; i++) {
			//get rid of the dumb ones
			if ((api[i].equals("{")) || (api[i].equals("]}")) || (api[i].equals(":[")) || (api[i].equals(",")) || (api[i].equals(":"))) {
				api[i] = null;
			}


		}
		//make a new al to get rid of the nulls
		ArrayList<String> t = new ArrayList<String>();
		for (String nig : api) {
			if (nig == null) {
			} else {
				t.add(nig);
			}
		}
		for (int i = 0; i < t.size(); i++) {
			if (i > 0) {
				if (t.get(i - 1).equals("ups") || t.get(i - 1).equals("nsfw") || t.get(i - 1).equals("spoiler")) {
					String s = t.get(i);
					t.set(i, s.substring(1, s.length() - 1));
				}
			}
		}
		//even is key and odd is value
		String a = null;
		String b = null;
		for (int i = 0; i > t.size(); i++) {
			if (i % 2 == 0) {
				a = t.get(i);
			} else {
				b = t.get(i);
				this.apiList.put(a, b);
				Log.d("apilist", "makeMap: putting " + a + "," + b);
			}
		}
	}
}
