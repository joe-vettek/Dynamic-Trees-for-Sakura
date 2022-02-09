package xueluoanping.dtsakuracompact.block;

import cn.mcmod.sakura.SakuraMain;
import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.block.BlockMapleSpile;
import cn.mcmod.sakura.block.tree.BlockMapleSapLog;
import cn.mcmod.sakura.client.SakuraParticleType;
import cn.mcmod.sakura.tileentity.TileEntityMapleCauldron;
import com.ferreusveritas.dynamictrees.blocks.BlockBranch;
import com.ferreusveritas.dynamictrees.entities.EntityFallingTree;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xueluoanping.dtsakuracompact.DynamicTrees_Sakura;
import xueluoanping.dtsakuracompact.pro.info;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.init.Blocks.AIR;


public class BlockMapleSpile_extened extends BlockMapleSpile {
    private static final AxisAlignedBB AABB_N = new AxisAlignedBB(0.3125D, 0.1875D, 0.1875D, 0.6875D, 0.6875D, 1.0D);
    private static final AxisAlignedBB AABB_S = new AxisAlignedBB(0.3125D, 0.1875D, 0D, 0.6875D, 0.6875D, 0.8125D);
    private static final AxisAlignedBB AABB_W = new AxisAlignedBB(0.1875D, 0.1875D, 0.3125D, 1.0D, 0.6875D, 0.6875D);
    private static final AxisAlignedBB AABB_E = new AxisAlignedBB(0D, 0.1875D, 0.3125D, 0.8125D, 0.6875D, 0.6875D);

    public BlockMapleSpile_extened() {
        super();
        setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case EAST:
                return AABB_E;
            case NORTH:
                return AABB_N;
            case SOUTH:
                return AABB_S;
            case WEST:
                return AABB_W;
            default:
                break;
        }
        return AABB_N;
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues())
            if (this.canPlaceAt(worldIn, pos, enumfacing)) return true;
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.setDefaultFacing(worldIn, pos, state);
    }

    public void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        for (EnumFacing enumfacing : FACING.getAllowedValues())
            if (this.canPlaceAt(worldIn, pos, enumfacing))
                worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
            boolean flag1 = !isExceptBlockForAttachWithPiston(block);
            boolean flag2 = block instanceof BlockMapleSapLog && iblockstate.getValue(BlockMapleSapLog.SAP_AGE).intValue() < 5;
            if (block == Block.getBlockFromName(info.modid + ":maplebranch"))
                flag2 = true;
//            if (block != AIR)
//                DynamicTrees_Sakura.LOGGER.debug("DTS:" + block.getLocalizedName() + iblockstate.getProperties().toString());
            return flag1 && flag2;
        }
        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (canWork(worldIn, pos, state)) {
            EnumFacing facing = state.getValue(FACING);
            BlockPos blockpos = pos.offset(facing.getOpposite());
            BlockPos blockpos0 = pos.down();
            IBlockState logState = worldIn.getBlockState(blockpos);
            IBlockState mapleCauldronState = worldIn.getBlockState(blockpos0);
            try {
                TileEntity te = worldIn.getTileEntity(blockpos0);
                if (te instanceof TileEntityMapleCauldron)
                    ((TileEntityMapleCauldron) te).getTank().fill(new FluidStack(BlockLoader.MAPLE_SYRUP_FLUID, 500), true);
                else return;
            } catch (Exception e) {
                DynamicTrees_Sakura.LOGGER.debug("DTS+randomTick" + mapleCauldronState.toString());
            }
            //sakura-maplelog
            if (worldIn.rand.nextInt(5000) == 0 && logState.getBlock() instanceof BlockMapleSapLog) {
                try {
                    if(logState.getValue(BlockMapleSapLog.SAP_AGE) < 5)
                    worldIn.setBlockState(blockpos, logState.withProperty(BlockMapleSapLog.SAP_AGE, logState.getValue(BlockMapleSapLog.SAP_AGE).intValue() + 1));
                } catch (Exception e0) {
                }
            }
            //dtsakura-maplelog
            if (worldIn.rand.nextInt(3000) == 0 && logState.getBlock() instanceof BlockBranch) {
                rotBranch(logState, worldIn, blockpos.up(), EnumFacing.UP);
                rotBranch(logState, worldIn, blockpos, EnumFacing.UP);
                rotBranch(logState, worldIn, blockpos.down(), EnumFacing.UP);
            }

        }
    }

    private boolean rotBranch(IBlockState logState, World worldIn, BlockPos blockpos, EnumFacing value) {
        try {
            if (((BlockBranch) logState.getBlock()).getRadius(logState) == 1 && logState.getBlock() instanceof BlockBranch) {
                {
//                            ((BlockBranch) logState.getBlock()).destroyBranchFromNode(worldIn, blockpos, state.getValue(FACING), true);
                    BranchDestructionData destroyData = ((BlockBranch) logState.getBlock()).destroyBranchFromNode(worldIn, blockpos, value, false);
                    if (destroyData.getNumBranches() != 0)
                        EntityFallingTree.dropTree(worldIn, destroyData, new ArrayList<ItemStack>(0), EntityFallingTree.DestroyType.HARVEST);
                    return true;
                }
            } else if (logState.getBlock() instanceof BlockBranch) {
                ((BlockBranch) logState.getBlock()).setRadius(worldIn, blockpos, ((BlockBranch) logState.getBlock()).getRadius(logState) - 1, value);
            }
        } catch (Exception exception2) {
//            DynamicTrees_Sakura.LOGGER.debug("DTS+randomTick" + ((BlockBranch) logState.getBlock()).getRadius(logState));
//            exception2.printStackTrace();
        }
        return false;
    }

    public static boolean canWork(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        BlockPos blockpos = pos.offset(facing.getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if ((block instanceof BlockMapleSapLog && iblockstate.getValue(BlockMapleSapLog.SAP_AGE).intValue() < 5))
            return true;
        if (block == Block.getBlockFromName(info.modid + ":maplebranch")) return true;
        return false;
    }

    //Just Render
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (canWork(worldIn, pos, stateIn)) {
            if (rand.nextInt(10) == 0) {
                double d0 = pos.getX() + 0.5D;
                double d1 = pos.getY() - 0.15D;
                double d2 = pos.getZ() + 0.5D;
                double d3 = 0D;
                double d4 = ((rand.nextFloat()) * 0.055D) + 0.015D;
                double d5 = 0D;
                SakuraMain.proxy.spawnParticle(SakuraParticleType.SYRUPDROP, d0, d1, d2, d3, -d4, d5);
            }
        }
    }


}
