package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {
	
	private List<Pair<String, Weather>> w_ws;
	
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if (ws == null) {
			throw new IllegalArgumentException("Weather event is empty");
		}
		
		w_ws = new ArrayList<Pair<String, Weather>>(ws);

	}
	
	public String toString() {
    	return "New SetWeather '"+w_ws+"'";
    } 

	@Override
	void execute(RoadMap map) {
		for (Pair<String, Weather> par : w_ws) { // sacamos cada par string(id), weather
			Road r = map.getRoad(par.getFirst());
			
			if (r == null) {	//vemos si existe en el mapa
				throw new IllegalArgumentException("That road does not exist");
			}
			
			r.setWeather(par.getSecond());	//asignamos el tiempo a su carretera
		}
		
	}

}
