package ISU.GameEngine.States;

import ISU.GameEngine.GameEngine;
import ISU.Main;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.util.stream.IntStream;

/***********************************************************
Name: Options
Due Date: January 2017
Purpose: The options page for the user to change the key bindings
************************************************************/
public class Options extends MenuAbstract {

    // field variables
    private int ID = 4;
    private int previousStateID = 4;
    private boolean changeKey = false;
    private int keyToChange;
    private TrueTypeFont infoFont;

    @Override
    public int getID() {
        return ID;
    }

    /**
     * @param previousStateID, used to return to the previous state
     */
    public Options(int previousStateID){
        this.previousStateID = previousStateID;
        // load the required font
        font = new Font("Times New Roman", Font.PLAIN, 30);
        typeFont = new TrueTypeFont(font, true);
        font = new Font("Arial", Font.BOLD, 15);
        infoFont = new TrueTypeFont(font, true);
    }

    /**
     * @param gameContainer: needed to override the init method in the BasicGameState class
     * @param stateBasedGame: needed to override the init method in the BasicGameState class
     */
    @Override
    // this method is just to extend the MenuAbstract class without an error
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame){}

    // allows for the updating of the stage to return to
    public void setPreviousStateID(int previousStateID){this.previousStateID = previousStateID;}

    /**
     * @param gameContainer: needed to override the init method in the BasicGameStateClass
     * @param stateBasedGame: needed to override the init method in the BasicGameStateClass
     * @param graphics: used to draw things to the screen and well, render
     */
    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        graphics.setBackground(new Color(95, 158, 160));
        for (int counter = 0; counter < GameEngine.keyUsage.length; counter++) {
            if (changeKey && counter == keyToChange)
                typeFont.drawString(100, 100+counter*50, GameEngine.keyUsage[counter]+":\t\t\t" + Input.getKeyName(GameEngine.keys[counter]), Color.red);
            else
                typeFont.drawString(100, 100+counter*50, GameEngine.keyUsage[counter]+":\t\t\t" + Input.getKeyName(GameEngine.keys[counter]));
        }
        infoFont.drawString((Main.width-infoFont.getWidth("***To exit the options menu press <BACKSPACE>***"))/2, 125+GameEngine.keyUsage.length*50, "***To exit the options menu press <BACKSPACE>***", Color.black);
        infoFont.drawString((Main.width-infoFont.getWidth("***To change a key, press the current keybinding and then the new key***"))/2, 150+GameEngine.keyUsage.length*50, "***To change a key, press the current keybinding and then the new key***", Color.black);
    }

    /**
     * @param gameContainer: used to override the method in the abstract class
     * @param stateBasedGame: used to enter the next states
     * @throws SlickException
     */
    @Override
    public void nextState(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException{
        stateBasedGame.enterState(previousStateID);
    }

    /**
     * @param gameContainer, used to check the input of the user
     * @return the key the user pressed, if any
     */
    private int returnKeyPressed(GameContainer gameContainer){
        // get the input variable
        Input input = gameContainer.getInput();
        // for each potential key code
        for (int counter = 1; counter<=223; counter++)
            // if the key is pressed
            if (input.isKeyPressed(counter))
                // return the key
                return counter;
        // otherwise return -1 which symbolizes no key is pressed
        return -1;
    }

    /**
     * @param gameContainer: used to get input from the user
     * @param stateBasedGame: used to enter/exit/add states
     * @param delta: used to override method in BasicGameState, however it denotes the amount of time since the update method was called
     * @throws SlickException
     */
    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        // if the user wishes to return to the previous state then return to the previous state
        if (gameContainer.getInput().isKeyPressed(Input.KEY_BACK))
            // call the next state
            nextState(gameContainer, stateBasedGame);
        // change screen mode if requested
        if (gameContainer.getInput().isKeyPressed(Input.KEY_ESCAPE))
            Main.changeMode();
        // basically the equivalent of goto
        checkKey:
        {
            // if there is a key selected to change
            if (changeKey) {
                // get the key the user pressed
                Integer newKey = returnKeyPressed(gameContainer);
                // if the user didn't press a key or the key they pressed is already a binded key then exit and continue through the method
                if (newKey == -1 || IntStream.of(GameEngine.keys).anyMatch(key -> key == newKey))
                    break checkKey;
                // otherwise if everything is valid then set the new key
                GameEngine.keys[keyToChange] = newKey;
                // set the changeKey variable to false so the keybindings are not constantly updating
                changeKey = false;
                // then reset the keyToChangeVariable
                keyToChange = -1;
            }
        }

        // for each keybinding
        for (int key = 0; key < GameEngine.keys.length && !changeKey; key++)
            // if the key is pressed
            if (gameContainer.getInput().isKeyPressed(GameEngine.keys[key])) {
                // set the variables so in the next update loop the user can update the keybindings
                changeKey = true;
                keyToChange = key;
            }

        // if changeKey is false then reset the keys pressed so it doesn't automatically trigger the checkKey scope when update is called again
        if (!changeKey)
            gameContainer.getInput().clearKeyPressedRecord();
    }


}
