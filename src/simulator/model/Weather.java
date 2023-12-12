package simulator.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Weather {
	SUNNY, CLOUDY, RAINY, WINDY, STORM;

	//No se si se refería a esto al decir que 
	//podría hacerse con el "r.getWeather().getImage()"
	//que menciona en las diapositivas
	public Image getImage(Road r) {
		Image weatherImg;
        switch (r.getWeather()) {
            case STORM:
                weatherImg = loadImage("storm.png");
                break;
            case SUNNY:
                weatherImg = loadImage("sun.png");
                break;
            case RAINY:
                weatherImg = loadImage("rain.png");
                break;
            case WINDY:
                weatherImg = loadImage("wind.png");
                break;
            case CLOUDY:
                weatherImg = loadImage("cloud.png");
                break;
            default:
                weatherImg = null;
        }
        return weatherImg;	
	}

	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
}