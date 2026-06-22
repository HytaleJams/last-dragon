package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;

import javax.annotation.Nonnull;

/**
 * Exits the current instance. Analogous to running /instance exit.
 */
public class ExitInstance extends TriggerEffect {
  public static final BuilderCodec<ExitInstance> CODEC = BuilderCodec
      .builder(ExitInstance.class, ExitInstance::new, TriggerEffect.BASE_CODEC)
      .build();

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    InstancesPlugin.exitInstance(triggerContext.getEntityRef(), triggerContext.getStore());
  }
}
