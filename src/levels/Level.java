package levels;

import entities.Crabby;
import main.Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;


    public Level(BufferedImage img){
        this.img=img;
        createLevelData();
        createEnemies();
        calculateLvlOffsets();
    }

    private void calculateLvlOffsets(){
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createLevelData(){
        lvlData = GetLevelData(img);
    }

    private void createEnemies(){
        crabs = GetCrabs(img);
    }

    public int getSpriteIndex(int x, int y){
        return lvlData[y][x];
    }

    public int [][] getLevelData(){
        return lvlData;
    }

    public int getLvlOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }
}
