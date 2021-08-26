package me.jakubok.nationsmod.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BorderSign extends Block implements BlockEntityProvider {

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
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACE);
        builder.add(BACK);
        builder.add(LEFT);
        builder.add(RIGHT);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.25f, 0f, 0.25f, 0.75f, 1f, 0.75f);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BorderSignEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        
        if (world.isClient)
            return super.onUse(state, world, pos, player, hand, hit);
        
        BorderSignEntity entity = (BorderSignEntity)world.getBlockEntity(pos);

        if (entity.getWorld() == null) {
            entity.setWorld(world);
        }
        entity.searchForBorderSigns();

        player.sendMessage(Text.of("Face: " + (entity.face.entity != null)), false);
        player.sendMessage(Text.of("Back: " + (entity.back.entity != null)), false);
        player.sendMessage(Text.of("Left: " + (entity.left.entity != null)), false);
        player.sendMessage(Text.of("Right: " + (entity.right.entity != null)), false);

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (world.isClient) {
            super.onBreak(world, pos, state, player);
            return;
        }

        ((BorderSignEntity)world.getBlockEntity(pos)).delete(world);

        super.onBreak(world, pos, state, player);
    }
}
