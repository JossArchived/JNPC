package josscoder.jnpc;

import cn.nukkit.Player;
import cn.nukkit.block.BlockBedrock;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        JNPC.init(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player pl = event.getPlayer();

        NPC npc = NPC.create(
                AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(pl.getLocation())
                        .controller(player -> player.sendMessage("Holaa"))
                        .build(),
                HumanAttributes.builder()
                        .skin(pl.getSkin())
                        .handItem(new BlockBedrock().toItem())
                .build()
        );

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&eHeader"))
                .addLine(new Line("&o&7Footer"))
                .build();
    }
}
