package me.jakubok.nationsmod.block;

import me.jakubok.nationsmod.collection.Pair;
import me.jakubok.nationsmod.collection.PlayerAccount;
import me.jakubok.nationsmod.collection.PlayerInfo;
import me.jakubok.nationsmod.geometry.MathEquation;
import me.jakubok.nationsmod.geometry.Polygon;
import me.jakubok.nationsmod.registries.BlockRegistry;
import me.jakubok.nationsmod.registries.PolygonRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Optional;
import java.util.UUID;

public class BorderSign extends Block implements BlockEntityProvider, Waterloggable {

    public static final BooleanProperty FACE = BooleanProperty.of("face");
    public static final BooleanProperty BACK = BooleanProperty.of("back");
    public static final BooleanProperty LEFT = BooleanProperty.of("left");
    public static final BooleanProperty RIGHT = BooleanProperty.of("right");
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public BorderSign() {
        super(FabricBlockSettings.of(Material.STONE).hardness(15.0f));
        setDefaultState(this.getStateManager().getDefaultState()
            .with(FACE, false)
            .with(BACK, false)
            .with(LEFT, false)
            .with(RIGHT, false)
            .with(WATERLOGGED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACE);
        builder.add(BACK);
        builder.add(LEFT);
        builder.add(RIGHT);
        builder.add(WATERLOGGED);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1f, 0.75f);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BorderSignEntity(pos, state);
    }

    private void update(World world, BlockPos pos) {
        if (world.isClient)
            return;
        if (world.getBlockEntity(pos) == null)
            return;
        
        BorderSignEntity entity = (BorderSignEntity)world.getBlockEntity(pos);

        if (entity.haveSearchedForBorderSigns)
            return;

        if (entity.getWorld() == null) {
            entity.setWorld(world);
        }

        entity.searchForBorderSigns();
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        this.update(world, pos);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (world.isClient) {
            super.onBreak(world, pos, state, player);
            return;
        }
        
        this.update(world, pos);
        ((BorderSignEntity)world.getBlockEntity(pos)).delete(world);

        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState()
            .with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : state.getFluidState();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return state;
    }
}
