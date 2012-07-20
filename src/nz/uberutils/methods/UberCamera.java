package nz.uberutils.methods;

import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Locatable;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 2:10 PM
 * Package: nz.artedungeon.utils;
 */
public class UberCamera
{
    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param tile the <tt>Tile</tt> to turn to
     * @param deviation max amount of deviation to add to angle
     */
    public static void turnTo(Locatable tile, int deviation) {
        int angle = Camera.getMobileAngle(tile);
        if(deviation < 16)
            deviation = 16;
        if (angle > Camera.getYaw()) {
            int times = Camera.getAngleTo(Camera.getMobileAngle(tile)) / 15;
            for (int i = 0; i < times; i++) {
                Camera.setAngle(Camera.getYaw() + 15);
                if (tile.getLocation().isOnScreen())
                    break;
            }
            Camera.setAngle(Camera.getYaw() + Random.nextInt(15, deviation));
        }
        else {
            int times = Camera.getAngleTo(Camera.getMobileAngle(tile)) / 15;
            for (int i = 0; i < times; i++) {
                Camera.setAngle(Camera.getYaw() - 15);
                if (tile.getLocation().isOnScreen())
                    break;
            }
            Camera.setAngle(Camera.getYaw() - Random.nextInt(15, deviation));
        }
    }

    /**
     * Turns the Camera to specified <tt>Locatable</tt>
     * @param locatable the <tt>Locatable</tt> to turn to
     */
    public static void turnTo(Locatable locatable) {
        turnTo(locatable, 0);
    }
}
