<h1>JNPC
<img src="https://github.com/Josscoder/JNPC/blob/master/.github/assets/logo.png" height="64" width="64" align="left" alt="">
</h1>

## 📙 Description

Library to manage npcs on your Nukkit server

# 📖 Features

- [x] Spawn any entity in minecraft
- [x] Entity scales
- [x] Fully manageable tag
- [x] 3D entities with animations
- [x] Click interaction
- [x] Simple API
- [x] Entity rotation
- [ ] Emote support for Human Entities
- [ ] Paths to walk

## 🌏 Add as dependency

Maven:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
<groupId>com.github.Josscoder</groupId>
<artifactId>JNPC</artifactId>
<version>1.0.0</version>
</dependency>
```

### Setup

```java
import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.JNPC;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);
    }
}
```

### Build a normal NPC

```java
package josscoder.jnpc;

import cn.nukkit.entity.mob.EntityEnderDragon;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC.create(AttributeSettings.builder()
                .networkId(EntitySheep.NETWORK_ID)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + npc.getEntityId())))
                .build());
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/1.png)

### Build a human NPC

Well, there are 3 ways, getting the skin from the player, getting the skin with our NPCSkinUtils class /
and/or obtaining a skin with geometry and texture with the same class, let's see:

#### Way #1

```java
package josscoder.jnpc;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.ItemEndCrystal;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;

public class JNPCTest extends PluginBase implements Listener {

    @Override
    public void onEnable() {
        JNPC.init(this);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        HumanAttributes humanAttributes = HumanAttributes.builder()
                .skin(event.getPlayer().getSkin())
                .handItem(new ItemEndCrystal())
                .build();

        NPC.create(AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                        .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                        .build(),
                humanAttributes
        );
    }
}
```

#### Way #2

```java
package josscoder.jnpc;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.event.Listener;
import cn.nukkit.item.ItemEndCrystal;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import josscoder.jnpc.utils.NPCSkinUtils;

import java.io.File;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        HumanAttributes humanAttributes = HumanAttributes.builder()
                .skin(NPCSkinUtils.from(new File(getDataFolder() + "/skins/").toPath().resolve("mySKin.png"))) //normal skin
                .handItem(new ItemEndCrystal())
                .build();

        NPC.create(AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                        .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                        .build(),
                humanAttributes
        );
    }
}
```

#### Way #3

```java
package josscoder.jnpc;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.Listener;
import cn.nukkit.item.ItemEndCrystal;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import josscoder.jnpc.utils.NPCSkinUtils;

import java.io.File;
import java.nio.file.Path;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        Path path = new File(getDataFolder() + "/skins/").toPath();
        Skin skinWithGeo = NPCSkinUtils.from(path.resolve("mySKin.png"), path.resolve("geo.json"), "custom.geo.skin");

        HumanAttributes humanAttributes = HumanAttributes.builder()
                .skin(skinWithGeo) // texture with geometry way #1
                .handItem(new ItemEndCrystal())
                .build();

        NPC.create(AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                        .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                        .build(),
                humanAttributes
        );
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/5.png)

### How to use controller (when click a NPC)

```java
package josscoder.jnpc;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.mob.EntityEnderman;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC.create(AttributeSettings.builder()
                .networkId(EntityEnderman.NETWORK_ID)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId()))) // here will send a message when the player clicking the NPC
                .build()
        );
    }
}
```

### Build a 3d NPC from TexturePack

```java
package josscoder.jnpc;

import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC.create(AttributeSettings.builder()
                .customEntity(true)
                .minecraftId("clover:ffa_npc")
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                .build()
        );
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/6.png)

### How to use the tag and lines

```java
package josscoder.jnpc;

import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .customEntity(true)
                .minecraftId("clover:ffa_npc")
                .boundingBoxHeight(2f)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                .build()
        );

        npc.getTagSettings()
                .addLine(new Line("&d&lFirst Header", 2)) //line with spaces
                .addLine(new Line("&eSub header")) //normal line
                .addLine(new Line("&o&7Footer")) //normal line
                .adjust(); //ajust lines

        npc.getTagSettings().getLine(0).rename("&bNew First"); //rename, Maybe this is good for player counters in game npcs!
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/7.png)
![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/8.png)

### Play with the sizes

```java
package josscoder.jnpc;

import cn.nukkit.entity.mob.EntitySlime;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .networkId(EntitySlime.NETWORK_ID)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                .build());

        npc.getTagSettings()
                .addLine(new Line("&d&lFirst Header", 2))
                .addLine(new Line("&eSub header"))
                .addLine(new Line("&o&7Footer"))
                .adjust();
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/2.png)

As we can see the slime is very small and the tag is very high, we have the solution for it:

```java
package josscoder.jnpc;

import cn.nukkit.entity.mob.EntitySlime;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .networkId(EntitySlime.NETWORK_ID)
                .boundingBoxHeight(0.5f) //Here!
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                .build());

        npc.getTagSettings()
                .addLine(new Line("&d&lFirst Header", 2))
                .addLine(new Line("&eSub header"))
                .addLine(new Line("&o&7Footer"))
                .adjust();
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/3.png)
*Wow, now it works great!*

### The legends were true... there are giant zombies!

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/4.png)

But... how did you do it? Simple, this way:

```java
package josscoder.jnpc;

import cn.nukkit.entity.mob.EntityZombie;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC.create(AttributeSettings.builder()
                .networkId(EntityZombie.NETWORK_ID)
                .scale(10f) //Here!
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage(TextFormat.colorize("Hello i'm NPC " + clickedNPC.getEntityId())))
                .build());
    }
}
```

### Follow player with the look (keepLooking)

```java
package josscoder.jnpc;

import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .networkId(EntityCreeper.NETWORK_ID)
                .keepLooking(true) //Here!
                .scale(2)
                .boundingBoxHeight(3f)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller((clickedNPC, player) -> player.sendMessage("Mineplex is better than josscodercraft"))
                .build());

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&a&lKarl the creeper"))
                .addLine(new Line("&o&7Click to receive love!"))
                .adjust();
    }
}

```

### Special thanks to my friends Brayan and Jose Luis for helping me with 3d entity support

## 📜 LICENSE

This plugin is licensed under the [GNU GENERAL PUBLIC LICENSE](https://github.com/Josscoder/JNPC/blob/master/LICENSE),
this plugin was completely created by Josscoder (José Luciano Mejia Arias)
