package io.github.hytalejams.lastdragon.sokoban;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import io.github.hytalejams.lastdragon.LastDragon;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.joml.Vector3d;

public class ResetSokoban extends TriggerEffect {
  public static final BuilderCodec<ResetSokoban> CODEC = BuilderCodec
      .builder(ResetSokoban.class, ResetSokoban::new).build();

  @Override
  public void execute(@NonNullDecl TriggerContext triggerContext) {
    var world = triggerContext.getEntityRef().getStore().getExternalData().getWorld();
    var entity = triggerContext.getEntityRef();

    if (entity.getStore()
        .getComponent(entity, LastDragon.getInstance().getInSokobanAreaComponentType()) == null) return;

    var defaultSokoban = LastDragon.getInstance().getDefaultGrid();
    world.getChunkStore()
        .getStore().replaceResource(LastDragon.getInstance().getSokobanGridResourceType(), defaultSokoban);

    defaultSokoban.syncWorldState(world, "FurnitureVillageCrate");

    entity.getStore().addComponent(entity, Teleport.getComponentType(),
        new Teleport(new Vector3d(-1794, 186.5, -373),
            new Rotation3f(0F, (float) Math.PI, 0F)));
  }
}
