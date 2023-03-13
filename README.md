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
import cn.nukkit.plugin.PluginBase;
import josscoder.jnpc.JNPC;

public class JNPCTest extends PluginBase {

    @Override
    public void onEnable() {
        JNPC.init(this);
    }
}
```

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/test1.jpeg)
![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/actions.jpeg)

### Build a Custom NPC
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

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/img.jpeg)

### Add line to NPC Tag
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

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/edit.jpeg)

### Rename line from NPC tag
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

![](https://github.com/Josscoder/JNPC/blob/master/.github/assets/final.jpeg)

### Special thanks to my friends Brayan and Jose Luis for helping me with 3d entity support

## 📜 LICENSE

This plugin is licensed under the [GNU GENERAL PUBLIC LICENSE](https://github.com/Josscoder/JNPC/blob/master/LICENSE), this plugin was completely created by Josscoder (José Luciano Mejia Arias)
