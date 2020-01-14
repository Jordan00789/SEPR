package io.github.jordan00789.sepr;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Firetruck extends Entity implements Attack, Moveable {

	private int water;
	private int maxWater;
	private float acceleration = 2;
	private float deceleration = 0.5f;
	private float direction = 0;
	private float velocity = 0;
	private Pixmap map = new Pixmap(Gdx.files.internal("map.png"));
	private Pixmap speedMap;
	public ArrayList<Projectile> drops = new ArrayList<Projectile>();
	private static float range = 2f;
	private static float flowRate = 40f;

	/**
	 * Creates a Firetruck sprite using the texture provided, with the specified
	 * amounts of health and water.
	 * 
	 * @param health   The amount of health the truck has
	 * @param maxWater The maximum amount of water in the truck
	 * @param texture  The texture given to the Firetruck sprite
	 */
	public Firetruck(int health, int maxWater, Texture texture) {
		super(health, texture);
		this.maxWater = water = maxWater;
		speedMap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), map.getFormat());
		speedMap.drawPixmap(map, 0, 0, map.getWidth(), map.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
	}

	/** @return The truck's current amount of water. */
	public int getWater() {
		return water;
	}

	/**
	 * Sets the amount of water in the truck if the amount is valid.
	 * 
	 * @param amount The amount that the water variable is set to
	 */
	private void setWater(int amount) {
		if (amount <= maxWater && amount > 0) {
			water = amount;
		}
	}

	/**
	 * Removes the specified amount of water from the truck.
	 * 
	 * @param amount The amount of water removed from the truck
	 */
	public void takeWater(int amount) {
		if (amount <= water && amount > 0) {
			water -= amount;
		}
	}

	/**
	 * Refills the truck to the maximum amount of water.
	 */
	public void refill() {
		setWater(maxWater);
	}

	/**
	 * Sets the trucks direction.
	 * 
	 * @param direction The value in degrees the truck's direction is set to
	 */
	private void setDirection(float direction) {
		this.direction = direction;
	}

	/**
	 * Returns the truck's current direction.
	 * 
	 * @return The truck's current direction
	 */
	public float getDirection() {
		return direction;
	}

	/**
	 * Sets the truck's velocity.
	 * 
	 * @param velocity The value the velocity is set to
	 */
	private void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns the truck's current velocity.
	 * 
	 * @return The truck's current velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	/** 
	 * Turns the truck left.
	 */
	@Override
	public void turnLeft() {
		if (getDirection() <= 0) {
			setDirection(360);
		} else {
			setDirection(getDirection() - getTurnSpeed());
		}
	}

	/**
	 * Turns the truck right.
	 */
	@Override
	public void turnRight() {
		if (getDirection() >= 360) {
			setDirection(0);
		} else {
			setDirection(getDirection() + getTurnSpeed());
		}
	}

	/**
	 * Calculates the current turning radius of the truck.
	 * 
	 * @return Turning speed
	 */
	private float getTurnSpeed() {
		if (velocity > 50f) {
			return (400f / velocity);
		} else {
			return 8f;
		}
	}

	/**
	 * Moves the truck forward.
	 */
	@Override
	public void goForward() {
		if (velocity < speedLimit()) {
			setVelocity(getVelocity() + acceleration);
		}
	}

	/**
	 * Moves the truck backward.
	 */
	@Override
	public void goBackward() {
		if (velocity > -speedLimit()) {
			setVelocity(getVelocity() - acceleration);
		}
	}

	/**
	 * Updates the position and rotation of the truck and it's corresponding water
	 * droplets.
	 * 
	 * @param delta The current delta time
	 */
	@Override
	public void update(float delta) {
		if (velocity > 0.01f) {
			velocity -= deceleration;
		} else if (velocity < -0.01f) {
			velocity += deceleration;
		} else {
			velocity = 0;
		}
		setRotation(-direction);
		float maxSpeed = speedLimit();
		//Checks the truck isn't breaking the speed limit.
		if (velocity > maxSpeed || velocity < -maxSpeed) {
			velocity = maxSpeed;
		}
		drops.removeIf(drop -> drop.isDisposable());
		drops.forEach(drop -> drop.update(delta));

		setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
				(float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));
	}

	/**
	 * Overrides the Sprite draw method so water droplets can be drawn too
	 * 
	 * @param batch The sprite batch to draw in
	 */
	@Override
	public void draw(Batch batch) {
		// drop is short for droplet
		drops.forEach(drop -> drop.draw(batch));
		super.draw(batch);
	}

	/**
	 * Calculates the truck's maximum speed and returns it.
	 * 
	 * @return Speed limit
	 * 
	 */
	private float speedLimit() {
		float c = (float) Math.PI / 180;
		int pixcolour;
		// Checks either front or back of the truck sprite depending on whether the
		// truck is moving forwards or backwards
		if (velocity > 0) {
			pixcolour = speedMap.getPixel(Math.round(getX() + 256 + ((float) Math.sin(direction * c) * 9)),
					Gdx.graphics.getHeight() - Math.round(getY() + 256 + ((float) Math.cos(direction * c) * 9)));
		} else {
			pixcolour = speedMap.getPixel(Math.round(getX() + 256 - ((float) Math.sin(direction * c) * 9)),
					Gdx.graphics.getHeight() - Math.round(getY() + 256 - ((float) Math.cos(direction * c) * 9)));
		}
		// Convert 32-bit RGBA8888 integer to 3-bit hex code, using a mask
		String col = "#" + Integer.toHexString(pixcolour & 15790320);
		if (col.length() > 2) {

			col = col.substring(0, 7);
		}
		switch (col) {
		case ("#c070f0"):// buildings
			return 100f;
		case ("#d070f0"):// buildings 2
			return 100f;
		case ("#f0f0f0"):// road
			return 100f;
		case ("#f0c0f0"):// grass
			return 30f;
		case ("#6040f0"):// walls
			setVelocity(0f);
			return 0f;
		case ("#6050f0"):// walls 2
			setVelocity(0f);
			return 0f;
		case ("#e0f0f0"):// water
			setVelocity(0f);
			return 0f;
		case ("#c0f0f0"):// water 2
			setVelocity(0f);
			return 0f;
		case ("#0"):// off of map
			setVelocity(0f);
			return 0f;
		default:
			// System.err.println("Unknown colour");
			return 100f;
		}
	}

	/**
	 * Creates a new water droplet and launches it from the truck's position
	 */
	@Override
	public void attack() {
		if (drops.size() < 50) {
			takeWater(1);
			Projectile drop = new Projectile(getX() + getOriginX() / 2, getY() + getOriginY() / 2, getDirection(),
					flowRate + velocity, range, new Texture("drop.png"));
			drops.add(drop);
		}
	}
}