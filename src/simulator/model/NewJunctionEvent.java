package simulator.model;

public class NewJunctionEvent extends Event{
	
		private String j_Id;
	    private LightSwitchingStrategy j_LsStrategy;
	    private DequeuingStrategy j_DqStrategy;
	    private int j_x, j_y;
	
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy
							lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		j_Id = id;
		j_LsStrategy = lsStrategy;
		j_DqStrategy = dqStrategy;
		j_x = xCoor;
		j_y = yCoor;
	}

	public String toString() {
    	return "New Junction '"+j_Id+"'";
    } 
	@Override
	void execute(RoadMap map) {
		map.addJunction(new Junction(j_Id, j_LsStrategy, j_DqStrategy, j_x, j_y));		
	}
}
