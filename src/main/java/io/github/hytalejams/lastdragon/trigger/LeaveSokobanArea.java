package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.hytalejams.lastdragon.CameraUtils;

import javax.annotation.Nonnull;

public class LeaveSokobanArea extends TriggerEffect {
  public static final BuilderCodec<LeaveSokobanArea> CODEC = BuilderCodec
      .builder(LeaveSokobanArea.class, LeaveSokobanArea::new).build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var playerRef = triggerContext.getEntityRef().getStore().getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());
    if (playerRef == null) return;

    CameraUtils.resetCamera(playerRef);
  }
}
