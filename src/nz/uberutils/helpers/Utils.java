package nz.uberutils.helpers;

import nz.uberutils.wrappers.LootItem;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;


public class Utils
{
    private static final int    WALL = 0x200000;
    private static final Logger log  = Logger.getAnonymousLogger();

    /**
     * Finds if the current world is a members world or not.
     *
     * @return <tt>true</tt> if the current world is a members world.
     * @author UberMouse
     */
    public static boolean isWorldMembers() {
//        int world = 0;
//        try {
//            world = Integer.valueOf(Widgets.get(550).getComponent(19).getText().replaceAll("[^0-9]", ""));
//        } catch (NumberFormatException e) {
//            Game.openTab(Game.TAB_FRIENDS);
//            Game.openTab(Game.TAB_INVENTORY);
//            isWorldMembers();
//        }
//        WorldData w = WorldData.lookup(world);
//        return w.isMember();
        return false;
    }

    /**
     * Log text
     *
     * @param text info to be logged
     */
    public static void log(Object text) {
        log.info("" + text);
    }

    /**
     * Check if array contains String(s) check.
     *
     * @param array     the array to check
     * @param contains, using .contains instead of .equals
     * @param check     the String(s) to check
     * @return true, if successful
     */
    public static boolean arrayContains(String[] array, boolean contains, String... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (String i : array) {
            if (i == null)
                continue;
            for (String l : check) {
                if (l == null)
                    continue;
                if (contains) {
                    if (l.toLowerCase().contains(i.toLowerCase())) {
                        return true;
                    }
                }
                else {
                    if (i.toLowerCase().equals(l.toLowerCase()))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if array contains String(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(String[] array, String... check) {
        return arrayContains(array, false, check);
    }

    /**
     * Check if array contains int(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(int[] array, int... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (int i : array) {
            for (int l : check) {
                if (i == l)
                    return true;
            }
        }
        return false;
    }

    /**
     * Check if array of LootItems contains id(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(LootItem[] array, int... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (LootItem i : array) {
            for (int l : check) {
                if (i != null && i.getId() == l)
                    return true;
            }
        }
        return false;
    }

    /**
     * Check if array of LootItems contains names(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(LootItem[] array, String... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (LootItem i : array) {
            for (String l : check) {
                if (i != null &&
                    l != null &&
                    i.getName() != null &&
                    (i.getName().equalsIgnoreCase(l) || l.toLowerCase().contains(i.getName().toLowerCase())))
                    return true;
            }
        }
        return false;
    }


    public static boolean arrayContains(int[][] arrays, int... check) {
        for (int[] array : arrays) {
            if (arrayContains(array, check))
                return true;
        }
        return false;
    }

    public static Tile getNearestNonWallTile(Tile tile) {
        return getNearestNonWallTile(tile, false);
    }

    public static Tile getNearestNonWallTile(Tile tile, boolean eightTiles) {
        Tile[] checkTiles = getSurroundingTiles(tile, eightTiles);
        int[] flags = getSurroundingCollisionFlags(tile, eightTiles);
        for (int i = 0; i < checkTiles.length; i++) {
            if ((flags[i] & WALL) == 0)
                return checkTiles[i];
            //            else
            //                getNearestNonWallTile(tile);
        }
        return null;
    }

    public static Tile[] getSurroundingTiles(Tile tile) {
        return getSurroundingTiles(tile, false);
    }

    public static Tile[] getSurroundingTiles(Tile tile, boolean eightTiles) {
        int x = tile.getX();
        int y = tile.getY();
        Tile north = new Tile(x, y + 1, 0);
        Tile east = new Tile(x + 1, y, 0);
        Tile south = new Tile(x, y - 1, 0);
        Tile west = new Tile(x - 1, y, 0);
        Tile northEast;
        Tile southEast;
        Tile southWest;
        Tile northWest;
        if (eightTiles) {
            northEast = new Tile(x + 1, y + 1, 0);
            southEast = new Tile(x + 1, y - 1, 0);
            southWest = new Tile(x - 1, y - 1, 0);
            northWest = new Tile(x - 1, y + 1, 0);
            return new Tile[]{north, northEast, east, southEast, south, southWest, west, northWest};
        }
        return new Tile[]{north, east, south, west};
    }

    public static Tile[] getDiagonalTiles(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        Tile northEast;
        Tile southEast;
        Tile southWest;
        Tile northWest;
        northEast = new Tile(x + 1, y + 1, 0);
        southEast = new Tile(x + 1, y - 1, 0);
        southWest = new Tile(x - 1, y - 1, 0);
        northWest = new Tile(x - 1, y + 1, 0);
        return new Tile[]{northEast, southEast, southWest, northWest};
    }

    public static int[] getSurroundingCollisionFlags(Tile tile) {
        return getSurroundingCollisionFlags(tile, false);
    }

    public static int[] getSurroundingCollisionFlags(Tile tile, boolean eightTiles) {
        int[][] flags = Walking.getCollisionFlags(Game.getPlane());
        int x = tile.getX();
        int y = tile.getY();
        int xOff = x - Game.getMapBase().getX() - Walking.getCollisionOffset(Game.getPlane()).getX();
        int yOff = y - Game.getMapBase().getY() - Walking.getCollisionOffset(Game.getPlane()).getY();
        int fNorth = flags[xOff][yOff + 1];
        int fEast = flags[xOff + 1][yOff];
        int fSouth = flags[xOff][yOff - 1];
        int fWest = flags[xOff - 1][yOff];
        int fNorthEast;
        int fSouthEast;
        int fSouthWest;
        int fNorthWest;
        if (eightTiles) {
            fNorthEast = flags[xOff + 1][yOff + 1];
            fSouthEast = flags[xOff + 1][yOff - 1];
            fSouthWest = flags[xOff - 1][yOff - 1];
            fNorthWest = flags[xOff - 1][yOff + 1];
            return new int[]{fNorth, fNorthEast, fEast, fSouthEast, fSouth, fSouthWest, fWest, fNorthWest};
        }
        return new int[]{fNorth, fEast, fSouth, fWest};
    }

    public static int getCollisionFlagAtTile(Tile tile) {
        int[][] flags = Walking.getCollisionFlags(Game.getPlane());
        int x = tile.getX();
        int y = tile.getY();
        int xOff = x - Game.getMapBase().getX() - Walking.getCollisionOffset(Game.getPlane()).getX();
        int yOff = y - Game.getMapBase().getY() - Walking.getCollisionOffset(Game.getPlane()).getY();
        try {
            return flags[xOff][yOff];
        } catch (ArrayIndexOutOfBoundsException e) {
            log("Tile: " + tile);
            log("xOff: " + xOff + " yOff: " + yOff);
            log("Mapbase: " + Game.getMapBase());
            log("Collision offset: " + Walking.getCollisionOffset(Game.getPlane()));
            log("Flags arraycount: " + flags.length + " Flag subarray length: " + flags[0].length);
        }
        return -1;
    }

    public static Tile[] getLoadedTiles() {
        int[][] flags = Walking.getCollisionFlags(Game.getPlane());
        ArrayList<Tile> t = new ArrayList<Tile>();
        for (int i = 0; i < flags.length; i++) {
            int xOff = i + Game.getMapBase().getX() + Walking.getCollisionOffset(Game.getPlane()).getX();
            for (int j = 0; j < flags[i].length; j++) {
                int yOff = j + Game.getMapBase().getY() + Walking.getCollisionOffset(Game.getPlane()).getY();
                t.add(new Tile(xOff, yOff, 0));
            }
        }
        return t.toArray(new Tile[t.size()]);
    }

    public static void saveImage(Image image, String location) {
        saveImage(image, location, null);
    }

    public static void saveImage(Image image, String location, String type) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        //using "painter" we can draw in to "bufferedImage"
        Graphics2D painter = bufferedImage.createGraphics();

        //draw the "image" to the "bufferedImage"
        painter.drawImage(image, null, null);

        //the new image file
        File outputImg = new File(location);
        if (!outputImg.exists())
            try {
                outputImg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        //write the image to the file
        try {
            ImageIO.write(bufferedImage, (type != null) ? type : "jpg", outputImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage loadImage(String url, String path) {
        if (!path.endsWith("\\"))
            path += "\\";
        final String[] split = url.split("/");
        final String imgName = split[split.length - 1];
        final File file = new File(path + imgName);
        try {
            if (file.exists())
                return ImageIO.read(file);
            else {
                final BufferedImage read = ImageIO.read(new URL(url));
                if (read != null)
                    Utils.saveImage(read, path + imgName, url.substring(url.lastIndexOf(".")+1, url.length()));
                return read;
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    public static void openURL(String url) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        try {

            java.net.URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception ignored) {
        }
    }

    //    public static Object callMethod(Object object, String name) {
    //        try {
    //            Method[] allMethods = object.getClass().getDeclaredMethods();
    //            for (Method m : allMethods) {
    //                String mname = m.getName();
    //                if (!mname.equals(name)) {
    //                    continue;
    //                }
    //                try {
    //                    m.setAccessible(true);
    //                    return m.invoke(object);
    //
    //                    // Handle any exceptions thrown by method to be invoked.
    //                } catch (InvocationTargetException x) {
    //                    Throwable cause = x.getCause();
    //                    cause.printStackTrace();
    //                }
    //            }
    //        } catch (IllegalAccessException x) {
    //            x.printStackTrace();
    //        }
    //        return null;
    //    }
    //
    //    public static Object callMethod(Object object, String name, Object argument) {
    //        try {
    //            Method[] allMethods = object.getClass().getDeclaredMethods();
    //            for (Method m : allMethods) {
    //                String mname = m.getName();
    //                if (!mname.equals(name)) {
    //                    continue;
    //                }
    //                try {
    //                    m.setAccessible(true);
    //                    return m.invoke(object, argument);
    //
    //                    // Handle any exceptions thrown by method to be invoked.
    //                } catch (InvocationTargetException x) {
    //                    Throwable cause = x.getCause();
    //                    cause.printStackTrace();
    //                }
    //            }
    //        } catch (IllegalAccessException x) {
    //            x.printStackTrace();
    //        }
    //        return null;
    //    }

    public static String parseTime(long millis, boolean newFormat) {
        long time = millis / 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        String days = Integer.toString((int) (time / (3600 * 24)));
        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2)
                seconds = "0" + seconds;
            if (minutes.length() < 2)
                minutes = "0" + minutes;
            if (hours.length() < 2)
                hours = "0" + hours;
        }
        if (!newFormat)
            return hours + ":" + minutes + ":" + seconds;
        days = days + " day" + ((Integer.valueOf(days) != 1) ? "s" : "");
        hours = hours + " hour" + ((Integer.valueOf(hours) != 1) ? "s" : "");
        minutes = minutes + " minute" + ((Integer.valueOf(minutes) != 1) ? "s" : "");
        seconds = seconds + " second" + ((Integer.valueOf(seconds) != 1) ? "s" : "");
        return days + ", " + hours + ", " + minutes + ", " + seconds;
    }

    public static String parseTime(long millis) {
        return parseTime(millis, false);
    }

    /**
     * Check if skill is boosted
     *
     * @param skill skill to check
     * @return true if skill is boosted
     */
    public static boolean isBoosted(int skill) {
        return isBoosted(skill, true);
    }

    /**
     * Check if skill is boosted
     *
     * @param skill      skill to check
     * @param boostEarly boost skill before it's fully unboosted
     * @return true if skill is boosted
     */
    public static boolean isBoosted(int skill, boolean boostEarly) {
        if (boostEarly)
            return Skills.getLevel(skill) >= ((int) Math.ceil(Skills.getRealLevel(skill) * 1.05 + 3));
        else
            return Skills.getLevel(skill) >= Skills.getRealLevel(skill);
    }

    /**
     * Sleep script
     *
     * @param min min time to sleep
     * @param max max time to sleep
     * @deprecated
     */
    public static void sleep(int min, int max) {
        Time.sleep(min, max);
    }

    /**
     * Sleep script
     *
     * @param time time to sleep
     * @deprecated
     */
    public static void sleep(int time) {
        Time.sleep(time);
    }

    /**
     * Debugs text along with function caller and line numbers.
     *
     * @param text the text
     */
    public static void debug(Object text) {
        if (true) {
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            if (className.contains("$"))
                className = className.split("\\$")[1];
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stacktrace = stackTraceElements[2];
            String methodName = stacktrace.getMethodName();
            int lineNumber = stacktrace.getLineNumber();
            log.info("[" +
                     stackTraceElements[3].getClassName() +
                     "#" +
                     stackTraceElements[3].getMethodName() +
                     ":" +
                     stackTraceElements[3].getLineNumber() +
                     "] -> [" +
                     className +
                     "#" +
                     methodName +
                     ":" +
                     lineNumber +
                     "] -> " +
                     text);
        }
    }

    public static int random(int min, int max) {
        return Random.nextInt(min, max);
    }

    /**
     * Returns a Widget containing the search text
     *
     * @param text the text to search for
     * @return Widget if found, else null
     */
    public static WidgetChild getWidgetWithText(String text) {
        for (Widget widget : Widgets.getLoaded()) {
            for (WidgetChild comp : widget.getChildren()) {
                if (comp.getText().toLowerCase().contains(text.toLowerCase()))
                    return comp;
                for (WidgetChild comp2 : comp.getChildren()) {
                    if (comp2.getText().toLowerCase().contains(text.toLowerCase()))
                        return comp2;
                }
            }
        }
        return null;
    }

//    /**
//     * Is item noted (Returns false for items no on GE
//     *
//     * @param id item id to check
//     * @return true if item is noted
//     */
//    public static boolean isNoted(int id) {
//        try {
//            return GrandExchange.getItemName(id).equals("");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    /**
     * Perform p/h calculation
     *
     * @param num       number to calculate p/h on
     * @param startTime starttime to use
     * @return p/h calculation
     */
    public static int calcPH(int num, long startTime) {
        return (int) ((num) * 3600000D / (System.currentTimeMillis() - startTime));
    }

    public static void waitUntilMoving(int timeout) {
        for (int i = 0; i < timeout && !UberPlayer.isMoving(); i++)
            sleep(100);
    }

    public static void waitUntilStopped(int timeout) {
        for (int i = 0; i < timeout && UberPlayer.isMoving(); i++)
            sleep(100);
    }

    /**
     * Click Item
     *
     * @param item to click
     */
    public static void clickItem(Item item) {
        Mouse.move(item.getWidgetChild().getCentralPoint());
        //Mouse.moveRandomly(0, 4);
        Mouse.click(true);
    }

    /**
     * Sleeps until widget is valid
     *
     * @param c1   Main component ID
     * @param c2   Sub component ID
     * @param time Time to wait (1 = 100ms)
     */
    public static void sleepUntilValid(int c1, int c2, int time) {
        for (int i = 0; i < time && !Widgets.get(c1, c2).validate(); i++)
            Utils.sleep(100);
    }

    /**
     * Sleeps until widget is valid
     *
     * @param c1 Main component ID
     * @param c2 Sub component ID
     */
    public static void sleepUntilValid(int c1, int c2) {
        sleepUntilValid(c1, c2, 15);
    }

    /**
     * Get random Tile in Area
     *
     * @param area Area to get Tile in
     * @return random Tile in Area
     */
    public static Tile getRandomTile(Area area) {
        return area.getTileArray()[random(0, area.getTileArray().length-1)];
    }

    /**
     * Waits for player to start and stop moving
     *
     * @param timeout to use for waiting for movement and then waiting to stop
     */
    public static void waitToStop(int timeout) {
        waitUntilMoving(timeout);
        waitUntilStopped(timeout);
    }

    /*
     * Waits for player to start and stop moving
    */
    public static void waitToStop() {
        waitToStop(7);
    }
}