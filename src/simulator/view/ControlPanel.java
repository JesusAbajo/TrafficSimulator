package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver {
		
		Controller _ctrl;
		RoadMap _roadmap;
		int _time;
		private boolean _stopped;
		private JFileChooser fileChooser;
		JToolBar toolbar;
		JButton eventButton, changeContClassButton, changeWeatherButton, runButton, pauseButton, exitButton;
		private JSpinner _ticksSpinner;		
		
	ControlPanel(Controller ctrl){
		_ctrl = ctrl;
		_roadmap = ctrl.getRoadMap();
		initGUI();
		_ctrl.addObserver(this);
		
	}
	
	private void initGUI() {
		toolbar = new JToolBar();
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.PAGE_START);
		
		// EVENT BUTTON
		fileChooser= new JFileChooser();
		fileChooser.setDialogTitle("Elige un archivo a cargar");
		eventButton = new JButton();
		eventButton.setToolTipText("Carga el archivo a la aplicación");
		eventButton.setIcon(new ImageIcon("./resources/icons/open.png"));
		eventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				loadFile();
			}
			
		});
		toolbar.add(eventButton);
		toolbar.addSeparator();
		// FIN EVENT BUTTON
		
		// CONTAMINATION BUTTON
		changeContClassButton = new JButton();
		changeContClassButton.setToolTipText("Cambia contaminacion vehiculo");
		changeContClassButton.setIcon(new ImageIcon("./resources/icons/co2class.png"));
		changeContClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				ChangeCO2Class();
			}
			
		});
		toolbar.add(changeContClassButton);
		toolbar.addSeparator();
		// FIN CONTAMINATION BUTTON
		
		// WEATHER BUTTON
		changeWeatherButton = new JButton();
		changeWeatherButton.setToolTipText("Cambia el tiempo de una carretera");
		changeWeatherButton.setIcon(new ImageIcon("./resources/icons/weather.png"));
		changeWeatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				ChangeWeather();
			}
			
		});
		toolbar.add(changeWeatherButton);
		toolbar.addSeparator();
		// FIN WEATHER BUTTON
		
		// Run button

		runButton=new JButton();
		runButton.setToolTipText("Enciende el simulador");
		runButton.setIcon(new ImageIcon("./resources/icons/run.png"));
		runButton.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					start();	
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null,"Something went wrong: "+ex.getMessage() ,"ERROR",JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		toolbar.add(runButton);
		
		
		
		//Stop button
		pauseButton=new JButton();
		pauseButton.setToolTipText("Para el simulador");
		pauseButton.setIcon(new ImageIcon("./resources/icons/stop.png"));
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {			
				stop_sim();
			}
			
		});
		
		toolbar.add(pauseButton);
		
		JLabel ticksLabel=new JLabel("Ticks: ");
		toolbar.add(ticksLabel);
		_ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		_ticksSpinner.setToolTipText("Simulation tick to run: 1-10000");
		_ticksSpinner.setMaximumSize(new Dimension(80, 40));
		_ticksSpinner.setMinimumSize(new Dimension(80, 40));
		_ticksSpinner.setPreferredSize(new Dimension(80, 40));
		
		toolbar.add(_ticksSpinner);
		
		//Exit button
		toolbar.addSeparator();
		toolbar.add(Box.createGlue());
		exitButton=new JButton();
		exitButton.setAlignmentX(RIGHT_ALIGNMENT);
		exitButton.setToolTipText("Salir del simulador");
		exitButton.setIcon(new ImageIcon("./resources/icons/exit.png"));
		
		exitButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) { 
				 exitButton();
			}
		});
		toolbar.add(exitButton);
		
		
	}
	
	private void loadFile() { // EVEN BUTTON
		// TODO Auto-generated method stub
		int returnVal = fileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(file);
				_ctrl.reset();
				_ctrl.loadEvents(inputStream);
					
					
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,"Something went wrong: "+e1.getMessage() ,"ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void start() {
		
		enableToolBar(false);
		
		_stopped=false;
		
		run_sim((Integer)_ticksSpinner.getValue());
		
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
		// TODO show error message and _stopped = true
		}
		SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			run_sim(n - 1);
			}
			});
			} else {
				enableToolBar(true);
				_stopped = true;
		}
	}
	
	private void stop_sim() {
		_stopped = true;
	}
	
	// TODO Auto-generated method stub
	private void enableToolBar(boolean bool) {
		eventButton.setEnabled(bool);
		changeContClassButton.setEnabled(bool); 
		changeWeatherButton.setEnabled(bool);
		runButton.setEnabled(bool);
		exitButton.setEnabled(bool);
	}
	
	private void exitButton() {
		
		int select=JOptionPane.showOptionDialog(new JFrame(), "¿Seguro de que desea salir?", "Confirmar salida",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		
		if(select==JOptionPane.YES_OPTION) System.exit(0);
				
	}
	
	private void ChangeWeather() {
		ChangeWeatherDialog weatherDialog = new ChangeWeatherDialog(null);
		List<Pair<String, Weather>> w_cs = new ArrayList<Pair<String, Weather>>();
		
		if (weatherDialog.open(_roadmap) == 1) {
			 
			 Pair<String, Weather> pareja =
					 new Pair<String, Weather>(weatherDialog.getRoad().getId(),
							 weatherDialog.getWeather());
			 
			 w_cs.add(pareja);
			 try {
			_ctrl.addEvent(new SetWeatherEvent(_time + weatherDialog.getTicks(), w_cs));
			 }catch (Exception ex){
				 
			 } 
		}
		
	}
	
	private void ChangeCO2Class() {
		// TODO Auto-generated method stub
		ChangeCO2Dialog co2Dialog = new ChangeCO2Dialog(null);
		List<Pair<String, Integer>> c_cs = new ArrayList<Pair<String, Integer>>();
		
		if (co2Dialog.open(_roadmap) == 1) {
			 
			 Pair<String, Integer> pareja =
					 new Pair<String, Integer>(co2Dialog.getVehicle().getId(),
							 co2Dialog.getCO2Class());
			 
			 c_cs.add(pareja);
			 try {
			_ctrl.addEvent(new SetContClassEvent(_time + co2Dialog.getTicks(), c_cs));
			 }catch (Exception ex){
				 
			 }
		}
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		_time = time;
		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
        _roadmap = map;
        _time = time;
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
        _roadmap = map;
        _time = time;
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
        _roadmap = map;
        _time = time;		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
