package josscoder.jnpc;

import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .networkId(EntityCreeper.NETWORK_ID)
                .keepLooking(true)
                .scale(2)
                .boundingBoxHeight(3f)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&a&lKarl the creeper"))
                .addLine(new Line("&o&7Click to receive love!"))
                .adjust();
    }
}
