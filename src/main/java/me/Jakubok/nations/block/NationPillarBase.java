package me.Jakubok.nations.block;

import me.Jakubok.nations.util.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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

public class NationPillarBase extends Block implements BlockEntityProvider {

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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!(world.getBlockEntity(pos) instanceof NationPillarEntity)) return super.onUse(state, world, pos, player, hand, hit);
        NationPillarEntity nationPillarEntity = (NationPillarEntity) world.getBlockEntity(pos);

        if (player.getStackInHand(hand).isItemEqual(new ItemStack(Items.HEART_OF_NATION)) && !nationPillarEntity.health_inserted) {
            player.clearActiveItem();
            nationPillarEntity.health_inserted = true;
            return ActionResult.CONSUME;
        }

        if (!nationPillarEntity.health_inserted) {
            player.sendMessage(new TranslatableText("block.nationsmod.nation_pillar.lore.no_heart"), true);
            return ActionResult.SUCCESS;
        }

        if (player.hasStatusEffect(StatusEffect.byRawId(32))) {
            if (nationPillarEntity.charge_level + (player.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1) < 6) {
                nationPillarEntity.charge_level += (player.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1);
                player.removeStatusEffect(StatusEffect.byRawId(32));
                return ActionResult.SUCCESS;
            }
        }

        player.sendMessage(new TranslatableText("block.nationsmod.nation_pillar.lore." + Integer.toString(nationPillarEntity.charge_level)), true);
        return ActionResult.SUCCESS;
    }
}
