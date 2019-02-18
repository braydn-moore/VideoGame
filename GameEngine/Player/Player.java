package ISU.GameEngine.Player;
import ISU.GameEngine.Entity;
import ISU.GameEngine.GameEngine;
import ISU.GameEngine.Timer;
import ISU.GameEngine.Util;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;

/***********************************************************
Name: Player
Due Date: January 2017
Purpose: The users avatar class
************************************************************/
public class Player extends Entity {

    // fields
    private Util.directions up;
    private Util.directions down;
    private Util.directions left;
    private Util.directions right;
    private Camera camera;
    public float mana;
    private MagicController magicController;
    private static final float velocity = 2;

    /**
     * @param startX: player starting x
     * @param startY: player starting y
     * @param imagePath: path to the spritesheet
     * @param map: the map
     * @param left: the key to move left
     * @param right: the key to move right
     * @param up: the key to move moveLess
     * @param down: the key to move movePositive
     * @param camera: the camera
     * @throws SlickException
     */
    public Player(int startX, int startY, String imagePath, TiledMap map, int left, int right, int up, int down, Camera camera) throws SlickException{
        // entity constructor
        super(startX, startY, 250, 1.8f, map, "player", 32);
        // initialize all the variables
        this.right = Util.directions.right;
        this.left = Util.directions.left;
        this.up = Util.directions.up;
        this.down = Util.directions.down;
        this.x = startX;
        this.y = startY;
        mana = 100f;
        this.camera = camera;
        // creates the spritesheets for animations
        EntitySheet spriteSheet = new EntitySheet(imagePath, 32, 32);
        this.animation = 0;
        this.map = map;
        this.animations = new HashMap<Util.directions, Image[]>(){{
            put(Util.directions.down,spriteSheet.getSpriteAnimationArrayHorizontal(0,96));
            put(Util.directions.left, spriteSheet.getSpriteAnimationArrayHorizontal(1,96));
            put(Util.directions.right, spriteSheet.getSpriteAnimationArrayHorizontal(2,96));
            put(Util.directions.up, spriteSheet.getSpriteAnimationArrayHorizontal(3,96));
        }};
        this.attackAnimations = new HashMap<Util.directions, Image>(){{
            put(Util.directions.down,new Image("ISU/resources/swordDown.png"));
            put(Util.directions.left, new Image("ISU/resources/swordLeft.png"));
            put(Util.directions.right, new Image("ISU/resources/swordRight.png"));
            put(Util.directions.up, new Image("ISU/resources/swordForward.png"));
        }};
        this.timer = new Timer();
        magicController = new MagicController(map);
    }

    /**
     * @param gameContainer, used to get user input, control display mode, exit, pause, etc.
     * @param entities, an arraylist of entities containing all the entities currently in the game
     */
    @Override
    public void update(GameContainer gameContainer, ArrayList<Entity> entities){
        // get user input
        Input keyPress = gameContainer.getInput();
        // move the player according to key input
        movement(keyPress, entities);
        // if there is a collision then revert the player to previous position
        if (didCollide(entities)) revert();
        // update the collision circle
        updateMainShapes();
        // update the magic spells, aka fire
        magicController.update(gameContainer, entities, map);
        // if the mana is less than max, then add steadily increase the mana
        if (mana<100)
            mana+=0.1;

        // if the user has entered to attack
        if (attack) {
            // time since attack
            attackTimeElasped+=timer.getElapsedTime();
            // if the time since attack is more than 0.5 seconds allow the user to attack again
            if (attackTimeElasped > 0.5)
                attack = false;
        }
    }

    /**
     * @param camera: the camera object to set the field
     */
    public void setCamera(Camera camera){this.camera = camera;}

    /**
     * @param graphics: used to draw things to the screen
     */
    private void renderAttacks(Graphics graphics){
        // if the user has chosen to attack and it is within 0.2 seconds of the user pressing <ENTER> then draw the animation
        if (attack && attackTimeElasped<0.2) {
            graphics.drawImage(attackAnimations.get(currentDirection), getXAttack(), getYAttack());
        }
    }

    @Override
    public void attack(){
        // set attack to true
        attack = true;
        // reset all the time keeping variables
        timer.reset();
        attackTimeElasped = 0;
    }

    /**
     * @param graphics, used to draw things to the screen
     */
    @Override
    public void render(Graphics graphics){
        // if the user is facing moveLess
        if (currentDirection!=up) {
            // draw the character and then the sword
            if (animations.get(currentDirection) != null && animation < 3)
                graphics.drawImage(animations.get(currentDirection)[(int)animation], x, y);
            if (attack) {
                renderAttacks(graphics);
            }
        }
        // otherwise draw the sword and then the character
        else{
            if (attack)
                renderAttacks(graphics);
            if (animations.get(currentDirection) != null && animation < 3)
                graphics.drawImage(animations.get(currentDirection)[(int)animation], x, y);
        }
        // render all the magic objects
        magicController.render(graphics);
        // set the color to be red
        graphics.setColor(Color.red);
        // draw the health of the player
        displayHealth(graphics);
        // draw the indicators for the mana and health
        graphics.drawString("Mana: "+(int)mana, 10-camera.getXTranslate(),Main.height-50-camera.getYTranslate());
        graphics.drawString("Health: "+(int)health, 30+graphics.getFont().getWidth("Mana: "+(int)mana)-camera.getXTranslate(),Main.height-50-camera.getYTranslate());
        // reset the color for later
        graphics.setColor(Color.white);

    }

    /**
     * @param keyPress: the input that has been taken from the main game container
     * @param entities: all the entities in the game
     */
    private void movement(Input keyPress, ArrayList<Entity> entities){
        // sets the previous x, y and animation in case the player has to be reverted
        previousX = x;
        previousY =y;
        previousAnimation = animation;

        if (keyPress.isKeyPressed(Input.KEY_F5))
            System.out.println(blockedDirections.toString());

        // if the user presses <ENTER> and they can attack
        if (keyPress.isKeyPressed(GameEngine.keys[5]) && !attack) {
            attack();
        }

        // if the user presses <SPACE> and they have the required mana cast the fireball
        else if (keyPress.isKeyPressed(GameEngine.keys[4]) && mana>10) {
            entities.add(magicController.addFire(x, y, currentDirection));
            mana-=10;
        }

        // move the player based on the keys they press
        if (keyPress.isKeyDown(GameEngine.keys[3])) {
            y += velocity;
            currentDirection = down;
        }
        else if (keyPress.isKeyDown(GameEngine.keys[2])) {
            y -= velocity;
            currentDirection = up;
        }
        else if (keyPress.isKeyDown(GameEngine.keys[0])) {
            x -= velocity;
            currentDirection = left;
        }
        else if (keyPress.isKeyDown(GameEngine.keys[1])) {
            x += velocity;
            currentDirection = right;
        }
        // if no key is pressed reverse the animation of the next if statement so it does not constantly animate
        else
            animation-=(float) 1/tickDelay;

        // animate the character
        animate();
    }

    @Override
    public void animate(){
        // animate the character
       animation+=(float) 1/tickDelay;

        // if the animations exceeds the length of the array then reset it
        if (animation>animations.get(currentDirection).length)
            animation = 0;
    }

    /**
     * @return the attack shape to check if entities collide to damage them
     */
    public Rectangle getAttackShape(){
        // if the player attacks return the rectangle of the players sword
        if (attack)
            return new Rectangle(getXAttack(), getYAttack(), attackAnimations.get(currentDirection).getWidth(), attackAnimations.get(currentDirection).getHeight());
        // otherwise return a null pointer
        return null;
    }

}
