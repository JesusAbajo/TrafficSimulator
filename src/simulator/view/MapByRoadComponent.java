package simulator.view;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{

	private static final int _JRADIUS = 10;
	private static final int _ICONSIZE = 32;
	private final Color _BG_COLOR = Color.WHITE;
	private final Color _ROAD_LABEL_COLOR = Color.BLACK;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private RoadMap _map;
	private Image _car;
	private Image[] _arrImg;
	
	
    MapByRoadComponent(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }
	
	private void initGUI() {
		_arrImg = new Image[6];
		for (int i = 0; i < _arrImg.length; i++) {
			_arrImg[i] = loadImage("cont_" + i + ".png");
		}
		_car = loadImage("car.png");
		setPreferredSize(new Dimension(300,200));
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {			
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics2D g) {
		int y = 50;
		int x1 = 50;
		int x2 = getWidth() - 100;		
		
		for (Road r : _map.getRoads()) {	
			
			drawRoads(g, y, x1, x2, r);
			drawJunctions(g, y, x1, x2, r);			
			drawVehicles(g, y, x1, x2, r);				       	        	        
	       	drawCont(g, y, x1, x2, r);	
	        drawWeather(g, y, x1, x2, r);	        
	        
			y += 50;// separacion de carreteras
		}
		
	}
    	
	private void drawWeather(Graphics2D g, int y, int x1, int x2, Road r) {
		g.setColor(_BG_COLOR);
        g.drawImage(r.getWeather().getImage(r), x2 + 9, y - _JRADIUS * 2, _ICONSIZE, _ICONSIZE, this);		
	}

	private void drawCont(Graphics2D g, int y, int x1, int x2, Road r) {
		int c = (int) Math.floor(Math.min((double)r.getTotalCO2() / (1.0 + (double)r.getContLimit() ), 1.0) / 0.19);
        Image co2Img;
        co2Img = _arrImg[c];
        g.setColor(_BG_COLOR);
        g.drawImage(co2Img, x2 + 50, y - _JRADIUS * 2, _ICONSIZE, _ICONSIZE, this);		
	}

	private void drawVehicles(Graphics2D g, int y, int x1, int x2, Road r) {
		for (Vehicle v : r.getVehicles()) {
            if (v.getStatus() != VehicleStatus.ARRIVED) {
                int x = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));             
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));

				g.drawImage(_car, x - _JRADIUS / 2, y - _JRADIUS, 16, 16, this);
				g.drawString(v.getId(), x - 4, y - _JRADIUS);
            }
        }		
	}

	private void drawJunctions(Graphics2D g, int y, int x1, int x2, Road r) {
		//SrcJunction
		g.setColor(Color.BLUE);
        g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

        g.setColor(_JUNCTION_LABEL_COLOR);
        g.drawString(r.getSrc().getId(), x1 - _JRADIUS / 2, y - _JRADIUS);
        
        //DestJunction
        if (r.getDest().getGreenLightIndex() != -1
                && r.equals(r.getDest().getInRoads().get(r.getDest().getGreenLightIndex())) )
            g.setColor(Color.GREEN);
        else
        	g.setColor(Color.RED);
        g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

        g.setColor(_JUNCTION_LABEL_COLOR);
        g.drawString(r.getDest().getId(), x2 - _JRADIUS / 2, y - _JRADIUS);		
	}

	private void drawRoads(Graphics2D g, int y, int x1, int x2, Road r) {
		g.setColor(_ROAD_LABEL_COLOR);
		g.drawString(r.getId(), x1 - 30, y + _JRADIUS / 4);
		g.drawLine(x1, y, x2, y);		
	}
    
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
	}

}
