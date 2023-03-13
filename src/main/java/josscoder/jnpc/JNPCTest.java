package josscoder.jnpc;

import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .customEntity(true)
                .minecraftId("clover:selectors_npc")
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&d&lNEW!", 2))
                .addLine(new Line("&e&lSkyWars"))
                .addLine(new Line("&70 Playing"))
                .addLine(new Line("&aCLICK TO PLAY"))
                .build();
    }
}
