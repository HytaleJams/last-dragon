package io.github.hytalejams.lastdragon.sokoban;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

// marker component
public class InSokobanArea implements Component<EntityStore> {
  @Nullable
  @Override
  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public Component<EntityStore> clone() {
    return new InSokobanArea();
  }
}
