package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.hytalejams.lastdragon.CameraUtils;

import javax.annotation.Nonnull;

public class EnterSokobanArea extends TriggerEffect {
  public static final BuilderCodec<EnterSokobanArea> CODEC = BuilderCodec
      .builder(EnterSokobanArea.class, EnterSokobanArea::new).build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var playerRef = triggerContext.getEntityRef().getStore().getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());
    if (playerRef == null) return;

    CameraUtils.setSokobanCameraPosition(playerRef);
  }


}
