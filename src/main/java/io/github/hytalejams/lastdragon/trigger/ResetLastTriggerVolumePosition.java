package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class ResetLastTriggerVolumePosition extends TriggerEffect {
  public static final BuilderCodec<ResetLastTriggerVolumePosition> CODEC = BuilderCodec
      .builder(ResetLastTriggerVolumePosition.class, ResetLastTriggerVolumePosition::new, TriggerEffect.BASE_CODEC)
      .build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var lastTriggerVolumePosition = LastDragon.getInstance().getLastTriggerVolumePositionComponentType();

    triggerContext.getStore().forEachChunk(lastTriggerVolumePosition,
        (c, b) -> {
      for (int i = 0; i < c.size(); i++) {
        var ref = c.getReferenceTo(i);
        var comp = b.getComponent(ref, lastTriggerVolumePosition);
        assert comp != null;

        comp.positions.remove(triggerContext.getVolume().getId());
      }
        });
  }
}
