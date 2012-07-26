package nz.uberutils.helpers;

import org.powerbot.concurrent.Task;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 11:51 AM
 * Package: nz.uberfalconry;
 */
public abstract class Strategy extends org.powerbot.concurrent.strategy.Strategy implements Task
{
    public void run() {
        UberScript.status = getStatus();
        loop();
    }

    abstract public void loop();

    abstract public boolean validate();

    abstract public String getStatus();
}
