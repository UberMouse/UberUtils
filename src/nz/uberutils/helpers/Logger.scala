package nz.uberutils.helpers

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 7/20/11
 * Time: 11:51 PM
 * Package: nz.uberutils.helpers;
 */
object Logger {
  final val LOGGING_MODE_ALL    : Int = 0
  final val LOGGING_MODE_DEBUG  : Int = 1
  final val LOGGING_MODE_WARNING: Int = 2
  final val LOGGING_MODE_INFO   : Int = 3
  var instance: Logger = null
}

class Logger(var logFile: File, name: String, loggingLevel: Int, fileExt: String) {

  import Logger._

  private final val outputList              = new util.ArrayList[String]
  private final val dFormat                 = new SimpleDateFormat("hh:mm:ss.SSS")
  private       var logFile: File           = null
  private       var wr     : BufferedWriter = null
  var loggingMode        = 0
  var scriptName: String = null
  private var stop = false
  var fileExtension = ".txt"

  fileExtension = fileExt
  setLogFile(logFile)
  instance = this
  new Thread(new Logger#OutputThread).start()

  def this(logFile: String, name: String, loggingLevel: Int, fileExt: String) {
    this(new File(logFile), name, loggingLevel, fileExt)
  }

  def this(logFile: String, name: String) {
    this(logFile, name, 1, ".txt")
  }

  /**
   * Sets File to write to for logging
   *
   * @param fName name of file to construct File object to write to
   */
  def setLogFile(fName: String) {
    setLogFile(new File(fName))
  }

  /**
   * Sets File to write to for logging
   *
   * @param f File to write to
   */
  def setLogFile(f: File) {
    var file = f
    val fName: String = file.getName + "-" + new SimpleDateFormat("MMM-dd-EEE.hh-mm-ss")
                                             .format(new Date) + fileExtension
    file = new File(file.getParent + System.getProperty("file.separator") + fName)
    logFile = file
    if (!file.exists) try {
      if (!file.getParentFile.exists) file.getParentFile.mkdirs
      file.createNewFile
    }
    catch {
      case e: IOException => {
        System.err.println("Error creating new logfile")
        e.printStackTrace()
      }
    }
    try {
      wr = new BufferedWriter(new FileWriter(file))
    }
    catch {
      case e: IOException => {
        System.err.println("Failed to open log file for writing")
        e.printStackTrace()
      }
    }
  }

  /**
   * Perform cleanup and finishing operations, should always be closed on application exit
   */
  def cleanup() {
    stop = true
    if (wr != null) {
      try {
        wr.close()
      }
      catch {
        case e: IOException => {
          System.err.println("Error closing file writer")
          e.printStackTrace()
        }
      }
    }
  }

  /**
   * Log to logfile at given log level with message
   *
   * @param level   log level to log at
   * @param m message to log to file
   */
  def log(level: Int, m: String, replacements: AnyRef*) {
    var message = m
    if (level < loggingMode) return
    for (replace <- replacements) message = message.replaceFirst("\\{\\}", replace.toString)
    var lvlMessage: String = ""
    level match {
      case 0 =>
        lvlMessage = "TRACE"
      case 1 =>
        lvlMessage = "DEBUG"
      case 2 =>
        lvlMessage = "WARN"
      case 3 =>
        lvlMessage = "INFO"
    }
    val msg: String = lvlMessage + ": " + message
    if (lvlMessage == "TRACE") java.util.logging.Logger.getLogger(scriptName).info(msg)
    outputList.add(msg)
  }

  /**
   * Log to logfile at trace level
   *
   * @param message      message to log
   * @param replacements Array of replacements for {} place holders
   */
  def trace(message: String, replacements: AnyRef*) {
    log(LOGGING_MODE_ALL, message, replacements)
  }

  /**
   * Log to logfile at debug level
   *
   * @param message      message to log
   * @param replacements Array of replacements for {} place holders
   */
  def debug(message: String, replacements: AnyRef*) {
    log(LOGGING_MODE_DEBUG, message, replacements)
  }

  /**
   * Log to logfile at warning level
   *
   * @param message      message to log
   * @param replacements Array of replacements for {} place holders
   */
  def warn(message: String, replacements: AnyRef*) {
    log(LOGGING_MODE_WARNING, message, replacements)
  }

  /**
   * Log to logfile at info level
   *
   * @param message      message to log
   * @param replacements Array of replacements for {} place holders
   */
  def info(message: String, replacements: AnyRef*) {
    log(LOGGING_MODE_INFO, message, replacements)
  }

  /**
   * Log to logfile at trace level
   *
   * @param message message to log
   */
  def trace(message: String) {
    trace(message, "")
  }

  /**
   * Log to logfile at debug level
   *
   * @param message message to log
   */
  def debug(message: String) {
    debug(message, "")
  }

  /**
   * Log to logfile at warning level
   *
   * @param message message to log
   */
  def warn(message: String) {
    warn(message, "")
  }

  /**
   * Log to logfile at info level
   *
   * @param message message to log
   */
  def info(message: String) {
    info(message, "")
  }

  /**
   * Class for handling threaded file writing to prevent slowdowns
   */
  private[helpers] class OutputThread extends Runnable {
    def run() {
      while (!stop) {
        try {
          if (!outputList.isEmpty && wr != null) {
            try {
              wr.write(dFormat.format(new Date) + " " + scriptName + " " + outputList.get(0) + "\r\n")
              wr.flush()
              outputList.remove(0)
            }
            catch {
              case e: IOException => {
                System.err.println("Error writing output to log")
                e.printStackTrace()
              }
            }
          }
          Thread.sleep(25)
        }
        catch {
          case e: InterruptedException => {
            e.printStackTrace()
          }
        }
      }
    }
  }

}