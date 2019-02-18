package ISU.GameEngine;

import ISU.GameEngine.Player.Camera;

import java.util.Random;

/***********************************************************
Name: Util
Due Date: January 2017
Purpose: Various utilities used, not specific to a single class
************************************************************/
public class Util {

    private static Camera camera;

    // class of directions
    public enum directions{
        up,left,down,right
    }

    // random object
    private static Random random = new Random();

    // get a random float
    public static float nextFloat(){
        return random.nextFloat();
    }

    /**
     * @param min: the minimum value
     * @param max: the maximum value
     * @param value: the current value
     * @return: whether or not the value passed in is in between the minimum and maximum values(inclusive of min and max)
     */
    public static boolean inRange(float min, float max, float value){
        return (value>=min && value<=max);
    }

    /**
     * @param direction: the current direction
     * @return: the opposite direction
     */
    public static directions oppositeDirection(directions direction){
        // simple switch statement
        switch (direction){
            case up:
                return directions.down;
            case down:
                return directions.up;
            case right:
                return directions.left;
            case left:
                return directions.right;
            default:
                return null;
        }
    }

    /**
     * @param min: the minimum required value
     * @param max: the maximum required value
     * @return: a random float in between the minimum and maximum value
     */
    public static float randomFloat(int min, int max){
        return min + random.nextFloat() * ( max - min );
    }

    public static void setCamera(Camera camera){Util.camera = camera;}

    public static float getPerspectiveValue(float value, boolean x, boolean y){
        return ((x&&y)||(!x&&!y))?value:x?value-camera.getXTranslate():value-camera.getYTranslate();
    }
}