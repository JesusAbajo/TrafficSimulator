package simulator.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> JunctionList;
	private List<Road> RoadList;
	private List<Vehicle> VehicleList;
	
	private Map<String, Junction> JunctionMap;
	private Map<String, Road> RoadMap;
	private Map<String, Vehicle> VehicleMap;
	
	RoadMap(){
		JunctionList = new ArrayList<Junction>();
		RoadList = new ArrayList<Road>();
		VehicleList = new ArrayList<Vehicle>();
		JunctionMap = new HashMap<String, Junction>();
		RoadMap = new HashMap<String, Road>();
		VehicleMap = new HashMap<String, Vehicle>();
	}

	void addJunction(Junction j) {
		if (!(JunctionMap.containsKey(j.getId()))) {
			JunctionList.add(j);
			JunctionMap.put(j.getId(), j);			
		}
		else 
            throw new IllegalArgumentException("Junction already in RoadMap");
	}
	
	void addRoad(Road r) {
        if (RoadMap.containsKey(r.getId()))
            throw new IllegalArgumentException("Road already in RoadMap");
        else if(!(JunctionMap.containsValue(r.getSrc())) || !(JunctionMap.containsValue(r.getDest())))
        	throw new IllegalArgumentException("Junctions connected to this road don't exist");
        else {
        	RoadList.add(r);
        	RoadMap.put(r.getId(), r);
        }       
	}
	
	void addVehicle(Vehicle v) {
		if((VehicleMap.containsKey(v.getId()))) {
			throw new IllegalArgumentException("Vehicle already in RoadMap");
		}
		for (int i = 0; i < v.getItinerary().size() - 1 ; i++) {
            if (v.getItinerary().get(i).roadTo(v.getItinerary().get(i+1)) == null)
                throw new IllegalArgumentException("Junctions of the itinerary doesn't exist");
        }
		VehicleList.add(v);
		VehicleMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		return JunctionMap.get(id);
		
	}
	public Road getRoad(String id) {
		return RoadMap.get(id);
		
	}
	public Vehicle getVehicle(String id) {
		return VehicleMap.get(id);
		
	}
	public List<Junction>getJunctions(){
		return Collections.unmodifiableList(JunctionList);//Funcion para crear listas de solo lectura
	}
	
	public List<Road>getRoads(){
		return Collections.unmodifiableList(RoadList);//Funcion para crear listas de solo lectura
	}
	
	public List<Vehicle>getVehicles(){
		return Collections.unmodifiableList(VehicleList);//Funcion para crear listas de solo lectura
	}
	
	void reset() {
		JunctionList.clear();
		RoadList.clear();
		VehicleList.clear();
		JunctionMap.clear();
		RoadMap.clear();
		VehicleMap.clear();
	}
	
	public JSONObject report() {
		JSONObject report = new JSONObject();
		JSONArray eachReport = new JSONArray();  //inicializamos
		
		for (Junction j : JunctionList) {
			eachReport.put(j.report());
		}
		report.put("junctions", eachReport);
		
		eachReport = new JSONArray();  // vaciamos para reutilizar
		
		for (Road r : RoadList) {
			eachReport.put(r.report());
		}
		report.put("roads", eachReport);
		
		eachReport = new JSONArray(); // vaciamos para reutilizar
		
		for (Vehicle v : VehicleList) {
			eachReport.put(v.report());
		}
		report.put("vehicles", eachReport);
		
		return report;
	}
	
}
