package io.github.hytalejams.lastdragon.sokoban;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Resource;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class SokobanGrid implements Resource<ChunkStore> {
  public static final BuilderCodec<SokobanGrid> CODEC =
      BuilderCodec.builder(SokobanGrid.class, SokobanGrid::new)
          .append(new KeyedCodec<>("Cells", SokobanCell.ARRAY_CODEC), (self, cells) -> {
            for (var cell : cells) self.cells.put(new Vector2i(cell.x, cell.z), cell);
          }, (self) -> self.cells.values().toArray(SokobanCell[]::new))
          .add()
          .build();

  public static class SokobanCell {
    public enum State {
      Empty,
      GoalEmpty,
      Crate,
      GoalCrate;

      public boolean isEmpty()  {
        return switch (this) {
          case Empty, GoalEmpty -> true;
          case Crate, GoalCrate -> false;
        };
      }
    }

    public static final BuilderCodec<SokobanCell> CODEC =
        BuilderCodec.builder(SokobanCell.class, SokobanCell::new)
            .append(new KeyedCodec<>("x", Codec.INTEGER), (self, value) -> self.x = value, self -> self.x)
            .add()
            .append(new KeyedCodec<>("z", Codec.INTEGER), (self, value) -> self.z = value, self -> self.z)
            .add()
            .append(new KeyedCodec<>("State", new EnumCodec<>(State.class)), (self, state) -> self.state = state, self -> self.state)
            .add()
            .build();

    public static final ArrayCodec<SokobanCell> ARRAY_CODEC = new ArrayCodec<>(CODEC, SokobanCell[]::new);

    private int x;
    private int z;
    public State state;

    public SokobanCell() {
      state = State.Empty;
    }

    public SokobanCell(Vector2i position) {
      this.x = position.x;
      this.z = position.y;
    }

    public Vector2i getPosition() {
      return new Vector2i(x, z);
    }
  }

  private Map<Vector2i, SokobanCell> cells;

  public SokobanGrid() {
    this.cells = new HashMap<>();
  }

  public enum PushDirection {
    North(0, -1),
    East(1, 0),
    South(1, 0),
    West(-1, 0);

    public final int x;
    public final int z;

    PushDirection(int x, int z) {
      this.x = x;
      this.z = z;
    }
  }

  public boolean tryMove(Vector2i cell, PushDirection direction) {
    var sourceCell = cells.get(cell);
    if (sourceCell == null || sourceCell.state.isEmpty()) return false;

    var targetCell = cells.get(new Vector2i(cell.x + direction.x, cell.y + direction.z));
    if (targetCell == null || !targetCell.state.isEmpty()) return false;

    sourceCell.state = switch (sourceCell.state) {
      case Crate -> SokobanCell.State.Empty;
      case GoalCrate -> SokobanCell.State.GoalEmpty;
      default -> throw new AssertionError("Unexpected enum variant " + sourceCell.state);
    };

    targetCell.state = switch (targetCell.state) {
      case Empty -> SokobanCell.State.Crate;
      case GoalEmpty -> SokobanCell.State.GoalCrate;
      default -> throw new AssertionError("Unexpected enum variant " + targetCell.state);
    };

    return true;
  }

  @Override
  public Resource<ChunkStore> clone() {
    try {
      return (Resource<ChunkStore>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
}
