package josscoder.jnpc.entity;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;
import lombok.Getter;

@Getter
public class NPC implements ISpawnable {

    private final AttributeSettings attributeSettings;
    private final TagSettings tagSettings;

    protected long entityId = -1;

    public NPC(AttributeSettings attributeSettings, TagSettings tagSettings) {
        this.attributeSettings = attributeSettings;
        this.tagSettings = tagSettings;
    }

    public static NPC create(AttributeSettings attributeSettings, TagSettings tagSettings) {
        NPC npc = new NPC(attributeSettings, tagSettings);

        NPCFactory.getInstance().store(npc);

        return npc;
    }

    public void lookAt(Vector3 vector3) {

    }

    @Override
    public void show(Player player) {

    }

    @Override
    public void hide(Player player) {

    }
}
