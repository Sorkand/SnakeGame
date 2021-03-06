/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package godemperorsdune;

import audio.AudioPlayer;
import environment.Environment;
import environment.GraphicsPalette;
import environment.LocationValidatorIntf;
import grid.Grid;
import images.ResourceTools;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author sofyashipova
 */
class DuneEnvironment extends Environment implements GridDrawData, LocationValidatorIntf {

    Grid grid;
    private Snake snake;
    private Score score;

    public final int SLOW_SPEED = 10;
    public final int MEDIUM_SPEED = 7;
    public final int HIGH_SPEED = 3;

    private int moveDelayLimit = MEDIUM_SPEED;
    private int moveDelayCounter = 0;
    public Point randomize;

    private ArrayList<GridObject> gridObjects;

    private GameState gameState = GameState.PLAYING;

    public DuneEnvironment() {
    }

    @Override
    public void initializeEnvironment() {

        this.setBackground(ResourceTools.loadImageFromResource("resources/sand_tile.jpg"));
        Color backgroundie = new Color(255, 255, 255, 20);
        grid = new Grid(30, 22, 22, 22, new Point(140, 50), backgroundie);

        snake = new TailSnake();
        snake.setDirection(Direction.DOWN);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 2));
        body.add(new Point(2, 3));
        body.add(new Point(3, 3));
        body.add(new Point(4, 3));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            gridObjects.add(new GridObject(GridObjectType.APPLE, getRandomPoint()));
        }
        for (int i = 0; i < 15; i++) {
            gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, getRandomPoint()));

        }

        score = new Score();
        score.setPosition(new Point(20, 20));

    }

    public Point getRandomPoint() {
        return new Point((int) (grid.getRows() * Math.random()), (int) (grid.getRows() * Math.random()));
    }

    @Override
    public void timerTaskHandler() {
        if (snake != null) {
            if (moveDelayCounter >= moveDelayLimit) {
                moveDelayCounter = 0;
                snake.move();
            } else {
                moveDelayCounter++;
            }
        }

    }

    @Override
    public void keyPressedHandler(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            grid.setShowCellCoordinates(!grid.getShowCellCoordinates());
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            snake.setDirection(Direction.UP);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            snake.setDirection(Direction.LEFT);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            snake.setDirection(Direction.RIGHT);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            snake.setDirection(Direction.DOWN);
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            snake.togglePaused();
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            snake.grow(2);
        } else if (e.getKeyCode() == KeyEvent.VK_M) {
            AudioPlayer.play("/resources/Music.wav");
        }

    }

    @Override
    public void keyReleasedHandler(KeyEvent e) {

    }

    @Override
    public void environmentMouseClicked(MouseEvent e) {

    }

    @Override
    public void paintEnvironment(Graphics graphics) {
        switch (gameState) {
            case START:

                break;

            case PLAYING:
                if (grid != null) {
                    grid.paintComponent(graphics);
                }

                if (snake != null) {
                    snake.draw(graphics);
                }

                if (gridObjects != null) {
                    for (GridObject gridObject : gridObjects) {
                        if (gridObject.getType() == GridObjectType.POISON_BOTTLE) {
                            GraphicsPalette.drawPoisonBottle(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.GREEN);

                        } else if (gridObject.getType() == GridObjectType.APPLE) {
                            GraphicsPalette.drawApple(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.RED);

                        }
                    }
                }

                if (score != null) {
                    score.draw(graphics);
                }

                break;

            case OVER:
                this.setBackground(ResourceTools.loadImageFromResource("resources/GameOver.png"));
                snake.togglePaused();


                break;

        }

        if (score.getValue() < 0 || snake == null) {
            gameState = GameState.OVER;
        }

    }

//<editor-fold defaultstate="collapsed" desc="LocationValidatorIntf">
    @Override
    public Point validateLocation(Point point) {
        if (snake.selfHit()) {
            snake.grow(-3);
            moveDelayLimit = SLOW_SPEED;
        }

        if (point.x >= this.grid.getColumns()) {
            point.x = 0;
        } else if (point.x < 0) {
            point.x = this.grid.getColumns() - 1;
        } else if (point.y < 0) {
            point.y = this.grid.getRows() - 1;
        } else if (point.y >= this.grid.getRows()) {
            point.y = 0;
        }

        //check if the snake hits a GridObjectm then take approproate action:
        // - Apple - grow snake by 3
        // - Poison - make sound, kill snake
        //
        //Look at all the locations stored in the gridObject ArrayList
        //for each, compare it to the head location stored
        // in the "point" parameter
        for (GridObject object : this.gridObjects) {
            if (object.getLocation().equals(point)) {
                if (object.getType() == GridObjectType.APPLE) {
                    snake.grow(3);
                    object.setLocation(getRandomPoint());
                    increaseSnakeSpeed();
                    score.increaseValue();

                } else if (object.getType() == GridObjectType.POISON_BOTTLE) {
                    snake.grow(-3);
                    object.setLocation(getRandomPoint());
                    decreaseSnakeSpeed();
                    score.decreaseValue();

                }
            }
        }

        return point;

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GridDrawData Interface">
    @Override
    public int getCellHeight() {
        return grid.getCellHeight();
    }

    @Override
    public int getCellWidth() {
        return grid.getCellWidth();

    }

    @Override
    public Point getCellSystemCoordinate(Point cellCoordinate) {
        return grid.getCellSystemCoordinate(cellCoordinate);
    }

    private void increaseSnakeSpeed() {
        if (moveDelayLimit == MEDIUM_SPEED) {
            moveDelayLimit = HIGH_SPEED;
        } else if (moveDelayLimit == SLOW_SPEED) {
            moveDelayLimit = MEDIUM_SPEED;
        }
    }

    private void decreaseSnakeSpeed() {
        if (moveDelayLimit == MEDIUM_SPEED) {
            moveDelayLimit = SLOW_SPEED;
        } else if (moveDelayLimit == HIGH_SPEED) {
            moveDelayLimit = MEDIUM_SPEED;
        } else if (moveDelayLimit == SLOW_SPEED) {

            gameState = GameState.OVER;
        }
    }

    /**
     * @return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState the gameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
//</editor-fold>
