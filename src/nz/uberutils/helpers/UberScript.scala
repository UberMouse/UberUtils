package nz.uberutils.helpers

import nz.uberutils.helpers.tasks.PriceThread
import nz.uberutils.paint.PaintController
import nz.uberutils.paint.components.PDialogue
import org.powerbot.game.api.ActiveScript
import org.powerbot.game.api.Manifest
import org.powerbot.game.api.methods.Environment
import org.powerbot.game.api.methods.Game
import org.powerbot.game.api.methods.tab.Skills
import org.powerbot.game.api.util.Time
import org.powerbot.game.bot.Context
import org.powerbot.game.bot.event.listener.PaintListener
import java.awt._
import java.awt.event._
import java.util.ArrayList
import collection.mutable.ListBuffer

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/17/11
 * Time: 5:13 PM
 * Package: nz.uberutils.helpers;
 */
abstract class UberScript extends ActiveScript with PaintListener with KeyListener with MouseListener with MouseMotionListener {

  protected        var status            = "Starting..."
  protected        val strategies        = new ListBuffer[Strategy]
  private          val manifest          = getClass.getAnnotation(classOf[Manifest])
  protected        val name              = manifest.name
  protected        var threadId          = 0
  private[helpers] var sep               = System.getProperty("file.separator")
  protected        val logger            = new Logger(Environment
                                                      .getStorageDirectory + sep + "uberbots" + sep + name + sep + "logs" + sep + name,
                                                      name)
  protected        var changeLog         = Array[String]("Not set")
  private          var setup             = false
  protected        var paintType: IPaint = null

  private def sleep(min: Int, max: Int) {
    Time.sleep(min, max)
  }

  private def sleep(time: Int) {
    Time.sleep(time)
  }

  def onStart = {
    if (!Game.isLoggedIn) {
      logger.info("Not logged in... waiting for login random to finish")
      while (!Game.isLoggedIn || Skills.getLevel(Skills.AGILITY) < 1) sleep(100)
      sleep(3000)
    }
    Options.setNode(name + Context.client.getCurrentUsername)
    getContainer.submit(new PriceThread)
    PaintController.reset()
    PaintController.startTimer()
    Options.add("prevVersion", manifest.version)
    onBegin
  }

  def onFinish() {
    Options.put("prevVersion", manifest.version)
    Options.save()
    logger.cleanup()
    onEnd()
  }

  def onEnd() {
  }

  def onBegin = {
    true
  }

  def loop: Int = {
    try {
      if (!setup) {
        if (Options.getDouble("prevVersion") < manifest.version) {
          PaintController
          .addComponent(new PDialogue("Script has been updated!", changeLog, new Font("Arial", 0, 12), PDialogue
                                                                                                       .ColorScheme
                                                                                                       .GRAPHITE,
                                      PDialogue
                                      .Type
                                      .INFORMATION) {
            override def okClick() {
              PaintController.removeComponent(this)
            }

            override def shouldHandleMouse = {
              shouldPaint
            }

            override def shouldPaint = {
              Game.isLoggedIn
            }
          })
        }
        setup = true
      }
      if (!Game.isLoggedIn) return 100
      miscLoop()
      for (strategy <- strategies) {
        if (strategy.validate()) {
          status = strategy.getStatus
          strategy.run()
          return Utils.random(400, 500)
        }
      }
    }
    catch {
      case e: Exception => {
      }
    }
    0
  }

  protected def miscLoop() {
  }

  def keyTyped(keyEvent: KeyEvent) {
    if (paintType == null) return
    paintType.keyTyped(keyEvent)
  }

  def keyPressed(keyEvent: KeyEvent) {
    if (paintType == null) return
    paintType.keyPressed(keyEvent)
  }

  def keyReleased(keyEvent: KeyEvent) {
    if (paintType == null) return
    paintType.keyReleased(keyEvent)
  }

  def mouseClicked(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseClicked(mouseEvent)
  }

  def mousePressed(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mousePressed(mouseEvent)
  }

  def mouseReleased(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseReleased(mouseEvent)
  }

  def mouseEntered(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseEntered(mouseEvent)
  }

  def mouseExited(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseExited(mouseEvent)
  }

  def mouseDragged(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseDragged(mouseEvent)
  }

  def mouseMoved(mouseEvent: MouseEvent) {
    if (paintType == null) return
    paintType.mouseMoved(mouseEvent)
  }

  def onRepaint(graphics: Graphics) {
    if (paintType == null) return
    if (paintType.paint(graphics)) paint(graphics.asInstanceOf[Graphics2D])
  }

  protected def paint(g: Graphics2D) {
  }
}