package josscoder.jnpc.settings;

import cn.nukkit.level.Location;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class TagSettings {

    public static float ONE_BREAK_LINE = 0.32f;

    private final List<Line> lines = new ArrayList<>();

    @Setter
    private NPC linkedNPC;

    public TagSettings addLine(Line line) {
        lines.add(line);
        return this;
    }

    public Line getLine(int index) {
        return lines.get(index);
    }

    public void adjust() {
        AtomicInteger index = new AtomicInteger(0);

        Collections.reverse(lines);

        lines.forEach(line -> {
            line.setLinkedNPC(linkedNPC);

            AttributeSettings npcAttributes = linkedNPC.getAttributeSettings();

            Location location;

            if (index.get() == 0) {
                location = npcAttributes.getLocation().add(0, npcAttributes.getBoundingBoxHeight(), 0);
            } else {
                location = lines.get(index.get() - 1).getAttributeSettings().getLocation().add(0, (ONE_BREAK_LINE * line.getSeparator()), 0);
            }

            line.getAttributeSettings().setLocation(location);

            index.addAndGet(1);
        });
    }
}
