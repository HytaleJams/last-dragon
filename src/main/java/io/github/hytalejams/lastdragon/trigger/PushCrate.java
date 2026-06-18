package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Vector3iUtil;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import io.github.hytalejams.lastdragon.LastDragon;
import io.github.hytalejams.lastdragon.sokoban.SokobanGrid;
import org.joml.Vector2i;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

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

    var exactCenter = new Vector3d(min).add(max).div(2);

    var playerDistanceFromExactCenter = new Vector3d();
    transform.getPosition().sub(exactCenter, playerDistanceFromExactCenter);

    var sokobanGrid = store.getExternalData().getWorld().getChunkStore()
        .getStore().getResource(LastDragon.getInstance().getSokobanGridResourceType());

    var relative = exactCenter.sub(Vector3iUtil.toVector3d(sokobanGrid.getOrigin()));

    var gridX = ((int)Math.floor(relative.x)) / sokobanGrid.getCellWidth();
    var gridZ = ((int)Math.floor(relative.z)) / sokobanGrid.getCellWidth();

    var xMag = Math.abs(playerDistanceFromExactCenter.x);
    var zMag = Math.abs(playerDistanceFromExactCenter.z);

    // check if the player is too close to the corner
    if (Math.abs(xMag - zMag) < 0.25) return;

    SokobanGrid.PushDirection pushDirection;
    if (xMag > zMag) { // push along east-west (x) axis
      if (playerDistanceFromExactCenter.x < 0) pushDirection = SokobanGrid.PushDirection.East;
      else pushDirection = SokobanGrid.PushDirection.West;
    } else { // push along north-south (z) axis
      if (playerDistanceFromExactCenter.z < 0) pushDirection = SokobanGrid.PushDirection.South;
      else pushDirection = SokobanGrid.PushDirection.North;
    }

    // only move the physical blocks if the push is valid
    if (!sokobanGrid.tryMove(new Vector2i(gridX, gridZ), pushDirection)) return;

    var world = store.getExternalData().getWorld();
    for (int x = (int) Math.ceil(min.x); x < (int) Math.floor(max.x); x++) {
      for (int y = (int) Math.ceil(min.y); y < (int) Math.floor(max.y); y++) {
        for (int z = (int) Math.ceil(min.z); z < (int) Math.floor(max.z); z++) {
          var oldBlockType = world.getBlockType(x, y, z);
          if (oldBlockType == null || oldBlockType.getId().equalsIgnoreCase("Empty")) continue;

          world.setBlock(x + (pushDirection.x * sokobanGrid.getCellWidth()),
              y,
              z + (pushDirection.z * sokobanGrid.getCellWidth()), oldBlockType.getId());
          world.setBlock(x, y, z, "Empty");
        }
      }
    }

    if (sokobanGrid.isWinCondition()) {
      // TODO: win condition handling
    }
  }
}
