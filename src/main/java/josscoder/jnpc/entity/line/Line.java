package josscoder.jnpc.entity.line;

import cn.nukkit.entity.mob.EntityCreeper;
import josscoder.jnpc.entity.npc.NPC;
import josscoder.jnpc.entity.spawnable.Spawnable;
import josscoder.jnpc.settings.AttributeSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Line extends Spawnable {

    private final int separator;

    @Setter
    private NPC linkedNPC;

    /**
     * @param separator The amount of separation between this line and the next one
     */
    public Line(int separator) {
        super(AttributeSettings.builder()
                        .networkId(EntityCreeper.NETWORK_ID)
                        .boundingBoxWidth(0f)
                        .scale(0.004f)
                        .build(),
                null
        );

        this.separator = separator;
    }
}
