/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package godemperorsdune;

import images.ResourceTools;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 *
 * @author sofyashipova
 */
public class TailSnake extends Snake {

    private Image segmentImage;

    {
        segmentImage = ResourceTools.loadImageFromResource("resources/SnakeSegment.png");
    }

    @Override
    public void draw(Graphics graphics) {
        Image segment = segmentImage.getScaledInstance(getDrawData().getCellWidth(), getDrawData().getCellHeight(), Image.SCALE_FAST);

        for (Point bodySegmentLocation : getSafeBody()) {
            Point topLeft = getDrawData().getCellSystemCoordinate(bodySegmentLocation);
            graphics.drawImage(segment, topLeft.x, topLeft.y, null);
        }
    }

}


