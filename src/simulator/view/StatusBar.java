package simulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	private Controller _ctrl;
    private JLabel _timeLabel;
    private JLabel _msgLabel;

	StatusBar(Controller ctrl){
		_ctrl = ctrl;
		initGUI();
        _ctrl.addObserver(this);
	}
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		createLabels();
		addLabels();
		
	}
	
	private void createLabels() {
		_timeLabel = new JLabel("Time: ");
		_msgLabel = new JLabel("Event msg: ");
		
		_timeLabel.setPreferredSize(new Dimension(100, 25));
		_msgLabel.setPreferredSize(new Dimension(400, 25));
		
	}
	
	private void addLabels() {
		JSeparator sep =  new JSeparator(SwingConstants.VERTICAL);
		
		this.add(_timeLabel);
		this.add(sep);
		this.add(_msgLabel);		
	}
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText("Time: "+ (time));	
		//No funciona run si esta puesto 

	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText("Time: "+ (time));	 
		//No funciona run si esta puesto 
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		_timeLabel.setText("Time: "+ (time));	
		_msgLabel.setText("Event msg: " + (e.toString()));
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText("Time: "+ (time));

		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		_timeLabel.setText("Time: "+ (time));

		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
