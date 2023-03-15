package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.settings.AttributeSettings;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Line extends Spawnable {

    private final String name;
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
        SetEntityDataPacket packet = new SetEntityDataPacket();
        packet.eid = entityId;
        packet.metadata = new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(name));
        linkedNPC.getPlayerList().forEach(player -> player.dataPacket(packet));
    }

    @Override
    public void show(Player player) {
        EntityMetadata metadata = new EntityMetadata();
        metadata.putString(Entity.DATA_NAMETAG, TextFormat.colorize(name));
        metadata.putByte(Entity.DATA_ALWAYS_SHOW_NAMETAG, 1);
        metadata.putByte(Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG, 1);
        metadata.putByte(Entity.DATA_FLAG_CAN_SHOW_NAMETAG, 1);
        metadata.putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
        metadata.putFloat(Entity.DATA_BOUNDING_BOX_WIDTH, 0);
        metadata.putFloat(Entity.DATA_SCALE, 0.004f);
        mergeMetadata(metadata);

        super.show(player);
    }
}
