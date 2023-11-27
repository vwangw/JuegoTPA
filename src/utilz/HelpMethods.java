package utilz;

import entities.Crabby;
import main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.CRABBY;

/**
 * métodos que se usan en varias clases, para tenerlos en un mismo lugar
 * @author Vicente
 * @version 27/11/2023
 */
public class HelpMethods {
    /**
     * se puede mover dependiendo del hitbox
     * @param x donde comienza el hitbox en la eje x
     * @param y donde comienza el hitbox en la eje y
     * @param width lo sumo a la x para saber donde está la esquina de la derecha
     * @param height lo sumo a la y para saber donde está la esquina de la derecha
     * @param lvlData datos del nivel
     * @return devuelve true cuando ninguna de las 4 esquinas colisiona
     */
    public static boolean canMoveHere(float x, float y, float width, float height,int[][] lvlData){

        if(!isSolid(x,y,lvlData)){
            if(!isSolid(x+width,y+height,lvlData)){
                if(!isSolid(x+width,y,lvlData)){
                    if(!isSolid(x,y+height,lvlData)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * método estático booleano para saber si hay un bloque caminable
     * @param x posición x
     * @param y posición y
     * @param lvlData datos del nivel
     * @return devuelve true si hay un bloque
     */
    private static boolean isSolid(float x, float y, int[][] lvlData){
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if(x<0 || x>=maxWidth){
            return true;
        }
        if(y<0 || y>=Game.GAME_HEIGHT){
            return true;
        }

        float xIndex=x/Game.TILES_SIZE;
        float yIndex=y/Game.TILES_SIZE;

        return isTileSolid((int)(xIndex), (int)(yIndex), lvlData);
    }

    /**
     * método para saber si es sólido el bloque
     * @param xTile la posición x del bloque
     * @param yTile la posición y de bloque
     * @param lvlData datos del nivel
     * @return devuelve true si puede pasar
     */
    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData){
        int value=lvlData[yTile][xTile];

        if(value>=48 || value<0 || value!=11){
            return true;
        }
        return false;
    }

    /**
     * método para que la cámara se muevasi el personaje está en el borde
     * @param hitbox el hitbox del jugador
     * @param xSpeed velocidad de la cámara
     * @return devuelve la cantidad que se mueve
     */
    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile=(int)(hitbox.x / Game.TILES_SIZE);
        if(xSpeed>0){
            //Right
            int tileXPos=currentTile*Game.TILES_SIZE;
            int xOffset=(int)(Game.TILES_SIZE-hitbox.width);
            return tileXPos + xOffset - 1;
        }else{
            //Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * método para conseguir el salto y gravedad
     * @param hitbox hitbox del personaje
     * @param airSpeed velocidad en el aire
     * @return devuelve el valor del salto o caída
     */
    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if(airSpeed > 0){
            //Falling - touching floor
            int tileYPos = currentTile*Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE-hitbox.height);
            return tileYPos + yOffset - 1;
        }else {
            //Jumping
            return currentTile * Game.TILES_SIZE;
        }
    }

    /**
     * para saber si está en el suelo
     * @param hitbox hitbox del personaje
     * @param lvlData datos del nivel
     * @return devuelve si está en el suelo o no
     */
    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // Check the pixel below bottom left and bottom right
        if(!isSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData)){
            if(!isSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height+1, lvlData)){
                return false;
            }
        }
        return true;
    }

    /**
     * para que se sepa si es suelo
     * @param hitbox hitbox del enemigo
     * @param xSpeed para cuanto se mueve
     * @param lvlData datos del nivel
     * @return devuelve true si es solido
     */
    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        if(xSpeed > 0)
            return isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        else
            return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    /**
     * complementa las clases que sirven que el enemigo patrulle
     * @param xStart donde empieza
     * @param xEnd donde acaba
     * @param y la altitud
     * @param lvlData datos del nivel
     * @return si son solidos o no
     */
    public static boolean isAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xEnd - xStart; i++){
            if(isTileSolid(xStart + i, y, lvlData)){
                return false;
            }
            if(!isTileSolid(xStart + i, y + 1, lvlData)){
                return false;
            }
        }
        return true;
    }

    /**
     * si no hay ningún obstáculo entre el enemigo y el jugador
     * @param lvlData datos del nivel
     * @param firstHitbox hitbox del primer personaje
     * @param secondHitbox hitbox del segundo personaje
     * @param yTile la altitud
     * @return devuelve true si no hay nada
     */
    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);

        if(firstXTile > secondXTile){
            return isAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        }else{
            return isAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }

    public static int [][] GetLevelData(BufferedImage img){
        int[][] lvlData=new int [img.getHeight()][img.getWidth()];

        for(int j=0;j<img.getHeight();j++){
            for(int i=0;i< img.getWidth();i++){
                Color color=new Color(img.getRGB(i,j));
                int value=color.getRed();
                if(value>=48){
                    value=0;
                }
                lvlData[j][i]=value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img){
        ArrayList<Crabby> list = new ArrayList<>();

        for(int j=0;j<img.getHeight();j++){
            for(int i=0;i< img.getWidth();i++){
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value == CRABBY){
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
                }
            }
        }
        return list;
    }
    public static Point  GetPlayerSpawn(BufferedImage img){
        for(int j=0;j<img.getHeight();j++){
            for(int i=0;i< img.getWidth();i++){
                Color color=new Color(img.getRGB(i,j));
                int value=color.getGreen();
                if(value == 100){
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
                }
            }
        }
        return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
    }
}
