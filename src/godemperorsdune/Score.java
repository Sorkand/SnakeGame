/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package godemperorsdune;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author sofyashipova
 */
public class Score {
    
    public void draw(Graphics graphics) {
        graphics.setFont(font);
        graphics.setColor(Color.white);
        graphics.drawString("Score: " + value, position.x, position.y);
        
        
    }
    
    
//<editor-fold defaultstate="collapsed" desc="Properties">
    private int value = 0;
    private Point position;
    private Font font = new Font("Calibri", Font.BOLD, 20);
    
    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
    
    public int increaseValue() {
        return value += 100;
    }
    
    public int decreaseValue() {
        return value -= 250;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    public void addToValue(int amount) {
        this.value += amount;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Point position) {
        this.position = position;
    }
    
//</editor-fold>
}
