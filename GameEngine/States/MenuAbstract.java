package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/***********************************************************
Name: MenuAbstract
Due Date: January 2017
Purpose: The abstract class that all menus extends, contains
         basic functions such as handling keypresses, mouse input, etc.
************************************************************/
abstract class MenuAbstract extends BasicGameState {

    // resources needed (fairly self explanatory)
    int playersChoice = 0;
    int numChoices;
    String[] playersOptions;
    java.awt.Font font;
    TrueTypeFont typeFont;
    org.newdawn.slick.Color notSelected = new org.newdawn.slick.Color(212,175,55);
    Game game;
    // used to dictate whether to use the horizontal or vertical keylayout when handling keypresses
    private boolean horizontal = false;

    /**
     * @param horizontal: set the horizontal value
     */
    void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    /**
     * @param gameContainer: needed to override the init method in the BasicGameState class
     * @param stateBasedGame: needed to override the init method in the BasicGameState class
     * @throws SlickException
     */
    @Override
    public abstract void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException;

    /**
     * @param gameContainer: used to get input from the user
     * @param stateBasedGame: used to enter/exit/add states
     * @param delta: used to override method in BasicGameState, however it denotes the amount of time since the update method was called
     * @throws SlickException
     */
    @Override
    public abstract void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException;

    /**
     * @param gameContainer: needed to pass into the nextState method
     * @param stateBasedGame: needed to pass into the nextState method
     * @param input: used to check user input
     * @throws SlickException
     */
    void mouseInput(GameContainer gameContainer, StateBasedGame stateBasedGame, Input input) throws SlickException{
        for (int i = 0; i < numChoices; i++) {
            // if the user choice is currently this option then adjust the rectangle to check for input and check if the user has clicked the button and if so enter the next state
            if (playersChoice == i && new org.newdawn.slick.geom.Rectangle(50, (float) (i * 50 + (Main.height - (numChoices * 60))), typeFont.getWidth(playersOptions[i])+50, typeFont.getHeight(playersOptions[i])).contains(input.getMouseX(), input.getMouseY()) && input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
                nextState(gameContainer, stateBasedGame);
                // otherwise if the users cursor is over a different option than previously thought then set their current "choice" to the choice their cursor is over
            else if (new org.newdawn.slick.geom.Rectangle(100, (float) (i * 50 + (Main.height - (numChoices * 60))), typeFont.getWidth(playersOptions[i]), typeFont.getHeight(playersOptions[i])).contains(input.getMouseX(), input.getMouseY()))
                playersChoice = i;
            // if the user clicks their mouse enter the next state
            //if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
            //    nextState(gameContainer, stateBasedGame);
        }
    }

    /**
     * @param gameContainer: needed to pass into the nextState method
     * @param stateBasedGame: needed to pass into the nextState method
     * @param input: used to check user input
     * @throws SlickException
     */
    void keyPress(GameContainer gameContainer, StateBasedGame stateBasedGame, Input input) throws SlickException{
        // if the user pressed the key for movePositive
        if (input.isKeyPressed(horizontal?GameEngine.keys[0]:GameEngine.keys[3])) {
            // if the choice will is exceeding the bounds of the array then reset it
            if (playersChoice == (numChoices - 1))
                playersChoice = 0;
                // otherwise move the selection movePositive
            else
                playersChoice++;
        }
        if (input.isKeyPressed(horizontal?GameEngine.keys[1]:GameEngine.keys[2])) {
            // if the players choice is at the top then reset it
            if (playersChoice == 0)
                playersChoice = numChoices - 1;
                // otherwise move the players choice moveLess
            else
                playersChoice--;
        }

        // if the user presses enter
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            nextState(gameContainer, stateBasedGame);
        }
    }

    /**
     * @param gameContainer: used to initialize the new states
     * @param stateBasedGame: used to add the new states
     * @throws SlickException
     */
    protected abstract void nextState(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException;

    /**
     * @param gameContainer: needed to override the init method in the BasicGameStateClass
     * @param stateBasedGame: needed to override the init method in the BasicGameStateClass
     * @param graphics: used to draw things to the screen and well, render
     * @throws SlickException
     */
    @Override
    public abstract void render(GameContainer gameContainer, StateBasedGame stateBasedGame, org.newdawn.slick.Graphics graphics) throws SlickException;

    void renderPlayersOptions() {
        // for all the user options
        for (int i = 0; i < numChoices; i++) {
            // if the user choice is currently this option then draw it in black and slightly to the left
            if (playersChoice == i)
                typeFont.drawString(50, (float)(i * 50 + (Main.height-(numChoices*60))), playersOptions[i], org.newdawn.slick.Color.black);
                // otherwise draw the text normally
            else
                typeFont.drawString(100, (float)(i * 50 + (Main.height-(numChoices*60))), playersOptions[i], notSelected);
        }
    }

}
