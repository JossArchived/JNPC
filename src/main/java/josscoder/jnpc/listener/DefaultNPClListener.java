package josscoder.jnpc.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.TransactionData;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import josscoder.jnpc.factory.NPCFactory;

public class DefaultNPClListener implements NPCListener {

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinHandleEntities(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NPCFactory.getInstance().showLevelNPCS(player.getLevel(), player);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCleanupEntities(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        NPCFactory.getInstance().hideLevelNPCS(player.getLevel(), player);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLevelChangeHandleEntities(EntityLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        Level origin = event.getOrigin();
        NPCFactory.getInstance().hideLevelNPCS(origin, player);

        Level target = event.getTarget();
        NPCFactory.getInstance().showLevelNPCS(target, player);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeftClickEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }

        NPCFactory.getInstance().handleNPCController(event.getEntity().getId(), (Player) damager);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClickEntity(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (!(packet instanceof InventoryTransactionPacket transactionPacket)) {
            return;
        }

        if (transactionPacket.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY) {
            return;
        }

        TransactionData data = transactionPacket.transactionData;
        if (!(data instanceof UseItemOnEntityData)) {
            return;
        }

        NPCFactory.getInstance().handleNPCController(((UseItemOnEntityData) data).entityRuntimeId, event.getPlayer());
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKeepLookingEntity(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        double distance = from.distance(to);
        if (distance < 0.1d || distance > 20d) {
            return;
        }

        NPCFactory.getInstance().filterByLevel(player.getLevel())
                .stream().filter(npc -> npc.getAttributeSettings().isKeepLooking())
                .forEach(npc -> npc.keepLooking(player.getLocation().asVector3f().asVector3(), player));
    }
}
