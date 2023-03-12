package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.*;
import josscoder.jnpc.exception.NPCException;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class Spawnable implements ISpawnable {

    protected final AttributeSettings attributeSettings;
    protected final HumanAttributes humanSettings;
    protected final List<Player> playerList = new ArrayList<>();

    protected long entityId;
    protected Set<EntityMetadata> mergedMetadataList = new HashSet<>();

    public Spawnable(AttributeSettings attributeSettings, HumanAttributes humanSettings) {
        this.attributeSettings = attributeSettings;

        if (isHuman() && humanSettings == null) {
            throw new NPCException("humanSettings cannot be null when you are spawning a human npc");
        }

        this.humanSettings = humanSettings;
        this.entityId = Entity.entityCount++;
    }

    public boolean isHuman() {
        return attributeSettings.getNetworkId() == EntityHuman.NETWORK_ID;
    }

    public void mergeMetadata(EntityMetadata metadata) {
        this.mergedMetadataList.add(metadata);
    }

    public void updateMetadata(List<EntityMetadata> metadataList) {
        metadataList.forEach(metadata -> {
            SetEntityDataPacket packet = new SetEntityDataPacket();
            packet.eid = entityId;
            packet.metadata = metadata;
            packet.frame = 0;
            playerList.forEach(player -> player.dataPacket(packet));
        });
        mergedMetadataList.addAll(metadataList);
    }

    @Override
    public void show(Player player) {
        UUID uuid = UUID.randomUUID();
        Location location = attributeSettings.getLocation();

        EntityMetadata metadata = new EntityMetadata();
        metadata.putString(Entity.DATA_NAMETAG, "");
        metadata.putByte(Entity.DATA_ALWAYS_SHOW_NAMETAG, 1);
        metadata.putLong(Entity.DATA_LEAD_HOLDER_EID, -1);
        metadata.putFloat(Entity.DATA_SCALE, attributeSettings.getScale());

        this.mergedMetadataList.forEach(mergedMetadata -> mergedMetadata.getMap().values().forEach((metadata::put)));

        if (isHuman()) {
            Skin skin = humanSettings.getSkin();

            PlayerListPacket playerListAdd = new PlayerListPacket();
            playerListAdd.type = PlayerListPacket.TYPE_ADD;
            playerListAdd.entries = new PlayerListPacket.Entry[] {
                    new PlayerListPacket.Entry(uuid, entityId, uuid.toString(), skin)
            };
            player.dataPacket(playerListAdd);

            AddPlayerPacket packet = new AddPlayerPacket();
            packet.username = "";
            packet.entityRuntimeId = entityId;
            packet.entityUniqueId = entityId;
            packet.uuid = uuid;
            packet.x = (float) location.getX();
            packet.y = (float) location.getY();
            packet.z = (float) location.getZ();
            packet.yaw = (float) location.getYaw();
            packet.pitch = (float) location.getPitch();
            packet.speedX = (float) 0;
            packet.speedY = (float) 0;
            packet.speedZ = (float) 0;
            packet.item = humanSettings.getHandItem();
            packet.putSkin(skin);
            packet.metadata = metadata;
            player.dataPacket(packet);

            PlayerSkinPacket skinPacket = new PlayerSkinPacket();
            skinPacket.skin = skin;
            skinPacket.newSkinName = skin.getSkinId();
            skinPacket.oldSkinName = skin.getSkinId();
            skinPacket.uuid = uuid;
            player.dataPacket(skinPacket);

            PlayerListPacket playerListRemove = new PlayerListPacket();
            playerListRemove.type = PlayerListPacket.TYPE_REMOVE;
            playerListRemove.entries = new PlayerListPacket.Entry[] {
                    new PlayerListPacket.Entry(uuid)
            };
            player.dataPacket(playerListRemove);
        } else {
            AddEntityPacket packet = new AddEntityPacket();
            packet.type = attributeSettings.getNetworkId();
            packet.entityRuntimeId = entityId;
            packet.entityUniqueId = entityId;
            packet.x = (float) location.getX();
            packet.y = (float) location.getY();
            packet.z = (float) location.getZ();
            packet.yaw = (float) location.getYaw();
            packet.pitch = (float) location.getPitch();
            packet.headYaw = (float) location.getHeadYaw();
            packet.speedX = (float) 0;
            packet.speedY = (float) 0;
            packet.speedZ = (float) 0;
            packet.metadata = metadata;
            player.dataPacket(packet);
        }

        if (!playerList.contains(player)) {
            playerList.add(player);
        }
    }

    @Override
    public void move(Location location) {
        MoveEntityAbsolutePacket packet = new MoveEntityAbsolutePacket();
        packet.eid = entityId;
        packet.forceMoveLocalEntity = true;
        packet.onGround = true;
        packet.teleport = true;
        packet.x = location.getX();
        packet.y = location.getY();
        packet.z = location.getZ();
        packet.yaw = location.getYaw();
        packet.headYaw = location.getHeadYaw();
        packet.pitch = location.getPitch();

        attributeSettings.setLocation(location);

        playerList.forEach(player -> {
            if (player != null) {
                player.dataPacket(packet);
            }
        });
    }

    @Override
    public void hide(Player player) {
        RemoveEntityPacket packet = new RemoveEntityPacket();
        packet.eid = entityId;
        player.dataPacket(packet);

        playerList.remove(player);
    }
}
