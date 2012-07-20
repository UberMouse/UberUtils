package nz.uberutils.helpers

import nz.uberutils.helpers.tasks.PriceThread
import nz.uberutils.methods.UberInventory
import nz.uberutils.methods.UberMovement
import org.powerbot.game.api.methods.Calculations
import org.powerbot.game.api.methods.node.GroundItems
import org.powerbot.game.api.methods.tab.Inventory
import org.powerbot.game.api.util.Filter
import org.powerbot.game.api.wrappers.Tile
import org.powerbot.game.api.wrappers.node.GroundItem
import org.powerbot.game.api.wrappers.node.Item
import java.awt._
import collection.mutable.ListBuffer
import java.util

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/27/11
 * Time: 3:28 PM
 * Package: nz.artezombies.utils;
 */
object Loot {

  private final val lootCounts = new util.HashMap[Int, Int]
  private final val lootNames  = new util.HashMap[Int, String]
  var totalPrice : Int                = 0
  var filter     : Filter[GroundItem] = new Filter[GroundItem] {
    def accept(groundItem: GroundItem): Boolean = {
      true
    }
  }
  var paintFilter: Filter[GroundItem] = new Filter[GroundItem] {
    def accept(groundItem: GroundItem): Boolean = {
      true
    }
  }

  def takeLoot(filter: Filter[GroundItem]) {
    val loot: GroundItem = GroundItems.getNearest(filter)
    val stackable = Inventory.getCount(true, loot.getId) > 1
    var foodCount = 0
    try {
      for (i <- UberInventory.getItems) {
        if (UberPlayer.isEdible(i))
          foodCount += 1
      }
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
    if (Inventory.isFull && !stackable && foodCount <= 5) return
    if (foodCount > 5 && Inventory.isFull) UberPlayer.eat()
    if (loot.validate) {
      val name = loot.getGroundItem.getName
      val id = loot.getId
      UberMovement.turnTo(loot.getLocation)
      if (loot.interact("Take " + name)) {
        if (lootCounts.containsKey(id)) lootCounts.put(id, lootCounts.get(id) + 1)
        else {
          lootCounts.put(id, 0)
          lootNames.put(id, name)
        }
        var add = 0
        if (PriceThread.priceForId(id) > 0) add = PriceThread.priceForId(id) * 1
        totalPrice += add
        val iCount = Inventory.getCount
        Wait.For(iCount != Inventory.getCount)
      }
    }
    else {
      if (loot.interact("Take ")) {
        val iCount: Int = Inventory.getCount
        Wait.For(iCount != Inventory.getCount)
      }
    }
  }

  def takeLoot(ids: Array[Int]) {
    takeLoot(new Filter[GroundItem] {
      def accept(t: GroundItem): Boolean = {
        Utils.arrayContains(ids, t.getId)
      }
    })
  }

  def repaint(g: Graphics2D) {
    val items = GroundItems.getLoaded(paintFilter)
    var offset: Int = 0
    val tiles = new ListBuffer[Tile]
    items.foreach(item => if (!tiles.contains(item.getLocation)) tiles += item.getLocation)

    val list = tiles.map(_.getId).toArray
    for (tile <- tiles) {
      for (item <- GroundItems.getLoadedAt(tile.getX, tile.getY)) {
        if (Utils.arrayContains(list, item.getId)) {
          lootInfo(g, item, offset)
          offset += 15
        }
      }
      offset = 0
    }
  }

  def lootInfo(g: Graphics2D, loot: GroundItem, offset: Int) {
    if (!loot.isOnScreen) return
    val groundItem: Item = loot.getGroundItem
    var lootString: String = groundItem.getName + " (" + groundItem.getStackSize + " * "
    lootString += {
      if ((PriceThread.priceForId(groundItem.getId) == -1)) "Calculating.."
      else if ((PriceThread
                .priceForId(groundItem
                            .getId) == -2)) "Unknown"
      else PriceThread.priceForId(groundItem.getId)
    }
    lootString += ")"
    val color1: Color = new Color(0, 51, 204, 50)
    val color2: Color = new Color(0, 0, 0)
    val color3: Color = new Color(255, 255, 255)
    val stroke1: BasicStroke = new BasicStroke(1)
    val font1: Font = new Font("Arial", 0, 9)
    val fm: FontMetrics = g.getFontMetrics(font1)
    val stringWidth: Int = fm.stringWidth(lootString) + 6
    val location: Tile = loot.getLocation
    val point: Point = Calculations.groundToScreen(location.getX, location.getX, location.getPlane, 0)
    g.setColor(color1)
    g.fillRect(point.x, point.y + offset, stringWidth, 15)
    g.setColor(color2)
    g.setStroke(stroke1)
    g.drawRect(point.x, point.y + offset, stringWidth, 15)
    g.setFont(font1)
    g.setColor(color3)
    g.drawString(lootString, point.x + 3, point.y + 10 + offset)
  }

  def isPaintValid = GroundItems.getLoaded(paintFilter).length > 0

  private def idToName(id: Int) = if (lootNames.containsKey(id)) lootNames.get(id) else null

  def addLoot(id: Int, name: String, price: Int) {
    lootNames.put(id, name)
    lootCounts.put(id, 0)
    PriceThread.addPrice(id, price)
  }

  def shouldLoot = {
    val stackable = GroundItems.getLoaded.filter(item => Inventory.getCount(true, item.getId) > 1).length > 0
    var count: Int = 0
    try {
      for (i <- UberInventory.getItems) if (UberPlayer.isEdible(i)) ({
        count += 1;
        count
      })
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
      }
    }
    GroundItems.getNearest(filter) != null && (!Inventory.isFull || stackable || count > 5)
  }
}