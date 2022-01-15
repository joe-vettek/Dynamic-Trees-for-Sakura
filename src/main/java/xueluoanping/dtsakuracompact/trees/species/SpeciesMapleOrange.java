package xueluoanping.dtsakuracompact.trees.species;

import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveGreen;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveOrange;
import cn.mcmod.sakura.world.biome.BiomeMapleForest;
import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.items.Seed;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenPodzol;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.systems.featuregen.FeatureGenFallenLeaves;

import java.util.Random;

public class SpeciesMapleOrange extends Species {
    public static Block leavesBlock = Block.getBlockFromName("sakura:maple_leaves_orange");
    public static Block logBlock = Block.getBlockFromName("sakura:maple_log");
    public static Block saplingBlock = Block.getBlockFromName("sakura:maple_sapling_orange");

    public SpeciesMapleOrange(TreeFamily treeFamily) {
        super(new ResourceLocation(info.modid, "mapleorange"), treeFamily, (ILeavesProperties) ModContent.mapleorangeLeavesProperties);
        setRequiresTileEntity(true);

        setBasicGrowingParameters(0.1F, 15.0F, 4, 4, 1.25F);
        envFactor(BiomeDictionary.Type.HOT, 0.8F);
        envFactor(BiomeDictionary.Type.DRY, 0.8F);
        envFactor(BiomeDictionary.Type.FOREST, 1.05F);
        envFactor(BiomeDictionary.Type.COLD, 1.05F);
        generateSeed();

        setupStandardSeedDropping();
        addGenFeature(new FeatureGenFallenLeaves().setFallenLeave(BlockLoader.FALLEN_LEAVES_MAPLE_ORANGE.getDefaultState()));

    }




    @Override
    public boolean isBiomePerfect(Biome biome) {
        if (biome instanceof BiomeMapleForest)
            return true;
        return false;
    }

    @Override
    public boolean rot(World world, BlockPos pos, int neighborCount, int radius, Random random, boolean rapid) {
        if (super.rot(world, pos, neighborCount, radius, random, rapid)) {
            if (radius > 4 && TreeHelper.isRooty(world.getBlockState(pos.down())) && world.getLightFor(EnumSkyBlock.SKY, pos) < 4) {
                world.setBlockState(pos, random.nextInt(3) == 0 ? ModBlocks.blockStates.redMushroom : ModBlocks.blockStates.brownMushroom);//Change branch to a mushroom
                world.setBlockState(pos.down(), ModBlocks.blockStates.podzol);//Change rooty dirt to Podzol
            }
            return true;
        }

        return false;
    }
}
