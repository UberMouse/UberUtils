package nz.uberutils.helpers

import nz.uberutils.methods.UberInventory
import nz.uberutils.methods.UberMovement
import org.powerbot.game.api.methods.interactive.NPCs
import org.powerbot.game.api.methods.interactive.Players
import org.powerbot.game.api.methods.tab.Inventory
import org.powerbot.game.api.methods.tab.Prayer
import org.powerbot.game.api.methods.tab.Skills
import org.powerbot.game.api.util.Filter
import org.powerbot.game.api.wrappers.Area
import org.powerbot.game.api.wrappers.interactive.NPC
import org.powerbot.game.api.wrappers.node.Item

object UberPlayer {

  sealed case class Potion(name: String, skill: Int)

  val POTIONS = List(Potion("Extreme Attack", Skills.ATTACK),
                     Potion("Extreme Strength", Skills.STRENGTH),
                     Potion("Extreme Defense", Skills.DEFENSE),
                     Potion("Super Attack", Skills.ATTACK),
                     Potion("Super Strength", Skills.STRENGTH),
                     Potion("Super Defense", Skills.DEFENSE),
                     Potion("Attack Potion", Skills.ATTACK),
                     Potion("Strength Potion", Skills.STRENGTH),
                     Potion("Defense Potion", Skills.ATTACK),
                     Potion("Combat Potion", Skills.STRENGTH),
                     Potion("Overload", Skills.STRENGTH))

  /**
   * Location.
   *
   * @return the player location
   */
  def location = UberPlayer.get.getLocation

  /**
   * Eat food.
   *
   * @param food the food ids
   */
  def eat(food: Array[Int]) {
    if (Inventory.getItem(food: _*) != null) Inventory.getItem(food: _*).getWidgetChild.interact("Eat")
  }

  /**
   * Eat food.
   */
  def eat() {
    val food: Item = edibleItem
    if (food != null) food.getWidgetChild.click(true)
  }

  /**
   * Returns player hp percent.
   *
   * @return hp percent remaining
   */
  def hp = get.getHpPercent

  /**
   * In area.
   *
   * @param area the area
   * @return true, if successful
   */
  def inArea(area: Area) = area contains location

  /**
   * Attack.
   *
   * @param enemy the enemy
   * @return boolean true if enemy was attacked or false if attacking failed
   */
  def attack(enemy: NPC): Boolean = {
    UberMovement.turnTo(enemy)
    if (enemy.interact("Attack"))
      return Wait.For(inCombat(), 15)
    false
  }

  /**
   * Get prayer points.
   *
   * @return current prayer points
   */
  def prayer = Prayer.getPoints

  /**
   * In combat.
   *
   * @param multi in multi combat area
   * @return true, if in combat
   */
  def inCombat(multi: Boolean = false) = {
    if (multi) UberPlayer.isInteracting && (UberPlayer.interacting.asInstanceOf[NPC]).getHpPercent > 0
    else UberPlayer.isInteracting && (UberPlayer.interacting.asInstanceOf[NPC])
                                     .getHpPercent > 0 && (getInteractor != null && (interacting == getInteractor) || getInteractor == null)
  }

  /**
   * Checks if is interacting.
   *
   * @return true, if is interacting
   */
  def isInteracting = UberPlayer.get.getInteracting != null

  /**
   * Return interacting NPC as Npc.
   *
   * @return the Npc
   */
  def interacting = UberPlayer.get.getInteracting

  /**
   * Gets the player.
   *
   * @return the player RSPlayer
   */
  def get = Players.getLocal

  /**
   * Checks if player is moving.
   *
   * @return true, if is moving
   */
  def isMoving = get.isMoving

  /**
   * Generic level to eat at
   *
   * @return true, if player hp is too low
   */
  def needToEat = hp < 50

  /**
   * Check if inventory contains potions
   *
   * @return true if any potions were found
   */
  def hasPotions: Boolean = {
    for (potion <- POTIONS) if (UberInventory.contains(potion.name)) return true
    false
  }

  /**
   * Drink any potions in inventory for unboosted skills
   */
  def drinkPotions() {
    if (!hasPotions) return
    for (potion <- POTIONS) if (!Utils.isBoosted(potion.skill) && UberInventory.contains(potion.name)) {
      UberInventory.getItem(potion.name).getWidgetChild.click(true)
      Utils.sleep(Utils.random(400, 500))
    }
  }

  /**
   * Should repot
   *
   * @return true if should repot
   */
  def shouldPot: Boolean = {
    if (!hasPotions) return false
    for (potion <- POTIONS) {
      if (!Utils.isBoosted(potion.skill) && UberInventory.contains(potion.name, true)) return true
    }
    false
  }

  /**
   * Gets edible item from Inventory
   *
   * @return Item if one is found, else null
   */
  def edibleItem: Item = {
    val is: Array[Item] = UberInventory.getItems(true)
    for (i <- is) {
      if (isEdible(i)) return i
    }
    null
  }

  def isEdible(item: Item): Boolean = {
    if (item == null || item.getWidgetChild.getActions == null || item.getWidgetChild.getActions.length < 1 || item
                                                                                                               .getWidgetChild
                                                                                                               .getActions()(0) == null) return false
    item.getWidgetChild.getActions(0).contains("Eat")
  }

  /**
   * Gets npc interating with you
   *
   * @return npc interacting with local player
   */
  def getInteractor: NPC = {
    NPCs.getNearest(new Filter[NPC] {
      def accept(npc: NPC): Boolean = {
        npc.getInteracting != null && (npc.getInteracting == get)
      }
    })
  }
}