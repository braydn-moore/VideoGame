package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.GameEngine.Util;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.awt.Font;

/***********************************************************
Name: GameOver
Due Date: January 2017
Purpose: The game over stage at the end of the game when the user dies
         to ask if they want to play again or exit
************************************************************/
public class GameOver extends MenuAbstract {

    private int ID = 3;
    private TrueTypeFont gameOverFont, boldFont;

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
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame){}

    /**
     * @param game: used to render the game screen as the background
     * @throws SlickException
     */
    public void init(Game game) throws SlickException{
        this.game = game;
        // creates the font to draw the text to the window
        font = new java.awt.Font("Times New Roman", Font.PLAIN, 25);
        typeFont = new TrueTypeFont(font, true);
        font = new java.awt.Font("Times New Roman", Font.BOLD, 25);
        boldFont = new TrueTypeFont(font, true);
        gameOverFont = new TrueTypeFont(new Font("Chiller", Font.BOLD, 60), true);
        // creates and array with all the players options
        numChoices = 2;
        playersOptions = new String[numChoices];
        playersOptions[0] = "PLAY AGAIN";
        playersOptions[1] = "QUIT";
        // sets the keys for movement to be horizontal, not vertical
        setHorizontal(true);
    }

    /**
     * @param gameContainer: needed to override the init method in the BasicGameStateClass
     * @param stateBasedGame: needed to override the init method in the BasicGameStateClass
     * @param graphics: used to draw things to the screen and well, render
     * @throws SlickException
     */
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException{
        game.render(gameContainer, stateBasedGame, graphics);
        graphics.drawImage(GameEngine.overlay, 0,0);
        gameOverFont.drawString(200, 200, "GAME OVER");
        boldFont.drawString(playersChoice==0?Util.getPerspectiveValue(200,true,false):Util.getPerspectiveValue(225+boldFont.getWidth(playersOptions[0]), true, false), Util.getPerspectiveValue(200+gameOverFont.getHeight("GAME OVER"), false, true), playersOptions[playersChoice], Color.red);
        typeFont.drawString(playersChoice!=0?Util.getPerspectiveValue(200,true,false):Util.getPerspectiveValue(225+boldFont.getWidth(playersOptions[0]), true, false), Util.getPerspectiveValue(200+gameOverFont.getHeight("GAME OVER"), false, true), playersOptions[Math.abs(playersChoice-1)]);
    }

    /**
     * @param gameContainer: used to get input from the user
     * @param stateBasedGame: used to enter/exit/add states
     * @param delta: used to override method in BasicGameState, however it denotes the amount of time since the update method was called
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException{
        Input input = gameContainer.getInput();
        // change screen mode if requested
        if (input.isKeyPressed(Input.KEY_ESCAPE))
            Main.changeMode();
        keyPress(gameContainer, stateBasedGame, input);
    }

    /**
     * @param gameContainer: used to initialize the new states
     * @param stateBasedGame: used to add the new states
     * @throws SlickException
     */
    @Override
    public void nextState(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{
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
        if (playersOptions[playersChoice].equalsIgnoreCase("play again")) {
            // reset the game
            gameContainer.setPaused(false);
            gameContainer.getInput().clearKeyPressedRecord();
            stateBasedGame.getState(GameEngine.game).init(gameContainer, stateBasedGame);
            // enter the game state
            stateBasedGame.enterState(GameEngine.game);
        }
    }



}
