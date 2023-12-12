package simulator.model;

public abstract class NewRoadEvent extends Event {

    protected String r_id;
    protected String  r_srcJun;
    protected String  r_destJun;
    protected int r_length;
    protected int r_co2Limit;
    protected int r_maxSpeed;
    protected Weather r_weather;

    public NewRoadEvent(int time, String id, String  srcJun, String  destJunc, 
    					int length, int co2Limit, int maxSpeed, Weather weather) {
        super(time);
        r_id = id;
        r_srcJun = srcJun;
        r_destJun = destJunc;
        r_length = length;
        r_co2Limit = co2Limit;
        r_maxSpeed = maxSpeed;
        r_weather = weather;       
    }
}