package nz.uberutils.helpers

import nz.uberutils.methods.UberSkills
import org.powerbot.game.api.methods.tab.Skills
import java.awt._
import java.text.NumberFormat
import java.util.Locale

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/4/11
 * Time: 2:19 PM
 * Package: nz.artedungeon.utils;
 */
object Skill {
  final val skills: Array[Skill] = Array(new Skill(0),
                                         new Skill(1),
                                         new Skill(2),
                                         new Skill(3),
                                         new Skill(4),
                                         new Skill(5),
                                         new Skill(6),
                                         new Skill(7),
                                         new Skill(8),
                                         new Skill(9),
                                         new Skill(10),
                                         new Skill(11),
                                         new Skill(12),
                                         new Skill(13),
                                         new Skill(14),
                                         new Skill(15),
                                         new Skill(16),
                                         new Skill(17),
                                         new Skill(18),
                                         new Skill(19),
                                         new Skill(20),
                                         new Skill(21),
                                         new Skill(22),
                                         new Skill(23),
                                         new Skill(24))
  val nf: NumberFormat = NumberFormat.getNumberInstance(new Locale("en", "IN"))
}

/**
 * Constructs a new Skill
 *
 * @param skillInt the index of the Skill
 */
class Skill(skillInt: Int) {

  private val startime  : Long   = 0L
  private val startxp   : Int    = 0
  private val startLevel: Int    = 0
  private val skillint  : Int    = 0
  private var lastxp    : Int    = 0
  private var lastLevel : Int    = 0
  private val name      : String = null
  val shortName: String = null

  def xpGained = Skills.getExperience(skillint) - startxp

  def levelsGained = curLevel - startLevel

  def xpTL = UberSkills.getExpToNextLevel(skillint)

  def percentTL = UberSkills.getPercentToNextLevel(skillint)

  def xpPH = ((xpGained) * 3600000D / (System.currentTimeMillis - startime)).asInstanceOf[Int]

  def curLevel = UberSkills.getRealLevel(skillint)

  def curXP = Skills.getExperience(skillint)

  def timeToLevel = {
    var TTL: String = "Calculating.."
    var ttlCalculations: Long = 0L
    if (xpPH != 0) {
      ttlCalculations = (xpTL * 3600000D).asInstanceOf[Long] / xpPH
      TTL = getTime(ttlCalculations)
    }
    TTL
  }

  def drawSkill(g: Graphics2D, x: Int, y: Int) {
    g
    .drawString(name + ": Gained " + xpGained + " P/H " + xpPH + " | TTL: " + timeToLevel + " | Level " + curLevel + " (" + levelsGained + ")",
                x,
                y)
  }

  private def getTime(millis: Long) = {
    val time: Long = millis / 1000
    var seconds: String = Integer.toString((time % 60).asInstanceOf[Int])
    var minutes: String = Integer.toString(((time % 3600) / 60).asInstanceOf[Int])
    var hours: String = Integer.toString((time / 3600).asInstanceOf[Int])
    var i: Int = 0
    while (i < 2) {
      if (seconds.length < 2) {
        seconds = "0" + seconds
      }
      if (minutes.length < 2) {
        minutes = "0" + minutes
      }
      if (hours.length < 2) {
        hours = "0" + hours
      }
      i += 1
    }
    hours + ":" + minutes + ":" + seconds
  }

  def getXpMinusLast = {
    val returnXP: Int = curXP - lastxp
    lastxp = curXP
    returnXP
  }

  def getLevelMinusLast = {
    val returnLevel: Int = curLevel - lastLevel
    lastLevel = curLevel
    returnLevel
  }

  final def getShortName = {
    skillint match {
      case Skills.AGILITY =>
        "AGIL"
      case Skills.ATTACK =>
        "ATT"
      case Skills.DEFENSE =>
        "DEF"
      case Skills.DUNGEONEERING =>
        "DUNG"
      case Skills.MAGIC =>
        "MAGE"
      case Skills.CONSTITUTION =>
        "HP"
      case Skills.CONSTRUCTION =>
        "CONS"
      case Skills.SMITHING =>
        "SMITH"
      case Skills.CRAFTING =>
        "CRAFT"
      case Skills.SLAYER =>
        "SLAY"
      case Skills.FLETCHING =>
        "FLETCH"
      case Skills.COOKING =>
        "COOK"
      case Skills.FARMING =>
        "FARM"
      case Skills.FIREMAKING =>
        "FMING"
      case Skills.WOODCUTTING =>
        "WCING"
      case Skills.HERBLORE =>
        "HERB"
      case Skills.HUNTER =>
        "HUNT"
      case Skills.PRAYER =>
        "PRAY"
      case Skills.RUNECRAFTING =>
        "RCING"
      case Skills.MINING =>
        "MINING"
      case Skills.THIEVING =>
        "THIEVE"
      case Skills.SUMMONING =>
        "SUMMON"
      case Skills.STRENGTH =>
        "STR"
      case _ =>
        "UNK"
    }
  }
}