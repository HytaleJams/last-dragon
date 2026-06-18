package io.github.hytalejams.lastdragon;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.interaction.ResetSokobanInteraction;
import io.github.hytalejams.lastdragon.sokoban.InSokobanArea;
import io.github.hytalejams.lastdragon.sokoban.SokobanGrid;
import io.github.hytalejams.lastdragon.trigger.EnterSokobanArea;
import io.github.hytalejams.lastdragon.trigger.LeaveSokobanArea;
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
    private ComponentType<EntityStore, InSokobanArea> inSokobanAreaComponentType;

    private final SokobanGrid initialGrid;

    public static LastDragon getInstance() {
        return instance;
    }

    public LastDragon(@Nonnull JavaPluginInit init) {
        super(init);

        this.initialGrid = new SokobanGrid() {{
          addCell(new SokobanCell(0, 0) {{ state = State.GoalEmpty; }});
          addCell(new SokobanCell(0, 1) {{ state = State.Empty; }});
          addCell(new SokobanCell(0, 2) {{ state = State.Empty; }});
          addCell(new SokobanCell(0, 3) {{ state = State.Empty; }});
          addCell(new SokobanCell(0, 4) {{ state = State.GoalEmpty; }});
          addCell(new SokobanCell(1, 1) {{ state = State.GoalEmpty; }});
          addCell(new SokobanCell(1, 3) {{ state = State.Empty; }});
          addCell(new SokobanCell(1, 4) {{ state = State.Empty; }});
          addCell(new SokobanCell(2, 1) {{ state = State.Crate; }});
          addCell(new SokobanCell(2, 2) {{ state = State.Crate; }});
          addCell(new SokobanCell(2, 3) {{ state = State.Empty; }});
          addCell(new SokobanCell(2, 4) {{ state = State.Empty; }});
          addCell(new SokobanCell(3, 0) {{ state = State.Empty; }});
          addCell(new SokobanCell(3, 1) {{ state = State.Crate; }});
          addCell(new SokobanCell(3, 2) {{ state = State.Empty; }});
          addCell(new SokobanCell(3, 4) {{ state = State.Empty; }});
          addCell(new SokobanCell(4, 0) {{ state = State.Empty; }});
          addCell(new SokobanCell(4, 1) {{ state = State.Empty; }});
          addCell(new SokobanCell(4, 2) {{ state = State.Empty; }});
          addCell(new SokobanCell(4, 3) {{ state = State.Empty; }});
          addCell(new SokobanCell(4, 4) {{ state = State.Empty; }});

          setOrigin(-1795, 0, -369);
          setCellWidth(2);
        }};
    }

    @Override
    protected void setup() {
        instance = this;
        LOGGER.atInfo().log("Setting up plugin " + this.getName() + " version " +
            this.getManifest().getVersion().toString());

      getCodecRegistry(TriggerEffect.CODEC)
          .register("EnterSokoban", EnterSokobanArea.class, EnterSokobanArea.CODEC)
          .register("LeaveSokoban", LeaveSokobanArea.class, LeaveSokobanArea.CODEC)
          .register("PushCrate", PushCrate.class, PushCrate.CODEC);

      getCodecRegistry(Interaction.CODEC)
          .register("ResetSokoban", ResetSokobanInteraction.class, ResetSokobanInteraction.CODEC);

      sokobanGridResourceType = getChunkStoreRegistry()
          .registerResource(SokobanGrid.class, "SokobanGrid", SokobanGrid.CODEC);

      inSokobanAreaComponentType = getEntityStoreRegistry()
          .registerComponent(InSokobanArea.class, InSokobanArea::new);
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Starting plugin " + this.getName());
    }

    public ResourceType<ChunkStore, SokobanGrid> getSokobanGridResourceType() {
      return sokobanGridResourceType;
    }

    public ComponentType<EntityStore, InSokobanArea> getInSokobanAreaComponentType() {

    }

    public SokobanGrid getDefaultGrid() {
      return initialGrid.clone();
    }
}