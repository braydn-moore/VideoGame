package ISU.GameEngine;


import ISU.Main;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/***********************************************************
Name: GameEngine
Due Date: January 2017
Purpose: Handle the state ID's so I can call them with variables so it's easier to understand my code
************************************************************/
public class GameEngine {

    // the state ID's
    public static final int menu = 0;
    public static final int game = 1;
    public static final int pause = 2;
    public static final int gameOver = 3;
    public static final int options = 4;
    public static String[] keyUsage = new String[]{"LEFT", "RIGHT", "UP", "DOWN", "FIREBALL", "SWORD ATTACK"};
    public static int[] keys = new int[]{ Input.KEY_A, Input.KEY_D, Input.KEY_W, Input.KEY_S, Input.KEY_SPACE, Input.KEY_ENTER};
    public static Image overlay;
    // colors
    public static final Color transparent = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    public static final Color red = new Color(1.0F, 0.0F, 0.0F, 0.5F);
    public static final Color blue = new Color(0.0F, 0.0F, 1.0F, 0.5F);
    public static final Color green = new Color(0.0F, 1.0F, 0.0F, 0.5F);
    public static final Color black = new Color(0.0F, 0.0F, 0.0F, 0.5F);
    public static final Color cyan = new Color(0.0F, 1.0F, 1.0F, 0.5F);
    public static final Color pink = new Color(255, 175, 175, 255);
    public static final Color orange = new Color(255, 200, 0, 0.5f);
    public static final Color magenta = new Color(255, 0, 255, 0.5f);

    /**
     * @param width: the width of the image
     * @param height: the height of the image
     * @throws SlickException
     */
    public static void  loadOverlay(int width, int height) throws SlickException{
        // load and scale the image
        overlay = new Image("ISU/resources/opaque.png");
        overlay = overlay.getScaledCopy(width, height);
    }


}
