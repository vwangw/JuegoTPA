package levels;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

/**
 * clase para los niveles
 * @author Vicente
 * @version 27/11/2023
 */
public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;


    public Level(BufferedImage img){
        this.img=img;
        createLevelData();
        createEnemies();
        calculateLvlOffsets();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn(){
        playerSpawn = GetPlayerSpawn(img);
    }

    /**
     * método para mover la cámara
     */
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

    public Point getPlayerSpawn(){
        return playerSpawn;
    }
}
