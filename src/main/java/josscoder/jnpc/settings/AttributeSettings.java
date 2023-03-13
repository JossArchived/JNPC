package josscoder.jnpc.settings;

import cn.nukkit.level.Location;
import josscoder.jnpc.controller.NPCController;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AttributeSettings {

    private int networkId;

    @Builder.Default
    private boolean customEntity = false;

    private String minecraftId;

    @Builder.Default
    private Location location = new Location(0, 100, 0);

    @Builder.Default
    private float scale = 1.0f;

    @Builder.Default
    private float boundingBoxHeight = 1.8f;

    @Builder.Default
    private NPCController controller = (npc, player) -> player.sendMessage("Hello world!");
}
