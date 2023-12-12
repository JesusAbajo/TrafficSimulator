package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Controller{
	
    private TrafficSimulator trafSim;
    private Factory<Event> evFactory;

    public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
        if (sim == null)
            throw new IllegalArgumentException("TrafficSimulator is null");
        else if (eventsFactory == null)
            throw new IllegalArgumentException("Events Factory is null");
        else {
        trafSim = sim;
        evFactory = eventsFactory;
        }
    }
    
    public void reset() {
        trafSim.reset();
    }
    
    public void loadEvents(InputStream in) {
    	
        JSONObject jo = new JSONObject(new JSONTokener(in));
        JSONArray ja = jo.getJSONArray("events");
        
        for (int i = 0; i < ja.length(); i++) {
            Event event = evFactory.createInstance(ja.getJSONObject(i));
            if (event == null)
            	throw new IllegalArgumentException("JSON is null");
            trafSim.addEvent(event);
        }
    }
    
    public void addObserver(TrafficSimObserver o){
    	trafSim.addObserver(o);
    }
    
    public void removeObserver(TrafficSimObserver o){
    	trafSim.removeObserver(o);
    }
    
    public void addEvent(Event e) {
    	trafSim.addEvent(e);//lo utilizan las 
    }
    
    public void run(int n, OutputStream out) {
    	
        PrintStream p = new PrintStream(out);
        p.println("{");
        p.println(" \"states\": [");
        for (int i = 0; i < n; i++) {
            trafSim.advance();
            p.print(trafSim.report());
            if (i != n-1) {
            	p.println(",");
            }
        }
        p.println("]");
        p.println("}");
    }
    
    public void run(int n) {
    	
        for (int i = 0; i < n; i++) {
            trafSim.advance();
        }
       
    }
    public RoadMap getRoadMap() {
        return trafSim.getRoadMap();
    }
    
}