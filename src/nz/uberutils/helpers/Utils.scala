package nz.uberutils.helpers

import nz.uberutils.wrappers.LootItem
import org.powerbot.game.api.methods.Game
import org.powerbot.game.api.methods.Walking
import org.powerbot.game.api.methods.Widgets
import org.powerbot.game.api.methods.input.Mouse
import org.powerbot.game.api.methods.tab.Skills
import org.powerbot.game.api.util.Random
import org.powerbot.game.api.util.Time
import org.powerbot.game.api.wrappers.Area
import org.powerbot.game.api.wrappers.Tile
import org.powerbot.game.api.wrappers.node.Item
import org.powerbot.game.api.wrappers.widget.WidgetChild
import javax.imageio.ImageIO
import java.awt._
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.{URI, URL}
import java.util

object Utils {

  private val WALL: Int    = 0x200000
  private val log : java.util.logging.Logger = java.util.logging.Logger.getAnonymousLogger

  /**
   * Finds if the current world is a members world or not.
   *
   * @return <tt>true</tt> if the current world is a members world.
   * @author UberMouse
   */
  def isWorldMembers = false

  /**
   * Log text
   *
   * @param text info to be logged
   */
  def log(text: AnyRef): Unit = log info "" + text

  /**
   * Check if array contains String(s) check.
   *
   * @param array     the array to check
   * @param contains, using .contains instead of .equals
   * @param check     the String(s) to check
   * @return true, if successful
   * @deprecated Don't use this faggot
   */
  def arrayContains(array: Array[String], contains: Boolean, check: String*): Boolean = {
    if (array == null || check == null || array.length < 1) return false
    array.exists(str =>
                   if (contains) check.exists(checkStr => str.contains(checkStr))
                   else check.exists(checkStr => str.equals(checkStr)))
  }

  /**
   * Check if array contains String(s) check.
   *
   * @param array the array
   * @param check the check
   * @return true, if successful
   */
  def arrayContains(array: Array[String], check: String*): Boolean = {
    arrayContains(array, false, check:_*)
  }

  /**
   * Check if array contains int(s) check.
   *
   * @param array the array
   * @param check the check
   * @return true, if successful
   */
  def arrayContains(array: Array[Int], check: Int*): Boolean = {
    if (array == null || check == null || array.length < 1) return false
    array.exists(num => check.exists(_ == num))
  }

  /**
   * Check if array of LootItems contains id(s) check.
   *
   * @param array the array
   * @param check the check
   * @return true, if successful
   */
  def arrayContains(array: Array[LootItem], check: Int*): Boolean = {
    if (array == null || check == null || array.length < 1) return false
    array.exists(loot => check.exists(_ == loot.getId))
  }

  def getNearestNonWallTile(tile: Tile, eightTiles: Boolean = false): Tile = {
    val checkTiles: Array[Tile] = getSurroundingTiles(tile, eightTiles)
    val flags: Array[Int] = getSurroundingCollisionFlags(tile, eightTiles)
    var i: Int = 0
    while (i < checkTiles.length) {
      if ((flags(i) & WALL) == 0) return checkTiles(i)
      i += 1
    }
    null
  }

  def getSurroundingTiles(tile: Tile, eightTiles: Boolean = false): Array[Tile] = {
    val x: Int = tile.getX
    val y: Int = tile.getY
    val north: Tile = new Tile(x, y + 1, 0)
    val east: Tile = new Tile(x + 1, y, 0)
    val south: Tile = new Tile(x, y - 1, 0)
    val west: Tile = new Tile(x - 1, y, 0)
    var northEast: Tile = null
    var southEast: Tile = null
    var southWest: Tile = null
    var northWest: Tile = null
    if (eightTiles) {
      northEast = new Tile(x + 1, y + 1, 0)
      southEast = new Tile(x + 1, y - 1, 0)
      southWest = new Tile(x - 1, y - 1, 0)
      northWest = new Tile(x - 1, y + 1, 0)
      return Array[Tile](north, northEast, east, southEast, south, southWest, west, northWest)
    }
    Array[Tile](north, east, south, west)
  }

  def getDiagonalTiles(tile: Tile): Array[Tile] = {
    val x: Int = tile.getX
    val y: Int = tile.getY
    var northEast: Tile = null
    var southEast: Tile = null
    var southWest: Tile = null
    var northWest: Tile = null
    northEast = new Tile(x + 1, y + 1, 0)
    southEast = new Tile(x + 1, y - 1, 0)
    southWest = new Tile(x - 1, y - 1, 0)
    northWest = new Tile(x - 1, y + 1, 0)
    Array[Tile](northEast, southEast, southWest, northWest)
  }

  def getSurroundingCollisionFlags(tile: Tile, eightTiles: Boolean = false): Array[Int] = {
    val flags: Array[Array[Int]] = Walking.getCollisionFlags(Game.getPlane)
    val x: Int = tile.getX
    val y: Int = tile.getY
    val xOff: Int = x - Game.getMapBase.getX - Walking.getCollisionOffset(Game.getPlane).getX
    val yOff: Int = y - Game.getMapBase.getY - Walking.getCollisionOffset(Game.getPlane).getY
    val fNorth: Int = flags(xOff)(yOff + 1)
    val fEast: Int = flags(xOff + 1)(yOff)
    val fSouth: Int = flags(xOff)(yOff - 1)
    val fWest: Int = flags(xOff - 1)(yOff)
    var fNorthEast: Int = 0
    var fSouthEast: Int = 0
    var fSouthWest: Int = 0
    var fNorthWest: Int = 0
    if (eightTiles) {
      fNorthEast = flags(xOff + 1)(yOff + 1)
      fSouthEast = flags(xOff + 1)(yOff - 1)
      fSouthWest = flags(xOff - 1)(yOff - 1)
      fNorthWest = flags(xOff - 1)(yOff + 1)
      return Array[Int](fNorth, fNorthEast, fEast, fSouthEast, fSouth, fSouthWest, fWest, fNorthWest)
    }
    Array[Int](fNorth, fEast, fSouth, fWest)
  }

  def getCollisionFlagAtTile(tile: Tile): Int = {
    val flags: Array[Array[Int]] = Walking.getCollisionFlags(Game.getPlane)
    val x: Int = tile.getX
    val y: Int = tile.getY
    val xOff: Int = x - Game.getMapBase.getX - Walking.getCollisionOffset(Game.getPlane).getX
    val yOff: Int = y - Game.getMapBase.getY - Walking.getCollisionOffset(Game.getPlane).getY
    try {
      return flags(xOff)(yOff)
    }
    catch {
      case e: ArrayIndexOutOfBoundsException => {
        log("Tile: " + tile)
        log("xOff: " + xOff + " yOff: " + yOff)
        log("Mapbase: " + Game.getMapBase)
        log("Collision offset: " + Walking.getCollisionOffset(Game.getPlane))
        log("Flags arraycount: " + flags.length + " Flag subarray length: " + flags(0).length)
      }
    }
    -1
  }

  def getLoadedTiles: Array[Tile] = {
    val flags: Array[Array[Int]] = Walking.getCollisionFlags(Game.getPlane)
    lazy val t: util.ArrayList[Tile] = new util.ArrayList[Tile] {
      var i: Int = 0
      while (i < flags.length) {
        val xOff: Int = i + Game.getMapBase.getX + Walking.getCollisionOffset(Game.getPlane).getX
        var j: Int = 0
        while (j < flags(i)
                   .length) {
          {
            val yOff: Int = j + Game
                                .getMapBase
                                .getY + Walking
                                        .getCollisionOffset(Game
                                                            .getPlane)
                                        .getY
                                  t
            .add(new Tile(xOff,
                          yOff,
                          0))
          }
          j += 1
        }
        i += 1
      }
    }
    t.toArray(new Array[Tile](t.size))
  }

  def saveImage(image: Image, location: String) {
    saveImage(image, location, null)
  }

  def saveImage(image: Image, location: String, `type`: String) {
    val bufferedImage: BufferedImage = new BufferedImage(image.getWidth(null),
                                                         image.getHeight(null),
                                                         BufferedImage.TYPE_INT_ARGB)
    val painter: Graphics2D = bufferedImage.createGraphics
    painter.drawImage(image, null, null)
    val outputImg: File = new File(location)
    if (!outputImg.exists) try {
      outputImg.createNewFile
    }
    catch {
      case e: IOException => {
        e.printStackTrace()
      }
    }
    try {
      ImageIO.write(bufferedImage, if ((`type` != null)) `type` else "jpg", outputImg)
    }
    catch {
      case e: IOException => {
        e.printStackTrace()
      }
    }
  }

  def loadImage(url: String, p: String): BufferedImage = {
    var path = p
    if (!path.endsWith("\\")) path += "\\"
    val split: Array[String] = url.split("/")
    val imgName: String = split(split.length - 1)
    val file: File = new File(path + imgName)
    try {
      if (file.exists) return ImageIO.read(file)
      else {
        val read: BufferedImage = ImageIO.read(new URL(url))
        if (read != null) Utils.saveImage(read, path + imgName, url.substring(url.lastIndexOf(".") + 1, url.length))
        return read
      }
    }
    catch {
      case ignored: Exception => {
        ignored.printStackTrace()
      }
    }
    null
  }

  def openURL(url: String) {
    if (!java.awt.Desktop.isDesktopSupported) {
      return
    }
    val desktop: Desktop = java.awt.Desktop.getDesktop
    if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
      return
    }
    try {
      val uri: URI = new URI(url)
      desktop.browse(uri)
    }
    catch {
      case ignored: Exception => {
      }
    }
  }

  def parseTime(millis: Long, newFormat: Boolean = false): String = {
    val time: Long = millis / 1000
    var seconds: String = Integer.toString((time % 60).asInstanceOf[Int])
    var minutes: String = Integer.toString(((time % 3600) / 60).asInstanceOf[Int])
    var hours: String = Integer.toString((time / 3600).asInstanceOf[Int])
    var days: String = Integer.toString((time / (3600 * 24)).asInstanceOf[Int])
    var i: Int = 0
    while (i < 2) {
      if (seconds
          .length < 2) seconds = "0" + seconds
      if (minutes
          .length < 2) minutes = "0" + minutes
      if (hours
          .length < 2) hours = "0" + hours
      i += 1
    }

    if (!newFormat) return hours + ":" + minutes + ":" + seconds
    days = days + " day" + (if ((Integer.valueOf(days) != 1)) "s" else "")
    hours = hours + " hour" + (if ((Integer.valueOf(hours) != 1)) "s" else "")
    minutes = minutes + " minute" + (if ((Integer.valueOf(minutes) != 1)) "s" else "")
    seconds = seconds + " second" + (if ((Integer.valueOf(seconds) != 1)) "s" else "")
    days + ", " + hours + ", " + minutes + ", " + seconds
  }

  /**
   * Check if skill is boosted
   *
   * @param skill      skill to check
   * @param boostEarly boost skill before it's fully unboosted
   * @return true if skill is boosted
   */
  def isBoosted(skill: Int, boostEarly: Boolean = true): Boolean = {
    if (boostEarly) Skills.getLevel(skill) >= (math
                                               .ceil(Skills.getRealLevel(skill) * 1.05 + 3)
                                               .asInstanceOf[Int])
    else Skills.getLevel(skill) >= Skills.getRealLevel(skill)
  }

  /**
   * Sleep script
   *
   * @param min min time to sleep
   * @param max max time to sleep
   * @deprecated
   */
  def sleep(min: Int, max: Int) {
    Time.sleep(min, max)
  }

  /**
   * Sleep script
   *
   * @param time time to sleep
   * @deprecated
   */
  def sleep(time: Int) {
    Time.sleep(time)
  }

  /**
   * Debugs text along with function caller and line numbers.
   *
   * @param text the text
   */
  def debug(text: AnyRef) {
    if (true) {
      var className: String = Thread.currentThread.getStackTrace()(2).getClassName
      if (className.contains("$")) className = className.split("\\$")(1)
      val stackTraceElements: Array[StackTraceElement] = Thread.currentThread.getStackTrace
      val stacktrace: StackTraceElement = stackTraceElements(2)
      val methodName: String = stacktrace.getMethodName
      val lineNumber: Int = stacktrace.getLineNumber
      log
      .info("[" + stackTraceElements(3).getClassName + "#" + stackTraceElements(3)
                                                             .getMethodName + ":" + stackTraceElements(3)
                                                                                    .getLineNumber + "] -> [" + className + "#" + methodName + ":" + lineNumber + "] -> " + text)
    }
  }

  def random(min: Int, max: Int) = Random.nextInt(min, max)

  /**
   * Returns a Widget containing the search text
   *
   * @param text the text to search for
   * @return Widget if found, else null
   */
  def getWidgetWithText(text: String): WidgetChild = {
    for (widget <- Widgets.getLoaded) {
      for (comp <- widget.getChildren) {
        if (comp.getText.toLowerCase.contains(text.toLowerCase)) return comp
        for (comp2 <- comp.getChildren) {
          if (comp2.getText.toLowerCase.contains(text.toLowerCase)) return comp2
        }
      }
    }
    null
  }

  /**
   * Perform p/h calculation
   *
   * @param num       number to calculate p/h on
   * @param startTime starttime to use
   * @return p/h calculation
   */
  def calcPH(num: Int, startTime: Long) = ((num) * 3600000D / (System.currentTimeMillis - startTime)).asInstanceOf[Int]

  /**
   * Click Item
   *
   * @param item to click
   */
  def clickItem(item: Item) {
    Mouse.move(item.getWidgetChild.getCentralPoint)
    Mouse.click(true)
  }

  /**
   * Sleeps until widget is valid
   *
   * @param c1   Main component ID
   * @param c2   Sub component ID
   * @param time Time to wait (1 = 100ms)
   */
  def sleepUntilValid(c1: Int, c2: Int, time: Int) {
    var i: Int = 0
    while (i < time && !Widgets.get(c1, c2).validate) {
      Utils.sleep(100)
      i += 1
    }
  }

  /**
   * Sleeps until widget is valid
   *
   * @param c1 Main component ID
   * @param c2 Sub component ID
   */
  def sleepUntilValid(c1: Int, c2: Int): Unit = sleepUntilValid(c1, c2, 15)

  /**
   * Get random Tile in Area
   *
   * @param area Area to get Tile in
   * @return random Tile in Area
   */
  def getRandomTile(area: Area) = area.getTileArray()(random(0, area.getTileArray.length - 1))
}