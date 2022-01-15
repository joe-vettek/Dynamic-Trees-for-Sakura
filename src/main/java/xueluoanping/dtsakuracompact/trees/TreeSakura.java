package xueluoanping.dtsakuracompact.trees;

import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import scala.tools.nsc.doc.model.Public;
import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.growthlogic.SakuraLogic;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.trees.species.SpeciesSakuraSmall;

import java.util.List;

public class TreeSakura extends TreeFamily {
    public static Block leavesBlock = Block.getBlockFromName("sakura:sakuraleaves");
    public static Block logBlock = Block.getBlockFromName("sakura:sakura_log");
    public static Block saplingBlock = Block.getBlockFromName("sakura:sakura_sapling");

    public class SpeciesSakura extends Species {

        SpeciesSakura(TreeFamily treeFamily) {
            super(treeFamily.getName(), treeFamily, ModContent.sakuraLeavesProperties);

            setBasicGrowingParameters(0.2f, 16f, 1, 2, 1.6f);
            envFactor(BiomeDictionary.Type.COLD, 1.05f);
            envFactor(BiomeDictionary.Type.HOT, 0.50f);
            envFactor(BiomeDictionary.Type.DRY, 0.50f);
            envFactor(BiomeDictionary.Type.FOREST, 1.05f);
            envFactor(BiomeDictionary.Type.WET, 1.05f);

            generateSeed();
//            setGrowthLogicKit(new SakuraLogic(12));
            setupStandardSeedDropping();
//            setSoilLongevity(16);


        }

        @Override
        public int maxBranchRadius() {
            return 4;
        }

        @Override
        public int getLowestBranchHeight(World world, BlockPos pos) {
            long day = world.getWorldTime() / 24000L;
            int month = (int) day / 30; // Change the hashs every in-game month

            return (int) (super.getLowestBranchHeight(world, pos) * biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.up(month), 3) % 4)); // Vary the height energy by a psuedorandom hash function
        }


    }

    public Species smallSakuraSpecies;

    protected boolean isLocationCold(World world, BlockPos pos) {
        return BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.COLD);
    }

    public TreeSakura() {
        super(new ResourceLocation(info.modid, "sakura"));

        setPrimitiveLog(logBlock.getDefaultState(), new ItemStack(logBlock, 1, 0));

        ModContent.sakuraLeavesProperties.setTree(this);

        addConnectableVanillaLeaves((state) -> state.getBlock() == leavesBlock);

//        // This will cause the swamp variation of the oak to grow when the player plants a common oak acorn in a swamp.
        addSpeciesLocationOverride((world, trunkPos) -> isLocationCold(world, trunkPos) ? smallSakuraSpecies : Species.NULLSPECIES);

    }

    @Override
    public void createSpecies() {
        setCommonSpecies(new SpeciesSakura(this));
        smallSakuraSpecies = new SpeciesSakuraSmall(this);
    }

    @Override
    public void registerSpecies(IForgeRegistry<Species> speciesRegistry) {
        super.registerSpecies(speciesRegistry);
        speciesRegistry.register(smallSakuraSpecies);
    }

    @Override
    public List<Item> getRegisterableItems(List<Item> itemList) {
        return super.getRegisterableItems(itemList);
    }

}
