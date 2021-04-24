package me.Jakubok.nations.item;

import me.Jakubok.nations.Nations;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartOfNationBase extends Item {

    protected int chargeLevel = 0;

    public HeartOfNationBase() {
        super(new FabricItemSettings()
                .group(Nations.nations_tab)
                .maxCount(1)
        );
    }

    //@Override
    //public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    //    Text text = new TranslatableText("item.nationsmod.health_of_nation.lore." + Integer.toString(getChargeLevel()));
    //    tooltip.add(text);
    //}

    //public int getChargeLevel() {
    //    return chargeLevel;
    //}

    //@Override
    //public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    //    if (world.isClient()) return super.use(world, user, hand);
    //
    //    if (user.hasStatusEffect(StatusEffect.byRawId(32))) {
    //        if (!(chargeLevel + (user.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1) > 5)) {
    //            chargeLevel += user.getStatusEffect(StatusEffect.byRawId(32)).getAmplifier()+1;
    //            user.removeStatusEffect(StatusEffect.byRawId(32));
    //        }
    //    }
    //    return TypedActionResult.success(user.getStackInHand(hand));
    //}
}
