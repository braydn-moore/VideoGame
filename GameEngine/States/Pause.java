package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.awt.Font;

/***********************************************************
Name: Pause
Due Date: January 2017
Purpose: The pause menu when the user wishes to pause the game
************************************************************/


public class Pause extends MenuAbstract {

    private int ID = 2;
    private Game game;

    // gets the ID for the game engine
    @Override
    public int getID(){
        return ID;
    }

    /**
     * @param gameContainer: needed to override the init method in the BasicGameState class
     * @param stateBasedGame: needed to override the init method in the BasicGameState class
     */
    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame){
        // creates the font to draw the text to the window
        font = new Font("Times New Roman", Font.BOLD, 40);
        typeFont = new TrueTypeFont(font, true);
        // creates and array with all the players options
        numChoices = 5;
        playersOptions = new String[numChoices];
        playersOptions[0] = "Resume";
        playersOptions[1] = "Save";
        playersOptions[2] = "Load";
        playersOptions[3] = "Options";
        playersOptions[4] = "Quit";
    }

    /**
     * @param gameState,  the current game
     */
    // the game object must be stored as it is used to render the game window to overlay
    public Pause(Game gameState){
        this.game = gameState;
    }

    /**
     * @param gameContainer: used to get input from the user
     * @param stateBasedGame: used to enter/exit/add states
     * @param delta: used to override method in BasicGameState, however it denotes the amount of time since the update method was called
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        // gets the user input
        Input input = gameContainer.getInput();
        // change screen mode if requested
        if (input.isKeyPressed(Input.KEY_ESCAPE))
            Main.changeMode();
        // check for keypress and mouse input
        keyPress(gameContainer, stateBasedGame, input);
        mouseInput(gameContainer, stateBasedGame, input);
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

    /**
     * @param gameContainer: used to pass into the game rendering function
     * @param stateBasedGame: used to pass into the game rendering function
     * @param graphics: used to draw images to the screen
     * @returns void
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        // draw the current game background
        game.render(gameContainer, stateBasedGame, graphics);
        // draws the slightly dark grey overlay over the game image
        graphics.drawImage(GameEngine.overlay,0,0);
        // draws the text options
        renderPlayersOptions();
    }

    @Override
    public void renderPlayersOptions() {
        // for each option the player has
        for (int i = 0; i < playersOptions.length; i++) {
            // if the current option is the one the player is on
            if (playersChoice == i)
                // draw the string slightly to the right accounting for the shift in the map position when the camera tracks the player
                typeFont.drawString(50-game.camera.getXTranslate(), (float)(i * 50 + (Main.height-(playersOptions.length*60)))-game.camera.getYTranslate(), playersOptions[i], Color.black);
            // otherwise draw the string normally
            else
                typeFont.drawString(100-game.camera.getXTranslate(), (float)(i * 50 + (Main.height-(playersOptions.length*60)))-game.camera.getYTranslate(), playersOptions[i], notSelected);

        }
    }

    // if the user has pressed a button continue to the next state
    /**
     * @param gameContainer: used to clear the input before entering a new state, so it doesn't register the input
     * @param stateBasedGame: used to enter and initialize the next state
     * @throws SlickException
     */
    public void nextState(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        // based on the players choice
        // if the user has chosen to quit
        if (playersOptions[playersChoice].equalsIgnoreCase("quit")) {
            // clear the input and un-pause the game
            gameContainer.getInput().clearKeyPressedRecord();
            gameContainer.setPaused(false);
            // re-initialize the game so it restarts if the user wishes to exit the game
            stateBasedGame.getState(GameEngine.game).init(gameContainer, stateBasedGame);
            // enter the menu with a fade-out to fade-in transition
            stateBasedGame.enterState(GameEngine.menu, new FadeOutTransition(), new FadeInTransition());
        }
        // if the user has chosen to resume
        else if (playersOptions[playersChoice].equalsIgnoreCase("resume")) {
            // clear the keys and un-pause
            gameContainer.getInput().clearKeyPressedRecord();
            gameContainer.setPaused(false);
            // enter the game state
            stateBasedGame.enterState(GameEngine.game);
        }
        // if the user has chosen to enter the options screen then enter the options screen
        else if (playersOptions[playersChoice].equalsIgnoreCase("options")){
            // clear the keys pressed
            gameContainer.getInput().clearKeyPressedRecord();
            // set the previous state id to return to this after the options screen has been open
            ((Options)stateBasedGame.getState(GameEngine.options)).setPreviousStateID(ID);
            // enter the options state
            stateBasedGame.enterState(GameEngine.options);
        }
    }
}
