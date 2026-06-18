package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class ResetSokoban extends TriggerEffect {
  public static final BuilderCodec<ResetSokoban> CODEC = BuilderCodec
      .builder(ResetSokoban.class, ResetSokoban::new, TriggerEffect.BASE_CODEC)
      .append(new KeyedCodec<>("CrateBlockId", Codec.STRING, false), (self, value) -> self.crateBlockId = value, self -> self.crateBlockId).add()
      .build();

  public String crateBlockId;

  public ResetSokoban() {
    this.crateBlockId = "Furniture_Village_Crate";
  }

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var world = triggerContext.getEntityRef().getStore().getExternalData().getWorld();

    var defaultSokoban = LastDragon.getInstance().getDefaultGrid();
    world.getChunkStore()
        .getStore().replaceResource(LastDragon.getInstance().getSokobanGridResourceType(), defaultSokoban);

    defaultSokoban.syncWorldState(world, crateBlockId);
  }
}
