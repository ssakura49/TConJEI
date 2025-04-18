package me.paypur.tconjei.jei;

import me.paypur.tconjei.TConJEI;
import me.paypur.tconjei.Utils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.tables.TinkerTables;

import java.util.List;

import static me.paypur.tconjei.TConJEI.MOD_ID;

@SuppressWarnings("unused")
@JeiPlugin
public class TConJEIPlugin implements IModPlugin {

    ResourceLocation UID = new ResourceLocation(MOD_ID, "jei_plugin");
    private static final RecipeType<MaterialStatsWrapper> HARVEST_STATS = RecipeType.create(MOD_ID, "harvest_stats", MaterialStatsWrapper.class);
    private static final RecipeType<MaterialStatsWrapper> RANGED_STATS = RecipeType.create(MOD_ID, "ranged_stats", MaterialStatsWrapper.class);
    private static final RecipeType<MaterialStatsWrapper> ARMOR_STATS = RecipeType.create(MOD_ID, "armor_stats", MaterialStatsWrapper.class);
    private static final RecipeType<MaterialStatsWrapper> CURIO_STATS = RecipeType.create(MOD_ID, "curio_stats", MaterialStatsWrapper.class);

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<MaterialStatsWrapper> statsWrappers = Utils.getMaterialWrappers();
        registration.addRecipes(HARVEST_STATS, statsWrappers.stream()
                .filter(w -> w.hasStats(TConJEI.HARVEST_STAT_IDS))
                .toList());
        registration.addRecipes(RANGED_STATS, statsWrappers.stream()
                .filter(w -> w.hasStats(TConJEI.RANGED_STAT_IDS))
                .toList());
        registration.addRecipes(ARMOR_STATS, statsWrappers.stream()
                .filter(w -> w.hasStats(TConJEI.ARMOR_STAT_IDS))
                .toList());
        registration.addRecipes(CURIO_STATS, statsWrappers.stream()
                .filter(w -> w.hasStats(TConJEI.CURIO_STAT_IDS))
                .toList());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        final IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new HarvestStatsCategory(guiHelper));
        registration.addRecipeCategories(new RangedStatsCategory(guiHelper));
        registration.addRecipeCategories(new ArmorStatsCategory(guiHelper));
        registration.addRecipeCategories(new CurioStatsCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TinkerTables.tinkerStation.asItem()), HARVEST_STATS, RANGED_STATS, ARMOR_STATS, CURIO_STATS);
        registration.addRecipeCatalyst(new ItemStack(TinkerTables.tinkersAnvil.asItem()), HARVEST_STATS, RANGED_STATS, ARMOR_STATS, CURIO_STATS);
        registration.addRecipeCatalyst(new ItemStack(TinkerTables.scorchedAnvil.asItem()), HARVEST_STATS, RANGED_STATS, ARMOR_STATS, CURIO_STATS);
    }

}
