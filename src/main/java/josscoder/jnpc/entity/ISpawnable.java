package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;

public interface ISpawnable {

    void show(Player player);

    void move(Vector3 vector3);

    void hide(Player player);

}
