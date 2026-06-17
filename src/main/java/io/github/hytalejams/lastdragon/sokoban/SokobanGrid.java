package io.github.hytalejams.lastdragon.sokoban;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.EnumCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.component.Resource;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3iUtil;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.joml.Vector2i;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class SokobanGrid implements Resource<ChunkStore> {
  private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

  public static final BuilderCodec<SokobanGrid> CODEC =
      BuilderCodec.builder(SokobanGrid.class, SokobanGrid::new)
          .append(new KeyedCodec<>("Cells", SokobanCell.ARRAY_CODEC), (self, cells) -> {
            for (var cell : cells) self.cells.put(new Vector2i(cell.x, cell.z), cell);
          }, (self) -> self.cells.values().toArray(SokobanCell[]::new))
          .add()
          .append(new KeyedCodec<>("Origin", Vector3iUtil.CODEC), (self, origin) -> self.origin = origin, self -> self.origin)
          .add()
          .append(new KeyedCodec<>("CellWidth", Codec.INTEGER), (self, cellWidth) -> self.cellWidth = cellWidth, (self) -> self.cellWidth)
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

    @Override
    public String toString() {
      return "SokobanCell{x=" + x + ", z=" + z + ", state=" + state + "}";
    }
  }

  private final Map<Vector2i, SokobanCell> cells;
  private Vector3i origin;
  private int cellWidth;

  public SokobanGrid() {
    this.cells = new HashMap<>();
    this.origin = new Vector3i(0, 0, 0);
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

  public Vector3i getOrigin() {
    return new Vector3i(origin);
  }

  public int getCellWidth() {
    return cellWidth;
  }

  public boolean tryMove(Vector2i cell, PushDirection direction) {
    LOGGER.atInfo().log("Trying to push cell at x=" + cell.x + ", z=" + cell.y + " in direction " + direction);

    var sourceCell = cells.get(cell);

    LOGGER.atInfo().log("Source cell: " + sourceCell);

    if (sourceCell == null || sourceCell.state.isEmpty()) {
      LOGGER.atInfo().log("Source cell is null or empty, can't push");
      return false;
    }

    var targetCell = cells.get(new Vector2i(cell.x + direction.x, cell.y + direction.z));
    LOGGER.atInfo().log("Target cell: " + targetCell);

    if (targetCell == null || !targetCell.state.isEmpty()) {
      LOGGER.atInfo().log("Target cell is null or empty, can't push");
      return false;
    }

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

    LOGGER.atInfo().log("Successfully pushed crate.");
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
