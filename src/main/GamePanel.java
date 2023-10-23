package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta=100, yDelta=100;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed=15;
    private int playerAction=IDLE;
    private int playerDir=-1;
    private boolean moving=false;

    public GamePanel(){
        mouseInputs=new MouseInputs(this);
        importImage();
        loadAnimations();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void loadAnimations(){
        animations = new BufferedImage[9][6];
        for(int j=0;j<animations.length;j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j*40, 64, 40);
            }
        }
    }

    public void importImage(){
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void setPanelSize(){
        Dimension size=new Dimension(1280,800);
        setPreferredSize(size);
    }

    public void setDirection(int direction){
        this.playerDir= direction;
        moving=true;
    }

    public void setMoving(boolean moving){
        this.moving=moving;
    }

    public void updateAnimationTick(){
        aniTick++;
        if(aniTick>=aniSpeed){
            aniTick=0;
            aniIndex++;
            if(aniIndex>= getSpriteAmount(playerAction)){
                aniIndex=0;
            }
        }
    }

    public void setAnimation(){
        if(moving){
            playerAction=RUNNING;
        }
        else{
            playerAction=IDLE;
        }
    }

    public void updatePos(){
        if(moving){
            switch(playerDir){
                case LEFT:
                    xDelta-=5;
                    break;
                case UP:
                    yDelta-=5;
                    break;
                case RIGHT:
                    xDelta+=5;
                    break;
                case DOWN:
                    yDelta+=5;
                    break;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        updateAnimationTick();

        setAnimation();
        updatePos();

        g.drawImage(animations[playerAction][aniIndex],(int)xDelta,(int)yDelta,120,80,null);
    }
}
