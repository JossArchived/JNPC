package josscoder.jnpc.controller;

import cn.nukkit.Player;
import josscoder.jnpc.entity.NPC;

public interface NPCController {

    void handle(NPC npc, Player player);

}
