# Survivor 

Survivor is an independent game made with JMonkeyEngine, which is focused on plugin system and freedom change like Minecraft. 
The target of Survivor is to make a Minecraft style game with a high-performance physical engine. Adding more physical items in the game will be much cooler.


# Build

This project has three submodules - PluginFramework, SurvivorCore, and Server. We use Ant & Maven to build those packages.

For each submodule, we can build it with maven tools:

```
mvn build
```

The main project is made with ant, which is limited by JMonkey's IDE. We use JMonkeyEngine 3.0.10 version. Please download the SDK first:
[Download link](https://github.com/jMonkeyEngine/sdk/releases/download/stable/jmonkeyplatform-linux-x64.sh)

Then use JMonkeyEngine IDE opens this project and choose build action.
You can also use command-line:
```
ant
```

