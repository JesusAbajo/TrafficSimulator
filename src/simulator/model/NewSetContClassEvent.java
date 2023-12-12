package simulator.model;

import java.util.ArrayList;
import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	
	private List<Pair<String, Integer>> c_cs;
	
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		
		super(time);
		
		if (cs == null) {
			throw new IllegalArgumentException("Weather event is empty");
		}
		
		c_cs = new ArrayList<Pair<String, Integer>>(cs);

	}

	@Override
	void execute(RoadMap map) {
		for (Pair<String, Integer> par : c_cs) { // sacamos cada par string(id), weather
			Road r = map.getRoad(par.getFirst());
			
			if (r == null) {	//vemos si existe en el mapa
				throw new IllegalArgumentException("That road does not exist");
			}
			
			r.setTCo2(par.getSecond());	//asignamos el tiempo a su carretera
		}
		
	}
}
