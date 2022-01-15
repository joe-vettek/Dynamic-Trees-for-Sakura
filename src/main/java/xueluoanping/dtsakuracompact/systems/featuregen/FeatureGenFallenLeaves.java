package xueluoanping.dtsakuracompact.systems.featuregen;

import com.ferreusveritas.dynamictrees.ModBlocks;
import com.ferreusveritas.dynamictrees.api.IPostGrowFeature;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NodeFindEnds;
import com.ferreusveritas.dynamictrees.trees.Species;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class FeatureGenFallenLeaves implements IPostGrowFeature {
    IBlockState fallenLeave;

    public FeatureGenFallenLeaves setFallenLeave(IBlockState leave) {
        this.fallenLeave = leave;
        return this;
    }

    @Override
    public boolean postGrow(World world, BlockPos rootPos, BlockPos treePos, Species species, int soilLife, boolean natural) {

        NodeFindEnds endFinder = new NodeFindEnds();
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(endFinder));
        List<BlockPos> endPoints = endFinder.getEnds();
        if (!endPoints.isEmpty()) {

            Random random = world.rand;
            BlockPos pos = endPoints.get(random.nextInt(endPoints.size()));

            int x = pos.getX() + random.nextInt(5) - 2;
            int z = pos.getZ() + random.nextInt(5) - 2;

            final int darkThreshold = 4;

            for (int i = 0; i < 32; i++) {

                BlockPos offPos = new BlockPos(x, pos.getY() - 1 - i, z);

                if (!world.isAirBlock(offPos)) {
                    Block block = world.getBlockState(offPos).getBlock();

                    if (block instanceof BlockBranch || block instanceof BlockMushroom || block instanceof BlockLeaves) {//Skip past Mushrooms and branches on the way down
                        continue;
                    } else if (block instanceof BlockFlower || block instanceof BlockTallGrass || block instanceof BlockDoublePlant) {//Kill Plants
                        if (world.getLightFor(EnumSkyBlock.SKY, offPos) <= darkThreshold) {
                            world.setBlockToAir(pos);
                        }
                        continue;
                    } else if (block != Blocks.AIR) {//Convert grass or dirt to podzol
                        testAir(world, offPos);
                        testAir(world, offPos.east(1));
                        testAir(world, offPos.east(1).north(1));
                        testAir(world, offPos.east(1).south(1));
                        testAir(world, offPos.west(1));
                        testAir(world, offPos.west(1).north(1));
                        testAir(world, offPos.west(1).south(1));
                        testAir(world, offPos.south(1));
                        testAir(world, offPos.north(1));
                    }
                    break;
                }
            }
        }

        return true;
    }

    private void testAir(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() != Blocks.AIR) {
            pos = pos.up(1);
            if (world.getBlockState(pos).getBlock() instanceof BlockBranch || world.getBlockState(pos.down(1)).getBlock() instanceof BlockBranch) {
                return;
            } else {
                if (world.getBlockState(pos).getBlock() == Blocks.AIR)
                    if (world.getBlockState(pos.down(1)).getBlock().isFullBlock(world.getBlockState(pos.down(1)))) {
                        //random generate , maybe not
                        if (world.rand.nextInt(5) < 3)
                            world.setBlockState(pos, fallenLeave);
                    }
            }
        }

    }

    public static void spreadFallenLeave(World world, BlockPos pos, IBlockState fallenLeave) {

//        int Podzolish = 0;
//
//        for (EnumFacing dir : EnumFacing.HORIZONTALS) {
//            BlockPos deltaPos = pos.offset(dir);
//            Block testBlock = world.getBlockState(deltaPos).getBlock();
//            Podzolish += (testBlock == Blocks.DIRT) && (world.getBlockState(deltaPos.up(1)) == fallenLeave) ? 1 : 0;
//            Podzolish += testBlock == ModBlocks.blockRootyDirt ? 1 : 0;
//            if (Podzolish >= 3) {
//                    world.setBlockState(pos, fallenLeave);
//                break;
//            }
//        }
    }
}
