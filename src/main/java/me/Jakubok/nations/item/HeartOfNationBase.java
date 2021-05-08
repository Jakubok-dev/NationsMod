package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;

public class HeartOfNationBase extends Item {

    protected int chargeLevel = 0;
    //protected ChunkBinaryTree tree = new ChunkBinaryTree();

    public HeartOfNationBase() {
        super(new FabricItemSettings()
                .group(Nations.nations_tab)
        );
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
