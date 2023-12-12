package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy {

	public List<Vehicle> dequeue(List<Vehicle> q) {
		
		List<Vehicle> o = new ArrayList<Vehicle>(1);
		if(q.size() > 0) {
			o.add(q.get(0));
		}
		return o;		
		
	}
}
