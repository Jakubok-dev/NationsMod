package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import me.Jakubok.nations.administration.Town;
import me.Jakubok.nations.administration.TownDistrict;
import me.Jakubok.nations.terrain.ModChunkPos;
import me.Jakubok.nations.util.GlobalChunkRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;

public class HeartOfNationBase extends Item {

    protected int chargeLevel = 0;
    //protected ChunkBinaryTree tree = new ChunkBinaryTree();

    public HeartOfNationBase() {
        super(new FabricItemSettings()
                .group(Nations.nations_tab)
                .maxCount(1)
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            user.sendMessage(Text.of("Client"), false);
        else user.sendMessage(Text.of("Server"), false);

        ModChunkPos pos = new ModChunkPos(new ChunkPos(user.getBlockPos()));

        if (user.hasStatusEffect(StatusEffect.byRawId(32))) {
            List<ModChunkPos> list = GlobalChunkRegistry.toList(world);
            for (ModChunkPos elem : list) {
                user.sendMessage(Text.of(elem.x + " " + elem.z), false);
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        if (!GlobalChunkRegistry.contains(pos, world)) {
            user.sendMessage(Text.of("Nobody"), false);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        if (GlobalChunkRegistry.get(world, pos).getOwner(user.getBlockPos()) == null) {
            user.sendMessage(Text.of("Nobody"), false);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        else if (GlobalChunkRegistry.get(world, pos).getOwner(user.getBlockPos()) instanceof TownDistrict) {
            TownDistrict dist = (TownDistrict)GlobalChunkRegistry.get(world, pos).getOwner(user.getBlockPos());
            user.sendMessage(Text.of(dist.name), false);
            user.sendMessage(Text.of(dist.town.name), false);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    // For tree debugging
//    @Override
//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        if (world.isClient) return super.use(world, user, hand);
//
//        ModChunkPos pos = new ModChunkPos(new ChunkPos(user.getBlockPos()));
//        if (!tree.contains(pos)) {
//            user.sendMessage(Text.of("Creating chunk"), true);
//            tree.add(pos);
//        }
//        pos = tree.get(pos);
//
//        if (user.hasStatusEffect(StatusEffect.byRawId(32))) {
//            List<ModChunkPos> list = tree.treeToList();
//            for (ModChunkPos elem : list) {
//                user.sendMessage(Text.of(elem.x + " " + elem.z), false);
//            }
//            return TypedActionResult.success(user.getStackInHand(hand));
//        }
//
//        Node<ModChunkPos> leaf = tree.getNode(pos);
//        user.sendMessage(Text.of("Node: " + leaf.value.x + " " + leaf.value.z), false);
//        Node<ModChunkPos> left = leaf.left;
//        Node<ModChunkPos> right = leaf.right;
//        if (left == null)
//            left = new Node<ModChunkPos>(new ModChunkPos(new ChunkPos(2137, 2137)));
//        if (right == null)
//            right = new Node<ModChunkPos>(new ModChunkPos(new ChunkPos(2137, 2137)));
//        user.sendMessage(Text.of("Left child: " + left.value.x + " " + left.value.z), false);
//        user.sendMessage(Text.of("Right child: " + right.value.x + " " + right.value.z), false);
//
//        return TypedActionResult.success(user.getStackInHand(hand));
//    }
}
