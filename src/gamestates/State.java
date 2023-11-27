package gamestates;

import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;

    public State(Game game){
        this.game=game;
    }

    /**
     * método para saber si el ratón está en el botón
     * @param e el evento del ratón, en este caso si está en el botón
     * @param mb el botón
     */
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame(){
        return game;
    }
}
