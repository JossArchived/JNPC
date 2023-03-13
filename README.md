<h1>JNPC
<img src="https://github.com/Josscoder/JNPC/blob/master/.github/assets/logo.png" height="64" width="64" align="left" alt="">
</h1>

## 📙 Description
Library to manage npcs on your Nukkit server

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/example.png)


# 📖 Features

- [x] Spawn any entity in minecraft
- [x] Entity scales
- [x] Fully manageable tag
- [x] 3D entities with animations
- [x] Click interaction
- [x] Simple API
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

### Build a Normal NPC

```java
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.entity.mob.EntityMagmaCube;
import cn.nukkit.item.ItemBed;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.JNPC;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.HumanAttributes;
import josscoder.jnpc.utils.NPCSkinUtils;

import java.io.File;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        //human
        NPC npc = NPC.create(AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                        .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                        .build(),
                HumanAttributes.builder()
                        .skin(NPCSkinUtils.from(new File(getDataFolder() + "/skins/").toPath().resolve("mySKin.png")))
                        .handItem(new ItemBed())
                        .build());

        //creeper
        NPC creeper = NPC.create(AttributeSettings.builder()
                .networkId(EntityCreeper.NETWORK_ID)
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        //entity with weight diff

        NPC magma = NPC.create(AttributeSettings.builder()
                .networkId(EntityMagmaCube.NETWORK_ID)
                .boundingBoxHeight(0.5f) //adjust tag to size
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());
    }
}

```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/test1.jpeg)
![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/actions.jpeg)

### Build a Custom NPC
```java
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.JNPC;
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

        NPC wayOne = NPC.create(AttributeSettings.builder()
                .customEntity(true) //mark this as true
                .minecraftId("clover:selectors_npc") //entity identifier
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        Path path = new File(getDataFolder() + "/skins/").toPath();
        Skin skinWithGeo = NPCSkinUtils.from(path.resolve("mySKin.png"), path.resolve("geo.json"), "custom.geo.skin");

        NPC wayTwo = NPC.create(AttributeSettings.builder()
                        .networkId(EntityHuman.NETWORK_ID)
                        .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                        .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                        .build(),
                HumanAttributes.builder()
                        .skin(skinWithGeo)
                        .build());
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/img.png)

### Add line to NPC Tag
```java
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.JNPC;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .customEntity(true)
                .minecraftId("clover:selectors_npc")
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&d&lNEW!", 2)) //with separator
                .addLine(new Line("&e&lSkyWars"))
                .addLine(new Line("&70 Playing"))
                .addLine(new Line("&aCLICK TO PLAY"))
                .build();
    }
}

```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/edit.jpeg)

### Rename line from NPC tag
```java
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import josscoder.jnpc.JNPC;
import josscoder.jnpc.entity.Line;
import josscoder.jnpc.entity.NPC;
import josscoder.jnpc.settings.AttributeSettings;
import josscoder.jnpc.settings.TagSettings;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);

        NPC npc = NPC.create(AttributeSettings.builder()
                .customEntity(true)
                .minecraftId("clover:selectors_npc")
                .location(new Location(0, 100, 0, 100, 0, getServer().getDefaultLevel()))
                .controller(player -> player.sendMessage(TextFormat.colorize("&bSending you tu SkyWars...")))
                .build());

        TagSettings tagSettings = npc.getTagSettings();
        tagSettings
                .addLine(new Line("&d&lNEW!", 2)) //with separator
                .addLine(new Line("&e&lSkyWars"))
                .addLine(new Line("&70 Playing"))
                .addLine(new Line("&aCLICK TO PLAY"))
                .build();

        tagSettings.getLine(0).rename("&c&lOLD!"); //change new to old
    }
}

```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/final.jpeg)

### Special thanks to my friends Brayan and Jose Luis for helping me with 3d entity support

## 📜 LICENSE

This plugin is licensed under the [GNU GENERAL PUBLIC LICENSE](https://github.com/Josscoder/JNPC/blob/master/LICENSE), this plugin was completely created by Josscoder (José Luciano Mejia Arias)
