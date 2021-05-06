package me.Jakubok.nations.block;

import me.Jakubok.nations.collections.InstitutionsHandler;
import me.Jakubok.nations.gui.TownCreationDescription;
import me.Jakubok.nations.util.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class NationPillarEntity extends BlockEntity implements NamedScreenHandlerFactory {

    public int charge_level = 0;
    public InstitutionsHandler institutions = new InstitutionsHandler();
    protected CompoundTag tempTag = null;

    public NationPillarEntity() {
        super(Blocks.NATION_PILLAR_ENTITY);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("charge_level", charge_level);
        tag.put("institutions", institutions.saveToTag(new CompoundTag()));
        super.toTag(tag);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        charge_level = tag.getInt("charge_level");
        if (!hasWorld()) {
            tempTag = tag;
            return;
        }
        institutions = new InstitutionsHandler((CompoundTag) tag.get("institutions"), world);
    }

    @Override
    public Text getDisplayName() {
        // Using the block name as the screen title
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new TownCreationDescription(syncId, player.inventory, ScreenHandlerContext.create(world, pos));
    }

    protected boolean institutionsImported() {
        return tempTag == null;
    }

    public void importInstitutions() {
        if (institutionsImported()) return;
        institutions = new InstitutionsHandler((CompoundTag) tempTag.get("institutions"), world);
        tempTag = null;
    }
}
