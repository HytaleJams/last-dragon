package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.hytalejams.lastdragon.CameraUtils;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class EnterSokobanArea extends TriggerEffect {
  public static final BuilderCodec<EnterSokobanArea> CODEC = BuilderCodec
      .builder(EnterSokobanArea.class, EnterSokobanArea::new).build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var store = triggerContext.getEntityRef().getStore();

    if (!triggerContext.getEntityRef().isValid()) return;

    store.ensureComponent(triggerContext.getEntityRef(), LastDragon.getInstance().getInSokobanAreaComponentType());

    var playerRef = store.getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());
    var hotbar = store.getComponent(triggerContext.getEntityRef(), InventoryComponent.Hotbar.getComponentType());

    if (playerRef == null || hotbar == null) return;

    CameraUtils.setSokobanCameraPosition(playerRef);

    hotbar.getInventory().setItemStackForSlot((short) 4, new ItemStack("Sokoban_Reset", 1));
  }
}
