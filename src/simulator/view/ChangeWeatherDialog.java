package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int estado = 0;
	private JComboBox<Road> rcomboBox;
	private DefaultComboBoxModel<Road> roadModel;

	private JSpinner _time;
	private DefaultComboBoxModel<Weather> weatherModel;
	private JComboBox<Weather> weathercomboBox;

	public ChangeWeatherDialog(Frame parent) {
		super(parent,true); // el true hace que no podamos usar la aplicacion hasta cerrar el dialogo
		this.initGUI();
	}

	private void initGUI() {
		
		this.setTitle("Change Weather");
		//MAIN
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel helpMsg = new JLabel("Change Weather");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(helpMsg);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//---------------------------------------------------------------------------
		//SPINNER & COMBOBOX
				
		JPanel dataPanel = new JPanel();
		dataPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(dataPanel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		//Road
		roadModel = new DefaultComboBoxModel<>();
		rcomboBox = new JComboBox<>(roadModel);	
		rcomboBox.setMaximumSize(new Dimension(80, 40));
		rcomboBox.setMinimumSize(new Dimension(80, 40));
		rcomboBox.setPreferredSize(new Dimension(80, 40));

		dataPanel.add(new JLabel("Road"), rcomboBox);
		dataPanel.add(rcomboBox);
		
		//Weather
		weatherModel = new DefaultComboBoxModel<>();
		weathercomboBox = new JComboBox<>(weatherModel);
		weathercomboBox.setMaximumSize(new Dimension(80, 40));
		weathercomboBox.setMinimumSize(new Dimension(80, 40));
		weathercomboBox.setPreferredSize(new Dimension(80, 40));
		
		dataPanel.add(new JLabel("Weather"), weathercomboBox);
		dataPanel.add(weathercomboBox);
		
		//time
		_time = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		_time.setToolTipText("Simulation tick to run: 1-10000");
		_time.setMaximumSize(new Dimension(80, 40));
		_time.setMinimumSize(new Dimension(80, 40));
		_time.setPreferredSize(new Dimension(80, 40));
		
		dataPanel.add(new JLabel("Ticks"), _time);
		dataPanel.add(_time);
		
		//---------------------------------------------------------------------------
		//BOTONES
		
		JPanel panelBotones = new JPanel(new FlowLayout());
		panelBotones.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(panelBotones);
		
		 JButton okButton = new JButton("OK");
		 okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e ) {
				if (rcomboBox.getSelectedItem() != null) {
					estado = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
			  
		  });
		 panelBotones.add(okButton);
		 
		 JButton cancelButton = new JButton("Cancel");
		 cancelButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
				   estado = 0;
				   ChangeWeatherDialog.this.setVisible(false);
					
				}
				  
			  });
		 panelBotones.add(cancelButton);
		 
	    setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(RoadMap roadMap) {
		for (Road r : roadMap.getRoads()) {
			roadModel.addElement(r);
		}
        for (Weather w : Weather.values()) {
            weatherModel.addElement(w);
        }
		this.setVisible(true);
		return estado;
	}
	
	public Integer getTicks() {
		return (Integer) _time.getValue();
	}
	
	public Road getRoad() {
		return (Road) rcomboBox.getSelectedItem();
	}
	
	public Weather getWeather() {
		return (Weather) weathercomboBox.getSelectedItem();
	}
	
	

}
