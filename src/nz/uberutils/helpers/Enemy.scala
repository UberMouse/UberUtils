package nz.uberutils.helpers

import org.powerbot.game.api.methods.interactive.NPCs
import org.powerbot.game.api.util.Filter
import org.powerbot.game.api.wrappers.Tile
import org.powerbot.game.api.wrappers.interactive.NPC

object Enemy {

  var npc: NPC = null

  /**
   * Get enemy hp percent.
   *
   * @return hp percent
   */
  private def hp = if (npc != null) npc.getHpPercent else -1

  /**
   * Location.
   *
   * @return the enemy location
   */
  def location = if (npc != null) npc.getLocation else null

  /**
   * Checks if current enemy is valid.
   *
   * @return true, if is valid
   */
  def isValid = npc != null && hp > 0

  /**
   * Do action.
   *
   * @param action the action
   * @return true, if successful
   */
  def interact(action: String) = if (npc != null) npc.interact(action) else false

  /**
   * Pick enemy using ids.
   *
   * @param ids the valid ids
   */
  def pickEnemy(ids: Array[Int]): NPC = {
    NPCs.getNearest(new Filter[NPC] {
      def accept(npc: NPC): Boolean = {
        if (npc.getHpPercent == 0) return false
        if (npc.getInteracting != null && !(npc.getInteracting == UberPlayer.get)) return false
        Utils.arrayContains(ids, npc.getId)
      }
    })
  }

  /**
   * Pick enemy using names.
   *
   * @param names the valid names
   */
  def pickEnemy(names: Array[String]): NPC = {
    NPCs.getNearest(new Filter[NPC] {
      def accept(npc: NPC): Boolean = {
        if (npc.getHpPercent == 0) return false
        if (npc.getInteracting != null && !(npc.getInteracting == UberPlayer.get)) return false
        Utils.arrayContains(names, true, npc.getName)
      }
    })
  }

  /**
   * Set enemy using ids.
   *
   * @param names the valid names
   */
  def setEnemy(names: Array[String]) = npc = pickEnemy(names)

  /**
   * Set enemy using ids.
   *
   * @param ids the valid ids
   */
  def setEnemy(ids: Array[Int]) = npc = pickEnemy(ids)

  /**
   * Checks if NPC is dead.
   *
   * @return true, if is dead
   */
  def isDead = hp == 0
}