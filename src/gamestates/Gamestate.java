package gamestates;

/**
 * enumera los tipos de estado en que se puede encontrar el juego
 */
public enum Gamestate {

    PLAYING, MENU, OPTIONS, QUIT;

    public static Gamestate state = MENU;
}
