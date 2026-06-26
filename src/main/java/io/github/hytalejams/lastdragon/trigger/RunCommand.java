package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import javax.annotation.Nonnull;

public class RunCommand extends TriggerEffect {
  private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

  public static final BuilderCodec<RunCommand> CODEC = BuilderCodec
      .builder(RunCommand.class, RunCommand::new, TriggerEffect.BASE_CODEC)
      .append(new KeyedCodec<>("Command", Codec.STRING, true), (self, value) -> self.command = value, self -> self.command).add()
      .build();

  public String command;

  public RunCommand() {
    this.command = "";
  }

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
    var player = triggerContext.getStore()
        .getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());

    // can't run command as non-player
    if (player == null) return;

    CommandManager.get().handleCommand(player, command).whenComplete((_, err) ->
        LOGGER.atWarning().log("Failed to run command!\n" + err));
  }
}
