package gamestates;

/**
 * enumera los tipos de estado en que se puede encontrar el juego
 * @author Vicente
 * @version 27/11/2023
 */
public enum Gamestate {

    PLAYING, MENU, OPTIONS, QUIT;

    public static Gamestate state = MENU;
}
