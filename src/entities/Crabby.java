package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.EnemyConstants.*;

/**
 * clase para un tipo de enemigo
 * @author Vicente
 * @version 27/11/2023
 */
public class Crabby extends Enemy{
    /**
     * variable del hitbox de ataque para cuando se mueva la pantalla
     */
    private int attackBoxOffsetX;

    /**
     *Constructor del enemigo crabby
     * @param x posición x
     * @param y posición y
     */
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox();

    }

    /**
     * Método para inicicializar hitbox del ataque
     */
    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    /**
     * Método de actualizar el enemigo
     * @param lvlData datos del nivel
     * @param player el comportamiento del enemigo depende de esta variable
     */
    public void update(int[][] lvlData, Player player){
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    /**
     * Método para actualizar la posición del hitbox de atacar
     */
    private void updateAttackBox(){
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    /**
     * Método para el comportamiento del enemigo
     * @param lvlData los datos del nivel
     * @param player el comportamiento del enemigo depende de esta variable
     */
    private void updateBehavior(int[][] lvlData, Player player){
        if(firstUpdate)
            firstUpdateCheck(lvlData);

        if(inAir){
            updateInAir(lvlData);
        }else {
            switch (state){
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:

                    if(canSeePlayer(lvlData,player)){
                        turnTowardsPlayer(player);
                        if(isPlayerCloseForAttack(player)){
                            newState(ATTACK);
                        }
                    }

                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0){
                        attackChecked = false;
                    }
                    if(aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, player);
                    break;
                case HIT:
                    break;
            }
        }
    }

    /**
     * Para cambiar donde mira el enemigo
     * @return dependiendo de la dirección del enemigo cambiará de dirección o no
     */
    public int flipX(){
        if(walkDir == RIGHT){
            return width;
        }else{
            return 0;
        }
    }
    /**
     * Para cambiar donde mira el enemigo
     * @return dependiendo de la dirección del enemigo cambiará de dirección o no
     */
    public int flipW(){
        if(walkDir == RIGHT){
            return -1;
        }else{
            return 1;
        }
    }

}
