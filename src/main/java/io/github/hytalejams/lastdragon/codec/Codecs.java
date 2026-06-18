package io.github.hytalejams.lastdragon.codec;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.math.vector.Vector2fUtil;
import com.hypixel.hytale.math.vector.Vector3fUtil;
import com.hypixel.hytale.protocol.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class Codecs {
  public static final BuilderCodec<Direction> DIRECTION_CODEC = BuilderCodec.builder(Direction.class, Direction::new)
      .append(new KeyedCodec<>("Pitch", Codec.FLOAT, false), (self, value) -> self.pitch = value, self -> self.pitch).add()
      .append(new KeyedCodec<>("Yaw", Codec.FLOAT, false), (self, value) -> self.yaw = value, self -> self.yaw).add()
      .append(new KeyedCodec<>("Roll", Codec.FLOAT, false), (self, value) -> self.roll = value, self -> self.roll).add()
      .build();

  public static final BuilderCodec<Position> POSITION_CODEC = BuilderCodec.builder(Position.class, Position::new)
      .append(new KeyedCodec<>("X", Codec.DOUBLE, false), (self, value) -> self.z = value, self -> self.x).add()
      .append(new KeyedCodec<>("Y", Codec.DOUBLE, false), (self, value) -> self.y = value, self -> self.y).add()
      .append(new KeyedCodec<>("Z", Codec.DOUBLE, false), (self, value) -> self.x = value, self -> self.z).add()
      .build();

  public static final BuilderCodec<ServerCameraSettings> CAMERA_SETTINGS_CODEC =
      BuilderCodec.builder(ServerCameraSettings.class, ServerCameraSettings::new)
          .append(new KeyedCodec<>("PositionLerpSpeed", Codec.FLOAT, false), (self, value) -> self.positionLerpSpeed = value, self -> self.positionLerpSpeed).add()
          .append(new KeyedCodec<>("RotationLerpSpeed", Codec.FLOAT, false), (self, value) -> self.rotationLerpSpeed = value, self -> self.rotationLerpSpeed).add()
          .append(new KeyedCodec<>("Distance", Codec.FLOAT, false), (self, value) -> self.distance = value, self -> self.distance).add()
          .append(new KeyedCodec<>("SpeedModifier", Codec.FLOAT, false), (self, value) -> self.speedModifier = value, self -> self.speedModifier).add()
          .append(new KeyedCodec<>("AllowPitchControls", Codec.BOOLEAN, false), (self, value) -> self.allowPitchControls = value, self -> self.allowPitchControls).add()
          .append(new KeyedCodec<>("DisplayCursor", Codec.BOOLEAN, false), (self, value) -> self.displayCursor = value, self -> self.displayCursor).add()
          .append(new KeyedCodec<>("DisplayReticle", Codec.BOOLEAN, false), (self, value) -> self.displayReticle = value, self -> self.displayReticle).add()
          .append(new KeyedCodec<>("MouseInputTargetType", new EnumCodec<>(MouseInputTargetType.class), false), (self, value) -> self.mouseInputTargetType = value, self -> self.mouseInputTargetType).add()
          .append(new KeyedCodec<>("SendMouseMotion", Codec.BOOLEAN, false), (self, value) -> self.sendMouseMotion = value, self -> self.sendMouseMotion).add()
          .append(new KeyedCodec<>("SkipCharacterPhysics", Codec.BOOLEAN, false), (self, value) -> self.skipCharacterPhysics = value, self -> self.skipCharacterPhysics).add()
          .append(new KeyedCodec<>("IsFirstPerson", Codec.BOOLEAN, false), (self, value) -> self.isFirstPerson = value, self -> self.isFirstPerson).add()
          .append(new KeyedCodec<>("MovementForceRotationType", new EnumCodec<>(MovementForceRotationType.class), false), (self, value) -> self.movementForceRotationType = value, self -> self.movementForceRotationType).add()
          .append(new KeyedCodec<>("MovementForceRotation", DIRECTION_CODEC, false), (self, value) -> self.movementForceRotation = value, self -> self.movementForceRotation).add()
          .append(new KeyedCodec<>("AttachedToType", new EnumCodec<>(AttachedToType.class), false), (self, value) -> self.attachedToType = value, self -> self.attachedToType).add()
          .append(new KeyedCodec<>("AttachedToEntityId", Codec.INTEGER, false), (self, value) -> self.attachedToEntityId = value, self -> self.attachedToEntityId).add()
          .append(new KeyedCodec<>("EyeOffset", Codec.BOOLEAN, false), (self, value) -> self.eyeOffset = value, self -> self.eyeOffset).add()
          .append(new KeyedCodec<>("PositionDistanceOffsetType", new EnumCodec<>(PositionDistanceOffsetType.class), false), (self, value) -> self.positionDistanceOffsetType = value, self -> self.positionDistanceOffsetType).add()
          .append(new KeyedCodec<>("PositionOffset", POSITION_CODEC, false), (self, value) -> self.positionOffset = value, self -> self.positionOffset).add()
          .append(new KeyedCodec<>("RotationOffset", DIRECTION_CODEC, false), (self, value) -> self.rotationOffset = value, self -> self.rotationOffset).add()
          .append(new KeyedCodec<>("PositionType", new EnumCodec<>(PositionType.class), false), (self, value) -> self.positionType = value, self -> self.positionType).add()
          .append(new KeyedCodec<>("Position", POSITION_CODEC, false), (self, value) -> self.position = value, self -> self.position).add()
          .append(new KeyedCodec<>("RotationType", new EnumCodec<>(RotationType.class), false), (self, value) -> self.rotationType = value, self -> self.rotationType).add()
          .append(new KeyedCodec<>("Rotation", DIRECTION_CODEC, false), (self, value) -> self.rotation = value, self -> self.rotation).add()
          .append(new KeyedCodec<>("CanMoveType", new EnumCodec<>(CanMoveType.class), false), (self, value) -> self.canMoveType = value, self -> self.canMoveType).add()
          .append(new KeyedCodec<>("ApplyMovementType", new EnumCodec<>(ApplyMovementType.class), false), (self, value) -> self.applyMovementType = value, self -> self.applyMovementType).add()
          .append(new KeyedCodec<>("MovementMultiplier", Vector3fUtil.CODEC, false),
              (self, value) -> self.movementMultiplier = value,
              self -> self.movementMultiplier == null ? null : new Vector3f(self.movementMultiplier.x(), self.movementMultiplier.y(), self.movementMultiplier.z())).add()
          .append(new KeyedCodec<>("ApplyLookType", new EnumCodec<>(ApplyLookType.class), false), (self, value) -> self.applyLookType = value, self -> self.applyLookType).add()
          .append(new KeyedCodec<>("ApplyLookType", new EnumCodec<>(ApplyLookType.class), false), (self, value) -> self.applyLookType = value, self -> self.applyLookType).add()
          .append(new KeyedCodec<>("LookMultiplier", Vector2fUtil.CODEC, false),
              (self, value) -> self.lookMultiplier = value,
              self -> self.lookMultiplier == null ? null : new Vector2f(self.lookMultiplier.x(), self.lookMultiplier.y())).add()
          .append(new KeyedCodec<>("MouseInputType", new EnumCodec<>(MouseInputType.class), false), (self, value) -> self.mouseInputType = value, self -> self.mouseInputType).add()
          .append(new KeyedCodec<>("PlaneNormal", Vector3fUtil.CODEC, false),
              (self, value) -> self.planeNormal = value,
              self -> self.planeNormal == null ? null : new Vector3f(self.planeNormal.x(), self.planeNormal.y(), self.planeNormal.z())).add()
          .build();
}
