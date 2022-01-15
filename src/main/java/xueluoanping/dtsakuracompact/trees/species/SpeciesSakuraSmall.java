package xueluoanping.dtsakuracompact.trees.species;

import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.world.biome.BiomeBambooForest;
import cn.mcmod.sakura.world.biome.BiomeMapleForest;
import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.growthlogic.SakuraLogic;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.systems.featuregen.FeatureGenFallenLeaves;

import java.util.Random;

public class SpeciesSakuraSmall extends Species {

    public SpeciesSakuraSmall(TreeFamily treeFamily) {
        super(new ResourceLocation(info.modid, "sakurasmall"), treeFamily, ModContent.sakuraLeavesProperties);
        setRequiresTileEntity(true);
        this.setBasicGrowingParameters(0.5f, 6.0f, 3, 1, 2.0f);
        envFactor(BiomeDictionary.Type.COLD, 1.05f);
        envFactor(BiomeDictionary.Type.HOT, 0.50f);
        envFactor(BiomeDictionary.Type.DRY, 0.50f);
        envFactor(BiomeDictionary.Type.FOREST, 1.05f);
        envFactor(BiomeDictionary.Type.WET, 1.05f);
        setGrowthLogicKit(new SakuraLogic());
        setupStandardSeedDropping();
    }

    //Swamp Oaks are just oaks in a swamp..  So they have the same seeds
    @Override
    public ItemStack getSeedStack(int qty) {
        return getFamily().getCommonSpecies().getSeedStack(qty);
    }

    //Swamp Oaks are just oaks in a swamp..  So they have the same seeds
    @Override
    public Seed getSeed() {
        return getFamily().getCommonSpecies().getSeed();
    }

    @Override
    public boolean isBiomePerfect(Biome biome) {
        if (biome instanceof BiomeBambooForest)
            return true;
        return false;
    }
}
