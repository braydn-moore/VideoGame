package ISU.GameEngine.Player.Magic;

import ISU.GameEngine.Entity;
import org.newdawn.slick.tiled.TiledMap;

/***********************************************************
Name: MagicEntity
Due Date: January 2017
Purpose: An abstract class that implements the Entity class, used for all magic
         entities, such as fireballs
************************************************************/
public abstract class MagicEntity extends Entity {

    // calls the constructor for the entity class
    MagicEntity(float x, float y, TiledMap map, String type, float initialSize, float damage, float sizeMultiplier){
        super(x,y,1, damage, map, type, initialSize);
    }

    // abstract class for isDead to allow for removal of dead sprites in the main game loop
    public abstract boolean isDead();

}
