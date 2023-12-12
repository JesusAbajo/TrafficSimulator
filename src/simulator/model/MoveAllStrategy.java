package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	public List<Vehicle> dequeue(List<Vehicle> q) {
		
		List<Vehicle> o = new ArrayList<Vehicle>(q);
		
		return o;
	}

}
