package ISU.GameEngine;

/***********************************************************
Name: Timer
Due Date: January 2017
Purpose: Fairly obvious, used to keep time
************************************************************/
public class Timer {
    // the time of the last check
    private double timeSinceLastCheck;

    /**
     * @return return the time since 1970 in seconds
     */
    private double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * @return the elapsed time since the last time this was called
     */
    public float getElapsedTime() {
        // get the time
        double time = getTime();
        // find the elapsed time between loops
        float elapsedTime = (float) (time - timeSinceLastCheck);
        // reset the loop time
        timeSinceLastCheck = time;

        // return the elapsed time
        return elapsedTime;

    }

    // reset the time keeping device
    public void reset(){
        timeSinceLastCheck = getTime();
    }
}