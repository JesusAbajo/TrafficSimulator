package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> c_cs;
	
	
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		
		super(time);
		
		if (cs == null) {
			throw new IllegalArgumentException("Weather event is empty");
		}
		
		c_cs = new ArrayList<Pair<String, Integer>>(cs);

	}
	
	public String toString() {
    	return "New SetWeather '"+c_cs+"'";
    } 


	@Override
	void execute(RoadMap map) {
		for (Pair<String, Integer> par : c_cs) { // sacamos cada par string(id), weather
			Vehicle v = map.getVehicle(par.getFirst());
			
			if (v == null) {	//vemos si existe en el mapa
				throw new IllegalArgumentException("That Vehicle does not exist");
			}
			
			v.setContClass(par.getSecond());	//asignamos el tiempo a su carretera
		}
		
	}
}
