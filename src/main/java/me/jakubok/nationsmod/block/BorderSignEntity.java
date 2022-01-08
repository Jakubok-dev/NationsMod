package me.jakubok.nationsmod.block;

import me.jakubok.nationsmod.registries.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BorderSignEntity extends BlockEntity {

    public Connection face = new Connection(Direction.FACE);
    public Connection back = new Connection(Direction.BACK);
    public Connection left = new Connection(Direction.LEFT);
    public Connection right = new Connection(Direction.RIGHT);

    public class Connection {
        public final Direction direction;
        public BorderSignEntity entity;
        public Connection(Direction direction) { this.direction = direction; }
    }
    private enum Direction {
        FACE(1, 0),
        BACK(-1, 0),
        LEFT(0, 1),
        RIGHT(0, -1);

        public final int x, z;

        Direction(int x, int z) {
            this.x = x; this.z = z;
        }
    }

    private Direction negateDirection(Direction direction) {
        if (direction == Direction.FACE)
            return Direction.BACK;
        if (direction == Direction.BACK)
            return Direction.FACE;
        if (direction == Direction.LEFT)
            return Direction.RIGHT;
        if (direction == Direction.RIGHT)
            return Direction.LEFT;

        return null;
    }

    private Connection getConnection(Direction direction) {
        if (direction == Direction.FACE)
            return this.face;
        if (direction == Direction.BACK)
            return this.back;
        if (direction == Direction.LEFT)
            return this.left;
        if (direction == Direction.RIGHT)
            return this.right;

        return null;
    }

    private BooleanProperty directionToProperty(Direction direction) {
        if (direction == Direction.FACE)
            return BorderSign.FACE;
        if (direction == Direction.BACK)
            return BorderSign.BACK;
        if (direction == Direction.LEFT)
            return BorderSign.LEFT;
        if (direction == Direction.RIGHT)
            return BorderSign.RIGHT;

        return null;
    }

    public BorderSignEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.BORDER_SIGN_ENTITY_TYPE, pos, state);
    }

    public void delete(World world) {
        if (face.entity != null) {
            world.setBlockState(face.entity.getPos(), face.entity.getCachedState().with(this.directionToProperty(Direction.BACK), false));
            world.setBlockState(this.getPos(), this.getCachedState().with(this.directionToProperty(Direction.FACE), false));
            face.entity.back.entity = null;
        }
        if (back.entity != null) {
            world.setBlockState(back.entity.getPos(), back.entity.getCachedState().with(this.directionToProperty(Direction.FACE), false));
            world.setBlockState(this.getPos(), this.getCachedState().with(this.directionToProperty(Direction.BACK), false));
            back.entity.face.entity = null;
        }
        if (left.entity != null) {
            world.setBlockState(left.entity.getPos(), left.entity.getCachedState().with(this.directionToProperty(Direction.RIGHT), false));
            world.setBlockState(this.getPos(), this.getCachedState().with(this.directionToProperty(Direction.LEFT), false));
            left.entity.right.entity = null;
        }
        if (right.entity != null) {
            world.setBlockState(right.entity.getPos(), right.entity.getCachedState().with(this.directionToProperty(Direction.LEFT), false));
            world.setBlockState(this.getPos(), this.getCachedState().with(this.directionToProperty(Direction.RIGHT), false));
            right.entity.left.entity = null;
        }
    }

    public boolean haveSearchedForBorderSigns = false;

    public void searchForBorderSigns() {
        if (haveSearchedForBorderSigns)
            return;
        if (world == null)
            return;
        this.search(Direction.FACE);
        this.search(Direction.BACK);
        this.search(Direction.LEFT);
        this.search(Direction.RIGHT);
        haveSearchedForBorderSigns = true;
    }

    private void search(Direction direction) {
        if (this.getConnection(direction).entity != null)
            return;
        
        for (int y = this.getPos().getY() + 5; y > this.getPos().getY() - 6; y--) {
            for (int i = 1; i < 6; i++) {
                if (this.getWorld().isClient())
                    return;

                if (this.getWorld().getBlockEntity(new BlockPos(
                    this.getPos().getX() + direction.x * i,
                    y,
                    this.getPos().getZ() + direction.z * i
                )) instanceof BorderSignEntity) {
                    ((BorderSignEntity)this.getWorld().getBlockEntity(new BlockPos(
                        this.getPos().getX() + direction.x * i,
                        y,
                        this.getPos().getZ() + direction.z * i
                    ))).connect(direction, this, this.world);
                    return;
                }
            }
        }
    }
    
    public boolean connect(Direction direction, BorderSignEntity entity, World world) {
        if (this.getConnection(this.negateDirection(direction)).entity != null)
            return false;

        this.getConnection(this.negateDirection(direction)).entity = entity;
        world.setBlockState(this.getPos(), this.getCachedState().with(this.directionToProperty(this.negateDirection(direction)), true));

        entity.getConnection(direction).entity = this;
        world.setBlockState(entity.getPos(), entity.getCachedState().with(this.directionToProperty(direction), true));

        this.searchForBorderSigns();
        return true;
    }
}
