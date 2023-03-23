package josscoder.jnpc.entity.npc;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import josscoder.jnpc.settings.TagSettings;
import josscoder.jnpc.entity.spawnable.Spawnable;
import lombok.Getter;

@Getter
public class NPC extends Spawnable {

    private final TagSettings tagSettings;

    private NPC(AttributeSettings attributeSettings, HumanAttributes humanSettings) {
        super(attributeSettings, humanSettings);

        this.tagSettings = new TagSettings();
        this.tagSettings.setLinkedNPC(this);
    }

    /**
     * Action to create the new entities
     *
     * @param attributeSettings entity attributes
     * @param humanSettings the attributes of the entity human
     * @return the NPC
     */
    public static NPC create(AttributeSettings attributeSettings, HumanAttributes humanSettings, boolean store) {
        NPC npc = new NPC(attributeSettings, humanSettings);

        if (store) {
            NPCFactory.getInstance().store(npc);
        }

        return npc;
    }

    public static NPC create(AttributeSettings attributeSettings, HumanAttributes humanAttributes) {
        return create(attributeSettings, humanAttributes, true);
    }

    /**
     * Action to create the new entities
     *
     * @param attributeSettings entity attributes
     * @return the NPC
     */
    public static NPC create(AttributeSettings attributeSettings) {
        return NPC.create(attributeSettings, null);
    }

    /**
     * Action to make the entity look at a position
     *
     * @param vector3 the position at which the NPC will look
     */
    public void lookAt(Vector3 vector3) {
        lookAt(vector3, false);
    }

    /**
     * Action to make the entity look at a position
     *
     * @param vector3 the position at which the NPC will look
     * @param update  this is left false when you add it when creating the entity, because in theory there is not yet a position to update and true when the entity is already created and you want it to update its position
     */
    public void lookAt(Vector3 vector3, boolean update) {
        Location location = attributeSettings.getLocation();

        double xDist = (vector3.getX() - location.getX());
        double zDist = (vector3.getZ() - location.getZ());

        location.yaw = Math.atan2(zDist, xDist) / Math.PI * 180 - 90;

        if (location.yaw < 0) {
            location.yaw += 360.0;
        }

        if (update) {
            move(location);
        }
    }

    @Override
    public void show(Player player) {
        super.show(player);
        spawnLines(player);
    }

    @Override
    public void move(Location location) {
        Location oldLocation = attributeSettings.getLocation();

        super.move(location);

        if (location.getX() != oldLocation.getX() || location.getY() != oldLocation.getY() || location.getZ() != oldLocation.getZ()) {
            tagSettings.readjust(location);
        }
    }

    @Override
    public void hide(Player player) {
        super.hide(player);
        hideLines(player);
    }

    /**
     * Action to display all lines to player
     *
     * @param player player to whom the tag is shown
     */
    public void spawnLines(Player player) {
        tagSettings.getLines().forEach(line -> line.show(player));
    }

    /**
     * Action to hide all lines from player
     *
     * @param player player to whom the tag is hide
     */
    public void hideLines(Player player) {
        tagSettings.getLines().forEach(line -> line.hide(player));
    }

    /**
     * Action to reload all lines of the tag
     */
    public void reloadLines() {
        viewerList.forEach(player -> {
            if (player != null) {
                hideLines(player);
                spawnLines(player);
            }
        });
    }
}
