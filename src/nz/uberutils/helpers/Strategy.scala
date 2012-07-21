package nz.uberutils.helpers

import org.powerbot.concurrent.Task

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 11:51 AM
 * Package: nz.uberfalconry;
 */
trait Strategy extends org.powerbot.concurrent.strategy.Strategy with Task {
  def getStatus: String
}