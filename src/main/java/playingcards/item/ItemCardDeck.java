package playingcards.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import playingcards.PlayingCards;
import playingcards.entity.EntityCardDeck;
import playingcards.item.base.ItemBase;
import playingcards.util.CardHelper;
import playingcards.util.ItemHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCardDeck extends ItemBase {

    public ItemCardDeck() {
        super(new Item.Properties().group(PlayingCards.TAB).maxStackSize(1));
        this.addPropertyOverride(new ResourceLocation("skin"), (stack, world, player) -> ItemHelper.getNBT(stack).getByte("SkinID"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = ItemHelper.getNBT(stack);
        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Cover: " + TextFormatting.AQUA + CardHelper.CARD_SKIN_NAMES[nbt.getByte("SkinID")]));
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {

        if (group == PlayingCards.TAB) {

            for (byte colorID = 0; colorID < CardHelper.CARD_SKIN_NAMES.length; colorID++) {

                ItemStack stack = new ItemStack(this);
                CompoundNBT nbt = ItemHelper.getNBT(stack);

                nbt.putByte("SkinID", colorID);
                items.add(stack);
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        CompoundNBT nbt = ItemHelper.getNBT(context.getItem());
        EntityCardDeck cardDeck = new EntityCardDeck(world, context.getHitVec(), context.getPlacementYaw(), nbt.getByte("SkinID"));
        world.addEntity(cardDeck);
        context.getItem().shrink(1);
        return ActionResultType.SUCCESS;
    }
}
