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

    /**
     * entity network id, example EntityHuman.NETWORK_ID
     */
    private int networkId;

    /**
     * define if the entity is custom or no
     */
    @Builder.Default
    private boolean customEntity = false;


    /**
     * custom entity id
     */
    private String minecraftId;

    /**
     * custom entity behaviorId
     */
    private String minecraftBehaviorId;

    /**
     * entity location x,y,z yaw, pitch, headYaw
     */
    @Builder.Default
    private Location location = new Location(0, 100, 0);

    /**
     * entity size
     */
    @Builder.Default
    private float scale = 1.0f;

    /**
     * Height from where the tag will start to be generated
     */
    @Builder.Default
    private float boundingBoxHeight = 1.8f;

    /**
     * the action to execute when touching the entity
     */
    @Builder.Default
    private NPCController controller = (npc, player) -> player.sendMessage("Hello world!");


    /**
     * define if you want the entity to follow you with its gaze all the time
     */
    @Builder.Default
    private boolean keepLooking = false;

}
