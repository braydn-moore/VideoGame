package ISU.GameEngine.Player;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/***********************************************************
Name: EntitySheet
Due Date: January 2017
Purpose: The class that handles spritesheets and turns them into arrays of Images
************************************************************/
public class EntitySheet {

    // sprite sheet
    private org.newdawn.slick.SpriteSheet spriteSheet;
    // height and width of the character
    private int characterWidth,characterHeight;

    /**
     * @param path: path to the spritesheet
     * @param characterWidth: the height of each character
     * @param characterHeight: the width of each character
     */
    public EntitySheet(String path, int characterWidth, int characterHeight){
        // set variables
        this.characterHeight = characterHeight;
        this.characterWidth = characterWidth;
        // load sprite sheet
        try {
            spriteSheet = new org.newdawn.slick.SpriteSheet(path, characterWidth, characterHeight, new Color(32, 155, 2));
        }catch (SlickException e){}
    }

    /**
     * @param yStartRow: the row on Y to get
     * @param imageWidth: the width of the total spritesheet image
     * @return: an array of images that was taken from the specified row on the spritesheet
     */
    public Image[] getSpriteAnimationArrayHorizontal(int yStartRow, int imageWidth){
        // number of iterations or images in the row
        int iterations = imageWidth/characterWidth;
        // create the array of images to return later
        Image[] animation = new Image[iterations];
        // cut moveLess the spritesheet row into images and place them in the array
        for(int counter = 0; counter<iterations; counter++)
            animation[counter] = spriteSheet.getSprite(counter,yStartRow);
        // return the array
        return animation;
    }

    /**
     * @param xStartRow: the vertical x coord to cut into an array of images
     * @param imageHeight: the height of the spritesheet image
     * @return an array of images that was taken from the specified column on the spritesheet
     */
    public Image[] getSpriteAnimationArrayVertical(int xStartRow, int imageHeight){
        // same as the above method for horizontally cutting moveLess a spritesheet but cuts it moveLess vertically
        int iterations = imageHeight/characterHeight;
        Image[] animation = new Image[iterations];
        for(int counter = 0; counter<iterations; counter++)
            animation[counter] = spriteSheet.getSprite(xStartRow, counter);
        return animation;
    }

}
