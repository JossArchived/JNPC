package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.level.Location;

public interface ISpawnable {

    void show(Player player);

    void move(Location location);

    void hide(Player player);

}
