package josscoder.jnpc.entity.spawnable;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

public interface ISpawnable {

    /**
     * Action to display the entity
     *
     * @param player player to whom the entity is shown
     */
    void show(Player player);

    /**
     * Action to move the entity
     *
     * @param location Location where the entity will be moved
     */
    void move(Location location);

    /**
     * Action to hide the entity
     *
     * @param player player to whom the entity is hide
     */
    void hide(Player player);

}
