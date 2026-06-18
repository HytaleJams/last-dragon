package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.hytalejams.lastdragon.CameraUtils;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class LeaveSokobanArea extends TriggerEffect {
  public static final BuilderCodec<LeaveSokobanArea> CODEC = BuilderCodec
      .builder(LeaveSokobanArea.class, LeaveSokobanArea::new).build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var store = triggerContext.getEntityRef().getStore();

    if (store.getComponent(triggerContext.getEntityRef(),
        LastDragon.getInstance().getInSokobanAreaComponentType()) == null) return;

    store.removeComponent(triggerContext.getEntityRef(), LastDragon.getInstance().getInSokobanAreaComponentType());

    var playerRef = store.getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());
    if (playerRef != null) CameraUtils.resetCamera(playerRef);

    var hotbar = store.getComponent(triggerContext.getEntityRef(), InventoryComponent.Hotbar.getComponentType());
    if (hotbar != null) hotbar.getInventory().removeItemStackFromSlot((short) 4);
  }
}
