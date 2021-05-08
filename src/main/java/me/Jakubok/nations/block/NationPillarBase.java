package me.Jakubok.nations.block;

import me.Jakubok.nations.util.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NationPillarBase extends BlockWithEntity {

    public NationPillarBase() {
        super(FabricBlockSettings.of(Material.STONE)
        .strength(50.0f, 50.0f)
        .sounds(BlockSoundGroup.STONE)
        .requiresTool());
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new NationPillarEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        //With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        // Casting
        if (!(world.getBlockEntity(pos) instanceof NationPillarEntity)) return super.onUse(state, world, pos, player, hand, hit);
        NationPillarEntity nationPillarEntity = (NationPillarEntity) world.getBlockEntity(pos);
        nationPillarEntity.importInstitutions();

        // Charging
        if (player.hasStatusEffect(StatusEffect.byRawId(32))) {
            if (nationPillarEntity.charge_level + (player.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1) < 6) {
                nationPillarEntity.charge_level += (player.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1);
                player.removeStatusEffect(StatusEffect.byRawId(32));
                return ActionResult.SUCCESS;
            }
        }

        // Activating
        if (player.getStackInHand(hand).getItem() == Items.HEART_OF_NATION) {
            // Creating a town
            if (nationPillarEntity.charge_level > 1 && nationPillarEntity.institutions.town == null) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
        }

        player.sendMessage(new TranslatableText("block.nationsmod.nation_pillar.lore." + Integer.toString(nationPillarEntity.charge_level)), true);
        return ActionResult.SUCCESS;
    }
}
