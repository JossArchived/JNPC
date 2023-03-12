package josscoder.jnpc.entity;

import cn.nukkit.Player;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Line implements ISpawnable {

    private final NPC linkedNPC;
    private final String name;

    @Override
    public void show(Player player) {

    }

    @Override
    public void hide(Player player) {

    }
}
