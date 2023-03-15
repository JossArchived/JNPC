package josscoder.jnpc.settings;

import cn.nukkit.level.Location;
import josscoder.jnpc.npc.Line;
import josscoder.jnpc.npc.NPC;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class TagSettings {

    /**
     * Separation between line
     */
    private static final float ONE_BREAK_LINE = 0.32f;

    private final List<Line> lines = new ArrayList<>();

    @Setter
    private NPC linkedNPC;

    /**
     * Action to add a new line
     *
     * @param line the line class
     * @return TagSettings class
     */
    public TagSettings addLine(Line line) {
        lines.add(line);
        return this;
    }

    /**
     * Action to get the line by index
     *
     * @param index the index
     * @return the line or null
     */
    public Line getLine(int index) {
        return lines.get(index);
    }

    public void readjust(Location location) {
        AtomicInteger index = new AtomicInteger(0);

        lines.forEach(line -> {
            Location lineLoc;

            if (index.get() == 0) {
                lineLoc = location.getLocation().add(0, linkedNPC.getAttributeSettings().getBoundingBoxHeight(), 0);
            } else {
                lineLoc = lines.get(index.get() - 1).getAttributeSettings().getLocation().add(0, (ONE_BREAK_LINE * line.getSeparator()), 0);
            }

            line.move(lineLoc);

            index.addAndGet(1);
        });
    }

    /**
     * adjust the lines so they are not crowded
     */
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
