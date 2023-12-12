package simulator.model;

public class CityRoad extends Road {

	public CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		if(getWeather() == Weather.WINDY|| getWeather() == Weather.STORM) {
			x = 10;
		}
		else  {
			x = 2;
		}
		this.tCo2 = Math.max(tCo2 - x, 0);

	}

	@Override
	void updateSpeedLimit() {
		this.speedLimit = this.maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		// TODO Auto-generated method stub
		return(((11 - v.getContClass()) * getSpeedLimit())/ 11);
	}

}
