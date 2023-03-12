package josscoder.jnpc.factory;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import josscoder.jnpc.controller.NPCController;
import josscoder.jnpc.entity.NPC;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NPCFactory {

    @Getter
    private static NPCFactory instance;

    private final List<NPC> npcList = new ArrayList<>();

    public NPCFactory() {
        instance = this;
    }

    public static void make() {
        new NPCFactory();
    }

    public void store(NPC npc) {
        npcList.add(npc);
    }

    public List<NPC> filter(Predicate<? super NPC> predicate) {
        return npcList.stream().filter(predicate).collect(Collectors.toList());
    }

    public List<NPC> filterByLevel(Level level) {
        return filter(npc -> npc.getAttributeSettings().getLocation().getLevel().equals(level));
    }

    public void hideLevelNPCS(Level level, Player player) {
        filterByLevel(level).forEach(npc -> npc.hide(player));
    }

    public void showLevelNPCS(Level level, Player player) {
        filterByLevel(level).forEach(npc -> npc.show(player));
    }

    public void handleClickNPC(long entityId, Player player) {
        Optional<NPC> npcToHandle = npcList.stream().filter(npc -> npc.getEntityId() == entityId).findFirst();
        if (!npcToHandle.isPresent()) {
            return;
        }

        NPCController controller = npcToHandle.get().getAttributeSettings().getController();
        if (controller == null) {
            return;
        }

        controller.handle(player);
    }
}
