package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * clase de lo que pasa cuando pierdes
 * @author Vicente
 * @version 27/11/2023
 */
public class GameOverOverlay {

    private Playing playing;

    public GameOverOverlay(Playing playing){
        this.playing = playing;
    }

    /**
     * se dibuja para que sea más oscuro la parte de atrás
     */
    public void draw(Graphics g){
        g.setColor(new Color(0,0,0,200));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Gamer Over", Game.GAME_WIDTH / 2, 150);
        g.drawString("Press esc to enter Main Menu", Game.GAME_WIDTH / 2, 300);
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
