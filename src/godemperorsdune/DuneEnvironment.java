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

    public final int SLOW_SPEED = 8;
    public final int MEDIUM_SPEED = 5;
    public final int HIGH_SPEED = 3;

    private int moveDelayLimit = MEDIUM_SPEED;
    private int moveDelayCounter = 0;
    public Point randomize;

    private ArrayList<GridObject> gridObjects;

    public DuneEnvironment() {
    }

    @Override
    public void initializeEnvironment() {

        this.setBackground(ResourceTools.loadImageFromResource("resources/sand_tile.jpg").getScaledInstance(1280, 800, Image.SCALE_SMOOTH));
        grid = new Grid(25, 22, 25, 25, new Point(20, 50), Color.BLACK);
        grid.setColor(Color.yellow);

        snake = new TailSnake();
        snake.setDirection(Direction.DOWN);
        snake.setDrawData(this);
        snake.setLocationValidator(this);

        ArrayList<Point> body = new ArrayList<>();
        body.add(new Point(3, 1));
        body.add(new Point(3, 2));
        body.add(new Point(2, 2));
        body.add(new Point(2, 3));

        snake.setBody(body);

        gridObjects = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            gridObjects.add(new GridObject(GridObjectType.POISON_BOTTLE, getRandomPoint()));

        }

    }

    public Point getRandomPoint() {
        return new Point((int) (grid.getRows() * Math.random()), (int) (grid.getColumns() * Math.random()));
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
            AudioPlayer.play("/resources/Creepy.wav");
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
        if (grid != null) {
            grid.paintComponent(graphics);
        }

        if (snake != null) {
            snake.draw(graphics);
        }

        if (gridObjects != null) {
            for (GridObject gridObject : gridObjects) {
                if (gridObject.getType() == GridObjectType.POISON_BOTTLE) {
                    GraphicsPalette.drawPoisonBottle(graphics, grid.getCellSystemCoordinate(gridObject.getLocation()), grid.getCellSize(), Color.YELLOW);
                }
            }
        }
    }

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

    @Override
    public Point validateLocation(Point point) {
        if (point.x >= this.grid.getColumns()) {
            point.x = 0;
        } else if (point.x < 0) {
            point.x = this.grid.getColumns() - 1;
        } else if (point.y < 0) {
            point.y = this.grid.getRows() - 1;
        } else if (point.y >= this.grid.getRows()) {
            point.y = 0;
        }

        return point;

    }

}
//</editor-fold>
