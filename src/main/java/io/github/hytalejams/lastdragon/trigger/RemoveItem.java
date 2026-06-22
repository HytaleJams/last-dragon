package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Removes an item of a certain ID from the player's inventory. Used because the only current way to remove items via
 * TVs is through a condition.
 */
public class RemoveItem extends TriggerEffect {
  public static final BuilderCodec<RemoveItem> CODEC = BuilderCodec
      .builder(RemoveItem.class, RemoveItem::new, TriggerEffect.BASE_CODEC)
      .append(new KeyedCodec<>("ItemId", Codec.STRING, true), (self, value) -> self.itemId = value, self -> self.itemId).add()
      .build();

  private String itemId;

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var itemId = this.itemId;
    if (itemId == null) return;

    var entity = triggerContext.getEntityRef();
    var store = triggerContext.getStore();

    InventoryComponent.getCombined(store, entity, InventoryComponent.EVERYTHING)
        .removeItemStack(new ItemStack(itemId, 1));
  }
}
