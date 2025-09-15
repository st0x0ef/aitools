package com.st0x0ef.aitools.common.blocks.entities;

import com.st0x0ef.aitools.common.components.AIToolData;
import com.st0x0ef.aitools.common.menus.ComputerMenu;
import com.st0x0ef.aitools.common.recipes.ComputerInput;
import com.st0x0ef.aitools.common.recipes.ComputerRecipe;
import com.st0x0ef.aitools.common.registries.BlockEntitiesRegistry;
import com.st0x0ef.aitools.common.registries.DataComponentsRegistry;
import com.st0x0ef.aitools.common.registries.RecipesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComputerBlockEntity extends BaseContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
    private final RecipeManager.CachedCheck<ComputerInput, ComputerRecipe> quickCheck = RecipeManager.createCheck(RecipesRegistry.COMPUTER_TYPE.get());


    public ComputerBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntitiesRegistry.COMPUTER.get(), pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.aitools.computer");
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        items = NonNullList.copyOf(newItems);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new ComputerMenu(i, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
    }

    public void tick(Level level) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack outputStack = getItem(3);
        if (outputStack.isEmpty()) {
            Optional<RecipeHolder<ComputerRecipe>> recipeHolder = quickCheck.getRecipeFor(new ComputerInput(getLevel().getBlockEntity(getBlockPos()), getItems()) , level);
            if (recipeHolder.isPresent()) {
                ComputerRecipe recipe = recipeHolder.get().value();
                ItemStack resultStack = recipe.getResultItem(level.registryAccess()).copy();

                List<AIToolData> AIToolDataList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = getItem(i);
                    if (stack.has(DataComponentsRegistry.AI_TOOL_DATA.get())) {
                        AIToolDataList.add(stack.get(DataComponentsRegistry.AI_TOOL_DATA.get()));
                    }
                    stack.shrink(1);

                    if (stack.isEmpty()) {
                        setItem(i, ItemStack.EMPTY);
                    }
                }

                if (AIToolDataList.size() == 2) {
                    resultStack.set(DataComponentsRegistry.AI_TOOL_DATA.get(), AIToolData.add(AIToolDataList.get(0), AIToolDataList.get(1)));
                }

                setItem(3, resultStack);

                setChanged();
            }
        }
    }

}
