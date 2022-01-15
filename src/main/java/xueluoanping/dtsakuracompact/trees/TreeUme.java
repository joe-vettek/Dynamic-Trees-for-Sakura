package xueluoanping.dtsakuracompact.trees;

import cn.mcmod.sakura.SakuraMain;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenFruit;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.growthlogic.UmeLogic;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.proxy.CommonProxy;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleGreen;

import java.util.List;

public class TreeUme extends TreeFamily {
    public static Block leavesBlock = Block.getBlockFromName("sakura:umeleaves");
    public static Block logBlock = Block.getBlockFromName("sakura:ume_log");

    public static Block saplingBlock = Block.getBlockFromName("sakura:ume_sapling");
    public static Item fruit = Item.getByNameOrId("sakura:foodset");

    public class SpeciesUme extends Species {

        SpeciesUme(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.umeLeavesProperties);

            // Small,  skinny, fast growing trees.
            this.setBasicGrowingParameters(0.5f, 6.0f, 1, 2, 2.0f);
            envFactor(BiomeDictionary.Type.COLD, 1.05f);
            envFactor(BiomeDictionary.Type.HOT, 0.50f);
            envFactor(BiomeDictionary.Type.WET, 1.2f);
            envFactor(BiomeDictionary.Type.DRY, 0.50f);
            envFactor(BiomeDictionary.Type.FOREST, 1.2f);
            generateSeed();

            setupStandardSeedDropping();
//            plum
            CommonProxy.plum.setSpecies(this);
//            ItemStack plum =TreeUme.fruit.getDefaultInstance();
            ItemStack plum = new ItemStack(fruit, 1, 166);
//            plum.setItemDamage(166);
            addGenFeature(new FeatureGenFruit(CommonProxy.plum.setDroppedItem(plum)).setRayDistance(2));
//            setGrowthLogicKit(new UmeLogic());
        }

        @Override
        public int maxBranchRadius() {
            return 20;
        }
    }

    public TreeUme() {
        super(new ResourceLocation(info.modid, "ume"));
        setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));
        ModContent.umeLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesUme(this));
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {

        return super.getRegisterableItems(itemList);
    }

}
