package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	
	int _timeSlot;
	public MostCrowdedStrategy(int timeSlot){  // esta el public explicito porque daba error en el builder
		_timeSlot = timeSlot;
	}
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, 
							   int currGreen, int lastSwitchingTime, int currTime) {
		
	if (roads.size() == 0) {
			return -1;
	}		
	
	if(currGreen != -1 && currTime-lastSwitchingTime < _timeSlot) {
		return currGreen;
	}
	else 	
		return searchNextGreen(qs, (currGreen+1)%roads.size());
	}
	
	private int searchNextGreen(List<List<Vehicle>> qs, int startIndex) {
		int max = qs.get(startIndex).size();
		int maxIndex = startIndex;
		
		int i = (startIndex+1) % qs.size();
		while (i != startIndex) {
			int s = qs.get(i).size();
			if (s > max) {
				max = s;
				maxIndex = i;
				
			}
			i = (i + 1) % qs.size();
		}
		return maxIndex;
	}
	
}
