package io.github.hytalejams.lastdragon.system;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.HolderSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class InitializeOldInventorySystem extends HolderSystem<EntityStore> {
  @Override
  public @Nonnull Set<Dependency<EntityStore>> getDependencies() {
    return Set.of(new SystemDependency<>(Order.BEFORE, PreserveInventorySystem.class));
  }

  @Nullable
  @Override
  public Query<EntityStore> getQuery() {
    return Player.getComponentType();
  }

  @Override
  public void onEntityAdd(@Nonnull Holder<EntityStore> holder,
                          @Nonnull AddReason addReason,
                          @Nonnull Store<EntityStore> store) {
    var plugin = LastDragon.getInstance();
    if (!plugin.isLastDragonInstance(store.getExternalData().getWorld())) return;

    holder.ensureComponent(plugin.getOldInventoryComponentType());
  }

  @Override
  public void onEntityRemoved(@Nonnull Holder<EntityStore> holder,
                              @Nonnull RemoveReason removeReason,
                              @Nonnull Store<EntityStore> store) { }
}
