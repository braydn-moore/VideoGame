package ISU.GameEngine.AI;

import ISU.GameEngine.Entity;
import ISU.GameEngine.GameEngine;
import ISU.GameEngine.Player.EntitySheet;
import ISU.GameEngine.Player.Player;
import ISU.GameEngine.Util;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ISU.GameEngine.Util.directions.*;

/***********************************************************
Name: Zombie
Due Date: January 2017
Purpose: Main enemy class, specifically a zombie.
         It extends the AI class
************************************************************/
public class Zombie extends AI {

    /*******************************************************************************************************************************************
     |       NAME           |           Type                    |                          Usage
     |chasingCircle         | org.newdawn.slick.geom.Circle     |   The circle to check if the zombie should stop chasing the player and attack
     | pauseBetweenAttack   | float                             |   The amount of time to pause between attacks
     | spriteSheet          | ISU.GameEngine.Player.EntitySheet |   The sprite sheet for the zombie
     | originalHealth       | float                             |   The original health a zombie has starting off, before merging
     | colorLevelIndex      | int                               |   The index of the color in the "levelFilters" array to color over the zombie
     | levelFilters         | org.newdawn.slick.Color[]         |   An array of colors the zombies change as they level up
    *********************************************************************************************************************************************/
    private Circle chasingCircle;
    private float pauseBetweenAttack;
    private static EntitySheet spriteSheet = new EntitySheet("ISU/resources/zombie.png", 32, 32);
    private static final float originalHealth = 100;
    private int colorLevelIndex = 1;
    private Color[] levelFilters = new Color[]{GameEngine.black, GameEngine.green, GameEngine.cyan, GameEngine.orange, GameEngine.magenta, GameEngine.pink, GameEngine.blue, GameEngine.red};

    /**
     * @param startX: starting x
     * @param startY: starting y
     * @param player: the player
     * @param map: the map
     * @param sizeMultiplier: multiplier of the zombie size
     * @throws SlickException
     */
    public Zombie(float startX, float startY, Player player, TiledMap map, float sizeMultiplier) throws SlickException{
        // construct the AI class
        super(startX,startY, map, originalHealth, 1.25f);
        // tick delay, every 15 seconds/ticks
        tickDelay = 15;
        //this.sizeMultiplier = sizeMultiplier;
        // load the animations
        this.animations = new HashMap<Util.directions, Image[]>(){{
            put(down,spriteSheet.getSpriteAnimationArrayHorizontal(0,96));
            put(left, spriteSheet.getSpriteAnimationArrayHorizontal(1,96));
            put(right, spriteSheet.getSpriteAnimationArrayHorizontal(2,96));
            put(up, spriteSheet.getSpriteAnimationArrayHorizontal(3,96));
        }};
        this.player = player;
        // create the "chasing circle"
        chasingCircle = new Circle(x+(size), y+(size), (size));
    }

    // update the chasing and main circles
    private void updateCircle(){
        // update the collision shapes
        updateMainShapes();
        // update the chasing circle
        chasingCircle.setCenterX(x+(size));
        chasingCircle.setCenterY(y+(size));
    }

    /**
     * @param graphics, used to draw things to the screen
     */
    @Override
    public void render(Graphics graphics){
        // if it is possible to animate then draw the enemy image
        if (animations.get(currentDirection) != null && animation < 3) {
            // draw the zombie character
            animations.get(currentDirection)[(int) animation].draw(x, y);
            // draw the semi-transparent filter over top
            animations.get(currentDirection)[(int) animation].draw(x, y, levelFilters[colorLevelIndex-1]);
        }
        // display the health bar
        displayHealth(graphics);
    }

    @Override
    public void animate(){
        // add the float of the tick to the animation
        animation+=(float) 1/tickDelay;

        // if the animations exceeds the length of the array then reset it
        if (animation>animations.get(currentDirection).length)
            animation = 0;
    }

    @Override
    public void attack(){
        // if the zombie can attack
        if (!attack){
            // set the attack boolean to true
            attack = true;
            // reset all timekeeping variables
            timer.reset();
            attackTimeElasped = 0;
            pauseBetweenAttack = 0;
        }
    }

    // update the attack times and if it is possible to attack again
    private void updateAttack(){
        // if the zombie is attacking
        if (attack){
            // adjusts the elapsed time
            attackTimeElasped+=timer.getElapsedTime();
            // if the time elapsed is over 0.2 seconds then set attack to false so the zombie can attack again
            if (attackTimeElasped>0.2)
                attack = false;
        }
    }

    @Override
    protected void revert(){
        // fairly self explanatory
        x = previousX;
        y = previousY;
    }

    /**
     * @param entities: used to get the entities that collide with this entity to merge
     */
    private void merge(ArrayList<Entity> entities){
        // create a list of allyCollisions
        List<Entity> allyCollisions = allyCollision(entities);
        // for each ally the zombie collides with
        for (Entity entity : allyCollisions) {
            // increase the attack damage
            this.attackDamage += entity.attackDamage / 3;
            // merge the health
            this.health += entity.health / 2;
            // increase the color level index to change the filter color
            colorLevelIndex+=((Zombie)entity).colorLevelIndex;
            // if the zombie has reached the max level then don't reset it
            if (colorLevelIndex>levelFilters.length)
                colorLevelIndex = levelFilters.length;
        }
        // if the zombie has collided with other allies
        if (allyCollisions.size()>0) {
            // adjust the health bar accordingly
            this.updateHealthPercent(health);
        }
        // remove all allies that this zombie collided against
        entities.removeAll(allyCollisions);
    }

    /**
     * @param gameContainer, used to get user input, control display mode, exit, pause, etc.
     * @param entities, an arraylist of entities containing all the entities currently in the game
     */
    @Override
    public void update(GameContainer gameContainer, ArrayList<Entity> entities){
        // update the previous location
        updatePreviousLocation();
        // if the zombie collides with another entity then revert it
        if (didCollide(entities)) revert();
        // check for merges with other zombies
        merge(entities);
        // if the zombie is not attacking adjust the time variable for between attacks
        if (!attack)
            pauseBetweenAttack+=timer.getElapsedTime();

        // if the chasing circle of this zombie does not intersects the player then move towards the player
        if (!chasingCircle.intersects(player.getCircle())) {
            // if the zombie is not within range of the center of the players x then move towards it
            if (!Util.inRange(getCircle().getX(), getCircle().getX()+getCircle().getRadius()*2-5, player.getX()))
                updateX();

            // if the zombie is not within range of the center of the players y then move towards it
            else if (!Util.inRange(getCircle().getY(), getCircle().getY()+getCircle().getRadius()*2-5, player.getY()))
                updateY();


            // set the previous animation
            previousAnimation = animation;
            // animate the zombie
            animate();

        }
        // if the chasing circle of the zombie does intersects the player and the zombie can attack and the pause between attack is greater than 1 second
        else if (chasingCircle.intersects(player.getCircle()) && !attack && pauseBetweenAttack>1f)
            // attack
            attack();

        // update the attack and the circles
        updateAttack();
        updateCircle();

    }

    /**
     * @return: return the circle of the zombie for it's attack
     */
    public Circle getAttackShape(){
        // return the zombies main circle
        return getCircle();
    }

}
