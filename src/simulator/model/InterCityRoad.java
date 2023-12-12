package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);

	}

	@Override
	void reduceTotalContamination() {
		int x= 0;
		if(getWeather() == Weather.SUNNY) {
			x = 2;
		}
		else if (getWeather() == Weather.CLOUDY) {
			x = 3; 
		}
		else if (getWeather() == Weather.RAINY) {
			x = 10;
		}
		else if (getWeather() == Weather.WINDY) {
			x = 15;
		}
		else if (getWeather() == Weather.STORM) {
			x = 20;
		}
		setTCo2(((100 - x )* this.getTotalCO2())/100);
	}

	@Override
	void updateSpeedLimit() {
		if (getTotalCO2() > (getContLimit())) {
			setSpdLmt(getMaxSpeed()/2);
		}
		else {
			setSpdLmt(getMaxSpeed());
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if (getWeather()== Weather.STORM){
				return (getSpeedLimit()*8)/10;
		}
		else {
			return getSpeedLimit();
		}
	}

}
