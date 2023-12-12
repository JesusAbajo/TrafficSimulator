package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	//Cambié como se crean los bordes metiendo el set border en el metodo
	private static final long serialVersionUID = 1L;
	private Border _defaultBorder = BorderFactory.createLineBorder(Color.black, 2);
	private Controller _ctrl;
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		_ctrl = ctrl;
		initGUI();
	}
	private void initGUI() {
			JPanel mainPanel = new JPanel(new BorderLayout());
			this.setContentPane(mainPanel);
			mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
			mainPanel.add(new StatusBar(_ctrl),BorderLayout.PAGE_END);
			JPanel viewsPanel = new JPanel(new GridLayout(1, 2));
			mainPanel.add(viewsPanel, BorderLayout.CENTER);
			JPanel tablesPanel = new JPanel();
			tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
			viewsPanel.add(tablesPanel);
			JPanel mapsPanel = new JPanel();
			mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
			viewsPanel.add(mapsPanel);
			
			// tables
			JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(_ctrl)), "Events");
			eventsView.setPreferredSize(new Dimension(500, 150));
			tablesPanel.add(eventsView);
			
			// TODO add other tables 
			//VEHICLES
			JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(_ctrl)), "Vehicles");
			vehiclesView.setPreferredSize(new Dimension(500, 150));
			tablesPanel.add(vehiclesView);
			
			//ROADS
			JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(_ctrl)), "Roads");
			roadsView.setPreferredSize(new Dimension(500, 150));
			tablesPanel.add(roadsView);
			
			//JUNCTIONS
			JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(_ctrl)), "Junctions");
			junctionsView.setPreferredSize(new Dimension(500, 150));
			tablesPanel.add(junctionsView);
					
			//maps
			JPanel mapView = createViewPanel(new MapComponent(_ctrl), "Map");
			mapView.setPreferredSize(new Dimension(500, 300));
			mapsPanel.add(mapView);
			
			//TODO add a map for MapByRoadComponent
	        JPanel mapByRoad = createViewPanel(new MapByRoadComponent(_ctrl), "Map By Road");
	        mapByRoad.setPreferredSize(new Dimension(500, 300));
	        mapsPanel.add(mapByRoad);
	        
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout() );
        p.setBorder(BorderFactory.createTitledBorder(_defaultBorder, title));
		p.add(new JScrollPane(c));
		return p;
		} 
	
}
