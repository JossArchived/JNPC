package josscoder.jnpc;

import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.factory.NPCFactory;
import josscoder.jnpc.listener.GeneralListener;

public class JNPC {

    public static void init(PluginBase pluginBase) {
        NPCFactory.make();
        pluginBase.getServer().getPluginManager().registerEvents(new GeneralListener(), pluginBase);
    }
}
