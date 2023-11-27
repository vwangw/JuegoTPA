package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * clase donde están la mayoría de los métodos para los enemigos
 */
public class EnemyManager {
    /**
     * variable que se usa cuando está jugando
     */
    private Playing playing;
    /**
     * lista de la animación del enemigo
     */
    private BufferedImage[][] crabbyArray;
    /**
     * lista de enemigos
     */
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    /**
     * constructor de la clase
     */
    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
    }

    /**
     * método para cargar los enemigos
     */
    public void loadEnemies(Level level){
        crabbies = level.getCrabs();
    }

    /**
     * método de actualizar el juego
     */
    public void update(int[][] lvlData, Player player){
        boolean isAnyActive = false;
        for(Crabby c : crabbies){
            if(c.isActive()){
                c.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if(!isAnyActive){
            playing.setLevelCompleted(true);
        }
    }

    /**
     * método para dibujar el juego
     */
    public void draw(Graphics g, int xLvlOffset){
        drawCrabs(g, xLvlOffset);
    }

    /**
     * método para dibujar el enemigo con sus estados
     */
    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            if(c.isActive()){
                g.drawImage(crabbyArray[c.getState()][c.getAniIndex()], (int) (c.getHitbox().x - CRABBY_DRAWOFFSET_X) - xLvlOffset + c.flipX(), (int) (c.getHitbox().y- CRABBY_DRAWOFFSET_Y), CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
            }
        }
    }

    /**
     * método para ver si el enemigo fue golpeado
     */
    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Crabby c : crabbies){
            if(c.isActive()){
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
                }
            }
        }
    }

    /**
     * método para cargar la animación
     */
    private void loadEnemyImgs(){
        crabbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for(int j = 0; j < crabbyArray.length; j++){
            for(int i = 0; i < crabbyArray[j].length; i++){
                crabbyArray[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    /**
     * resetear los enemigos
     */
    public void resetAllEnemies(){
        for(Crabby c : crabbies){
           c.resetEnemy();
        }
    }
}
