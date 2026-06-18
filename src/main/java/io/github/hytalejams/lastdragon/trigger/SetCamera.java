package io.github.hytalejams.lastdragon.trigger;

import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerContext;
import com.hypixel.hytale.builtin.triggervolumes.effect.TriggerEffect;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.protocol.ClientCameraView;
import com.hypixel.hytale.protocol.ServerCameraSettings;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import io.github.hytalejams.lastdragon.codec.Codecs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SetCamera extends TriggerEffect {
  public static final BuilderCodec<SetCamera> CODEC = BuilderCodec.builder(SetCamera.class, SetCamera::new, TriggerEffect.BASE_CODEC)
      .append(new KeyedCodec<>("ClientCameraView", new EnumCodec<>(ClientCameraView.class), false), (self, value) -> self.clientCameraView = value, self -> self.clientCameraView).add()
      .append(new KeyedCodec<>("IsLocked", Codec.BOOLEAN, false), (self, value) -> self.isLocked = value, self -> self.isLocked).add()
      .append(new KeyedCodec<>("CameraSettings", Codecs.CAMERA_SETTINGS_CODEC, false), (self, value) -> self.cameraSettings = value, self -> self.cameraSettings).add()
      .build();

  public ClientCameraView clientCameraView;
  public boolean isLocked;
  public @Nullable ServerCameraSettings cameraSettings;

  public SetCamera() {
    this.clientCameraView = ClientCameraView.Custom;
    this.isLocked = true;
  }

  @Override
  public void execute(@Nonnull TriggerContext triggerContext) {
      var playerRef = triggerContext.getStore()
        .getComponent(triggerContext.getEntityRef(), PlayerRef.getComponentType());

    if (playerRef != null) playerRef
        .getPacketHandler()
        .writeNoCache(new SetServerCamera(clientCameraView, isLocked, cameraSettings));
  }
}
