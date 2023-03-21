package josscoder.jnpc.npc;

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
import josscoder.jnpc.utils.CustomEntityUtil;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class Spawnable implements ISpawnable {

    protected final AttributeSettings attributeSettings;
    protected final HumanAttributes humanSettings;
    protected final List<Player> viewerList = new ArrayList<>();

    protected long entityId;
    protected Set<EntityMetadata> mergedMetadataList = new HashSet<>();

    public Spawnable(AttributeSettings attributeSettings, HumanAttributes humanSettings) {
        this.attributeSettings = attributeSettings;

        if (!isHuman() && !Entity.getKnownEntityIds().contains(attributeSettings.getNetworkId())) {
            if (attributeSettings.isCustomEntity()) {
                String minecraftId = attributeSettings.getMinecraftId();
                String behaviorId = attributeSettings.getMinecraftBehaviorId() == null ? minecraftId : attributeSettings.getMinecraftBehaviorId();
                CustomEntityUtil.updateStaticPacketCache(minecraftId, behaviorId);
            } else {
                throw new NPCException("That NETWORK_ID does not exist, if you are using custom entities, put attributeSettings.customEntity(true)");
            }
        }

        this.humanSettings = humanSettings;
        this.entityId = Entity.entityCount++;
    }

    public boolean isHuman() {
        return attributeSettings.getNetworkId() == EntityHuman.NETWORK_ID;
    }

    /**
     * Action to add the metadata for when the entity spawns
     *
     * @param metadata the data
     */
    public void mergeMetadata(EntityMetadata metadata) {
        this.mergedMetadataList.add(metadata);
    }

    /**
     * Action to update the metadata when the entity is already spawned
     *
     * @param metadataList the data list
     */
    public void updateMetadata(List<EntityMetadata> metadataList) {
        metadataList.forEach(metadata -> {
            SetEntityDataPacket packet = new SetEntityDataPacket();
            packet.eid = entityId;
            packet.metadata = metadata;
            packet.frame = 0;
            viewerList.forEach(player -> {
                if (player != null) {
                    player.dataPacket(packet);
                }
            });
        });
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

        Float boundingBoxHeight = attributeSettings.getBoundingBoxHeight();
        if (boundingBoxHeight != null) {
            metadata.putFloat(Entity.DATA_BOUNDING_BOX_HEIGHT, boundingBoxHeight);
        }

        Float boundingBoxWidth = attributeSettings.getBoundingBoxWidth();
        if (boundingBoxWidth != null) {
            metadata.putFloat(Entity.DATA_BOUNDING_BOX_WIDTH, boundingBoxWidth);
        }

        mergedMetadataList.forEach(mergedMetadata -> mergedMetadata.getMap().values().forEach((metadata::put)));

        if (isHuman()) {
            Skin skin = humanSettings.getSkin();

            PlayerListPacket playerListAddPacket = new PlayerListPacket();
            playerListAddPacket.type = PlayerListPacket.TYPE_ADD;
            playerListAddPacket.entries = new PlayerListPacket.Entry[]{
                    new PlayerListPacket.Entry(uuid, entityId, uuid.toString(), skin)
            };
            player.dataPacket(playerListAddPacket);

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

            PlayerListPacket playerListRemovePacket = new PlayerListPacket();
            playerListRemovePacket.type = PlayerListPacket.TYPE_REMOVE;
            playerListRemovePacket.entries = new PlayerListPacket.Entry[]{
                    new PlayerListPacket.Entry(uuid)
            };
            player.dataPacket(playerListRemovePacket);

        } else {
            AddEntityPacket packet = new AddEntityPacket();
            packet.id = attributeSettings.getMinecraftId();
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

        if (!viewerList.contains(player)) {
            viewerList.add(player);
        }
    }

    @Override
    public void move(Location location) {
        attributeSettings.setLocation(location);

        MoveEntityAbsolutePacket packet = new MoveEntityAbsolutePacket();
        packet.eid = entityId;
        packet.forceMoveLocalEntity = true;
        packet.onGround = false;
        packet.teleport = true;
        packet.x = location.getX();
        packet.y = location.getY();
        packet.z = location.getZ();
        packet.yaw = location.getYaw();
        packet.headYaw = location.getHeadYaw();
        packet.pitch = location.getPitch();

        viewerList.forEach(player -> {
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

        viewerList.remove(player);
    }
}
