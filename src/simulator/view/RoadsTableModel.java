package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;
	private static String[] _cols =  { "Id", "Lenth", "Weather","Max.Speed","Speed Limit","Total CO2", "CO2 Limit"};
	
	private List<Road> _roads;
	
	RoadsTableModel(Controller ctrl) {
		_roads = new ArrayList<>();
		ctrl.addObserver(this);
		
	}
	@Override
	public int getColumnCount() {
		return _cols.length;
	}

	@Override
	public int getRowCount() {
		return _roads.size();
	}

	public String getColumnName(int column) {
		return _cols[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		Road r = _roads.get(rowIndex);
		switch ( columnIndex ) {
			case 0:
				s = r.getId();
				break;
			case 1:
				s = "" + r.getLength();
				break;
			case 2:
				s = r.getWeather().toString();
				break;
			case 3:
				s = "" + r.getMaxSpeed();
				break;
			case 4:
				s = "" + r.getSpeedLimit();
				break;
			case 5:
				s = "" + r.getTotalCO2();
				break;
			case 6:
				s = "" + r.getContLimit();
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
		_roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
