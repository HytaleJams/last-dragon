package io.github.hytalejams.lastdragon.component;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.inventory.InventoryComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nullable;

/**
 * Used to preserve inventory and gamemode when transporting out of a Last Dragon instance.
 */
public class OldInventory implements Component<EntityStore> {
  public static final BuilderCodec<OldInventory> CODEC = BuilderCodec
      .builder(OldInventory.class, OldInventory::new)
      .append(new KeyedCodec<>("OldInventory", new ArrayCodec<>(LastDragon.getInstance().getInventoryComponentCodec(), InventoryComponent[]::new)), (self, value) -> self.oldInventory = value, self -> self.oldInventory).add()
      .append(new KeyedCodec<>("HasOldInventory", Codec.BOOLEAN), (self, value) -> self.hasOldInventory = value, self -> self.hasOldInventory).add()
      .append(new KeyedCodec<>("OldGameMode", new EnumCodec<>(GameMode.class)), (self, value) -> self.oldGameMode = value, self -> self.oldGameMode).add()
      .build();

  public InventoryComponent[] oldInventory;
  public boolean hasOldInventory;

  public @Nullable GameMode oldGameMode;

  public OldInventory() {
    this.oldInventory = new InventoryComponent[InventoryComponent.EVERYTHING.length];
  }

  private OldInventory(OldInventory that) {
    if (that.oldInventory == null) this.oldInventory = null;
    else {
      this.oldInventory = new InventoryComponent[InventoryComponent.EVERYTHING.length];
      for (int i = 0; i < that.oldInventory.length; i++) {
        var otherComponent = that.oldInventory[i];
        this.oldInventory[i] = otherComponent == null ? null : (InventoryComponent) otherComponent.clone();
      }
    }

    this.hasOldInventory = that.hasOldInventory;
    this.oldGameMode = that.oldGameMode;
  }

  @Override
  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public OldInventory clone() {
    return new OldInventory(this);
  }
}
