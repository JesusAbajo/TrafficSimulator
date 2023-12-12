package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	private static String[] _cols =  { "Id", "Location", "Itinerary","Co2 Class","MaxSpeed","Speed","Total Co2", "Distance"};
	
	private List<Vehicle> _vehicles;
	
	VehiclesTableModel(Controller ctrl) {
		_vehicles = new ArrayList<>();
		ctrl.addObserver(this);
		
	}
	@Override
	public int getRowCount() {
		return _vehicles.size();
	}
	@Override
	public int getColumnCount() {
		return _cols.length;
	}

	public String getColumnName(int column) {
		return _cols[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		Vehicle v = _vehicles.get(rowIndex);
		switch ( columnIndex ) {
			case 0:
				s = v.getId();
				break;
			case 1:
				switch (v.getStatus()) {
					case PENDING:
					s = "Pending";
					break;
				case TRAVELING:
					s = v.getRoad().getId() + ":" +v.getLocation();
					break;
				case WAITING:
					s = "Waiting:"+v.getRoad().getDest().getId();
					break;
				case ARRIVED:
					s = "Arrived";
					break;
				}
				break;
			case 2:
				s = v.getItinerary().toString();
				break;
			case 3:
				s = "" + v.getContClass();
				break;
			case 4:
				s = "" + v.getMaxSpeed();
				break;
			case 5:
				s = "" + v.getSpeed();
				break;
			case 6:
				s = "" + v.getTotalCO2();
				break;
			case 7:
				s = "" + v.getDistance();
				break;
			default:
				assert(false);
		}	
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_vehicles = map.getVehicles();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
