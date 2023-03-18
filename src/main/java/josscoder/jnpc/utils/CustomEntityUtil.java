package josscoder.jnpc.utils;

import cn.nukkit.Nukkit;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AvailableEntityIdentifiersPacket;
import com.google.common.collect.ImmutableMap;
import josscoder.jnpc.exception.NPCException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class CustomEntityUtil {

    @SuppressWarnings("unchecked")
    public static void updateStaticPacketCache(int networkId, String id, String bid) {
        try {
            //MODIFY AddEntityPacket to support new entity

            Class<AddEntityPacket> addEntityPacketClass = AddEntityPacket.class;
            Field legacyIdsField = addEntityPacketClass.getField("LEGACY_IDS");
            legacyIdsField.setAccessible(true);

            Map<Integer, String> legacyIds = new HashMap<>((ImmutableMap<Integer, String>) legacyIdsField.get(null));
            legacyIds.put(networkId, id);

            ImmutableMap<Integer, String> immutableMap = ImmutableMap.copyOf(legacyIds);

            legacyIdsField.set(new AddEntityPacket(), immutableMap);

            //get entity_identifiers.dat bytes and add new entity

            InputStream inputStream = Nukkit.class.getClassLoader().getResourceAsStream("entity_identifiers.dat");
            CompoundTag nbtFile = NBTIO.read(inputStream, ByteOrder.BIG_ENDIAN, true);
            ListTag<CompoundTag> idlist = nbtFile.getList("idlist", CompoundTag.class);

            CompoundTag nbtEntry = new CompoundTag();
            nbtEntry.putBoolean("hasspawnegg", false)
                    .putBoolean("summonable", false)
                    .putString("id", id)
                    .putString("bid", bid)
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
    }
}
