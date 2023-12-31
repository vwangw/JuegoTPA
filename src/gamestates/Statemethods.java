package gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * métodos que siempre tienen que ir juntos
 * @author Vicente
 * @version 27/11/2023
 */
public interface Statemethods {
    void update();

    void draw(Graphics g);

    void mouseClicked(MouseEvent e);

    void mousePressed(MouseEvent e);

    void mouseReleased(MouseEvent e);

    void mouseMoved(MouseEvent e);

    void keyPressed(KeyEvent e);

    void keyReleased(KeyEvent e);

}
