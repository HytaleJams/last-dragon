package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import org.joml.Vector3d;
import org.joml.Vector3i;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class PushCrate extends TriggerEffect {
  public static final BuilderCodec<PushCrate> CODEC = BuilderCodec
      .builder(PushCrate.class, PushCrate::new, TriggerEffect.BASE_CODEC).build();

  public PushCrate() {}

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var entity = triggerContext.getEntityRef();
    var store = triggerContext.getStore();

    var transform = store.getComponent(entity, TransformComponent.getComponentType());
    if (transform == null) return;

    var volume = triggerContext.getVolume();

    var min = new Vector3d();
    var max = new Vector3d();

    volume.getShape().getWorldAABB(volume.getPosition(), min, max);

    var blockTypes = new HashMap<Vector3i, String>();

    for (int x = (int) Math.ceil(min.x); x < (int) Math.floor(max.x); x++) {
      for (int y = (int) Math.ceil(min.y); y < (int) Math.floor(max.y); y++) {
        for (int z = (int) Math.ceil(min.z); z < (int) Math.floor(max.z); z++) {
          var block = store.getExternalData().getWorld().getBlockType(x, y, z);
          if (block != null && !block.getId().equalsIgnoreCase("Empty")) {
            blockTypes.put(new Vector3i(x, y, z), block.getId());
          }
        }
      }
    }

    // we didn't find any solid blocks, there's nothing to push: return
    if (blockTypes.isEmpty()) return;


  }
}
