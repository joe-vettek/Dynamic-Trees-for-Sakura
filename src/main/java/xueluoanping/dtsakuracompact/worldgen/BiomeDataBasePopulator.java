package xueluoanping.dtsakuracompact.worldgen;

import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.block.BlockPlantBamboo;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveGreen;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveOrange;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveRed;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveYellow;
import cn.mcmod_mmf.mmlib.util.WorldUtil;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.IBiomeDataBasePopulator;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBase;
import com.ferreusveritas.dynamictrees.worldgen.BiomeDataBasePopulatorJson;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xueluoanping.dtsakuracompact.DynamicTrees_Sakura;
import xueluoanping.dtsakuracompact.pro.info;
import cn.mcmod.sakura.world.biome.*;

import java.util.Random;

public class BiomeDataBasePopulator implements IBiomeDataBasePopulator {

    private static Species sakura;

    public static final String RESOURCEPATH = "worldgen/default.json";

    private final BiomeDataBasePopulatorJson jsonPopulator;

    public BiomeDataBasePopulator() {
        jsonPopulator = new BiomeDataBasePopulatorJson(new ResourceLocation(info.modid, RESOURCEPATH));
    }

    public void populate(BiomeDataBase dbase) {
        this.jsonPopulator.populate(dbase);
        removeTreeGen(SakuraBiomes.BAMBOOFOREST);
        umeGen(dbase);
    }

    private static Species ume;

    private void umeGen(BiomeDataBase dbase) {
        ume = TreeRegistry.findSpecies(new ResourceLocation(info.modid, "ume"));
        Biome.REGISTRY.forEach(biome -> {
            if (!BiomeDictionary.hasType(biome, BiomeDictionary.Type.DEAD)
                    && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) {
                BiomePropertySelectors.RandomSpeciesSelector selector = new BiomePropertySelectors.RandomSpeciesSelector().add(100).
                        add(ume, 2);
                dbase.setSpeciesSelector(biome, selector, BiomeDataBase.Operation.SPLICE_BEFORE);

            }
        });
    }


    private void removeTreeGen(Biome extendedBiome) {
        MinecraftForge.TERRAIN_GEN_BUS.register(new DecorateEventHandler_bambooForest());
//        MinecraftForge.TERRAIN_GEN_BUS.register(new DecorateEventHandler_mapleForest());
    }


    //Here is the custom bamboo forest tree generation (bamboo)
    public class DecorateEventHandler_bambooForest {
        @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
        public void onEvent(DecorateBiomeEvent.Decorate event) {
            if (event.getType() == DecorateBiomeEvent.Decorate.EventType.TREE) {

                //Disable vanilla generation
                event.setResult(Event.Result.DENY);

                //Get coordinates
                World worldIn = event.getWorld();
                Random rand = worldIn.rand;
                int x0 = event.getChunkPos().x * 16;
                int z0 = event.getChunkPos().z * 16;
                BlockPos center = new BlockPos(x0 + rand.nextInt(16) + 8
                        , 0
                        , z0 + rand.nextInt(16) + 8);

                //Only live in bamboo forest of the Sakura Mod
                Biome biome = event.getWorld().getBiomeForCoordsBody(center);
                if (!(biome instanceof BiomeBambooForest)) {
                    return;
                }

                //Gernerate bamboo and bamboo shoots around
                int times = 9;
                while (times > 0) {
                    times--;

                    //Gernerate bamboo
                    int xn = x0 + rand.nextInt(16) + 8;
                    int zn = z0 + rand.nextInt(16) + 8;
                    BlockPos newP = new BlockPos(xn, 0, zn);
                    BlockPos position = WorldUtil.getInstance().findGround(worldIn, newP, true, true, true);
                    if(position==null)return;
                    int i = 9 + rand.nextInt(9);
                    for (int i2 = 0; i2 < i; ++i2) {
                        BlockPos blockpos = position.up(i2);
                        if ((BlockLoader.BAMBOOSHOOT.canBlockStay(worldIn, blockpos) ||
                                worldIn.getBlockState(blockpos.down()).getBlock() instanceof BlockPlantBamboo) &&
                                (worldIn.isAirBlock(blockpos) ||
                                        worldIn.getBlockState(blockpos).getMaterial() == Material.PLANTS
                                                && !(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLeaves))) {
                            worldIn.setBlockState(blockpos, BlockLoader.BAMBOO.getDefaultState(), 2);
                        }
                    }

                    //Probability to gernerate bamboo shoots around bamboo
                    if ((rand.nextInt(5) < 3)) {
                        continue;
                    }
                    int xn2 = xn + (rand.nextBoolean() ? 1 : -1);
                    int zn2 = zn + (rand.nextBoolean() ? 1 : -1);
                    BlockPos newP_shoot = new BlockPos(xn2, 0, zn2);
                    BlockPos newPos_bambooshoot = WorldUtil.getInstance().findGround(worldIn, newP_shoot, true, true, true);
                    if(newPos_bambooshoot==null)return;
                    if ((newPos_bambooshoot != null) && (BlockLoader.BAMBOOSHOOT.canBlockStay(worldIn, newPos_bambooshoot))) {
                        worldIn.setBlockState(newPos_bambooshoot, BlockLoader.BAMBOOSHOOT.getDefaultState(), 2);
                        //DynamicTrees_Sakura.LOGGER.debug("register_BambooForest");
                    }

                    //Does not affect roofed forests
                    // Make custom trees here or do nothing. event.setResult(Result.DENY);
                    // Result.DENY will prevent vanilla trees from being created
                }
            }
        }
    }

    //Here is the custom maple forest tree generation (fallenleaves)
    public class DecorateEventHandler_mapleForest {
        @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
        public void onEvent(DecorateBiomeEvent.Decorate event) {
            if (event.getType() == DecorateBiomeEvent.Decorate.EventType.TREE) {

                //Disable vanilla generation
                event.setResult(Event.Result.DENY);

                //Get coordinates
                World worldIn = event.getWorld();
                Random rand = worldIn.rand;
                int x0 = event.getChunkPos().x * 16;
                int z0 = event.getChunkPos().z * 16;
                BlockPos center = new BlockPos(x0 + rand.nextInt(16) + 8
                        , 0
                        , z0 + rand.nextInt(16) + 8);

                //Only live in bamboo forest of the Sakura Mod
                Biome biome = event.getWorld().getBiomeForCoordsBody(center);
                if (!(biome instanceof BiomeMapleForest)) {
                    return;
                }

                //Gernerate fallen leaves
                int times = 20;
                while (times > 0) {
                    times--;
                    //Gernerate fallen leaves
                    int xn = x0 + rand.nextInt(16) + 8;
                    int zn = z0 + rand.nextInt(16) + 8;
                    BlockPos newP = new BlockPos(xn, 0, zn);
                    BlockPos position = WorldUtil.getInstance().findGround(worldIn, newP, false, true, true);
                    int type = rand.nextInt(4);
                    switch (type) {
                        case 0:
                            worldIn.setBlockState(position, BlockLoader.FALLEN_LEAVES_MAPLE_GREEN.getDefaultState(), 0);
                        case 1:
                            worldIn.setBlockState(position, BlockLoader.FALLEN_LEAVES_MAPLE_ORANGE.getDefaultState(), 0);
                        case 2:
                            worldIn.setBlockState(position, BlockLoader.FALLEN_LEAVES_MAPLE_RED.getDefaultState(), 0);
                        case 3:
                            worldIn.setBlockState(position, BlockLoader.FALLEN_LEAVES_MAPLE_YELLOW.getDefaultState(), 0);
                        default:
                            return;
                    }
                }
            }
        }

    }


}

