package ISU.GameEngine;

import ISU.GameEngine.Player.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/***********************************************************
Name: Entity
Due Date: January 2017
Purpose: Abstract class that all entities in the game extend,
         mainly used for collision detection and attacks
************************************************************/
public abstract class Entity{

    /****************************************************************************************************************
                NAME            |                   TYPE                |                PURPOSE
     | serial                   | float                                 |   Used to compare entities, their unique identifier
     | circle                   | org.newdawn.slick.geom.Circle         |   The main circle used to check for collisions
     | x                        | float                                 |   The x coord of the entity
     | y                        | float                                 |   The y coord of the entity
     | previousX                | float                                 |   The previous x position of the entity
     | previousY                | float                                 |   The previous y position of the entity
     | timer                    | ISU.GameEngine.Timer                  |   Used to keep time for the entity eg. for attacks
     | currentDirection         | ISU.GameEngine.Util.directions        |   The current direction that the entity is moving in
     | map                      | org.newdawn.slick.tiled.TiledMap      |   The map
     | tickDelay                | int                                   |   The amount of time in between animations of the entity
     | animation                | float                                 |   The animation index for the array
     | previousAnimation        | float                                 |   The previous animation index
     | animations               | java.util.HashMap<ISU.GameEngine.Util.directions,org.newdawn.slick.Image[]>   |  A hashmap of the array of images based on the direction the entity is moving in
     | attackAnimations         | java.util.HashMap<ISU.GameEngine.Util.directions,org.newdawn.slick.Image>     | The image to show when the entity attacks, based on the direction they're going
     | attack                   | boolean                               |   The boolean value to indicate whether or not the entity is currently attacking
     | attackTimeElapsed        | float                                 |   The time elapsed since the entity initiated the attack
     | player                   | ISU.GameEngine.Player.Player          |   The player object
     | health                   | float                                 |   The health of the entity
     | percentWidth             | float                                 |   The amount of pixels per point of health the entity has, used when drawing the health bar
     | blockedDirections        | java.util.ArrayList<ISU.GameEngine.Util.directions>  |  An arraylist of directions the entity cannot travel
     | attackDamage             | float                                 |   The amount of damage the entity does per second of attacking
     | topLeft/topRight/bottomLeft/bottomRight | ISU.GameEngine.Entity.Point2D  |   The points of the player, used to determine which directions are blocked
     | type                     | java.lang.String                      |   The type of entity it is
     | size                     | float                                 |   The size of the entity
     | debug                    | boolean                               |   Used to determine whether or not to print the debugging info
    ****************************************************************************************************************/
    private float serial;
    private Circle circle;
    protected float x,y,previousX,previousY;
    protected Timer timer;
    protected Util.directions currentDirection;
    protected TiledMap map;
    protected int tickDelay;
    protected float animation, previousAnimation;
    protected HashMap<Util.directions, Image[]> animations;
    protected HashMap<Util.directions, Image> attackAnimations;
    protected boolean attack = false;
    protected float attackTimeElasped;
    protected Player player;
    public float health;
    private float percentWidth;
    protected ArrayList<Util.directions> blockedDirections;
    public float attackDamage;
    private Point2D topLeft;
    private Point2D topRight;
    private Point2D bottomLeft;
    private Point2D bottomRight;
    private String type;
    protected float size;
    private boolean debug;

    /**
     *
     * @param x, x of the entity
     * @param y, y of the entity
     * @param health, health of the entity
     * @param attackDamage, damage done by the entity when attacking
     * @param map, the map used to check for collisions
     */
    protected Entity(float x, float y, float health, float attackDamage, TiledMap map, String type, float initialSize){
        // create the circle in the center of the entity, with a radius of (size*sizeMultiplier/2)
        circle = new Circle(x+(initialSize/2),y+(initialSize/2),(initialSize/2));
        // set and define all the variables
        this.x = x;
        this.y = y;
        previousX = x;
        previousY = y;
        tickDelay = 10;
        timer = new Timer();
        serial = Util.nextFloat();
        currentDirection = Util.directions.right;
        this.health = health;
        serial = Util.nextFloat();
        this.map = map;
        blockedDirections = new ArrayList<>();
        this.attackDamage = attackDamage;
        this.type = type;
        this.size = initialSize;
        // width of each health point, the width is the entities size plus 10 on each side is the width of the health bar
        updateHealthPercent(health);
        // set the corners of the entity
        topRight = new Point2D(getX()+initialSize,getY());
        topLeft = new Point2D(getX(), getY());
        bottomLeft = new Point2D(getX(), getY()+initialSize);
        bottomRight = new Point2D(getX()+initialSize, getY()+initialSize);
    }

    /**
     * @param maxHealth: divide the amount of pixels the health bar can take moveLess by make health to determine how many pixels each point of health will take moveLess
     */
    protected void updateHealthPercent(float maxHealth){
        // define the number of pixels for each point of health
        percentWidth=(size +20)/maxHealth;
    }

    /**
     * @param entity, entity to compare
     * @return whether or not the entity is equal to this one
     */
    private boolean equals(Entity entity){
        return serial == entity.serial;
    }

    /**
     * @param graphics, used to draw things to the screen
     */
    public abstract void render(Graphics graphics);

    /**
     * @param gameContainer, used to get user input, control display mode, exit, pause, etc.
     * @param entities, an arraylist of entities containing all the entities currently in the game
     */
    public abstract void update(GameContainer gameContainer, ArrayList<Entity> entities);

    // used to animate the entity, defined by the class that implements it
    public abstract void animate();

    // used to attack, defined by the class that implements it
    public abstract void attack();

    // update the circle, and the corners of the imaginary square of the entity
    protected void updateMainShapes(){
        // update the circle center and radius
        circle.setCenterX(x+(size /2));
        circle.setCenterY(y+(size /2));
        circle.setRadius(size /2);
        // set the Point2D of the top left,right and bottom left,right of the entity
        topLeft = new Point2D(getX()-5, getY()-5);
        topRight = new Point2D(getX()+ size -5,getY()-5);
        bottomLeft = new Point2D(getX()-5, getY()+ size -5);
        bottomRight = new Point2D(getX()+ size -5, getY()+ size -5);
    }

    /**
     * @return the circle of the entity used for collisions
     */
    public Circle getCircle(){
        return circle;
    }

    /**
     * @return: returns the x coordinate for the entities attack
     */
    protected float getXAttack(){
        // set the x of the attack image based on the direction
        if (currentDirection == Util.directions.right || currentDirection == Util.directions.down)
            return x+(size /2);
        else if (currentDirection == Util.directions.left)
            return x-(size /2);
        else
            return x;

    }

    /**
     * @return: returns the y coordinate for the entities attack
     */
    protected float getYAttack(){
        // set the y of the attack image based on the direction
        if (currentDirection == Util.directions.up)
            return y-(size /2);
        else
            return y+(size /2);
    }

    // getters and setters for x and y
    public float getX() {
        return x;
    }
    protected void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    protected void setY(float y) {
        this.y = y;
    }

    // reverts the entity's location and animation state. Usually used when entities collide
    protected void revert(){
        x = previousX;
        y = previousY;
        animation = previousAnimation;
    }

    // update the previous location so the entity can be reverted if need be
    public void updatePreviousLocation(){
        previousX = x;
        previousY = y;
    }

    /**
     * @param graphics, used to draw the health bar
     * Used to draw the health of the entity 10 pixels above their head
     */
    protected void displayHealth(Graphics graphics){
        graphics.fillRect(getX()-10, getY()-10, percentWidth*health, 2);
    }

    /**
     * @param entities
     * @return whether or not this entity has collided with anything or the position is a valid one
     */
    public boolean didCollide(ArrayList<Entity> entities){
        // if this entity collides with an object within the map, another entity, or hits the edge of the world return true
        return (didPlayerCollide(map.getLayerIndex("Objects"), entities)) || y < 0 || x < 0 || x + size > map.getWidth() * map.getTileWidth() || y + size > map.getHeight() * map.getTileHeight();
    }

    /**
     * @param entity, the entity that we are checking if this entity collided with
     * @return whether or not this entity collided with the entity passed in as a param
     */
    private boolean didCollideEntity(Entity entity){
        // if the entity passed in is this entity then there is no collision
        if (entity.serial == serial)
            return false;
        // get the entities circle
        Circle entityCircle = entity.getCircle();
        // these next 4 if statements determine which side the entities intersect, if they ever do and add it to the blocked directions
        // NOTE: my method for intersection treats the circle as a square when using the points to determine blocked directions
        if (topLeft.intersects(entityCircle) && topRight.intersects(entityCircle))
            blockedDirections.add(Util.directions.up);
        if (topLeft.intersects(entityCircle) && bottomLeft.intersects(entityCircle))
            blockedDirections.add(Util.directions.left);
        if (bottomRight.intersects(entityCircle) && topRight.intersects(entityCircle))
            blockedDirections.add(Util.directions.right);
        if (bottomRight.intersects(entityCircle) && bottomLeft.intersects(entityCircle))
            blockedDirections.add(Util.directions.down);

        // return whether the entity intersects this entity
        return entity.getCircle().intersects(new Circle(getX() + (size /2), getY() + (size /2), 12));
    }

    /**
     * @param layerID: the ID of the object layer of the map
     * @param map: the map itself
     * @param circle: the circle that we are checking for collision against
     * @return whether or not the circle has collided with the map
     */
    protected boolean didCollideMap(int layerID, TiledMap map, Shape circle) {
        // boolean array of the corners to check if they have been hit later
        boolean[] cornersHit = new boolean[4];
        boolean collision = false;
        // for each tile in the map
        for (int xTile = 0; xTile < map.getWidth(); xTile++) {
            for (int yTile = 0; yTile < map.getHeight(); yTile++) {
                // if the tile is not a transparent/non-existent tile
                if (map.getTileId(xTile, yTile, layerID) != 0) {
                    // if the circle intersects the tiles rectangle
                    if (circle.intersects(new Rectangle(xTile*map.getTileWidth(), yTile*map.getTileHeight(), map.getTileWidth(), map.getTileHeight()))) {
                        //check all the predefined points to see if they intersect with the tile to determine blocked directions later
                        if (topLeft.intersects(xTile * map.getTileWidth(), yTile * map.getTileHeight(), map.getTileWidth(), map.getTileHeight()))
                            cornersHit[0] = true;
                        if (topRight.intersects(xTile * map.getTileWidth(), yTile * map.getTileHeight(), map.getTileWidth(), map.getTileHeight()))
                            cornersHit[1] = true;
                        if (bottomLeft.intersects(xTile * map.getTileWidth(), yTile * map.getTileHeight(), map.getTileWidth(), map.getTileHeight()))
                            cornersHit[2] = true;
                        if (bottomRight.intersects(xTile * map.getTileWidth(), yTile * map.getTileHeight(), map.getTileWidth(), map.getTileHeight()))
                            cornersHit[3] = true;

                        // set collision true as we have collided
                        collision = true;
                    }
                }
            }
        }
        // determine what directions are blocked based on what corners are intersecting the obstacle
        if (cornersHit[0] && cornersHit[1])
            blockedDirections.add(Util.directions.up);
        if (cornersHit[2] && cornersHit[3])
            blockedDirections.add(Util.directions.down);
        if (cornersHit[0] && cornersHit[2])
            blockedDirections.add(Util.directions.left);
        if (cornersHit[1] && cornersHit[3])
            blockedDirections.add(Util.directions.right);
        // return if we have collided
        return collision;
    }

    /**
     * @return the shape that will be checked against other entities collision circles to handle damage
     */
    protected abstract Shape getAttackShape();

    /**
     * @return whether or not the entity is dead
     */
    public boolean isDead(){
        return health<=0;
    }

    /**
     * @param entities: all the entities in the game
     */
    public void handleAttacks(ArrayList<Entity> entities) {
        // if the entity is attacking
        if (attack) {
            // for all the entities in the game
            for (Entity currentEntity : entities){
                // if the current entity is not this object(so we don't damage our self)
                // and the attack shape is valid and intersects the entities collision circle
                // and the entity we are checking are not "allies" or of the same type
                if (!currentEntity.equals(this)&& getAttackShape()!=null&&currentEntity.getCircle().intersects(getAttackShape()) && !currentEntity.type.equals(type))
                    // attack enemy
                    currentEntity.health-=attackDamage;
            }
        }
    }

    /**
     * @param layerID: ID of the object layer of the map
     * @param entities: all the entities in the game
     * @return if this entity is in a valid position and is not colliding with anything
     */
    private boolean didPlayerCollide(int layerID, ArrayList<Entity> entities){
        // clear all blocked directions
        blockedDirections.clear();
        // map collision
        if (didCollideMap(layerID, map, getCircle()) && blockedDirections.contains(currentDirection)) {
            return true;
        }

        // collision with enemy
        if (enemyCollision(entities, type)) {
            return true;
        }

        // debugging, greatly needed
        if (debug) System.out.println(blockedDirections.toString());

        // if it has not alreay returned true then it has not collided with anything and return false
        return false;
    }

    /**
     * @param entities: all the entities in the game
     * @return all the enemies this entity intersects
     */
    private ArrayList<Entity> enemyCollision(ArrayList<Entity> entities){
        // declare the list for all the entities this entity has collided with
        ArrayList<Entity> collisionEntities = new ArrayList<>();
        // for each entity
        Iterator<Entity> entityIterator = entities.iterator();
        Entity entity;
        while (entityIterator.hasNext()) {
            entity = entityIterator.next();
            // if we collided with the entity and we are facing a blocked directions or there are no blocked directions
            if (didCollideEntity(entity) && (blockedDirections.contains(currentDirection) || blockedDirections.isEmpty()))
                // add the entity to collisions
                collisionEntities.add(entity);

        }
        // return the array of entities we have collided with
        return collisionEntities;
    }

    /**
     * @param entities: all the entities in the game
     * @param type: the type of this entity, eg. zombie, player, etc.
     * @return whether or not we have intersected with anyone that is not an ally
     */
    private boolean enemyCollision(ArrayList<Entity> entities, String type){
        // create a stream of the arraylist, filter the entities by their type, convert it to a list and check if it is empty
        return !enemyCollision(entities).stream().filter(entity -> !entity.type.equals(type)).collect(Collectors.toList()).isEmpty();
    }

    /**
     * @param entities: all the entities in the game
     * @return: an arraylist of all the allies this entity has collided against
     */
    protected List<Entity> allyCollision(ArrayList<Entity> entities){
        // get the enemies this entity has collided with, filter them by type, and convert it to a list
        return enemyCollision(entities).stream().filter(entity -> entity.type.equals(type)).collect(Collectors.toList());
    }

    // override of Point2D
    private static class Point2D extends java.awt.geom.Point2D.Double{

        // constructor just calls the actual Point2D class
        Point2D(double x, double y){super(x,y);}

        /**
         * @param x: the x coord of the shape
         * @param y: the y coord of the shape
         * @param width: the width of the shape
         * @param height: the height of the shape
         * @return whether or not the point is located within the x,y,width and height
         */
        boolean intersects(int x, int y, int width, int height){
            return (Util.inRange(x,x+width, (float) getX())&&Util.inRange(y, y+height, (float)getY()));
        }

        /**
         * @param shape: the shape to check if the point is within
         * @return whether or not the point is located within the shape
         */
        boolean intersects(Shape shape){
            return (Util.inRange(shape.getX(),shape.getX()+shape.getWidth(), (float) getX())&&Util.inRange(shape.getY(), shape.getY()+shape.getHeight(), (float)getY()));
        }

    }

}
