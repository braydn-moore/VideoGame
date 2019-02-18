package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.GameEngine.Player.Camera;
import ISU.GameEngine.AI.Zombie;
import ISU.GameEngine.Entity;
import ISU.GameEngine.Player.Player;
import ISU.GameEngine.Util;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Iterator;

/***********************************************************
Name: Game
Due Date: January 2017
Purpose: The main game class that handles the logic, rendering and everything else in-between
************************************************************/
public class Game extends BasicGameState {

    // ID to be accessed by the game engine later
    int ID = 1;
    // variables regarding map, player and the entities within the game
    private TiledMap map;
    private Player player;
    private ArrayList<Entity> entities;
    public Camera camera;
    private int round;

    // gets the ID
    public int getID(){
        return ID;
    }

    /**
     * @param gameContainer: used to clear the input when entering this state
     * @param stateBasedGame: used to override the method in BasicStateGame
     */
    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame){
        // clears the input keys so it doesn't register the keys pressed in the previous state
        gameContainer.getInput().clearKeyPressedRecord();
    }

    private void addEnemy() throws SlickException{
        // declare the zombie
        Zombie zombie;
        // infinitely
        while (true){
            // generate a new zombie with a random x, y coordinates
            zombie = new Zombie(Util.randomFloat(64,map.getHeight()*map.getTileHeight()-64),Util.randomFloat(64,map.getHeight()*map.getTileHeight()-64),player, map, 1);
            // exit out of the loop if the new zombie doesn't collide with any other entity
            if (!zombie.didCollide(entities))
                break;
        }
        // add the new zombie to the entities arraylist
        entities.add(0, zombie);
    }

    /**
     * @param gameContainer: the main game container, in this method it isn't used however it is needed to override the method in abstract class this class extends
     * @param stateBasedGame: used to add new game states
     * @throws SlickException
     */
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{
        // creation of entities, map, and player
        entities = new ArrayList<>();
        map = new TiledMap("ISU/resources/dungeonArena.tmx");
        player = new Player(0, 0, "ISU/resources/me.png", map, Input.KEY_A, Input.KEY_D, Input.KEY_W, Input.KEY_S, camera);
        entities.add(player);
        // creates the camera to move with the player
        camera = new Camera(map, player);
        Util.setCamera(camera);
        // set the players camers
        player.setCamera(camera);
        // load the overlay for the pause and game over menu
        GameEngine.loadOverlay(map.getHeight()*map.getTileHeight(), map.getWidth()*map.getTileWidth());
        // create and add the pause state
        BasicGameState pauseState = new Pause(this);
        GameOver gameOverState = new GameOver();
        pauseState.init(gameContainer, stateBasedGame);
        gameOverState.init(this);
        stateBasedGame.addState(pauseState);
        stateBasedGame.addState(gameOverState);
        // set the round to zero as the game will automatically detect there are no enemies and enter the new round method which adds
        // one to the round
        round=0;
    }

    /**
     * @param graphics: used to draw things to the screen
     */
    public void debug(Graphics graphics){
        // set the color to red
        graphics.setColor(Color.red);
        // for each entity draw their collision circle around them
        Iterator<Entity> entityIterator = entities.iterator();
        while (entityIterator.hasNext()){
            graphics.draw(entityIterator.next().getCircle());
        }
    }

    /**
     * @param gameContainer: simply there to override the method in the abstract class
     * @param stateBasedGame: simply there to override the method in the abstract class
     * @param graphics: used to draw entities to the screen
     * @throws SlickException
     */
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.setBackground(Color.black);
        // translate the camera
        camera.translate(graphics);
        // render the map
        map.render(0, 0);
        // render all the entities
        Iterator<Entity> entityIterator = entities.iterator();
        while (entityIterator.hasNext())
            entityIterator.next().render(graphics);
        // draw the round number in the top right corner
        graphics.drawString("Round: "+round, Main.width-graphics.getFont().getWidth("Round: "+round)-camera.getXTranslate()-10, 0-camera.getYTranslate());
    }

    private void newRound() throws SlickException{
        // next round
        round++;
        // add new enemies for the next round
        for (int counter = 0; counter<round*Math.ceil((double)round/2); counter++)
            addEnemy();
        // reset the player health and mana
        player.health = 250;
        player.mana = 100;
    }

    /**
     * @param gameContainer: used to get user input
     * @param stateBasedGame: used to translate to pause menu and update entities
     * @param delta: time since last update
     * @throws SlickException
     */
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        // grab user input
        Input keyPress = gameContainer.getInput();
        // change screen mode if requested
        if (keyPress.isKeyPressed(Input.KEY_ESCAPE))
            Main.changeMode();

        // pause the game and enter the pause menu state
        else if (keyPress.isKeyPressed(Input.KEY_P)) {
            gameContainer.setPaused(!gameContainer.isPaused());
            stateBasedGame.enterState(GameEngine.pause);
        }

        if (!gameContainer.isPaused()) {
            // declare all variables before the loop
            Entity currentEntity;
            boolean allEnemiesDead = true;
            // for all entities
            for (int counter = 0; counter < entities.size(); counter++) {
                // update the entity and check if they're dead or alive
                currentEntity = entities.get(counter);
                currentEntity.update(gameContainer, entities);
                if (currentEntity.isDead())
                    entities.remove(counter);
                // handle enemy attacks
                currentEntity.handleAttacks(entities);
                // if this entity is of the Zombie class then all the enemies are not dead
                if (currentEntity.getClass() == Zombie.class)
                    allEnemiesDead = false;
            }
            // if all the enemies are dead, start a new round
            if (allEnemiesDead)
                newRound();
        }
        // if the player is dead
        if (player.isDead())
            // enter the game over phase
            stateBasedGame.enterState(GameEngine.gameOver);
    }

}
