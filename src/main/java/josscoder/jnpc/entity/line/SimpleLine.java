package josscoder.jnpc.entity.line;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;

import java.util.ArrayList;

public class SimpleLine extends Line {

    @Getter
    private String name;

    /**
     * @param name The name of the line, what will be displayed
     */
    public SimpleLine(String name) {
        this(name, 1);
    }

    /**
     * @param name      The name of the line, what will be displayed
     * @param separator The amount of separation between this line and the next one
     */
    public SimpleLine(String name, int separator) {
        super(separator);

        this.name = name;

        mergeMetadata(new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(name)));
    }

    /**
     * @param name The new name of the line
     */
    public void rename(String name) {
        updateMetadata(new ArrayList<EntityMetadata>(){{
            add(new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(name)));
        }});
        this.name = name;
    }
}
