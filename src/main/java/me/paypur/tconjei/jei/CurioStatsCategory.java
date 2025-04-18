package me.paypur.tconjei.jei;

import com.ssakura49.sakuratinker.content.tools.stats.CharmChainMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.STExtraMaterialStats;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.paypur.tconjei.ColorManager.*;
import static me.paypur.tconjei.TConJEI.MOD_ID;

public class CurioStatsCategory extends AbstractMaterialStatsCategory {

    public CurioStatsCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.icon = guiHelper.createDrawable(new ResourceLocation(MOD_ID, "textures/gui/jei.png"), 48, 0, 16, 16);
        this.title = Component.translatable("tconjei.tool_stats.curio");
        this.recipeType = RecipeType.create(MOD_ID, "curio_stats", MaterialStatsWrapper.class);
        this.tag = TinkerTags.Items.ARMOR; // 根据实际修改Tag
    }

    @Override
    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        super.draw(wrapper, recipeSlotsView, gui, mouseX, mouseY);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<CharmChainMaterialStats> charmChainOptional = wrapper.getStats(CharmChainMaterialStats.ID);
        Optional<STExtraMaterialStats> charmCoreOptional = wrapper.getStats(STExtraMaterialStats.CHARM_CORE.getIdentifier());

//        Optional<? extends IMaterialStats> statOptional = charmChainOptional
//                .map(IMaterialStats.class::cast)
//                .or(() -> charmCoreOptional.map(IMaterialStats.class::cast));
        Optional<? extends IMaterialStats> statOptional = Stream.of(charmChainOptional, charmCoreOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();


        if (statOptional.isPresent()) {
            drawTraits(gui, wrapper.getTraits(statOptional.get().getIdentifier()), lineNumber);
        }

        if (charmChainOptional.isPresent()) {
            CharmChainMaterialStats stats = charmChainOptional.get();
            drawComponent(gui, stats.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            for (int i = 0; i < 5; i++) { // 5个统计属性
                if (i < stats.getLocalizedInfo().size()) {
                    drawStatComponent(gui, stats.getLocalizedInfo().get(i), lineNumber++);
                }
            }
            lineNumber += 1.5f;   // 攻击力
        }

        if (charmCoreOptional.isPresent()) {
            STExtraMaterialStats curioCore = charmCoreOptional.get();
            drawComponent(gui, curioCore.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            drawComponent(gui, curioCore.getLocalizedInfo().get(0), 0, lineNumber, TEXT_COLOR, false);
//            lineNumber += LINE_SPACING;
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        // 材料基础提示
        List<Component> materialTooltips = getMaterialTooltip(wrapper, mouseX, mouseY);
        if (!materialTooltips.isEmpty()) {
            tooltip.addAll(materialTooltips);
            return;
        }

        float lineNumber = 2f;

        // 处理CharmChain提示
        Optional<CharmChainMaterialStats> charmChainOptional = wrapper.getStats(CharmChainMaterialStats.ID);
        Optional<STExtraMaterialStats> charmCoreOptional = wrapper.getStats(STExtraMaterialStats.CHARM_CORE.getIdentifier());

        Optional<? extends IMaterialStats> statOptional = Stream.of(charmChainOptional, charmCoreOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        if (statOptional.isPresent()) {
            List<Component> traitTooltips = getTraitTooltips(wrapper.getTraits(statOptional.get().getIdentifier()), mouseX, mouseY, lineNumber);
            if (!traitTooltips.isEmpty()) {
                tooltip.addAll(traitTooltips);
                return;
            }
        }

        if (charmChainOptional.isPresent()) {
            lineNumber++;
            CharmChainMaterialStats chain = charmChainOptional.get();
            Optional<List<Component>> component = Stream.of(
                            getStatTooltip(chain, 0, mouseX, mouseY, lineNumber++),
                            getStatTooltip(chain, 1, mouseX, mouseY, lineNumber++),
                            getStatTooltip(chain, 2, mouseX, mouseY, lineNumber++),
                            getStatTooltip(chain, 3, mouseX, mouseY, lineNumber++),
                            getStatTooltip(chain, 4, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                tooltip.addAll(component.get());
                return;
            }
            lineNumber += LINE_SPACING;
        }

        if (charmCoreOptional.isPresent()) {
            lineNumber += LINE_SPACING + 2;
        }
    }
}