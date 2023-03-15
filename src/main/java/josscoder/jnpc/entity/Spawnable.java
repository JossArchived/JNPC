package josscoder.jnpc.entity;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Location;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.*;
import com.google.common.collect.ImmutableMap;
import josscoder.jnpc.exception.NPCException;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.util.*;

@Getter
public abstract class Spawnable implements ISpawnable {

    protected final AttributeSettings attributeSettings;
    protected final HumanAttributes humanSettings;
    protected final List<Player> playerList = new ArrayList<>();

    protected long entityId;
    protected Set<EntityMetadata> mergedMetadataList = new HashSet<>();

    @SuppressWarnings("unchecked")
    public Spawnable(AttributeSettings attributeSettings, HumanAttributes humanSettings) {
        this.attributeSettings = attributeSettings;

        int networkId = attributeSettings.getNetworkId();
        if (networkId == 0) { //this is special for custom entities, to generate a different network id that doesn't interfere with the current ones
            int newRuntimeId = NPCFactory.getInstance().getCurrentRuntimeId();
            attributeSettings.setNetworkId(newRuntimeId);
            networkId = newRuntimeId;
        }

        if (!isHuman() && !AddEntityPacket.LEGACY_IDS.containsKey(networkId)) { // Hack to add custom entities
            if (attributeSettings.isCustomEntity()) {
                String minecraftId = attributeSettings.getMinecraftId();

                try {
                    //MODIFY AddEntityPacket to support new entity

                    Class<AddEntityPacket> addEntityPacketClass = AddEntityPacket.class;
                    Field legacyIdsField = addEntityPacketClass.getField("LEGACY_IDS");
                    legacyIdsField.setAccessible(true);

                    Map<Integer, String> legacyIds = new HashMap<>((ImmutableMap<Integer, String>) legacyIdsField.get(null));
                    legacyIds.put(networkId, minecraftId);

                    ImmutableMap<Integer, String> immutableMap = ImmutableMap.copyOf(legacyIds);

                    legacyIdsField.set(new AddEntityPacket(), immutableMap);

                    //get entity_identifiers.dat bytes and add new entity

                    InputStream inputStream = Nukkit.class.getClassLoader().getResourceAsStream("entity_identifiers.dat");
                    CompoundTag nbtFile = NBTIO.read(inputStream, ByteOrder.BIG_ENDIAN, true);
                    ListTag<CompoundTag> idlist = nbtFile.getList("idlist", CompoundTag.class);

                    String behaviorId = attributeSettings.getMinecraftBehaviorId() == null ? minecraftId : attributeSettings.getMinecraftBehaviorId();

                    CompoundTag nbtEntry = new CompoundTag();
                    nbtEntry.putBoolean("hasspawnegg", false)
                            .putBoolean("summonable", false)
                            .putString("id", minecraftId)
                            .putString("bid", behaviorId)
                            .putInt("rid", networkId);
                    idlist.add(nbtEntry);
                    nbtFile.putList(idlist);

                    byte[] bytesToWrite = NBTIO.write(nbtFile, ByteOrder.BIG_ENDIAN, true);

                    //MODIFY TAG of AvailableEntityIdentifiersPacket with new bytes
                    Class<AvailableEntityIdentifiersPacket> availableEntityIdentifiersPacketClass = AvailableEntityIdentifiersPacket.class;
                    Field tagField = availableEntityIdentifiersPacketClass.getDeclaredField("TAG");
                    tagField.setAccessible(true);
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(tagField, tagField.getModifiers() & ~Modifier.FINAL);
                    tagField.set(null, bytesToWrite);

                } catch (NoSuchFieldException | IllegalAccessException | IOException e) {
                    throw new NPCException("Error adding new NETWORK_ID: " + e);
                }
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
            playerListAdd.entries = new PlayerListPacket.Entry[]{
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
            playerListRemove.entries = new PlayerListPacket.Entry[]{
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
        packet.onGround = false;
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
