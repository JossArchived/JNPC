package josscoder.jnpc;

import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.listener.DefaultNPClListener;
import josscoder.jnpc.listener.NPCListener;

public class JNPC {

    /**
     * Action to start the library
     *
     * @param pluginBase plugin that enables the library
     */
    public static void init(PluginBase pluginBase) {
        JNPC.init(pluginBase, new DefaultNPClListener());
    }

    /**
     * Action to start the library
     *
     * @param pluginBase plugin that enables the library
     * @param npcListener the listener to handle NPCS actions
     */
    public static void init(PluginBase pluginBase, NPCListener npcListener) {
        init();
        pluginBase.getServer().getPluginManager().registerEvents(npcListener, pluginBase);
    }

    /**
     * Action to start the library without default listener
     */
    public static void init() {
        NPCFactory.make();
    }
}
