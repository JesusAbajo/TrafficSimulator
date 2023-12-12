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

import simulator.model.RoadMap;
import simulator.model.Vehicle;

public class ChangeCO2Dialog extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int estado = 0;
	private JComboBox<Vehicle> vcomboBox;
	private DefaultComboBoxModel<Vehicle> vehicleModel;

	private JSpinner _time;
	private DefaultComboBoxModel<Integer> contClassModel;
	private JComboBox<Integer> contcomboBox;

	public ChangeCO2Dialog(Frame parent) {
		super(parent,true); // el true hace que no podamos usar la aplicacion hasta cerrar el dialogo
		this.initGUI();
	}

	private void initGUI() {
		
		this.setTitle("Change Contamination Class");
		//MAIN
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel helpMsg = new JLabel("Change Contamination Class");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(helpMsg);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		//---------------------------------------------------------------------------
		//SPINNER & COMBOBOX
		
		JPanel dataPanel = new JPanel();
		dataPanel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(dataPanel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		vehicleModel = new DefaultComboBoxModel<>();
		vcomboBox = new JComboBox<>(vehicleModel);	
		vcomboBox.setMaximumSize(new Dimension(80, 40));
		vcomboBox.setMinimumSize(new Dimension(80, 40));
		vcomboBox.setPreferredSize(new Dimension(80, 40));

		dataPanel.add(new JLabel("Vehicles"), vcomboBox);
		dataPanel.add(vcomboBox);
				
				
		
		contClassModel = new DefaultComboBoxModel<>();
		contcomboBox = new JComboBox<>(contClassModel);
		contcomboBox.setMaximumSize(new Dimension(80, 40));
		contcomboBox.setMinimumSize(new Dimension(80, 40));
		contcomboBox.setPreferredSize(new Dimension(80, 40));
		
		dataPanel.add(new JLabel("Cont Class"), contcomboBox);
		dataPanel.add(contcomboBox);
		
			
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
				if (vcomboBox.getSelectedItem() != null) {
					estado = 1;
					ChangeCO2Dialog.this.setVisible(false);
				}
			}
			  
		  });
		 panelBotones.add(okButton);
		 
		 JButton cancelButton = new JButton("Cancel");
		 cancelButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
				   estado = 0;
				   ChangeCO2Dialog.this.setVisible(false);
					
				}
				  
			  });
		 panelBotones.add(cancelButton);

	     
	     
	    setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	     
	}
	
	
	public int open(RoadMap roadMap) {
		for (Vehicle v : roadMap.getVehicles()) {
			vehicleModel.addElement(v);
		}
		for (int i = 0 ; i < 10 ; ++i) {
			contClassModel.addElement(i);
		}
		this.setVisible(true);
		return estado;
	}
	
	public Integer getTicks() {
		return (Integer) _time.getValue();
	}
	
	public Vehicle getVehicle() {
		return (Vehicle) vcomboBox.getSelectedItem();
	}
	
	public Integer getCO2Class() {
		return (Integer) contcomboBox.getSelectedItem();
	}
	
	

}
