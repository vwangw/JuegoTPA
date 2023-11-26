package entities;

import main.Game;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{
    protected int enemyState, enemyType;
    protected int aniTick, aniIndex, aniSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected float gravity = 0.04f * Game.SCALE;
    protected float walkspeed = 0.35f * Game.SCALE;
    protected int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width,height);
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
        if(canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
            hitbox.y += fallSpeed;
            fallSpeed += gravity;
        }else{
            inAir = false;
            hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
        }
    }

    protected void move(int[][] lvlData){
        float xSpeed = 0;

        if(walkDir == LEFT){
            xSpeed = -walkspeed;
        }else {
            xSpeed = walkspeed;
        }
        if(canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
            if(isFloor(hitbox, xSpeed, lvlData)){
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    protected void updateAnimationTick(){
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= getSpriteAmount(enemyType, enemyState)){
                aniIndex = 0;
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
            if(canMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += fallSpeed;
                fallSpeed += gravity;
            }else{
                inAir = false;
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
            }
        }else {
            switch (enemyState){
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;

                    if(walkDir == LEFT){
                        xSpeed = -walkspeed;
                    }else {
                        xSpeed = walkspeed;
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

    protected void newState(int enemyState){
        this.enemyState = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected void changeWalkDir(){
        if(walkDir == LEFT){
            walkDir = RIGHT;
        }else{
            walkDir = LEFT;
        }
    }

    public int getAniIndex(){
        return aniIndex;
    }

    public int getEnemyState(){
        return enemyState;
    }
}
