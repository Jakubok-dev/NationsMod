package me.jakubok.nationsmod.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BorderSign extends Block implements BlockEntityProvider, Waterloggable {

    public static final BooleanProperty FACE = BooleanProperty.of("face");
    public static final BooleanProperty BACK = BooleanProperty.of("back");
    public static final BooleanProperty LEFT = BooleanProperty.of("left");
    public static final BooleanProperty RIGHT = BooleanProperty.of("right");

    public BorderSign() {
        super(FabricBlockSettings.of(Material.STONE).hardness(15.0f));
        setDefaultState(this.getStateManager().getDefaultState()
            .with(FACE, false)
            .with(BACK, false)
            .with(LEFT, false)
            .with(RIGHT, false)
            .with(Properties.WATERLOGGED, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACE);
        builder.add(BACK);
        builder.add(LEFT);
        builder.add(RIGHT);
        builder.add(Properties.WATERLOGGED);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        
        if (world.isClient)
            return ActionResult.SUCCESS;
        
        this.update(world, pos);

        return ActionResult.SUCCESS;
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
}
