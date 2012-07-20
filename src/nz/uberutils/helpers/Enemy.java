package nz.uberutils.helpers;

import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class Enemy {

	private static NPC npc;

	/**
	 * Gets the NPC.
	 *
	 * @return the npc
	 */
	public static NPC getNPC() {
		return npc;
	}

	/**
	 * Sets the NPC.
	 *
	 * @param npc the new npv
	 */
	public static void setNPC(NPC npc) {
		Enemy.npc = npc;
	}

	/**
	 * Get enemy hp percent.
	 *
	 * @return hp percent
	 */
	private static int hp() {
		return (npc != null) ? npc.getHpPercent() : 0;
	}

	/**
	 * Location.
	 *
	 * @return the enemy location
	 */
	public static Tile location() {
		return (npc != null) ? npc.getLocation() : null;
	}

	/**
	 * Checks if current enemy is valid.
	 *
	 * @return true, if is valid
	 */
	public static boolean isValid() {
		return (npc != null && hp() > 0);
	}

	/**
	 * Do action.
	 *
	 * @param action the action
	 * @return true, if successful
	 */
	public static boolean interact(String action) {
		return (npc != null) ? npc.interact(action) : null;
	}

	/**
	 * Pick enemy using ids.
	 *
	 * @param ids the valid ids
	 */
	public static NPC pickEnemy(final int... ids) {
		return NPCs.getNearest(new Filter<NPC>() {
            public boolean accept(NPC npc) {
                if (npc.getHpPercent() == 0)
                    return false;

                if (npc.getInteracting() != null && !npc.getInteracting().equals(UberPlayer.get()))
                    return false;

                return Utils.arrayContains(ids, npc.getId());
            }
        });
	}

    /**
	 * Pick enemy using names.
	 *
	 * @param names the valid names
	 */
    public static NPC pickEnemy(final String... names) {
        return NPCs.getNearest(new Filter<NPC>()
        {
            public boolean accept(NPC npc) {
                if (npc.getHpPercent() == 0)
                    return false;

                if (npc.getInteracting() != null && !npc.getInteracting().equals(UberPlayer.get()))
                    return false;

                return Utils.arrayContains(names, true, npc.getName());
            }
        });
    }

    /**
	 * Set enemy using ids.
	 *
	 * @param names the valid names
	 */
    public static void setEnemy(final String... names) {
        npc = pickEnemy(names);
    }

    /**
	 * Set enemy using ids.
	 *
	 * @param ids the valid ids
	 */
    public static void setEnemy(final int... ids) {
        npc = pickEnemy(ids);
    }

    /**
	 * Set enemy using supplied NPC.
	 *
	 * @param npc the npc to set it to
	 */
    public static void setEnemy(NPC npc) {
        Enemy.npc = npc;
    }

	/**
	 * Checks if is dead.
	 *
	 * @return true, if is dead
	 */
	public static boolean isDead() {
        return hp() == 0;
    }
}
