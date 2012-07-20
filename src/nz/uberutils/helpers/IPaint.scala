package nz.uberutils.helpers

import java.awt._
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.util.ArrayList

trait IPaint extends MouseListener with MouseMotionListener with KeyListener {
  def paint(g: Graphics): Boolean
}