package io.github.hytalejams.lastdragon.condition;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerCondition;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import io.github.hytalejams.lastdragon.LastDragon;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

/**
 * Custom trigger condition added for the red light minigame. Detects when a player
 * moves within the bounds of a trigger volume.
 */
public class MoveInVolumeCondition extends TriggerCondition {
  public static final BuilderCodec<MoveInVolumeCondition> CODEC = BuilderCodec
      .builder(MoveInVolumeCondition.class, MoveInVolumeCondition::new, TriggerCondition.BASE_CODEC)
      .build();

  @SuppressWarnings("SameReturnValue")
  private static boolean clearPositionForTV(TriggerContext context) {
    var store = context.getStore();
    var entity = context.getEntityRef();

    var positions = store.getComponent(entity,
        LastDragon.getInstance().getLastTriggerVolumePositionComponentType());
    if (positions == null) return false;

    positions.positions.remove(context.getVolume().getId());
    return false;
  }

  @Override
  public boolean test(@Nonnull TriggerContext context) {
    var store = context.getStore();
    var entity = context.getEntityRef();

    var transform = store.getComponent(entity, TransformComponent.getComponentType());
    if (transform == null) return clearPositionForTV(context);

    var entry = context.getVolume();
    var isInside = entry.getShape().contains(entry.getPosition(), transform.getPosition());
    if (!isInside) return clearPositionForTV(context);

    var positions = store.ensureAndGetComponent(entity,
        LastDragon.getInstance().getLastTriggerVolumePositionComponentType());

    var oldPosition = positions.positions.get(entry.getId());
    if (oldPosition == null) {
      positions.positions.put(entry.getId(), new Vector3d(transform.getPosition()));
      return false;
    }

    // sufficiently close, don't trigger
    if (oldPosition.distanceSquared(transform.getPosition()) < 0.001) return false;

    positions.positions.put(entry.getId(), new Vector3d(transform.getPosition()));
    return true;
  }
}
