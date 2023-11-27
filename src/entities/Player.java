package entities;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

/**
 * clase del jugador
 * @author Vicente
 * @version 27/11/2023
 */
public class Player extends Entity{

    private BufferedImage[][] animations;

    private boolean moving=false, attacking=false;
    private boolean left, right, jump;
    private int[][] lvlData;
    private float xDrawOffset=21* Game.SCALE;
    private float yDrawOffset=4* Game.SCALE;

    // Gravedad y Saltar
    private float jumpSpeed= -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision=0.5f*Game.SCALE;

    //Barra del estado
    private BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);

    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(34 * Game.SCALE);
    private int healthBarYStart = (int)(14 * Game.SCALE);


    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked = false;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing){
        super(x,y,width,height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = Game.SCALE;
        loadAnimations();
        initHitbox(20,27);
        initAttackBox();
    }

    /**
     * Saber donde aparece el personaje
     */
    public void setSpawn(Point spawn){
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * inicializar el hitbox del ataque
     */
    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update(){
        updateHealthBar();

        if(currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }

        updateAttackBox();

        updatePos();
        if(attacking){
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    /**
     * comprobar si el ataque ha sido realizado
     */
    private void checkAttack(){
        if(attackChecked || aniIndex != 1){
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox(){
        if(right){
            attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar(){
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    /**
     * dibujar
     */
    public void render(Graphics g, int lvlOffset){

        g.drawImage(animations[state][aniIndex],(int)(hitbox.x-xDrawOffset) - lvlOffset + flipX,(int)(hitbox.y-yDrawOffset),width * flipW,height,null);
        drawUI(g);
    }

    /**
     * dibujar los botones
     */
    private void drawUI(Graphics g){
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    /**
     * metodo de la animacion
     */
    public void updateAnimationTick(){
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= getSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    /**
     * para saber que animaciones usar
     */
    public void setAnimation(){

        int startAni=state;

        if(moving){
            state=RUNNING;
        }
        else{
            state=IDLE;
        }

        if(inAir){
            if(airSpeed < 0){
                state = JUMP;
            }else {
                state = FALLING;
            }
        }

        if(attacking){
            state = ATTACK;
            if(startAni != ATTACK){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if(startAni != state){
            resetAniTick();
        }
    }

    /**
     * volver a empezar la animacion cuando esta cambie
     */
    public void resetAniTick(){
        aniTick=0;
        aniIndex=0;
    }

    /**
     * para saber como moverse dependiendo del estado del jugador
     */
    public void updatePos(){

        moving=false;
        if(jump){
            jump();
        }

        if(!inAir){
            if((!left && !right) || (right && left)){
                return;
            }
        }

        float xSpeed=0;

        if(left){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir){
            if(!isEntityOnFloor(hitbox, lvlData)){
                inAir=true;
            }
        }

        if(inAir){

            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            }else {
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        }else {
            updateXPos(xSpeed);
        }
        moving = true;
    }

    /**
     * saltar
     */
    private void jump(){
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    /**
     * resetear el hecho de que esté en el aire
     */
    private void resetInAir(){
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed){
        if(canMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height,lvlData)){
            hitbox.x+=xSpeed;
        }
        else{
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    /**
     * modificar la vida
     * @param value cantidad que se modifica
     */
    public void changeHealth(int value){
        currentHealth += value;

        if(currentHealth <= 0){
            currentHealth = 0;
            //fin del juego
        }else if(currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    /**
     * cargar animación
     */
    public void loadAnimations(){

        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][8];
        for(int j=0;j<animations.length;j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
            }
        }
        statusBarImg = LoadSave.getSpriteAtlas(LoadSave.STATUS_BAR);
    }

    /**
     * cargar datos del nivel
     */
    public void loadLvlData(int[][] lvlData){
        this.lvlData=lvlData;
        if(!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    /**
     * sirve para movimiento
     */
    public void resetDirBooleans(){
        left=false;
        right=false;
    }

    public void setAttacking(boolean attacking){
        this.attacking=attacking;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump){
        this.jump=jump;
    }

    /**
     * reset entero
     */
    public void resetAll(){
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitbox.x = x;
        hitbox.y = y;

        if(!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }
}
