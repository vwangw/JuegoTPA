package entities;

import main.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.GRAVITY;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{
    protected int enemyType;

    protected boolean firstUpdate = true;

    protected float walkSpeed;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        maxHealth = getMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    protected void firstUpdateCheck(int[][] lvlData){
        if(firstUpdate){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
            firstUpdate = false;
        }
    }

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

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player){
        if(attackBox.intersects(player.hitbox)){
            player.changeHealth(-getEnemyDmg(enemyType));
        }
        attackChecked = true;
    }

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

    public void update(int[][] lvlData){
        updateMove(lvlData);
        updateAnimationTick();

    }

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

    protected void turnTowardsPlayer(Player player){
        if(player.hitbox.x > hitbox.x){
            walkDir = RIGHT;
        }else{
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player){
        int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
        if(playerTileY == tileY){
            if(isPlayerOnRange(player)){
                return isSightClear(lvlData, hitbox, player.hitbox, tileY);
            }
        }
        return false;
    }

    protected boolean isPlayerOnRange(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player){
        int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance;
    }

    protected void newState(int enemyState){
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    public void hurt(int amount){
        currentHealth -= amount;
        if(currentHealth <= 0){
            newState(DEAD);
        } else{
            newState(HIT);
        }
    }

    protected void changeWalkDir(){
        if(walkDir == LEFT){
            walkDir = RIGHT;
        }else{
            walkDir = LEFT;
        }
    }

    public void resetEnemy(){
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;
    }

    public boolean isActive() {
        return active;
    }
}
