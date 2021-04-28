package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.collections.TreeIterator;
import me.Jakubok.nations.terrain.ModChunkPos;
import me.Jakubok.nations.util.GlobalChunkRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartOfNationBase extends Item {

    protected int chargeLevel = 0;
    protected ChunkBinaryTree tree = new ChunkBinaryTree();

    public HeartOfNationBase() {
        super(new FabricItemSettings()
                .group(Nations.nations_tab)
                .maxCount(1)
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (world.isClient) return super.use(world, user, hand);
        if (user.hasStatusEffect(StatusEffect.byRawId(32))) {
            TreeIterator<ModChunkPos> itr = tree.getIterator();
            int inc = 0;
            while (itr != null) {
                ModChunkPos pos = itr.value.value;
                user.sendMessage(Text.of(pos.x + " " + pos.z), false);
                itr = itr.next();
                inc++;
            }
            user.sendMessage(Text.of(""+inc), true);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        ModChunkPos pos = new ModChunkPos(new ChunkPos(user.getBlockPos()));
        if (!tree.contains(pos)) tree.add(pos);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
