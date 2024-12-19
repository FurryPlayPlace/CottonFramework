# 🧵 Cotton Framework

[![Version](https://img.shields.io/badge/version-1.0.0-brightgreen)](https://github.com/FurryPlayPlace/CottonFramework)
[![Fabric API](https://img.shields.io/badge/Fabric-1.21.x-blue.svg)](https://fabricmc.net/)
[![Modding Framework](https://img.shields.io/badge/Framework-ServerSide-success)](https://github.com/FurryPlayPlace/CottonFramework)
[![License](https://img.shields.io/github/license/FurryPlayPlace/CottonFramework)](https://github.com/FurryPlayPlace/CottonFramework/blob/main/LICENSE)

---

## 🌟 Introduction

Cotton Framework is a modern and lightweight modding framework built using **Fabric**. It simplifies the development of *
*server-side plugins** that interact seamlessly with custom mods. Designed to provide maximum flexibility,
Cotton Framework empowers developers to focus on creating feature-rich plugins while managing the complexities of mod
interaction behind the scenes.

Whether you're an experienced Fabric developer or new to Minecraft modding, Cotton Framework will guide you towards
building better server-side systems and features for your Minecraft mods.

---

## 🚀 Features

- 🛠 **Streamlined plugin development** – Build server-side mods faster with intuitive APIs.
- 🌐 **Fabric-powered** – Built specifically for mods using the Fabric ecosystem.
- 📜 **Highly configurable** – Provide flexible configuration options for users and developers.
- 🔗 **Custom event handlers** – Easily manage game events and interactions.
- 📦 **Dependency support** – Integrates seamlessly with other mods and libraries.
- ✅ **Lightweight and efficient** – Optimized to reduce performance overhead on servers.
- 🔄 **Bukkit-inspired event system** – Events are ported from Bukkit and are being implemented using Mixins and ASM to provide compatibility and flexibility.

---

## 📖 Getting Started

Follow the steps below to get started with **CottonFramework**:

### Prerequisites

- **Minecraft**: Server version compatible with Fabric.
- **Fabric API**: Ensure you have the latest version installed.
- **Java**: Minimum version 17.

### Installation

1. Install the **Fabric Loader** from [fabricmc.net](https://fabricmc.net/).
2. Add the **Fabric API** mod to your server's `/mods` folder.
3. Drop the latest version of **Cotton Framework** into your server's `/mods` folder from
   the [Releases](https://github.com/FurryPlayPlace/CottonFramework/releases) page.

---

## 💻 Usage

To start using CottonFramework in your mods:

1. Add **CottonFramework** as a dependency in your `build.gradle` or `fabric.mod.json`.
2. Import the provided APIs to create custom plugins for your server:

### Example Registering a Custom Plugin

Here’s an example of registering a custom game event:

```java
import net.furryplayplace.cotton.api.CottonAPI;
import net.furryplayplace.cotton.api.plugin.CottonPlugin;
import net.fabricmc.api.ModInitializer;

public class MyPlugin implements CottonPlugin, ModInitializer {
    
    public MyPlugin() {
        super("name", "version", List.of("authors"));
    }
    
    @Override
    public void onInitialize() {}
    
    @Override
    public void onEnable() {
        System.out.println("Hello World!");
        
        CottonAPI.get().pluginManager().getEventBus().register(this);
    }

    @Override
    public void onDisable() { }
    
    @Subscribe
    public void onPlayerJoin(PlayerJoinEvent event) { }
}
```

3. Build your mod using `Gradle` and deploy the generated `.jar` file into the `/plugins` folder.

For a detailed guide, check the [Documentation](https://github.com/FurryPlayPlace/CottonFramework/wiki).

---

## 📚 Documentation

Comprehensive guides, API references, and examples can be found in our
official [Wiki](https://github.com/FurryPlayPlace/CottonFramework/wiki).

---

## 🏗 Contributing

We welcome contributions from the community! To contribute:

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes and push your branch.
4. Open a pull request.

Please refer to our [Contributing Guide](https://github.com/FurryPlayPlace/CottonFramework/blob/main/CONTRIBUTING.md) for more
details.

---

## 📃 License

CottonFramework is licensed under the [MIT License](https://github.com/FurryPlayPlace/CottonFramework/blob/main/LICENSE).

---

## 🔗 Links
- **FurryPlayPlace**: [https://www.furryplayplace.net/](https://www.furryplayplace.net)
- **Fabric MC**: [https://fabricmc.net/](https://fabricmc.net/)
- **Releases
  **: [https://github.com/FurryPlayPlace/CottonFramework/releases](https://github.com/FurryPlayPlace/CottonFramework/releases)
- **Documentation
  **: [https://github.com/FurryPlayPlace/CottonFramework/wiki](https://github.com/FurryPlayPlace/CottonFramework/wiki)

---

## 🤝 Support

If you have any questions, feel free to open an issue on
our [GitHub Issues](https://github.com/FurryPlayPlace/CottonFramework/issues) page!

We hope Cotton Framework makes developing server-side plugins simpler and more fun. Happy modding! 🎉😍