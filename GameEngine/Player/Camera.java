package ISU.GameEngine.Player;
import ISU.Main;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

/***********************************************************
Name: Camera
Due Date: January 2017
Purpose: The camera that will track the player when they move through the map
************************************************************/
public class Camera {

    private Player player;
    private float xTranslate, yTranslate;
    private int mapHeight, mapWidth;

    /**
     * @param map: the game map
     * @param player: the player object
     */
    public Camera(TiledMap map, Player player){
        this.player = player;
        xTranslate = 0;
        yTranslate = 0;
        mapWidth = map.getWidth() * map.getTileWidth();
        mapHeight = map.getHeight() * map.getTileHeight();
    }

    /**
     * @param graphics: used to translate the graphics when drawing the map
     */
    public void translate(Graphics graphics){
        xTranslate = getXTranslate();

        yTranslate = getYTranslate();

        graphics.translate(xTranslate, yTranslate);
    }

    /**
     * @return: the value to translate the map along the x to keep the player in the center(this will be a negative value)
     */
    public float getXTranslate() {
        if (mapWidth > Main.width) {
            // if the player is still in the first part of the stage don't translate it
            if (player.getX() - Main.width / 2 + 16 < 0)
                return 0;
            // if the player is at the end of the map stop translating
            else if (player.getX() + Main.width / 2 + 16 > mapWidth)
                return Main.width - mapWidth;
            // otherwise move the map along with the player
            else
                return Main.width / 2 - player.getX() - 16;
        }
        else
            return 0;
    }

    /**
     * @return: the value to translate the map along the y to keep the player in the center(this will be a negative value)
     */
    public float getYTranslate() {
        // follows the same rules as above except uses the y axis
        if (mapHeight > Main.height) {
            if (player.getY() - Main.height / 2 + 16 < 0)
                return 0;
            else if (player.getY() + Main.height / 2 + 16 > mapHeight)
                return Main.height - mapHeight;
            else
                return Main.height / 2 - player.getY() - 16;
        }
        else
            return 0;
    }
}
