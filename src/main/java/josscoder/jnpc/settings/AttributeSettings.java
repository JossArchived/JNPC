package josscoder.jnpc.settings;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import josscoder.jnpc.controller.NPCController;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AttributeSettings {

    private final int networkId;

    @Builder.Default
    private final Vector3 position = new Vector3(0, 100, 0);

    private final Level level;

    @Builder.Default
    private final float scale = 1.0f;

    @Builder.Default
    private final NPCController controller = player -> player.sendMessage("Hello world!");

    //Only for HumanEntity
    private final Skin skin;
    private final ItemArmor helmet;
    private final ItemArmor chestPlate;
    private final ItemArmor leggings;
    private final ItemArmor boots;
    private final Item handItem;
}
