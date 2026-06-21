package io.github.hytalejams.lastdragon.command;

import com.hypixel.hytale.builtin.instances.InstancesPlugin;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import io.github.hytalejams.lastdragon.LastDragon;

import javax.annotation.Nonnull;

public class LastDragonCommand extends CommandBase {
  public LastDragonCommand() {
    super("lastdragon", "Sends you to a new Last Dragon minigame instance.");
  }

  @Override
  protected void executeSync(@Nonnull CommandContext commandContext) {
    var ref = commandContext.senderAsPlayerRef();
    if (ref == null) {
      commandContext.sendMessage(Message.raw("Only players can run this command!"));
      return;
    }

    var store = ref.getStore();
    var world = store.getExternalData().getWorld();
    var plugin = LastDragon.getInstance();

    if (!plugin.isLastDragonInstance(world)) {
      commandContext.sendMessage(Message.raw("You are already in the Last Dragon minigame!"));
      return;
    }

    world.execute(() -> {
      if (!ref.isValid()) return;

      var transform = store.getComponent(ref, TransformComponent.getComponentType());
      if (transform == null) return;

      var returnPoint = transform.getTransform().clone();
      InstancesPlugin.teleportPlayerToLoadingInstance(ref, store,
          plugin.getLastDragonInstance(world), returnPoint);
    });
  }
}
