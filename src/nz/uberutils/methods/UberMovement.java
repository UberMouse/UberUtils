package nz.uberutils.methods;

import nz.uberutils.helpers.UberPlayer;
import nz.uberutils.helpers.Utils;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.SceneObject;


public class UberMovement
{
    /**
     * Turns camera to Locatable and walks to it if off screen still.
     *
     * @param locatable the Locatable
     */
    public static void turnTo(Locatable locatable) {
        if (locatable == null)
            return;
        if (!locatable.getLocation().isOnScreen()) {
            if (Calculations.distanceTo(locatable) > 6)
                Walking.walk(locatable);
            UberCamera.turnTo(locatable.getLocation(), Random.nextInt(10, 25));
            if (!locatable.getLocation().isOnScreen())
                Walking.walk(locatable);
        }
    }

    /**
     * Reverse path.
     *
     * @param other the path to reverse
     * @return the reversed path
     */
    public static Tile[] reversePath(Tile[] other) {
        Tile[] t = new Tile[other.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = other[other.length - i - 1];
        }
        return t;
    }

    public static void walkTo(Tile tile) {
        UberMovement.turnTo(tile);
        if (!UberPlayer.isMoving() && tile.isOnScreen())
            if(tile.interact("Walk"))
                Utils.waitToStop();
    }

    public static void walkTo(SceneObject object) {
        walkTo(object.getLocation());
    }

    public static void walkTo(NPC n) {
        walkTo(n.getLocation());
    }

}
