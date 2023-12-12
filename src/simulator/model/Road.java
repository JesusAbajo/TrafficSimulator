package simulator.model;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Road extends SimulatedObject {
	
	protected int speedLimit, maxSpeed, co2, rlength, tCo2;
	protected Weather rweather;
	protected List<Vehicle> rvehicles = new ArrayList<Vehicle>();
	protected Junction source, destiny;
	
	protected Comparator<Vehicle> comparator = new Comparator<Vehicle>() {
		public int compare(Vehicle va, Vehicle vb) {
			if (va.getLocation() > vb.getLocation()) {
				return -1;
			} else if (vb.getLocation() > va.getLocation()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed,
			int contLimit, int length, Weather weather) {
		super(id);
		
		if(maxSpeed > 0) {
			speedLimit = maxSpeed;
			this.maxSpeed = maxSpeed;
		}else {
			throw new IllegalArgumentException("argument 'maxSpeed' can't be negative");
		}
		
		if(contLimit > 0) {
			co2 = contLimit;
		}else {
			throw new IllegalArgumentException("argument 'contLimit' can't be negative");
		}
		
		if(length > 0) {
			rlength = length;
		}else {
			throw new IllegalArgumentException("argument 'contLimit' can't be negative");
		}
		
		if(srcJunc != null && destJunc != null && weather != null) {
			source = srcJunc;
			destiny = destJunc;
			rweather = weather;
		}else {
			throw new IllegalArgumentException("argument is null");
		}
		
		tCo2 = 0;
		
		destiny.addIncommingRoad(this);
		source.addOutgoingRoad(this);
		
	} 
	
	//Getters
	public int getLength() {
		return rlength;
	}
	
	public Junction getDest() {
		return destiny;
	}
	
	public Junction getSrc() {
		return source;
	}
	
	public Weather getWeather() {
		return rweather;
	}

	public int getContLimit() {
		return co2;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
		
	public int getTotalCO2() {
		return tCo2;
	}
	
	public int getSpeedLimit() {
		return speedLimit;
	}
	
	public List<Vehicle> getVehicles() {
		return  Collections.unmodifiableList(rvehicles);
	}
	
	//Setters	
	public void setWeather(Weather w) {
		if(w != null ) {
			rweather = w;
		}else {
			throw new IllegalArgumentException("argument 'w' can't be null");
		}
	}
	
	void setTCo2(int _TCo2) {
		tCo2 = _TCo2;
	}
	
	void setSpdLmt(int _SpdLmt) {
		speedLimit = _SpdLmt;
	}
	
	void addContamination(int c) {
		if(c >= 0) {
			tCo2 += c;
		}else {
			throw new IllegalArgumentException("argument 'c' can't be negative");
		}
	}
	
	void enter(Vehicle v) {  
		if(v.getLocation() == 0 && v.getSpeed() == 0) {
			rvehicles.add(v);
		}else {
			throw new IllegalArgumentException("arguments 'location' & 'speed' must be 0");
		}
	}
	
	void exit(Vehicle v) {
		rvehicles.remove(v);	
	}	
	
	public void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();
		
		for(Vehicle v : rvehicles) {
			if(v.getStatus() == VehicleStatus.TRAVELING) {
				v.setSpeed(calculateVehicleSpeed(v));
				v.advance(time);
			}
		}
		rvehicles.sort(comparator);
	}
	
	public JSONObject report() {
		JSONObject report = new JSONObject();
		report.put("id", getId());
		report.put("speedlimit", speedLimit);
		report.put("weather", rweather.toString());
		report.put("co2", tCo2);
		JSONArray JSONArrVeh = new JSONArray();
        for (Vehicle v : rvehicles) {
               	JSONArrVeh.put(v.getId());
        }
        report.put("vehicles", JSONArrVeh);
		return report;
	}	
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
}
