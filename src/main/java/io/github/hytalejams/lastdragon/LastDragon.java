package io.github.hytalejams.lastdragon;

import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerCondition;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.lookup.BuilderCodecMapCodec;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.ResourceType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.command.LastDragonCommand;
import io.github.hytalejams.lastdragon.component.LastTriggerVolumePosition;
import io.github.hytalejams.lastdragon.component.OldInventory;
import io.github.hytalejams.lastdragon.condition.MoveInVolumeCondition;
import io.github.hytalejams.lastdragon.system.*;
import io.github.hytalejams.lastdragon.trigger.*;
import io.github.hytalejams.lastdragon.sokoban.SokobanGrid;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
@SuppressWarnings("unused")
public class LastDragon extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static LastDragon instance;

    private ResourceType<ChunkStore, SokobanGrid> sokobanGridResourceType;
    private ComponentType<EntityStore, LastTriggerVolumePosition> lastTriggerVolumePositionComponentType;
    private ComponentType<EntityStore, OldInventory> oldInventoryComponentType;
    private final BuilderCodecMapCodec<InventoryComponent> inventoryComponentCodec;

    private final SokobanGrid initialGrid;

    private final Object sync;
    private CompletableFuture<World> lastDragonInstance;

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

          setOrigin(-1796, 186, -370);
          setCellWidth(2);
        }};
        this.sync = new Object();
        this.inventoryComponentCodec = new BuilderCodecMapCodec<>();
    }

    @Override
    protected void setup() {
        instance = this;
        LOGGER.atInfo().log("Setting up plugin " + this.getName() + " version " +
            this.getManifest().getVersion().toString());

        var inventoryCodecRegistry = getCodecRegistry(inventoryComponentCodec);

        // the nonsense below is required to get around a bug with inventory component serialization
        for (var inventoryComponentType : InventoryComponent.EVERYTHING) {
          var type = inventoryComponentType.getTypeClass();

          Field field;
          try {
            field = type.getDeclaredField("CODEC");
          } catch (NoSuchFieldException e) {
            LOGGER.atWarning().log("Missing CODEC field for InventoryComponent subclass "
                + inventoryComponentType.getTypeClass());
            continue;
          }

          int modifiers = field.getModifiers();
          if (!Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers)) {
            LOGGER.atWarning().log("CODEC field of InventoryComponent subclass "
                + inventoryComponentType.getTypeClass()
                + " has improper modifiers");
            continue;
          }

          Object value;
          try {
            value = field.get(null);
          } catch (IllegalAccessException e) {
            LOGGER.atWarning().log("CODEC field of InventoryComponent subclass "
                + inventoryComponentType.getTypeClass()
                + " was null");
            continue;
          }

          if (value == null || !BuilderCodec.class.isAssignableFrom(value.getClass())) {
            LOGGER.atWarning().log("CODEC field of InventoryComponent subclass "
                + inventoryComponentType.getTypeClass()
                + " was not assignable to BuilderCodec");
            continue;
          }

          //noinspection unchecked,rawtypes
          inventoryCodecRegistry.register(type.getSimpleName(), (Class) type, (BuilderCodec) value);
        }

      getCodecRegistry(TriggerEffect.CODEC)
          .register("ResetSokoban", ResetSokoban.class, ResetSokoban.CODEC)
          .register("RemoveItem", RemoveItem.class, RemoveItem.CODEC)
          .register("ExitInstance", ExitInstance.class, ExitInstance.CODEC)
          .register("ResetLastTriggerVolumePosition", ResetLastTriggerVolumePosition.class, ResetLastTriggerVolumePosition.CODEC)
          .register("SetCamera", SetCamera.class, SetCamera.CODEC)
          .register("PushCrate", PushCrate.class, PushCrate.CODEC);

      getCodecRegistry(TriggerCondition.CODEC)
          .register("MoveInVolume", MoveInVolumeCondition.class, MoveInVolumeCondition.CODEC);

      sokobanGridResourceType = getChunkStoreRegistry()
          .registerResource(SokobanGrid.class, "SokobanGrid", SokobanGrid.CODEC);

      lastTriggerVolumePositionComponentType = getEntityStoreRegistry()
          .registerComponent(LastTriggerVolumePosition.class, "LastTriggerVolumePosition", LastTriggerVolumePosition.CODEC);

      oldInventoryComponentType = getEntityStoreRegistry()
          .registerComponent(OldInventory.class, "OldInventory", OldInventory.CODEC);

      getCommandRegistry().registerCommand(new LastDragonCommand());

      getEntityStoreRegistry().registerSystem(new PreserveInventorySystem());
      getEntityStoreRegistry().registerSystem(new PreventItemDropInLastDragonInstanceSystem());
      getEntityStoreRegistry().registerSystem(new InitializeOldInventorySystem());
      getEntityStoreRegistry().registerSystem(new PreserveGameModeSystem());
      getEntityStoreRegistry().registerSystem(new CleanWorldSystem());
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Starting plugin " + this.getName());
    }

    public ResourceType<ChunkStore, SokobanGrid> getSokobanGridResourceType() {
      return sokobanGridResourceType;
    }

    public ComponentType<EntityStore, LastTriggerVolumePosition> getLastTriggerVolumePositionComponentType() {
      return lastTriggerVolumePositionComponentType;
    }

    public ComponentType<EntityStore, OldInventory> getOldInventoryComponentType() {
      return oldInventoryComponentType;
    }

    public SokobanGrid getDefaultGrid() {
      return initialGrid.clone();
    }

    public CompletableFuture<World> getLastDragonInstance(World current) {
      synchronized (sync) {
        if (lastDragonInstance == null)
          lastDragonInstance = InstancesPlugin.get().spawnInstance("LastDragon", current, new Transform());
        return lastDragonInstance;
      }
    }

    public void removeLastDragonInstance() {
      synchronized (sync) {
        if (lastDragonInstance != null) LOGGER.at(Level.INFO).log("Cleaning up LastDragon instance...");
        lastDragonInstance = null;
      }
    }

    public Codec<InventoryComponent> getInventoryComponentCodec() {
      return inventoryComponentCodec;
    }

    public boolean isLastDragonInstance(World test) {
      return test.getWorldConfig().getGameplayConfig().equals("LastDragon");
    }
}