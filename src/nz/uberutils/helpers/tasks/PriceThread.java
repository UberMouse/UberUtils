package nz.uberutils.helpers.tasks;

import org.powerbot.concurrent.LoopTask;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/27/11
 * Time: 3:48 PM
 * Package: nz.artezombies.utils;
 */
public class PriceThread extends LoopTask {
//    private static final Map<Integer, Integer> prices = new HashMap<Integer, Integer>();
//    private static final ArrayList<Integer> priceQueue = new ArrayList<Integer>();

    @Override
    public int loop() {
//        try {
//            if (priceQueue.size() > 0) {
//                int id = priceQueue.remove(0);
//                GeItem item = GeItem.lookup((Utils.isNoted(id)) ? id - 1 : id);
//                if (item == null) {
//                    prices.put(id, -2);
//                    return 50;
//                }
//                prices.put(id, item.getGuidePrice());
//            }
//        } catch (Exception ignored) {
//        }
//        return 50;
        return 1000;
    }

   public static int priceForId(int id) {
//        if (prices.containsKey(id))
//            return prices.get(id);
//        else {
//            if (!priceQueue.contains(id))
//                priceQueue.add(id);
//            return -1;
//        }
       return 1;
    }
//
    public static void addPrice(int id, int price) {
//        prices.put(id, price);,
    }
}
