package io.github.hytalejams.lastdragon;

import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.protocol.packets.camera.SetServerCamera;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.joml.Vector3f;

public class CameraUtils {
  public static void resetCamera(PlayerRef playerRef) {
    playerRef.getPacketHandler().writeNoCache(
        new SetServerCamera(ClientCameraView.Custom, false, null)
    );
  }

  public static void setSokobanCameraPosition(PlayerRef playerRef) {
    ServerCameraSettings settings = new ServerCameraSettings();
    settings.positionLerpSpeed = 0.1f;
    settings.rotationLerpSpeed = 0.1f;
    settings.distance = 25.0f;
    settings.displayCursor = true;
    settings.isFirstPerson = false;
    settings.eyeOffset = true;
    settings.positionOffset = new Position(-7.0, 5.0, -7.0);
    settings.positionDistanceOffsetType = PositionDistanceOffsetType.DistanceOffset;
    settings.rotationType = RotationType.Custom;
    settings.rotation = new Direction(-2.7f, -0.75f, 0.0f);
    settings.mouseInputType = MouseInputType.LookAtTargetEntity;
    settings.planeNormal = new Vector3f(0.0f, 1.0f, 0.0f);

    playerRef
        .getPacketHandler()
        .writeNoCache(new SetServerCamera(ClientCameraView.Custom, true, settings));
  }
}
