package entities;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.canMoveHere;

public class Player extends Entity{
    private int aniTick, aniIndex, aniSpeed=15;
    private int playerAction=IDLE;
    private boolean moving=false, attacking=false;
    private boolean left, up, right, down;
    private float playerSpeed=2.0f;
    private int[][] lvlData;
    private float xDrawOffset=21* Game.SCALE;
    private float yDrawOffset=4* Game.SCALE;

    private BufferedImage[][] animations;
    public Player(float x,float y, int width, int height){
        super(x,y,width,height);
        loadAnimations();
        initHitbox(x,y,20*Game.SCALE,28*Game.SCALE);
    }

    public void update(){

        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g){

        g.drawImage(animations[playerAction][aniIndex],(int)(hitbox.x-xDrawOffset),(int)(hitbox.y-yDrawOffset),width,height,null);
        drawHitbox(g);
    }

    public void updateAnimationTick(){
        aniTick++;
        if(aniTick>=aniSpeed){
            aniTick=0;
            aniIndex++;
            if(aniIndex>= getSpriteAmount(playerAction)){
                aniIndex=0;
                attacking=false;
            }
        }
    }

    public void setAnimation(){

        int startAni=playerAction;

        if(moving){
            playerAction=RUNNING;
        }
        else{
            playerAction=IDLE;
        }

        if(attacking){
            playerAction=ATTACK_1;
        }

        if(startAni!=playerAction){
            resetAniTick();
        }
    }

    public void resetAniTick(){
        aniTick=0;
        aniIndex=0;
    }

    public void updatePos(){

        moving=false;
        if(!left && !right && !up && !down){
            return;
        }

        float xSpeed=0, ySpeed=0;

        if(left && !right){
            xSpeed = -playerSpeed;
        }
        else if (right && !left){
            xSpeed = playerSpeed;
        }

        if(up && !down){
            ySpeed = -playerSpeed;
        }
        else if (down && !up){
            ySpeed = playerSpeed;
        }
        if(canMoveHere(hitbox.x+xSpeed,hitbox.y+ySpeed,hitbox.width,hitbox.height,lvlData)){
            hitbox.x+=xSpeed;
            hitbox.y+=ySpeed;
            moving=true;
        }
    }

    public void loadAnimations(){

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][6];
        for(int j=0;j<animations.length;j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
            }
        }
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData=lvlData;
    }

    public void resetDirBooleans(){
        left=false;
        right=false;
        up=false;
        down=false;
    }

    public void setAttacking(boolean attacking){
        this.attacking=attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }
}
