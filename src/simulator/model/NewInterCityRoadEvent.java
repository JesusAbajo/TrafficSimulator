package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent{
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJun, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time, id, srcJun, destJun, length, co2Limit, maxSpeed, weather);
	}

	public String toString() {
    	return "New InterCityRoad '"+r_id+"'";
    } 
	@Override
	public void execute(RoadMap map) {
		map.addRoad(new InterCityRoad(r_id, map.getJunction(r_srcJun), map.getJunction(r_destJun), r_maxSpeed, r_co2Limit, r_length, r_weather));
	}
}
