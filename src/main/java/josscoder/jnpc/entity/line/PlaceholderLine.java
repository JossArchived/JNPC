package josscoder.jnpc.entity.line;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.function.Function;

public class PlaceholderLine extends Line {

    @Getter
    private Function<Player, String> placeholder;

    /**
     * @param placeholder text rendering for single player
     * @param separator The amount of separation between this line and the next one
     */
    public PlaceholderLine(Function<Player, String > placeholder, int separator) {
        super(separator);
        this.placeholder = placeholder;
    }

    /**
     * @param placeholder the new placeholder
     */
    public void rename(Function<Player, String> placeholder) {
        this.placeholder = placeholder;

        viewerList.forEach(player -> {
            if (player != null) {
                render(player);
            }
        });
    }

    @Override
    public void show(Player player) {
        super.show(player);
        render(player);
    }

    private void render(Player player) {
        String output = placeholder.apply(player);

        updateMetadataForPlayer(new ArrayList<EntityMetadata>(){{
            add(new EntityMetadata().putString(Entity.DATA_NAMETAG, TextFormat.colorize(output)));
        }}, player);
    }
}
