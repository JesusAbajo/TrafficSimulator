package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;
	private static String[] _cols =  { "Id", "Green", "Queues"};
	
	private List<Junction> _junctions;
	
	JunctionsTableModel(Controller ctrl) {
		_junctions = new ArrayList<>();
		ctrl.addObserver(this);
		
	}
	@Override
	public int getColumnCount() {
		return _cols.length;
	}

	@Override
	public int getRowCount() {
		return _junctions.size();
	}

	public String getColumnName(int column) {
		return _cols[column];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		Junction j = _junctions.get(rowIndex);
		switch ( columnIndex ) {
			case 0:
				s = j.getId();
				break;
			case 1:
				s = j.getGreenLightIndex() == -1 ? //
						"NONE" : //
						j.getInRoads().get(j.getGreenLightIndex()).getId();
				break;
			case 2:
				for (Road r : j.getInRoads()) {
					s = s + " " + r.getId() + ":" + j.getQueue(r);
				}
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
		_junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_junctions = map.getJunctions();
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
