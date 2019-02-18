package ISU.GameEngine.Player.Magic;

import ISU.GameEngine.Entity;
import ISU.GameEngine.Util;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;

/***********************************************************
Name: FireBall
Due Date: January 2017
Purpose: The fire class for when the player casts the fire ball spell
************************************************************/
public class FireBall extends MagicEntity {

    // fields required
    private float distanceTravelledX, distanceTravelledY;
    private boolean collide;
    private int velocity;

    // constructor
    public FireBall(float x, float y, Util.directions direction, Image[] images, TiledMap map){
        // call the MagicEntity constructor which calls the Entity constructor
        super(x,y, map, "player", 32, 2.8f, 0.3f);
        animations = new HashMap<>();
        animations.put(direction,images);
        currentDirection = direction;
        collide = false;
        // if the direction the fire is cast is right or movePositive the velocity in x or y will increase
        if (direction == Util.directions.right || direction == Util.directions.down)
            velocity = 3;
        // otherwise the velocity of the fire ball will be negative to travel left or moveLess
        else
            velocity = -3;
        // set the attack boolean to true as that is all the fireball does
        attack = true;
    }

    /**
     * @return: whether or not the fireball has reached the end of its life time
     */
    public boolean isDead(){
        // if the distance travelled in any direction is greater than 4 tiles(128 pixels) or the fireball collides with a map object, then it is dead
        return (Math.abs(distanceTravelledY)>=128 || Math.abs(distanceTravelledX)>=128 || collide);
    }

    /**
     * @param graphics: used to draw things to the screen
     */
    public void render(Graphics graphics){
        // draws the image in the animation array for the current direction
        graphics.drawImage(animations.get(currentDirection)[(int)animation],getX(), getY());
    }

    // move the fireball
    private void move(){
        // if the fireball is moving horizontally then add the velocity to the X value and the x-distance-travelled
        if (currentDirection == Util.directions.right || currentDirection == Util.directions.left) {
            distanceTravelledX += velocity;
            setX(getX()+ velocity);
        }
        // otherwise move the fireball vertically
        else {
            distanceTravelledY += velocity;
            setY(getY()+ velocity);
        }
    }

    /**
     * @return: returns the shape that dictates whether an attack hits and since the entire fireball is an attack then return the collision circle
     */
    public Circle getAttackShape(){
        return getCircle();
    }


    private void collision(){
        // if the fireball collides with a part of the map that is a "barrier" then set the collision variable to true
        if (didCollideMap(map.getLayerIndex("Objects"), map, getCircle()))
            collide = true;
    }

    public void animate(){
        // every 14 pixels the fireball travels update the animation
        if ((distanceTravelledY!=0 && distanceTravelledY%14==0) || (distanceTravelledX!=0 && distanceTravelledX%14==0))
            animation++;

        // if the animation exceeds the size of the animations array then reset the animation procedure
        if (animation>=animations.get(currentDirection).length)
            animation = 0;
    }


    // simply implement the abstract attack method to avoid errors, as the fireball doesn't require this method
    public void attack(){

    }

    /**
     * @param gameContainer, used to get user input, control display mode, exit, pause, etc.
     * @param entities, an arraylist of entities containing all the entities currently in the game
     */
    public void update(GameContainer gameContainer, ArrayList<Entity> entities){
        // move the fireball
        move();
        // update the collision shapes
        updateMainShapes();
        // check for collisions
        collision();
        // animate the fireball
        animate();
    }
}
