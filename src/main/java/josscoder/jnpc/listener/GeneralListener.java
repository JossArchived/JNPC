package josscoder.jnpc.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.TransactionData;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import josscoder.jnpc.factory.NPCFactory;

public class GeneralListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NPCFactory.getInstance().showLevelNPCS(player.getLevel(), player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        NPCFactory.getInstance().hideLevelNPCS(player.getLevel(), player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLevelChange(EntityLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        Level origin = event.getOrigin();
        NPCFactory.getInstance().hideLevelNPCS(origin, player);

        Level target = event.getTarget();
        NPCFactory.getInstance().showLevelNPCS(target, player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onAttackEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            return;
        }

        NPCFactory.getInstance().handleClickNPC(event.getEntity().getId(), (Player) damager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onClickEntity(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (!(packet instanceof InventoryTransactionPacket)) {
            return;
        }

        InventoryTransactionPacket transactionPacket = (InventoryTransactionPacket) packet;

        if (transactionPacket.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY) {
            return;
        }

        InventoryTransactionPacket inventoryTransactionPacket = (InventoryTransactionPacket) packet;

        TransactionData data = inventoryTransactionPacket.transactionData;
        if (!(data instanceof UseItemOnEntityData)) {
            return;
        }

        NPCFactory.getInstance().handleClickNPC(((UseItemOnEntityData) data).entityRuntimeId, event.getPlayer());
    }
}
