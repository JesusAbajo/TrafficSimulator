package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> inRoad = new ArrayList<Road>();
	private Map<Junction, Road> outRoad = new HashMap<Junction, Road>();
	private List<List<Vehicle>> colasveh = new ArrayList<List<Vehicle>>();
	private Map<Road,List<Vehicle>> queueList = new HashMap<Road, List<Vehicle>>();
	private int currGreen/*semaforo verde (-1 todos rojo)*/, lastSwitchingTime/*paso cambio semaforo*/;
	private int x, y; //coordenadas
	private LightSwitchingStrategy lssg;
	private DequeuingStrategy dqs;
	
	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy
			dqStrategy, int xCoor, int yCoor) {
		super(id);
		
		currGreen = -1;
		lastSwitchingTime = 0;
		if(xCoor >= 0 && yCoor >= 0) {
			x = xCoor;
			y = yCoor;
		}else {
			throw new IllegalArgumentException("'x' and 'y' can't be negative");
		}
		
		if(lsStrategy != null && dqStrategy != null ) {
			lssg = lsStrategy;
			dqs = dqStrategy;
			
		}else {
			throw new IllegalArgumentException("strategy is null");
		}
		
		

	}

	//Getters
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getGreenLightIndex() {
		// TODO Auto-generated method stub
		return currGreen;
	}
	
	public List<Road> getInRoads() {
		// TODO Auto-generated method stub
		return inRoad;
	}
	
	void addIncommingRoad(Road r){
		
		if(r.getDest() != this) {
			throw new IllegalArgumentException("Road source is not Junction");
		}
		
		inRoad.add(r); //r a carreteras entrantes
		//comprobar entrante
		List<Vehicle> queue = new LinkedList<Vehicle>();
		queueList.put(r, queue);		
		colasveh.add(queue);		
		
	}
	
	void addOutgoingRoad(Road r){
		
		if(r.getSrc() != this /*|| outRoad.containsKey(r.getDest())*/) {
			throw new IllegalArgumentException("Road destination is not Junction");
		}
		
		outRoad.put(r.getDest(), r);
		
		//comprobar saliente
	}

	void enter(Vehicle v) {  //j.enter(v)
		
		queueList.get(v.getRoad()).add(v);
		
	}
	
	Road roadTo(Junction j) { //junct.roadto(j)

		return outRoad.get(j);
		
	}
	
	
	void advance(int time) {
		/*Iterator it = queueList.entrySet().iterator();
		int num = 0;
		while(it.hashCode() != trlight) {
			
		}*/
		//dqs.dequeue(queueList.get(this.));
		if((currGreen != -1)) {
			for(Vehicle v : dqs.dequeue(colasveh.get(currGreen))) {
				v.moveToNextRoad(); //va a la siguiente road el vehiculo seleccionado de la cola con semforo en verde
				
				colasveh.get(currGreen).remove(v); // y lo elimina de la cola que lo ha sacado
			}
		}
		
		int newGreen = lssg.chooseNextGreen(inRoad, colasveh, currGreen, lastSwitchingTime, time);
		if(newGreen != currGreen) {
			currGreen = newGreen;
			lastSwitchingTime = time;
		}
		
		
	}

	@Override
	public JSONObject report() {
		JSONObject report = new JSONObject();
		report.put("id", getId());
		if (currGreen != -1) 
			report.put("green", inRoad.get(currGreen)._id);
		 else 
			report.put("green", "none");
		
		JSONArray JSONArrQueues = new JSONArray();
		
		for ( Road road : inRoad) { // Carreteras entrantes 
			JSONObject queue = new JSONObject(); // Cola de Roads
			JSONArray JSONArrV_Ids = new JSONArray(); // Array de Ids de Vehicle
			
			queue.put("road", road.getId());

			for (Vehicle veh: queueList.get(road) ) { // Vehiculos de cada Road
				JSONArrV_Ids.put(veh.getId());
			}

			queue.put("vehicles", JSONArrV_Ids);
			
			JSONArrQueues.put(queue);
		}
		report.put("queues", JSONArrQueues);
		return report;		
	}

	public List<Vehicle> getQueue(Road r) {
		// TODO Auto-generated method stub
		return queueList.get(r);
	}


}
