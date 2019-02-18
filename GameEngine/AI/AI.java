package ISU.GameEngine.AI;

import ISU.GameEngine.Entity;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import static ISU.GameEngine.Util.directions.*;

/***********************************************************
Name: AI
Due Date: January 2017
Purpose: The AI behind the zombies movement
************************************************************/
abstract class AI extends Entity {

    /************************************************************************************************************
     |  NAME    |               TYPE                |                           USAGES
     | nextMove | org.newdawn.slick.geom.Rectangle  |   the next move of the zombie to see if it needs to move around an object
     | velocity | float                             |   the amount of pixels the AI moves per update
     ************************************************************************************************************/
    private Rectangle nextMove;
    private float velocity;

    /**
     * @param startX : starting x of the zombie
     * @param startY : starting y of the zombie
     * @param map : the map of the game
     * @param health : the health of the enemy
     */
    AI(float startX, float startY, TiledMap map, float health, float velocity){
        // call the constructor of the Entity class
        super(startX, startY, health, 0.7f, map, "zombie", (float) 32);
        // set the velocity
        this.velocity = velocity;
    }

    // move the Entity along the x axis
    void updateX(){
        // get the circle of the Entity
        Circle circle = getCircle();
        // get the rectangle of the next move
        nextMove = new Rectangle(circle.getX(), circle.getY(), circle.getWidth(), circle.getHeight());
        // move the rectangle to the right
        nextMove.setCenterX(nextMove.getCenterX()+5);
        // check if the nextMove rect will collide with the map if so then update the player y
        if (didCollideMap(map.getLayerIndex("Objects"), map, nextMove) && blockedDirections.contains(right))
            updateY(player.getY());
        // otherwise move the nextMove rect to the left and try again
        else{
            nextMove.setCenterX(nextMove.getCenterX()-10);
            if (didCollideMap(map.getLayerIndex("Objects"), map, nextMove) && blockedDirections.contains(left))
                updateY(player.getY());

            // if the next move will not collide then update the x
            else
                updateX(player.getX());
        }
    }

    // move the Entity along the y axis
    void updateY(){
        // mimic the updateX method except check the Y next move and if there is a collision update the X
        Circle circle = getCircle();
        nextMove = new Rectangle(circle.getX(), circle.getY(), circle.getWidth(), circle.getHeight());
        nextMove.setCenterY(nextMove.getCenterY()+5);
        if (didCollideMap(map.getLayerIndex("Objects"), map, nextMove) && blockedDirections.contains(up))
            updateX(player.getX());
        else{
            nextMove.setCenterY(nextMove.getCenterY()-10);
            if (didCollideMap(map.getLayerIndex("Objects"), map, nextMove) && blockedDirections.contains(down))
                updateX(player.getX());

            else
                updateY(player.getY());
        }

    }

    /**
     * @param playerX: the x coord of the player
     */
    private void updateX(float playerX){
        // move the entity towards the player based on the players x, and update the entity direction
        if (x<playerX) {
            x+=velocity;
            currentDirection = right;
        }
        else {
            x-=velocity;
            currentDirection = left;
        }
    }

    /**
     * @param playerY: the players y coord
     */
    private void updateY(float playerY) {
        // move the entity towards the player based on the players y, and update the entity direction
        if (y < playerY) {
            y+=velocity;
            currentDirection = down;
        } else{
            y-=velocity;
            currentDirection = up;
        }
    }
}
