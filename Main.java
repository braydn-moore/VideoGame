package ISU;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ISU.GameEngine.States.Menu;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.StateBasedGame;

// Main Game Class
public class Main extends StateBasedGame {

    // static variables to be accessed later for window sizeMultiplier
    public static int height;
    public static int width;
    // the game container
    private static AppGameContainer appGameContainer;

    // constructor
    private Main(String gamename)
    {
        super(gamename);
    }

    /**
     * @param gameContainer, game container that is used to interact with the main game
     * @throws SlickException
     */
    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        // sets deferred/threaded loading to false for now
        LoadingList.setDeferredLoading(true);
        // adds and enters the menu state
        this.addState(new Menu());
        enterState(0);
        //this.addState(new Game());
    }

    // change to window from full screen and vice-versa
    public static void changeMode() throws SlickException{
        // if it is in fullscreen set the game to be in a window of the width and height
        if (appGameContainer.isFullscreen()){
            appGameContainer.setFullscreen(false);
            appGameContainer.setDisplayMode(width,height,false);
        }
        // otherwise set the game in fullscreen
        else
            appGameContainer.setFullscreen(true);
    }


    // main method to enter
    public static void main(String[] args) {

        // if the java library path has not been set to the native directory(eg. in the jar)
        // set it for the user

        if (System.getProperty("org.lwjgl.librarypath") == null || !System.getProperty("org.lwjgl.librarypath").contains("native")) {
            try {
                System.setProperty("org.lwjgl.librarypath",
                        new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                                .toURI()).getParent()+"/native");
            } catch (URISyntaxException e) {
                System.err.println("Error adding native library to lwgl path... Exiting");
                System.exit(1);
            }
        }

        if (!System.getProperty("java.library.path").contains("native")) {
            try {
                System.setProperty("java.library.path",
                        System.getProperty("java.library.path")+":"+new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                                .toURI()).getParent()+"/native");
            } catch (URISyntaxException e) {
                System.err.println("Error adding native to java path... Exiting");
                System.exit(1);
            }
        }

        System.out.println(System.getProperty("java.library.path"));

        // catches all exceptions and logs them
        try {
            // creates a new game container
            appGameContainer = new AppGameContainer(new Main("Braydn's Game"));

            // sets it to fullscreen
            appGameContainer.setFullscreen(true);
            //appGameContainer.setDisplayMode(800,600,false);
            // set the width and height for later
            width = appGameContainer.getWidth();
            height = appGameContainer.getHeight();
            // set the vsync, target frame rate
            appGameContainer.setVSync(true);
            appGameContainer.setTargetFrameRate(60);
            System.out.println(appGameContainer.getAspectRatio());
            // start the game
            appGameContainer.start();
        }
        catch (SlickException ex) {
            // log the exception at a severe level
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}