package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {
	
	private RoadMap roadmap;
	private List<Event> events;
	private int time;
	private List<TrafficSimObserver> observerList;
	
	public TrafficSimulator(){
		roadmap = new RoadMap();
		events = new SortedArrayList<Event>();
		observerList = new ArrayList<TrafficSimObserver>();
		time = 0;
	}
	
	public void addEvent(Event event){
		events.add(event);
		//para no pasar parametros hacemos el for aqui sin un metodo auxiliar
		for(TrafficSimObserver o: observerList) {
			o.onEventAdded(roadmap, events,event, time);
		}
	}
		
		
	public void advance(){
		time++;
		
		notifyOnAdvanceStart();
		try {
			for (int i = 0; i < events.size(); i++) {
				if (time == events.get(i).getTime()) {
					events.get(i).execute(roadmap);
					//events.remove(events.get(i));
					//i--
				}
			}
			
			for (int j = 0; j < events.size(); j++) {
				if (time == events.get(j).getTime()) {
					events.remove(events.get(j));
				}
			}
			
			
			
			for (Junction j : roadmap.getJunctions()) {
				j.advance(time);
			}
			
			for (Road r : roadmap.getRoads()) {
				r.advance(time);
			}
		}
		catch(Exception e) {
			notifyOnError(e);
			throw e;
		}
		notifyOnAdvanceEnd();
	}
	
	public void reset(){
		roadmap.reset();
		events.clear();
		time = 0;
		notifyOnReset();
	}
	
	public void notifyOnAdvanceStart() {
		for(TrafficSimObserver o: observerList) {
			o.onAdvanceStart(roadmap, events, time);
		}
	}
	
	public void notifyOnAdvanceEnd() {
		for(TrafficSimObserver o: observerList) {
			o.onAdvanceEnd(roadmap, events, time);
		}
	}
	
	public void notifyOnReset() {
		for(TrafficSimObserver o: observerList) {
			o.onReset(roadmap, events, time);
		}
	}
	
	public void notifyOnError(Exception e) {
		for(TrafficSimObserver o: observerList) {
			o.onError(e.getMessage());
		}
	}
	
	public JSONObject report() {
		JSONObject report = new JSONObject();
		
		report.put("time", time);
		report.put("state", roadmap.report());
		
		return report;
	}


	@Override
	public void addObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		observerList.add(o);
		o.onRegister(roadmap, events, time);
		
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		observerList.remove(o);
		
	}
	public RoadMap getRoadMap() {
        return roadmap;
    }
}
