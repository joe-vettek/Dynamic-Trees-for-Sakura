package xueluoanping.dtsakuracompact.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.trees.TreeOak;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleGreen;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleOrange;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleRed;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleYellow;

import java.util.List;

public class TreeMaple extends TreeFamily {
    public static Block leavesBlock = Block.getBlockFromName("sakura:maple_leaves_red");
    public static Block logBlock = Block.getBlockFromName("sakura:maple_log");
    public static Block saplingBlock = Block.getBlockFromName("sakura:maple_sapling_red");
    public Species mapleGreen;
    public Species mapleOrange;
    public Species mapleYellow;

    public TreeMaple() {
        super(new ResourceLocation(info.modid, "maple"));
//        hasConiferVariants=true;
//        ModContent.sakuraLeavesProperties.setTree(this);
//        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);
        setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));
        ModContent.mapleredLeavesProperties.setTree(this);
        ModContent.maplegreenLeavesProperties.setTree(this);
        ModContent.mapleorangeLeavesProperties.setTree(this);
        ModContent.mapleyellowLeavesProperties.setTree(this);
        addConnectableVanillaLeaves((state) -> {
            return state.getBlock() == leavesBlock || (state.getBlock() == SpeciesMapleGreen.leavesBlock)||
                    (state.getBlock() == SpeciesMapleOrange.leavesBlock)|| (state.getBlock() == SpeciesMapleYellow.leavesBlock);
        });

    }


    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesMapleRed(this));
        mapleGreen = new SpeciesMapleGreen(this);
        mapleOrange = new SpeciesMapleOrange(this);
        mapleYellow=new SpeciesMapleYellow(this);
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.register(mapleGreen);
        speciesRegistry.register(mapleOrange);
        speciesRegistry.register(mapleYellow);
    }


    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        //Since we generated the three species internally we need to let the seed out to be registered.
        itemList.add(mapleGreen.getSeed());
        itemList.add(mapleOrange.getSeed());
        itemList.add(mapleYellow.getSeed());
        return super.getRegisterableItems(itemList);
    }



}
