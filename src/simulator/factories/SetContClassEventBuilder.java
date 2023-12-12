package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected SetContClassEvent createTheInstance(JSONObject data) {

		List<Pair<String, Integer>> contClass = new ArrayList<Pair<String,Integer>>();
		JSONArray info = data.getJSONArray("info");
		for(int i = 0; i < info.length(); i++) {
			
            String first = info.getJSONObject(i).getString("vehicle");
            Integer second = info.getJSONObject(i).getInt("class");
            
			contClass.add(new Pair<String, Integer>(first, second));
		}
		
		SetContClassEvent contClassEvent = new SetContClassEvent(data.getInt("time"), contClass);
		
		return contClassEvent;
	}

	

}