package io.github.hytalejams.lastdragon.interaction;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.math.vector.Rotation3f;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.SimpleInstantInteraction;
import io.github.hytalejams.lastdragon.LastDragon;
import org.joml.Vector3d;

import javax.annotation.Nonnull;

public class ResetSokobanInteraction extends SimpleInstantInteraction {
  public static final BuilderCodec<ResetSokobanInteraction> CODEC = BuilderCodec
      .builder(ResetSokobanInteraction.class, ResetSokobanInteraction::new).build();

  public ResetSokobanInteraction() {}

  @Override
  protected void firstRun(@Nonnull InteractionType interactionType,
                          InteractionContext interactionContext,
                          @Nonnull CooldownHandler cooldownHandler) {
    var world = interactionContext.getEntity().getStore().getExternalData().getWorld();
    var entity = interactionContext.getEntity();

    world.execute(() -> {
      if (entity.getStore()
          .getComponent(entity, LastDragon.getInstance().getInSokobanAreaComponentType()) == null) return;

      var defaultSokoban = LastDragon.getInstance().getDefaultGrid();
      world.getChunkStore()
          .getStore().replaceResource(LastDragon.getInstance().getSokobanGridResourceType(), defaultSokoban);

      defaultSokoban.syncWorldState(world, "FurnitureVillageCrate");

      entity.getStore().addComponent(entity, Teleport.getComponentType(),
          new Teleport(new Vector3d(-1794, 186.5, -373),
          new Rotation3f(0F, (float) Math.PI, 0F)));
    });
  }
}
