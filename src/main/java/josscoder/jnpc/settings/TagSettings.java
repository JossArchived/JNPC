package josscoder.jnpc.settings;

import cn.nukkit.level.Location;
import josscoder.jnpc.entity.line.Line;
import josscoder.jnpc.entity.npc.NPC;
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

    private float height = 1.8f;

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
        return lines.get(Math.max(lines.size() - index - 1, 0));
    }

    /**
     * Height from where the tag will start to be generated
     */
    public TagSettings height(float height) {
        this.height = height;
        return this;
    }

    public void readjust(Location location) {
        AtomicInteger index = new AtomicInteger(0);

        lines.forEach(line -> {
            Location lineLoc;

            if (index.get() == 0) {
                lineLoc = location.getLocation().add(0, height);
            } else {
                lineLoc = lines.get(index.get() - 1).getAttributeSettings().getLocation().add(0, (ONE_BREAK_LINE * line.getSeparator()));
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

            Location location;

            if (index.get() == 0) {
                location = linkedNPC.getAttributeSettings().getLocation().add(0, height);
            } else {
                location = lines.get(index.get() - 1).getAttributeSettings().getLocation().add(0, (ONE_BREAK_LINE * line.getSeparator()));
            }

            line.getAttributeSettings().setLocation(location);

            index.addAndGet(1);
        });
    }
}
