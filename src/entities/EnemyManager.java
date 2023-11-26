package entities;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies(){
        crabbies = LoadSave.getCrabs();
        System.out.println("size of crabs: " + crabbies.size());
    }

    public void update(int[][] lvlData, Player player){
        for(Crabby c : crabbies){
            if(c.isActive())
                c.update(lvlData, player);
        }
    }

    public void draw(Graphics g, int xLvlOffset){
        drawCrabs(g, xLvlOffset);
    }

    private void drawCrabs(Graphics g, int xLvlOffset) {
        for (Crabby c : crabbies) {
            if(c.isActive()){
                g.drawImage(crabbyArray[c.getEnemyState()][c.getAniIndex()], (int) (c.getHitbox().x - CRABBY_DRAWOFFSET_X) - xLvlOffset + c.flipX(), (int) (c.getHitbox().y- CRABBY_DRAWOFFSET_Y), CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);
                //c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

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

    private void loadEnemyImgs(){
        crabbyArray = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.CRABBY_SPRITE);
        for(int j = 0; j < crabbyArray.length; j++){
            for(int i = 0; i < crabbyArray[j].length; i++){
                crabbyArray[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies(){
        for(Crabby c : crabbies){
           c.resetEnemy();
        }
    }
}
