import java.util.ArrayList;
import java.util.Random;

import processing.core.*;
import processing.event.KeyEvent;

public class SubwaySurfers {
	private Player ph;

	private Environment g;

	//private ArrayList<Train> trains;

	private ArrayList<Obstacle> obstacles;
	
	
	/*
	 * Create new game with player at given x, y and train on the left track
	 */
	public SubwaySurfers() {
		this.ph = new Player();
		//this.trains = new ArrayList<Train>();
		this.obstacles = new ArrayList<Obstacle>();
		this.g = new Environment();	
	}

	/*
	 * Create new object with given player and train list
	 */
	public SubwaySurfers(Player ph, /*ArrayList<Train> t,*/ Environment g, ArrayList<Obstacle> o) {
		this.ph = ph;
		//this.trains = t;
		this.g = g;
		this.obstacles = o;
	}

	/**
	 * Renders a picture of the player and obstacles on the window
	 */
	public PApplet draw(PApplet c) {
		// colors the canvas background
		c.background(45, 160, 230);
		//c.lights();  // this is where lights functions go, needs tweaking to work. look at documentation
		Spawner.getAllTrains().forEach(train -> train.draw(c));
		obstacles.forEach(obstacle -> obstacle.draw(c));
		// positions the camera at (x1,y1,z1) looking toward (x2,y2,z2)
		// SSConstants.HEIGHT/2 + (SSConstants.HEIGHT/2 - p.pos.y)/2
		c.camera(ph.getPos().getX(), SSConstants.HEIGHT / 2 - (SSConstants.floorLvl - ph.getBounds().getbBound()), SSConstants.CAMERA_Z,
				ph.getPos().getX(), SSConstants.HEIGHT - (SSConstants.floorLvl - ph.getBounds().getbBound()), 0, 0, 1, 0);
		g.draw(c);
		ph.draw(c);
		return c;
	}

	/**
	 * Produces an updated world where the player and obstacles move if needed
	 */
	public SubwaySurfers update() {
		ph.update();
		
		if ( checkCollision() ) {
			System.out.println("bruh!!!");
			gameOver();
		}

		Spawner.updateTrains(); // updates all trains

		obstacles.removeIf(obstacle -> obstacle.offScreen); // removes trains that are off the screen
		obstacles.forEach(obstacle -> obstacle.update());

		

		return new SubwaySurfers(ph, /*trains,*/ g, obstacles);
	}

	/**
	 * Handles KeyEvents for the SubwaySurfers game
	 * 
	 * @param kev - the KeyEvent to be processed
	 * @return the updated game
	 */
	public SubwaySurfers keyPressed(KeyEvent kev) {
		ph.move(kev);

		if (kev.getKey() == '1') {
			Spawner.addTrain(1);
		} else if (kev.getKey() == '2') {
			Spawner.addTrain(2);
		} else if (kev.getKey() == '3') {
			Spawner.addTrain(3);
		} else if (kev.getKey() == '4') {
			obstacles.add(new Obstacle(1));
		} else if (kev.getKey() == '5') {
			obstacles.add(new Obstacle(2));
		} else if (kev.getKey() == '6') {
			obstacles.add(new Obstacle(3));
		}

		return new SubwaySurfers(ph, /*trains,*/ g, obstacles);
	}

	boolean checkCollision() {
	   for (int t = 0; t < Spawner.getAllTrains().size(); t++) {
		   Train tr = Spawner.getAllTrains().get(t);
		   
		   return tr.handleCollision(ph);
	   }
	   
	   for (int o = 0; 0 < obstacles.size(); o++) {
		   Obstacle ob = obstacles.get(o);
		      
		   return ob.handleCollision(ph);
	   }
	   
	   return false;
   }
	
	void gameOver() {
		SSConstants.gameSpd = 0;
	}
}
