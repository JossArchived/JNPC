package josscoder.jnpc.utils;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.custom.CustomEntityDefinition;
import cn.nukkit.entity.provider.CustomEntityProvider;

public class CustomEntityUtil {

    public static void updateStaticPacketCache(String id, String bid) {
        CustomEntityDefinition entityDefinition = CustomEntityDefinition.builder()
                .identifier(id)
                .spawnEgg(false)
                .summonable(false)
                .build();

        CustomEntityProvider entityProvider = new CustomEntityProvider(entityDefinition) {};

        Entity.registerCustomEntity(entityProvider);
    }
}
