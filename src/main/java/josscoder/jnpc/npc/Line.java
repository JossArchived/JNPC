package josscoder.jnpc.npc;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.settings.AttributeSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@ToString
public class Line extends Spawnable {

    private String name;
    private final int separator;
    @Setter
    private NPC linkedNPC;

    /**
     * @param name The name of the line, what will be displayed
     */
    public Line(String name) {
        this(name, 1);
    }

    /**
     * @param name      The name of the line, what will be displayed
     * @param separator The amount of separation between this line and the next one
     */
    public Line(String name, int separator) {
        super(AttributeSettings.builder()
                .networkId(EntityCreeper.NETWORK_ID)
                .boundingBoxWidth(0f)
                .scale(0.004f)
                .build(),
                null
        );

        this.name = name;
        this.separator = separator;
    }

    /**
     * @param name The new name of the line
     */
    public void rename(String name) {
        updateMetadata(new ArrayList<EntityMetadata>(){{
            add(new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(name)));
        }});
        this.name = name;
    }

    @Override
    public void show(Player player) {
        mergeMetadata(new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(name)));
        super.show(player);
    }
}
