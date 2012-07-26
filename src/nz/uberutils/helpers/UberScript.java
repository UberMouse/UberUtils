package nz.uberutils.helpers;

import nz.uberutils.helpers.tasks.PriceThread;
import nz.uberutils.paint.PaintController;
import nz.uberutils.paint.components.PDialogue;
import org.powerbot.concurrent.LoopTask;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.bot.Context;
import org.powerbot.game.bot.event.listener.PaintListener;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/17/11
 * Time: 5:13 PM
 * Package: nz.uberutils.helpers;
 */
public abstract class UberScript extends ActiveScript implements PaintListener,
        KeyListener,
        MouseListener,
        MouseMotionListener {
    public static   String   status   = "Starting...";
    protected final Manifest manifest = getClass().getAnnotation(Manifest.class);
    protected final String   name     = manifest.name();
    String sep = System.getProperty("file.separator");
    protected Logger   logger    = null;
    protected String[] changeLog = new String[]{"Not set"};
    protected IPaint   paintType = null;

    private void sleep(int min, int max) {
        Time.sleep(min, max);
    }

    private void sleep(int time) {
        Time.sleep(time);
    }

    public void setup() {
        logger = new Logger(Environment.getStorageDirectory().getAbsoluteFile() +
                                    sep +
                                    "uberbots" +
                                    sep +
                                    name +
                                    sep +
                                    "logs" +
                                    sep +
                                    name, name);
        if (!Game.isLoggedIn()) {
            logger.info("Not logged in... waiting for login random to finish");
            while (!Game.isLoggedIn() || Skills.getLevel(Skills.AGILITY) < 1)
                sleep(100);
            sleep(3000);
        }
        Options.setNode(name + Context.client().getCurrentUsername());
        getContainer().submit(new PriceThread());
        PaintController.reset();
        PaintController.startTimer();
        Options.add("prevVersion", manifest.version());
        if (Options.getDouble("prevVersion") < manifest.version()) {
            PaintController.addComponent(new PDialogue("Script has been updated!",
                                                       changeLog,
                                                       new Font("Arial", 0, 12),
                                                       PDialogue.ColorScheme.GRAPHITE,
                                                       PDialogue.Type.INFORMATION) {
                public void okClick() {
                    PaintController.removeComponent(this);
                }

                public boolean shouldHandleMouse() {
                    return shouldPaint();
                }

                public boolean shouldPaint() {
                    return Game.isLoggedIn();
                }
            });
        }
        getContainer().submit(new LoopThread());
        onBegin();
    }

    public void onStop() {
        Options.put("prevVersion", manifest.version());
        Options.save();
        logger.cleanup();
        onEnd();
    }

    public void onEnd() {

    }

    public void onBegin() {

    }

    public void keyTyped(KeyEvent keyEvent) {
        if (paintType == null)
            return;
        paintType.keyTyped(keyEvent);
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (paintType == null)
            return;
        paintType.keyPressed(keyEvent);
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (paintType == null)
            return;
        paintType.keyReleased(keyEvent);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseClicked(mouseEvent);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mousePressed(mouseEvent);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseReleased(mouseEvent);
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseEntered(mouseEvent);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseExited(mouseEvent);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseDragged(mouseEvent);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        if (paintType == null)
            return;
        paintType.mouseMoved(mouseEvent);
    }

    public void onRepaint(Graphics graphics) {
        if (paintType == null)
            return;
        if (paintType.paint(graphics))
            paint((Graphics2D) graphics);
    }

    protected void paint(Graphics2D g) {

    }

    public void update() {

    }

    private class LoopThread extends LoopTask {

        @Override
        public int loop() {
            update();
            return 100;
        }
    }
}