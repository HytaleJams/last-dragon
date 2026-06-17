package io.github.hytalejams.lastdragon;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import io.github.hytalejams.lastdragon.sokoban.SokobanGrid;
import io.github.hytalejams.lastdragon.trigger.PushCrate;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
@SuppressWarnings("unused")
public class LastDragon extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static LastDragon instance;

    private ResourceType<ChunkStore, SokobanGrid> sokobanGridResourceType;

    public static LastDragon getInstance() {
        return instance;
    }

    public LastDragon(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;
        LOGGER.atInfo().log("Setting up plugin " + this.getName() + " version " + this.getManifest().getVersion().toString());

      getCodecRegistry(TriggerEffect.CODEC).register("PushCrate", PushCrate.class, PushCrate.CODEC);
      sokobanGridResourceType = getChunkStoreRegistry().registerResource(SokobanGrid.class, "SokobanGrid", SokobanGrid.CODEC);
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Starting plugin " + this.getName());
    }

    public ResourceType<ChunkStore, SokobanGrid> getSokobanGridResourceType() {
      return sokobanGridResourceType;
    }
}