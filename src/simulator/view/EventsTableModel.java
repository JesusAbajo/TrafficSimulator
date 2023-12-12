package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	// ...
	private static String[] _cols =  { "Time", "Desc." };
		
	private List<Event> _events;
	
	EventsTableModel(Controller ctrl) {
		_events = new ArrayList<>();
		ctrl.addObserver(this);
		
	}
	@Override
	public int getRowCount() {
		return _events.size();
	}
	@Override
	public int getColumnCount() {
		return _cols.length;
	}
	@Override
	public String getColumnName(int column) {
		return _cols[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String s = "";
		Event e = _events.get(rowIndex);
		switch ( columnIndex ) {
			case 0:
				s = "" + e.getTime();
				break;
			case 1:
				s = e.toString();
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
		_events = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_events = events;
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_events = events;
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_events = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
