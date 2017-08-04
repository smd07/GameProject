package oiraga.logic;

import java.awt.Color;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import oiraga.model.Chip;
import oiraga.model.Enemy;
import oiraga.model.GameObject;
import oiraga.model.Mine;
import oiraga.model.Player;
import oiraga.view.GameFrame;
import oiraga.view.GamePanel;







/**
 * The logic parts of the game
 * @author samed
 * @version 1.0 
 * 
 *
 */

public class GameLogic {

	private Player player;
	private Random random;
	private Enemy enemy;
	private ArrayList<GameObject> gameObjects;
	private boolean isGameRunning;
	private GameFrame gameFrame;
	private GamePanel gamePanel;
	private int xTarget;
	private int yTarget;
	private int bestx = 1000;
	private int besty;
	private int targetx;
	private int targety;
	private int xx;
	private int yy;

	ArrayList<GameObject> objectsToRemove;

	
	/**
	 * 
	 * @param name  name of the player
	 * @param color color of the player
	 * @param n  number of enemies
	 * 
	 * 
	 */
	public GameLogic(String name, Color color, int n) {
		player = new Player(10, 10, 20, 2, color, name);

		gameObjects = new ArrayList<GameObject>();
		gameObjects.add(player);
		gameFrame = new GameFrame();
		gamePanel = new GamePanel(gameObjects);
		random = new Random();
		addMouseEvents();
		objectsToRemove = new ArrayList<GameObject>();

		fillEnemy(n);
		fillChips(30);
		fillMines(10);
	}

	
	
	/**
	 * Checking the collisions
	 * 
	 * 
	 */
	
	
	private synchronized void checkCollisions() {

		for (GameObject gameObject : gameObjects) {
			if (player.getRectangle().intersects(gameObject.getRectangle())) {
				if (gameObject instanceof Mine) {
					player.setRadius(player.getRadius() - 5);
					// gameObjects.remove(gameObject);
					objectsToRemove.add(gameObject);

				} else if (gameObject instanceof Chip) {
					player.setRadius(player.getRadius() + 5);
					// gameObjects.remove(gameObject);
					objectsToRemove.add(gameObject);

				} else if (gameObject instanceof Enemy) {
					if (gameObject.getRadius() < player.getRadius()) {
						player.setRadius(player.getRadius() + 5);
						objectsToRemove.add(gameObject);
					} else {
						player.setSpeed(0);
						isGameRunning = false;
					}
				}

				for (GameObject gameObject2 : gameObjects) {
					for (GameObject gameObject1 : gameObjects) {
						if (gameObject1 instanceof Enemy && gameObject2 instanceof Chip) {
							if (gameObject1.getRectangle().intersects(gameObject2.getRectangle())) {
								gameObject1.setRadius(gameObject1.getRadius() + 5);
								objectsToRemove.add(gameObject2);
							}

						}
						if (gameObject1 instanceof Enemy && gameObject2 instanceof Mine) {
							if (gameObject1.getRectangle().intersects(gameObject2.getRectangle())) {
								gameObject1.setRadius(gameObject1.getRadius() - 5);
								objectsToRemove.add(gameObject2);
							}

						}

						if (gameObject1 instanceof Enemy && gameObject2 instanceof Enemy) {
							if (gameObject1 != gameObject2) {

								if (gameObject1.getRectangle().intersects(gameObject2.getRectangle())) {
									if (gameObject1.getRadius() < gameObject2.getRadius()) {

										gameObject2.setRadius(gameObject2.getRadius() + 5);
										objectsToRemove.add(gameObject1);
									}
								}

							}

						}

					}
				}

			}

		}

		gameObjects.removeAll(objectsToRemove);

	}
	
	/**
	 * Adding new objects instead of removed ones
	 * 
	 */

	private synchronized void addNewObjects() {

		for (GameObject gameObject : objectsToRemove) {

			if (gameObject instanceof Chip) {
				fillChips(1);
			}
			if (gameObject instanceof Mine) {
				fillMines(1);
			}
			if (gameObject instanceof Enemy) {
				fillEnemy(1);
			}

		}

		objectsToRemove.clear();

	}
	
	
	/**
	 * Moving the player with tracking mouse move
	 *  xTarget is mouse's X coordinate
	 * yTarget is mouse's Y coordinate
	 */

	private synchronized void movePlayer() {
		if (xTarget > player.getX()) {
			player.setX(player.getX() + player.getSpeed());
		} else if (xTarget < player.getX()) {
			player.setX(player.getX() - player.getSpeed());
		}

		if (yTarget > player.getY()) {
			player.setY(player.getY() + player.getSpeed());
		} else if (yTarget < player.getY()) {
			player.setY(player.getY() - player.getSpeed());
		}
	}

	/**
	 * 
	 * Making random colors for the chips and enemies
	 * @return a color
	 */
	
	public Color renk() {

		switch (random.nextInt(5) + 1) {
		case 1:
			return Color.orange;
		case 2:
			return Color.pink;
		case 3:
			return Color.red;
		case 4:
			return Color.YELLOW;
		case 5:
			return Color.green;
		default:
			return Color.BLUE;
		}

	}

	private void fillMines(int n) {
		for (int i = 0; i < n; i++) {

			gameObjects.add(new Mine(random.nextInt(1920), random.nextInt(1080), 20, Color.BLACK));

		}

	}

	private void fillChips(int n) {
		for (int i = 0; i < n; i++) {

			gameObjects.add(new Chip(random.nextInt(1920), random.nextInt(1080), 10, renk()));

		}

	}
	
	
	
/**
 * 
 * Something like pathfinding but it's not working correctly (yet)
 * @return
 */
	private boolean enemyMineContoller() {
		

		for (GameObject gameObject : gameObjects) {
			for (GameObject gameObject1 : gameObjects) {

				if (gameObject instanceof Enemy && gameObject1 instanceof Mine) {

					gameObject.setX(gameObject.getX() + 10);
					gameObject.setY(gameObject.getY() + 10);
					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() - 12);
						gameObject.setY(gameObject.getY() - 11);
						return false;

					}

					gameObject.setX(gameObject.getX() - 10);
					gameObject.setY(gameObject.getY() - 10);

					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() + 12);
						gameObject.setY(gameObject.getY() + 11);

						return false;
					}

					gameObject.setX(gameObject.getX() - 10);
					gameObject.setY(gameObject.getY() + 10);
					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() + 12);
						gameObject.setY(gameObject.getY() - 11);
						return false;
					}

					gameObject.setX(gameObject.getX() + 10);
					gameObject.setY(gameObject.getY() - 10);
					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() - 12);
						gameObject.setY(gameObject.getY() + 11);
						return false;
					}

					gameObject.setX(gameObject.getX() + 10);

					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() - 12);

						return false;
					}

					gameObject.setY(gameObject.getY() + 10);
					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setY(gameObject.getY() - 11);
						return false;
					}

					gameObject.setY(gameObject.getY() - 10);
					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setY(gameObject.getY() + 11);
						return false;
					}
					gameObject.setX(gameObject.getX() - 10);

					if (gameObject.getRectangle().intersects(gameObject1.getRectangle())) {

						gameObject.setX(gameObject.getX() + 12);

						return false;
					}

				}
			}
		}
		return true;
	}

	
	
	
	/**
	 * 
	 * Moving the enemy,if it is smaller than player it will try to eats chips and gets bigger,otherwise it will comes for player
	 * 
	 * 
	 * @param enemy
	 */
	private void moveEnemy(Enemy enemy) {

		if (enemyMineContoller()) {

			if (enemy.getRadius() > player.getRadius()) {
				if (player.getX() > enemy.getX()) {
					enemy.setX(enemy.getX() + enemy.getSpeed());
				} else if (player.getX() < enemy.getX()) {
					enemy.setX(enemy.getX() - enemy.getSpeed());
				}

				if (player.getY() > enemy.getY()) {
					enemy.setY(enemy.getY() + enemy.getSpeed());
				} else if (player.getY() < enemy.getY()) {
					enemy.setY(enemy.getY() - enemy.getSpeed());
				}

			} else {

				for (GameObject gameObject : gameObjects) {

					if (gameObject instanceof Chip) {

						if (enemy.getX() < gameObject.getX()) {

							xx = gameObject.getX() - enemy.getX();

						} else {

							xx = enemy.getX() - gameObject.getX();
						}

						if (enemy.getY() < gameObject.getY()) {
							yy = gameObject.getY() - enemy.getY();
						} else {
							yy = enemy.getY() - gameObject.getY();
						}
						if (bestx > xx + yy) {
							bestx = xx + yy;
							targetx = gameObject.getX();
							targety = gameObject.getY();
						}

					}

				}

				if (targetx > enemy.getX()) {
					enemy.setX(enemy.getX() + enemy.getSpeed());
				} else if (targetx < enemy.getX()) {
					enemy.setX(enemy.getX() - enemy.getSpeed());
				}

				if (targety > enemy.getY()) {
					enemy.setY(enemy.getY() + enemy.getSpeed());
				} else if (targety < enemy.getY()) {
					enemy.setY(enemy.getY() - enemy.getSpeed());
				}
				bestx = 1000;
			}

		}

	}

	private void fillEnemy(int n) {

		for (int i = 0; i < n; i++) {

			gameObjects.add(new Enemy(random.nextInt(1920), random.nextInt(1080), random.nextInt(50 + 25), 1, renk()));

		}

	}

	
	private void startGame() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isGameRunning) {
					movePlayer();
					checkCollisions();
					addNewObjects();
					for (GameObject gameObject : gameObjects) {
						if (gameObject instanceof Enemy) {
							moveEnemy((Enemy) gameObject);
						}
					}
					enemyMineContoller();
					gamePanel.repaint();

					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();

	}

	public void startApplication() {

		gameFrame.setContentPane(gamePanel);
		gameFrame.setVisible(true);
		isGameRunning = true;
		startGame();

	}

	public void addMouseEvents() {

		gameFrame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				xTarget = player.getX();
				yTarget = player.getY();
				for (GameObject gameObject : gameObjects) {
					if (gameObject instanceof Enemy) {
						((Enemy) gameObject).setSpeed(0);
					}
				}

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				for (GameObject gameObject : gameObjects) {
					if (gameObject instanceof Enemy) {
						((Enemy) gameObject).setSpeed(1);
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		
		gameFrame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

				xTarget = e.getX() - (player.getRadius() / 2);
				yTarget = e.getY() - (player.getRadius() / 2);

			}

			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});

	}

}
