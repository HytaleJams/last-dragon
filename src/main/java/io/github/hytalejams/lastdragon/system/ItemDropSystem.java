package io.github.hytalejams.lastdragon.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class ItemDropSystem extends EntityEventSystem<EntityStore, DropItemEvent.PlayerRequest> {
  public ItemDropSystem() {
    super(DropItemEvent.PlayerRequest.class);
  }

  @Override
  public void handle(int i,
                     @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store,
                     @Nonnull CommandBuffer<EntityStore> commandBuffer,
                     @Nonnull DropItemEvent.PlayerRequest playerRequest) {
    playerRequest.setCancelled(true);
  }

  @Override
  public Query<EntityStore> getQuery() {
    return LastDragon.getInstance().getInSokobanAreaComponentType();
  }
}
