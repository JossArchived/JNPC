import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.JNPC;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);
    }
}
