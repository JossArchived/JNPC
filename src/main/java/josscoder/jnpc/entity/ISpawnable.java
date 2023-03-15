package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

public interface ISpawnable {

    /**
     * Action of displaying the entity
     *
     * @param player player to whom the entity is shown
     */
    void show(Player player);

    /**
     * Action of move the entity
     *
     * @param location Location where the entity will be moved
     */
    void move(Location location);

    /**
     * Action of hide the entity
     *
     * @param player player to whom the entity is hide
     */
    void hide(Player player);

}
