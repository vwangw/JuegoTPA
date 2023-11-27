package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import javax.swing.JPanel;
import java.awt.*;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

/**
 * el gamepanel
 * @author Vicente
 * @version 27/11/2023
 */
public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;

    public GamePanel(Game game){
        mouseInputs=new MouseInputs(this);
        this.game=game;

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    /**
     * tamaño del panel
     */
    public void setPanelSize(){
        Dimension size=new Dimension(GAME_WIDTH,GAME_HEIGHT);
        setPreferredSize(size);

    }

    /**
     * dibujar en el panel
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame(){
        return game;
    }
}
