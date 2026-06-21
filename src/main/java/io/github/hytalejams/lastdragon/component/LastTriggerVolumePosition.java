package io.github.hytalejams.lastdragon.component;

import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.math.vector.Vector3dUtil;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.Map;

public class LastTriggerVolumePosition implements Component<EntityStore> {
  public static final BuilderCodec<LastTriggerVolumePosition> CODEC =
      BuilderCodec.builder(LastTriggerVolumePosition.class, LastTriggerVolumePosition::new)
          .append(new KeyedCodec<>("Positions", new MapCodec<>(Vector3dUtil.CODEC, HashMap::new, false)), (self, value) -> self.positions = value, self -> self.positions).add()
          .build();

  public Map<String, Vector3d> positions;

  public LastTriggerVolumePosition() {
    this.positions = new HashMap<>();
  }

  private LastTriggerVolumePosition(LastTriggerVolumePosition that) {
    this.positions = new HashMap<>(that.positions);
    for (var entry : that.positions.entrySet()) this.positions.put(entry.getKey(), new Vector3d(entry.getValue()));
  }

  @Override
  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public LastTriggerVolumePosition clone() {
    return new LastTriggerVolumePosition(this);
  }
}
