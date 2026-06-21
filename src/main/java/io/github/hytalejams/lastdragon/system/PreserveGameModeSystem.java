package io.github.hytalejams.lastdragon.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefSystem;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class PreserveGameModeSystem extends RefSystem<EntityStore> {
  @Override
  public void onEntityAdded(@Nonnull Ref<EntityStore> ref,
                            @Nonnull AddReason addReason,
                            @Nonnull Store<EntityStore> store,
                            @Nonnull CommandBuffer<EntityStore> commandBuffer) {
    var plugin = LastDragon.getInstance();
    var world = store.getExternalData().getWorld();
    if (!plugin.isLastDragonInstance(world)) return;

    var oldInventory = commandBuffer.getComponent(ref, plugin.getOldInventoryComponentType());
    assert oldInventory != null;

    var player = commandBuffer.getComponent(ref, Player.getComponentType());
    assert player != null;

    oldInventory.oldGameMode = player.getGameMode();
    Player.setGameMode(ref, GameMode.Adventure, commandBuffer);
  }

  @Override
  public void onEntityRemove(@Nonnull Ref<EntityStore> ref,
                             @Nonnull RemoveReason removeReason,
                             @Nonnull Store<EntityStore> store,
                             @Nonnull CommandBuffer<EntityStore> commandBuffer) {
    var plugin = LastDragon.getInstance();
    var world = store.getExternalData().getWorld();
    if (!plugin.isLastDragonInstance(world)) return;

    var oldInventory = commandBuffer.getComponent(ref, plugin.getOldInventoryComponentType());
    assert oldInventory != null;

    var oldGameMode = oldInventory.oldGameMode;

    if (oldGameMode != null) {
      Player.setGameMode(ref, oldGameMode, commandBuffer);
      oldInventory.oldGameMode = null;
    }
  }

  @Nullable
  @Override
  public Query<EntityStore> getQuery() {
    return Query.and(Player.getComponentType(),
        MovementManager.getComponentType(),
        LastDragon.getInstance().getOldInventoryComponentType());
  }

  @Override
  public @Nonnull Set<Dependency<EntityStore>> getDependencies() {
    return Set.of(new SystemDependency<>(Order.AFTER, PlayerSystems.PlayerInitSystem.class),
        new SystemDependency<>(Order.AFTER, PlayerSystems.PlayerSpawnedSystem.class),
        new SystemDependency<>(Order.AFTER, PlayerSystems.PlayerAddedSystem.class));
  }
}
