package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	
    protected String v_id;
    protected int v_maxSpeed;
    protected int v_contClass;
    protected List<String> v_itinerary;

    public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
        
    	super(time);
        v_id = id;
        v_maxSpeed = maxSpeed;
        v_contClass = contClass;
        v_itinerary = new ArrayList<String>(itinerary);    
    }

    public String toString() {
    	return "New Vehicle '"+v_id+"'";
    } 
    
	@Override
	void execute(RoadMap map) {
		// TODO Auto-generated method stub
		List<Junction> itinerary = new ArrayList<Junction>();
		
		for (String j : v_itinerary) {
			itinerary.add(map.getJunction(j));
		}
		
		Vehicle v = new Vehicle(v_id, v_maxSpeed, v_contClass, itinerary);
		map.addVehicle(v);
		v.moveToNextRoad();
		}
}
