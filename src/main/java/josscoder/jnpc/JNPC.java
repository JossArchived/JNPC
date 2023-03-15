package josscoder.jnpc;

import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.listener.DefaultNPClListener;
import josscoder.jnpc.listener.NPCListener;

public class JNPC {

    public static void init(PluginBase pluginBase) {
        JNPC.init(pluginBase, new DefaultNPClListener());
    }

    public static void init(PluginBase pluginBase, NPCListener npcListener) {
        NPCFactory.make();
        pluginBase.getServer().getPluginManager().registerEvents(npcListener, pluginBase);
    }
}
