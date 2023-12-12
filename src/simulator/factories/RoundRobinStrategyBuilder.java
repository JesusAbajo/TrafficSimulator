package simulator.factories;

import org.json.JSONObject;

import simulator.model.RoundRobinStrategy;
import simulator.model.LightSwitchingStrategy;


public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {
	
	public RoundRobinStrategyBuilder() {
		
		super("round_robin_lss");
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int timeslot;

		if (!data.isNull("timeslot")) {
			timeslot = data.getInt("timeslot");
		} else {
			timeslot = 1;
		}
		
		return new RoundRobinStrategy(timeslot);
	}
}