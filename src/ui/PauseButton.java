package ui;

import java.awt.*;

/**
 * botón para pausar
 * @author Vicente
 * @version 27/11/2023
 */
public class PauseButton {

    protected int x,y,width,height;
    protected Rectangle bounds;

    public PauseButton(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds(){
        bounds = new Rectangle(x,y,width,height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
