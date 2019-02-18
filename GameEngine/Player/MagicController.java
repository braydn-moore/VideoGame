package ISU.GameEngine.Player;

import ISU.GameEngine.Entity;
import ISU.GameEngine.Player.Magic.FireBall;
import ISU.GameEngine.Player.Magic.MagicEntity;
import ISU.GameEngine.Util;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/***********************************************************
Name: MagicController
Due Date: January 2017
Purpose: Controls all magical entities in the game
************************************************************/
public class MagicController {

    // fields
    private ArrayList<MagicEntity> magic = new ArrayList<>();
    private Iterator<MagicEntity> entityIterator;
    private HashMap<Util.directions, Image[]> fireAnimations;
    private TiledMap map;

    /**
     * @param map: the map of the game
     */
    public MagicController(TiledMap map){
        // assigns variables and loads spritesheets and arrays
        this.map = map;
        fireAnimations = new HashMap<>();
        EntitySheet entitySheet;
        entitySheet = new EntitySheet("ISU/resources/fireLeft.png", 32,32);
        fireAnimations.put(Util.directions.left, entitySheet.getSpriteAnimationArrayHorizontal(0,192));
        entitySheet = new EntitySheet("ISU/resources/fireRight.png", 32,32);
        fireAnimations.put(Util.directions.right, entitySheet.getSpriteAnimationArrayHorizontal(0,192));
        entitySheet = new EntitySheet("ISU/resources/fireUp.png", 32,32);
        fireAnimations.put(Util.directions.up, entitySheet.getSpriteAnimationArrayVertical(0,192));
        entitySheet = new EntitySheet("ISU/resources/fireDown.png", 32,32);
        fireAnimations.put(Util.directions.down, entitySheet.getSpriteAnimationArrayVertical(0,192));
    }

    /**
     * @param gameContainer: used to pass the gameContainer into the entity update method
     * @param entities: all the entities in the game, used to pass into the update method
     * @param map: the map of the game
     */
    public void update(GameContainer gameContainer, ArrayList<Entity> entities, TiledMap map){
        // iterate over all magical entities
        entityIterator = magic.iterator();
        MagicEntity entityChecker;
        for (int counter = 0;  counter<magic.size(); counter++) {
            entityChecker = magic.get(counter);
            // update the entity
            entityChecker.update(gameContainer, entities);
            // if the magic entity is "dead" then remove it from the game
            if (entityChecker.isDead())
                magic.remove(counter);
        }
    }

    /**
     * @param graphics: used to draw things to the screen
     */
    public void render(Graphics graphics){
        // iterate over all magic entities and render them to the screen
        entityIterator = magic.iterator();
        while (entityIterator.hasNext())
            entityIterator.next().render(graphics);
    }

    /**
     * @param x: the x starting point of the fireball
     * @param y: the y starting point of the fireball
     * @param direction: the direction the fireball is travelling
     * @return the new fireball entity
     */
    public Entity addFire(float x, float y, Util.directions direction){
        // create the new fireball object
        FireBall newFireBall = new FireBall(x,y, direction, fireAnimations.get(direction), map);
        // add the fireball to the list of magical entities
        magic.add(newFireBall);
        // return the fireball
        return newFireBall;
    }

}
