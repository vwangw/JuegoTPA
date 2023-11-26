package utilz;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

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
    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData){
        int value=lvlData[yTile][xTile];

        if(value>=48 || value<0 || value!=11){
            return true;
        }
        return false;
    }

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

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // Check the pixel below bottom left and bottom right
        if(!isSolid(hitbox.x, hitbox.y+hitbox.height+1, lvlData)){
            if(!isSolid(hitbox.x+hitbox.width, hitbox.y+hitbox.height+1, lvlData)){
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData){
        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData){
        for(int i = 0; i < xEnd - xStart; i++){
            if(isTileSolid(xStart + i, y, lvlData)){
                return false;
            }
        }
        return true;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile){
        int firstXTile = (int)(firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int)(secondHitbox.x / Game.TILES_SIZE);

        if(firstXTile > secondXTile){
            return isAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        }else{
            return isAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
        }
    }
}
