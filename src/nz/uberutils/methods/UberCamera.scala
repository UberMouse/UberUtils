package nz.uberutils.methods

import org.powerbot.game.api.methods.widget.Camera
import org.powerbot.game.api.util.Random
import org.powerbot.game.api.wrappers.Locatable

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 2:10 PM
 * Package: nz.artedungeon.utils;
 */
object UberCamera {
  /**
   * Turns the Camera to specified <tt>Tile</tt>
   * @param tile the <tt>Tile</tt> to turn to
   * @param d max amount of deviation to add to angle
   */
  def turnTo(tile: Locatable, d: Int = 0) {
    var deviation = d
    val angle: Int = Camera.getMobileAngle(tile)
    if (deviation < 16) deviation = 16
    if (angle > Camera.getYaw) {
      val times: Int = Camera.getAngleTo(Camera.getMobileAngle(tile)) / 15
      var i: Int = 0
      while (i < times && !tile.getLocation.isOnScreen) {
        Camera.setAngle(Camera.getYaw + 15)
        i += 1
      }
      Camera.setAngle(Camera.getYaw + Random.nextInt(15, deviation))
    }
    else {
      val times: Int = Camera.getAngleTo(Camera.getMobileAngle(tile)) / 15
      var i: Int = 0
      while (i < times && !tile.getLocation.isOnScreen) {
        Camera.setAngle(Camera.getYaw - 15)
        i += 1
        Camera.setAngle(Camera.getYaw - Random.nextInt(15, deviation))
      }
    }
  }
}