package josscoder.jnpc.settings;

import josscoder.jnpc.entity.Line;
import lombok.Getter;

import java.util.LinkedList;

@Getter
public class TagSettings {

    private final LinkedList<Line> lines = new LinkedList<>();

    public void addLine(Line line) {
        lines.add(line);
    }

    public void setLine(int index, Line line) {
        lines.add(index, line);
    }

    public void setHeader(Line line) {
        lines.addFirst(line);
    }

    public void setFooter(Line line) {
        lines.addLast(line);
    }

    public Line getLine(int index) {
        return lines.get(index);
    }
}
