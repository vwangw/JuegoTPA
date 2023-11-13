package levels;

import main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LevelManager {

    private Game game;
    private BufferedImage levelSprite;

    public LevelManager(Game game){

        this.game=game;
        levelSprite= LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
    }

    public void draw(Graphics g){
        g.drawImage(levelSprite,0,0,null);
    }
    public void update(){

    }
}
