package nz.uberutils.helpers

import org.powerbot.game.api.util.Time

/**
 * Created by IntelliJ IDEA.
 * User: UberMouse
 * Date: 7/20/12
 * Time: 9:36 PM
 */

object Wait {
  def For(condition: => Boolean, timeout: Int = 10)(callback: => Unit = {}) {
    var times = 0
    while(times < timeout && !condition) {
      times += 1
      Time.sleep(100)
    }
    if (times != timeout)
      callback
    times != timeout
  }

  def While(condition: => Boolean, timeout: Int = 10)(callback: => Unit = {}) {
      var times = 0
      while(times < timeout && condition) {
        times += 1
        Time.sleep(100)
      }
      if (times != timeout)
        callback
      times != timeout
    }
}