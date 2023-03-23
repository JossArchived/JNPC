package josscoder.jnpc;

import cn.nukkit.entity.passive.EntityAllay;
import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.entity.line.PlaceholderLine;
import josscoder.jnpc.entity.line.SimpleLine;
import josscoder.jnpc.entity.npc.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .networkId(EntityAllay.NETWORK_ID)
                .scale(5)
                .build()
        );

        TagSettings tagSettings = npc.getTagSettings();

        tagSettings
                .height(4)
                .addLine(new PlaceholderLine(player -> "You're welcome " + player.getName(), 2))
                .addLine(new SimpleLine("Enjoy everything new"))
                .adjust();

        getServer().getScheduler().scheduleDelayedTask(() -> {
            PlaceholderLine placeHolderLine = (PlaceholderLine) tagSettings.getLine(0);
            placeHolderLine.rename(player -> "You're not welcome " + player.getName());

            SimpleLine simpleLine = (SimpleLine) tagSettings.getLine(1);
            simpleLine.rename("Don't enjoy anything anymore, I hate you");

            getServer().broadcastMessage("Actualizado pa");
        }, 20 * 60);
    }
}
