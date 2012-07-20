package nz.uberutils.helpers

import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences
import java.util

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/3/11
 * Time: 10:38 AM
 * Package: nz.artezombies.threads;
 */
object Options {

  private       var prefs: Preferences = Preferences.userRoot.node("UberOptions")
  private final val stringDefaults     = new util.HashMap[String, String]
  private final val intDefaults        = new util.HashMap[String, Int]
  private final val longDefaults       = new util.HashMap[String, Long]
  private final val booleanDefaults    = new util.HashMap[String, Boolean]
  private final val floatDefaults      = new util.HashMap[String, Float]
  private final val doubleDefaults     = new util.HashMap[String, Double]
  private final val byteDefaults       = new util.HashMap[String, Array[Byte]]

  def setNode(name: String) = prefs = Preferences.userRoot.node(name)


  def save() {
    try {
      prefs.flush()
    }
    catch {
      case ignored: BackingStoreException => {
      }
    }
  }

  def flip(name: String) = put(name, !getBoolean(name))
  def add(s: String, s1: String) = stringDefaults.put(s, s1)
  def add(s: String, i: Int) = intDefaults.put(s, i)
  def add(s: String, l: Long) = longDefaults.put(s, l)
  def add(s: String, b: Boolean) = booleanDefaults.put(s, b)
  def add(s: String, f: Float) = floatDefaults.put(s, f)
  def add(s: String, d: Double) = doubleDefaults.put(s, d)
  def add(s: String, bytes: Array[Byte]) = byteDefaults.put(s, bytes)


  def put(s: String, s1: String) {
    prefs.put(s, s1)
    stringDefaults.put(s, s1)
    save()
  }
  def get(s: String): String = prefs.get(s, stringDefaults.get(s))

  def put(s: String, i: Int) {
    prefs.putInt(s, i)
    intDefaults.put(s, i)
    save()
  }
  def getInt(s: String): Int = prefs.getInt(s, intDefaults.get(s))


  def put(s: String, l: Long) {
    prefs.putLong(s, l)
    longDefaults.put(s, l)
    save()
  }
  def getLong(s: String): Long = prefs.getLong(s, longDefaults.get(s))


  def put(s: String, b: Boolean) {
    prefs.putBoolean(s, b)
    booleanDefaults.put(s, b)
    save()
  }
  def getBoolean(s: String): Boolean = prefs.getBoolean(s, booleanDefaults.get(s))

  def put(s: String, v: Float) {
    prefs.putFloat(s, v)
    floatDefaults.put(s, v)
    save()
  }
  def getFloat(s: String): Float = prefs.getFloat(s, floatDefaults.get(s))

  def put(s: String, v: Double) {
    prefs.putDouble(s, v)
    doubleDefaults.put(s, v)
    save()
  }
  def getDouble(s: String): Double = prefs.getDouble(s, doubleDefaults.get(s))

  def put(s: String, bytes: Array[Byte]) {
    prefs.putByteArray(s, bytes)
    byteDefaults.put(s, bytes)
    save()
  }
  def getByteArray(s: String): Array[Byte] = prefs.getByteArray(s, byteDefaults.get(s))

  def containsKey(s: String): Boolean = {
    try {
      for (key <- prefs.keys) {
        if (key.toLowerCase == s.toLowerCase) return true
      }
    }
    catch {
      case ignored: BackingStoreException => {
      }
    }
    false
  }
}