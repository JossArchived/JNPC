package josscoder.jnpc.settings;

import cn.nukkit.block.BlockAir;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HumanAttributes {

    /**
     * the entity human skin
     */
    private final Skin skin;

    /**
     * the entity human in hand item
     */
    @Builder.Default
    private final Item handItem = new BlockAir().toItem();

}
