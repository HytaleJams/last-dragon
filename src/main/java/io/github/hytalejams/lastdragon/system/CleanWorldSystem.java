package io.github.hytalejams.lastdragon.system;

import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.system.StoreSystem;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSavingSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;
import java.util.Set;

public class CleanWorldSystem extends StoreSystem<EntityStore> {
  private static final Set<Dependency<EntityStore>> DEPENDENCIES =
      Set.of(new SystemDependency<>(Order.BEFORE, PlayerSavingSystems.WorldRemovedSystem.class));

  @Override
  public void onSystemAddedToStore(@Nonnull Store<EntityStore> store) { }

  @Override
  public void onSystemRemovedFromStore(@Nonnull Store<EntityStore> store) {
    var world = store.getExternalData().getWorld();
    var plugin = LastDragon.getInstance();

    if (!plugin.isLastDragonInstance(world)) return;
    plugin.removeLastDragonInstance();
  }

  @Nonnull
  @Override
  public Set<Dependency<EntityStore>> getDependencies() {
    return DEPENDENCIES;
  }
}
