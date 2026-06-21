package io.github.hytalejams.lastdragon.system;

import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.RemoveReason;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.container.ItemContainer;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;
import io.github.hytalejams.lastdragon.component.OldInventory;

import javax.annotation.Nonnull;
import java.util.Set;

public class PreserveInventorySystem extends PlayerSystems.PlayerRemovedSystem {
  private static final Set<Dependency<EntityStore>> DEPENDENCIES =
      Set.of(
          new SystemDependency<>(Order.BEFORE, PlayerSystems.PlayerRemovedSystem.class),
          new SystemDependency<>(Order.BEFORE, PlayerSystems.PlayerAddedSystem.class),
          new SystemDependency<>(Order.BEFORE, PlayerSystems.PlayerSpawnedSystem.class),
          new SystemDependency<>(Order.BEFORE, PlayerSystems.PlayerInitSystem.class));

  private static void loadOldInventory(Holder<EntityStore> holder, OldInventory oldInventory) {
    for (int i = 0; i < InventoryComponent.EVERYTHING.length; i++) {
      var old = oldInventory.oldInventory[i];
      var type = InventoryComponent.EVERYTHING[i];

      InventoryComponent copy;
      if (old == null || (copy = (InventoryComponent) old.clone()) == null) {
        var component = holder.getComponent(type);
        if (component != null) component.getInventory().clear();
      } else {
        var inventory = holder.ensureAndGetComponent(type).getInventory();
        inventory.clear();
        ItemContainer.copy(copy.getInventory(), inventory, null);
      }
    }
    oldInventory.oldInventory = new InventoryComponent[InventoryComponent.EVERYTHING.length];
  }

  @Override
  public void onEntityAdd(@Nonnull Holder<EntityStore> holder, @Nonnull AddReason reason, @Nonnull Store<EntityStore> store) {
    var plugin = LastDragon.getInstance();

    var oldInventory = holder.getComponent(plugin.getOldInventoryComponentType());
    assert oldInventory != null;

    var world = store.getExternalData().getWorld();
    if (!plugin.isLastDragonInstance(world)) {
      if (oldInventory.hasOldInventory) loadOldInventory(holder, oldInventory);
      oldInventory.hasOldInventory = false;
      return;
    }

    for (int i = 0; i < InventoryComponent.EVERYTHING.length; i++) {
      var type = InventoryComponent.EVERYTHING[i];
      var component = holder.getComponent(type);
      oldInventory.oldInventory[i] = component == null ? null : (InventoryComponent) component.clone();

      if (component != null) component.getInventory().clear();
    }

    oldInventory.hasOldInventory = true;
  }

  @Override
  public void onEntityRemoved(@Nonnull Holder<EntityStore> holder,
                              @Nonnull RemoveReason reason,
                              @Nonnull Store<EntityStore> store) {}

  @Override
  public Query<EntityStore> getQuery() {
    return Query.and(super.getQuery(), LastDragon.getInstance().getOldInventoryComponentType());
  }

  @Override
  public @Nonnull Set<Dependency<EntityStore>> getDependencies() {
    return DEPENDENCIES;
  }
}
