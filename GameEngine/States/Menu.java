package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.awt.Font;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/***********************************************************
 Name: Menu
 Due Date: January 2017
 Purpose: The main menu the user enters when they first run the game
 ************************************************************/
public class Menu extends MenuAbstract {

    private int ID = 0;

    // method to return the ID as it is private
    public int getID(){
        return ID;
    }

    private Image background;
    private Game game;
    private boolean start;
    // used to load the resources for the game without delay/the window stop responding
    private DeferredResource resourceToLoad;

    /**
     * @param gameContainer: needed to override the init method in the BasicGameState class
     * @param stateBasedGame: needed to override the init method in the BasicGameState class
     * @throws SlickException
     */
    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        // creates the font
        font = new Font("Times New Roman", Font.BOLD, 40);
        typeFont = new TrueTypeFont(font, true);
        // sets the user options
        numChoices = 5;
        playersOptions = new String[numChoices];
        playersOptions[0] = "Start";
        playersOptions[1] = "Save";
        playersOptions[2] = "Load";
        playersOptions[3] = "Options";
        playersOptions[4] = "Quit";
        // imports the background
        background = new Image("ISU/resources/background.jpg");
        // creates a scaled copy for the sizeMultiplier of the game
        background = background.getScaledCopy(Main.width, Main.height);
        stateBasedGame.addState(new Options(ID));
    }

    /**
     * @param gameContainer: used to get input from the user
     * @param stateBasedGame: used to enter/exit/add states
     * @param delta: used to override method in BasicGameState, however it denotes the amount of time since the update method was called
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        // change screen mode if requested
        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE))
            Main.changeMode();
        // if there are resources that need to be loaded(for the game when it starts)
        if (LoadingList.get().getRemainingResources()>0)
            // get the next resource to load
            resourceToLoad = LoadingList.get().getNext();
            // if the user has requested to start and all resources have been loaded then start the game
        else if (start)
            stateBasedGame.enterState(GameEngine.game, new FadeOutTransition(), new FadeInTransition());

        // if the resource is not null(it never should be, this is just a precaution)
        if (resourceToLoad!=null && LoadingList.get().getRemainingResources()>0) {
            // attempt to load the resource
            try {
                System.out.println("Loading "+resourceToLoad.getDescription());
                resourceToLoad.load();
            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Failed to load " + resourceToLoad.getDescription());
            }
        }

        // store the input in a variable
        Input input = gameContainer.getInput();
        //detect keypresses
        keyPress(gameContainer, stateBasedGame, input);
        //detect mouse input;
        mouseInput(gameContainer, stateBasedGame, input);

    }


    /**
     * @param gameContainer: used to initialize the new states
     * @param stateBasedGame: used to add the new states
     * @throws SlickException
     */
    public void nextState(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{

        // if the user chooses to quit then quit
        if (playersOptions[playersChoice].equalsIgnoreCase("quit"))
            gameContainer.exit();
        // if the user chooses to start make the game and add it to the possible states of the game and begin the loading of resources
        else if (playersOptions[playersChoice].equalsIgnoreCase("start")) {
            if (game == null) {
                game = new Game();
                stateBasedGame.addState(game);
                // NOTE: the init function inherited by a BasicStateGame will only be called when it add the state
                //       in the main entrance function for whatever reason, this would cause all the resources to be loaded at the same time
                //       so I must call it on my own
                game.init(gameContainer, stateBasedGame);
            }
            // once all the resources have loaded enter the game state(this code is found in the update function)
            start = true;
        }
        // if user wants to enter the options phase
        else if (playersOptions[playersChoice].equalsIgnoreCase("options"))
            // enter the options phase
            stateBasedGame.enterState(GameEngine.options, new FadeOutTransition(), new FadeInTransition());
    }

    /**
     * @param gameContainer: used to clear the input when entering this state
     * @param stateBasedGame: used to override the method in BasicStateGame
     */
    @Override
    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame){
        // clears the input keys so it doesn't register the keys pressed in the previous state
        gameContainer.getInput().clearKeyPressedRecord();
        // reset the start variable
        start = false;
    }

    /**
     * @param gameContainer: needed to override the init method in the BasicGameStateClass
     * @param stateBasedGame: needed to override the init method in the BasicGameStateClass
     * @param graphics: used to draw things to the screen and well, render
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        // draw the background image
        graphics.drawImage(background, 0, 0);
        // render the text options for the player
        renderPlayersOptions();
    }

}
