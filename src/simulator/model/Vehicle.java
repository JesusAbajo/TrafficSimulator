package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;




//Creo que hay que cambiar algo de setSpeed

public class Vehicle extends SimulatedObject {
	
	private int maxSpd, contCls, speed, location, co2 /*total co2*/, distance /*total distance*/;
	private List<Junction> vitinerary = new ArrayList<Junction>();
	private VehicleStatus status;
	private Road road;
	
	public Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
	// TODO complete
		if(maxSpeed > 0) {
			maxSpd = maxSpeed;
		}else {
			throw new IllegalArgumentException("maxSpeed must be positive");
		}
		if(contClass >= 0 && contClass <= 10) {
			contCls = contClass;
		}else {
			throw new IllegalArgumentException("contamination class must be non-negative");
		}
		
		if(itinerary.size() >= 2) {
			for(Junction junc : itinerary) {
				vitinerary.add(junc);
			}
		}else {
			throw new IllegalArgumentException("itinerary must be a list with at least two junctions");
		}
		status = VehicleStatus.PENDING;
		location = 0;
		speed = 0;
		distance = 0;
		
	} 
	
	public int getLocation() {
		return location;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getMaxSpeed() {
		return maxSpd;
	}
	
	public int getContClass() {
		return contCls;
	}
	
	public VehicleStatus getStatus() {
		return status;
	}
	
	public int getTotalCO2() {
		return co2;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public List<Junction> getItinerary(){
		return Collections.unmodifiableList(vitinerary);
	}
	
	public Road getRoad() {
		return road;
	}
	
	void setSpeed(int s) {
		
		if(s >= 0) {
			if(status == VehicleStatus.TRAVELING) {
				speed = Math.min(s, maxSpd);
			}
		}else {
			throw new IllegalArgumentException("argument 's' can't be negative");
		}
	}
	
	void setContClass(int c) {
		if(c >= 0 && c <= 10) {
			contCls = c;
		}else {
			throw new IllegalArgumentException("argument 'c' must be between 0 & 10");
		}
	}
	
	void advance(int time) {
		int newLoc = 0; //nueva localizacion
		int advDist = 0;//distancia avanzada
		int prodCont = 0; //contaminacion producida
		if(status == VehicleStatus.TRAVELING) {
			//rellenar el advance
			newLoc = Math.min(location + speed, road.getLength()); // minimo para no exceder la ongitud de la carretera
			advDist = newLoc - location;  // diastanca avanzada con la 	diferencia entre la anteior y la nueva
										  // no se pudede poner la velocidad porque excederiamos la longitud de la carretera en algunos casos 
			prodCont = advDist * contCls; //calculo contaminacion producida
			co2 += prodCont;
			road.addContamination(prodCont);
			location = newLoc;
			distance += advDist;
			
			if(location == road.getLength()) { // si acabamos la carretera
				status = VehicleStatus.WAITING; // ponemos el vehiculo en espera
				road.getDest().enter(this); // y lo colocamos en la cola correspondiente de la junction
				speed = 0;        // con velocidad y posicion a 0 para en road poder hacer el enter
			}
		}
	}
	
	public void moveToNextRoad() {
		//necesitamos road & junction
		
		if(status == VehicleStatus.PENDING) {
			road = vitinerary.get(0).roadTo(vitinerary.get(1)); //colocamos en road la carretera que une las dos primeras junctions del itinerario
			location = 0; //posicion al inicio de la carretera
			road.enter(this); //metemos el coche en la carretera
			status = VehicleStatus.TRAVELING; //al ser la entrada en la primera no hay esperas
			
		}else if(status == VehicleStatus.WAITING) {
			int destInd = vitinerary.indexOf(road.getDest());  //indice del jucntion destino que será el nuevo scource
			road.exit(this);  //salimos de la road actual
			
			if(destInd == (vitinerary.size() - 1)) { //puede que haga falta un -1
				status = VehicleStatus.ARRIVED;
				road = null;  // hemos acabado el itinerario, no entramos en mas roads
				location = 0;
			}else {
				road = vitinerary.get(destInd).roadTo(vitinerary.get(destInd + 1)); // siguiente road con las junctions del itinerario
				location = 0;
				road.enter(this);
				status = VehicleStatus.TRAVELING;
			}
		}else {
			throw new IllegalArgumentException("the vehicle cant move to next road");
		}
	}
	
	
	public JSONObject report() {
		JSONObject report = new JSONObject();
		report.put("id", getId());
		report.put("speed", speed);
		report.put("distance", distance);
		report.put("co2", co2);
		report.put("class", contCls);
		report.put("status", status.toString());
		if(status != VehicleStatus.ARRIVED && status != VehicleStatus.PENDING) { // si esta cambiando de road, no la ponemos, ni su localizacion
			report.put("road", road.getId());
			report.put("location", location);
		}
		return report;
	}
	
}
