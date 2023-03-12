package josscoder.jnpc.settings;

import cn.nukkit.block.BlockAir;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HumanAttributes {

    private final Skin skin;

    @Builder.Default
    private final Item handItem = new BlockAir().toItem();

}
