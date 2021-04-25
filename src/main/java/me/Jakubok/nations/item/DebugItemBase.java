package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.administration.TeritorryClaimer;
import me.Jakubok.nations.collections.ChunkBinaryTree;
import me.Jakubok.nations.terrain.ModChunkPos;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public class DebugItemBase extends Item {

    ChunkBinaryTree chunks = new ChunkBinaryTree();
    public DebugItemBase() {
        super(new FabricItemSettings()
        .maxCount(1)
        .group(Nations.nations_tab)
        .fireproof());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        BlockPos pos = user.getBlockPos();
        user.sendMessage(Text.of("Your co-ordinates: " + Integer.toString(pos.getX()) + " " + Integer.toString(pos.getY()) + " " + Integer.toString(pos.getZ())), false);
        Chunk chunk = world.getChunk(pos);
        ChunkPos chunkPos = chunk.getPos();
        user.sendMessage(Text.of("Your chunk: " + Integer.toString(chunkPos.x) + " " + Integer.toString(chunkPos.z)), false);

        ModChunkPos modChunkPos = new ModChunkPos(chunkPos, new TeritorryClaimer(world));
        if (!chunks.contains(modChunkPos)) chunks.add(modChunkPos);
        modChunkPos = chunks.get(modChunkPos);

        if (modChunkPos.isBelonging(pos)) {
            user.sendMessage(Text.of("This block was belonging"), true);
        }
        else {
            user.sendMessage(Text.of("This block wasn't belonging"), true);
        }

        Random random = new Random();
        modChunkPos.markAsBelonging(pos);

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
