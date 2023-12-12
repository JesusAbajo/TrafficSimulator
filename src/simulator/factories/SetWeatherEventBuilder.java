package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.RoadMap;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{	

	protected RoadMap roadMap;
	
	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected SetWeatherEvent createTheInstance(JSONObject data) {		
		
		List<Pair<String, Weather>> SetW = new ArrayList<Pair<String,Weather>>();
		JSONArray info = data.getJSONArray("info");
		for(int i = 0; i < info.length(); i++) {
			
            String first = info.getJSONObject(i).getString("road");
            Weather second = Weather.valueOf(info.getJSONObject(i).getString("weather"));
	
			SetW.add(new Pair<String, Weather>(first, second));
		}
		
		SetWeatherEvent weather = new SetWeatherEvent(data.getInt("time"), SetW);
		
		return weather;
	}

}