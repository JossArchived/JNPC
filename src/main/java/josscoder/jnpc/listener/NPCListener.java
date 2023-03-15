package josscoder.jnpc.listener;

import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;

public interface NPCListener extends Listener {

    default void onJoinHandleEntities(PlayerJoinEvent event) {

    }

    default void onCleanupEntities(PlayerQuitEvent event) {

    }

    default void onLevelChangeHandleEntities(EntityLevelChangeEvent event) {

    }

    default void onLeftEntity(EntityDamageByEntityEvent event) {

    }

    default void onRightEntity(DataPacketReceiveEvent event) {

    }

    default void onKeepLookingEntity(PlayerMoveEvent event) {

    }
}
