/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package godemperorsdune;

import audio.AudioPlayer;
import environment.LocationValidatorIntf;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author sofyashipova
 */
public class Snake {

    private ArrayList<Point> body = new ArrayList<>();
    private Direction direction = Direction.RIGHT;
    private GridDrawData drawData;
    private LocationValidatorIntf locationValidator;
    private boolean paused;

    private int growthCounter;

    private Image segmentImage;


    public void draw(Graphics graphics) {
        for (Point bodySegmentLocation : body) {

            Point topLeft = drawData.getCellSystemCoordinate(bodySegmentLocation);
            graphics.setColor(Color.red);
            graphics.fillRect(topLeft.x, topLeft.y, drawData.getCellWidth(), drawData.getCellHeight());

            drawData.getCellHeight();

        }
    }

    /**
     * @return the body
     */
    public ArrayList<Point> getSafeBody() {
        ArrayList<Point> safeBody = new ArrayList<>();
        body.stream().forEach((point) -> {
            safeBody.add(point);
        });
        return safeBody;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(ArrayList<Point> body) {
        this.body = body;
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * @return the drawData
     */
    public GridDrawData getDrawData() {
        return drawData;
    }

    /**
     * @param drawData the drawData to set
     */
    public void setDrawData(GridDrawData drawData) {
        this.drawData = drawData;
    }

    final int headPosition = 0;

    public void togglePaused() {
        if (paused) {
            paused = false;
        } else {
            paused = true;
        }
    }

    public void move() {
        if (paused == false) {
            
            
            Point newHead = (Point) getHead().clone();

            if (direction == Direction.DOWN) {
                newHead.y++;
            } else if (direction == Direction.UP) {
                newHead.y--;
            } else if (direction == Direction.LEFT) {
                newHead.x--;
            } else if (direction == Direction.RIGHT) {
                newHead.x++;
            }

            if (locationValidator != null) {
                body.add(headPosition, locationValidator.validateLocation(newHead));
            }

            if (growthCounter <= 0) {
                body.remove(body.size() - 1);
            } else {
                growthCounter--;
            }
        }

    }

    public void grow(int length) {
        growthCounter += length;
    }

    public Point getHead() {
        return body.get(headPosition);
    }

    /**
     * @return the locationValidator
     */
    public LocationValidatorIntf getLocationValidator() {
        return locationValidator;
    }

    /**
     * @param locationValidator the locationValidator to set
     */
    public void setLocationValidator(LocationValidatorIntf locationValidator) {
        this.locationValidator = locationValidator;
    }

    /**
     * @return the growthCounter
     */
    public int getGrowthCounter() {
        return growthCounter;
    }

    /**
     * @param growthCounter the growthCounter to set
     */
    public void setGrowthCounter(int growthCounter) {
        this.growthCounter = growthCounter;
    }

    /**
     * @return the segmentImage
     */
    public Image getSegmentImage() {
        return segmentImage;
    }

    /**
     * @param segmentImage the segmentImage to set
     */
    public void setSegmentImage(Image segmentImage) {
        this.segmentImage = segmentImage;
    }

}
