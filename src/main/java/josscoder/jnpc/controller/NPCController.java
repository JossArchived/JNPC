package josscoder.jnpc.controller;

import cn.nukkit.Player;
import josscoder.jnpc.entity.NPC;

public interface NPCController {

    /**
     * Handle the action when the npc is right/left clicked
     *
     * @param clickedNPC the NPC that was touched
     * @param player     the player who touched the NPC
     */
    void handle(NPC clickedNPC, Player player);

}
