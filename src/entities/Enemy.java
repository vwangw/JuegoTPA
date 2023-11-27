package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

/**
 * clase abstracta para los enemigos
 * @author Vicente
 * @version 27/11/2023
 */
public abstract class Enemy extends Entity{
    /**
     * tipo de enemigo
     */
    protected int enemyType;
    /**
     * variable para saber si es la primera vez en el juego
     */
    protected boolean firstUpdate = true;
    /**
     * velocidad al moverse
     */
    protected float walkSpeed;
    /**
     * dirección donde mira
     */
    protected int walkDir = LEFT;
    /**
     * tamaño de un bloque
     */
    protected int tileY;
    /**
     * rango de ataque
     */
    protected float attackDistance = Game.TILES_SIZE;
    /**
     * si está presente en el juego
     */
    protected boolean active = true;
    /**
     * revisar si el ataque se ha realizado
     */
    protected boolean attackChecked;

    /**
     * Constructor de la clase abstracta Enemy
     * @param x posición x
     * @param y posición y
     * @param width tamaño de la anchura
     * @param height tamaño de la altura
     * @param enemyType tipo de enemigo
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    /**
     * Método para saber si es la primera actualización de los enemigos
     * @param lvlData datos del nivel
     */
    protected void firstUpdateCheck(int[][] lvlData){
        if(firstUpdate){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
            firstUpdate = false;
        }
    }

    /**
     * Método para que pueda caer
     * @param lvlData datos del nivel
     */
    protected void updateInAir(int[][] lvlData){
        if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        }else{
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int)(hitbox.y / Game.TILES_SIZE);
        }
    }

    /**
     * Método para moverse
     * @param lvlData datos del nivel
     */
    protected void move(int[][] lvlData){
        float xSpeed;

        if(walkDir == LEFT){
            xSpeed = -walkSpeed;
        }else {
            xSpeed = walkSpeed;
        }
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            if(isFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    /**
     * Método para cuando el enemigo golpea al jugaddor
     * @param attackBox el hitbox del ataque
     * @param player el jugador mismo
     */
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player){
        if(attackBox.intersects(player.hitbox)){
            player.changeHealth(-getEnemyDmg(enemyType));
        }
        attackChecked = true;
    }

    /**
     * Método para actualizar las animaciones del enemigo
     */
    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= getSpriteAmount(enemyType, state)){
                aniIndex = 0;

                switch (state){
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    /**
     * Método para actualizar el enemigo
     * @param lvlData datos del nivel
     */
    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimationTick();

    }

    /**
     * Método para que se actualice el movimiento del enemigo
     * @param lvlData datos del nivel
     */
    private void updateMove(int[][] lvlData){
        if(firstUpdate){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
            firstUpdate = false;
        }

        if(inAir){
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
            }else{
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            }
        }else {
            switch (state){
                case IDLE:
                    state = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed;

                    if(walkDir == LEFT){
                        xSpeed = -walkSpeed;
                    }else {
                        xSpeed = walkSpeed;
                    }
                    if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
                        if(isFloor(hitbox, xSpeed, lvlData)){
                            hitbox.x += xSpeed;
                            return;
                        }
                    }

                    changeWalkDir();

                    break;
            }
        }
    }

    /**
     * Método para que el enemigo mire al jugador
     * @param player el jugador mismo
     */
    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x){
            walkDir = RIGHT;
        }else{
            walkDir = LEFT;
        }
    }

    /**
     * Método para saber si el jugador puede ser visto o no por el enemigo
     * @param lvlData datos del nivel
     * @param player el jugador
     * @return devuelve verdadero o falso dependiendo de la altura y la distancia entre el enemigo y el jugador
     */
    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY){
            if(isPlayerOnRange(player)){
                return isSightClear(lvlData, hitbox, player.hitbox, tileY);
            }
        }
        return false;
    }

    /**
     * Método para saber si el jugador está en la distancia de detección del enemigo, complementa el método canSeePlayer
     * @param player el jugador
     * @return devuelve verdadero si está en la distancia
     */
    protected boolean isPlayerOnRange(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    /**
     * Nétodo para saber si está en la distancia del ataque
     * @param player el jugador
     * @return devuelve verdadero si está en la distancia del ataque
     */
    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    /**
     * Método para hacer que el enemigo cambie de estado
     * @param enemyState el estado del enemigo que queremos
     */
    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    /**
     * Método para cuando el enemigo es golpeado
     * @param amount cantidad de daño que recibe el enemigo
     */
    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0){
            newState(DEAD);
        } else{
            newState(HIT);
        }
    }

    /**
     * Método para cambiar dónde camina dependiendo de su dirección original
     */
    protected void changeWalkDir(){
        if(walkDir == LEFT){
            walkDir = RIGHT;
        }else{
            walkDir = LEFT;
        }
    }

    /**
     * Método para resetear los enemigos
     */
    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    /**
     * Método booleano para saber si el enemigo está activo en el juego
     * @return
     */
    public boolean isActive() {
        return active;
    }
}
