package xueluoanping.dtsakuracompact.proxy;

import cn.mcmod.sakura.client.SakuraParticleType;
import cn.mcmod.sakura.client.particle.*;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesPaging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import xueluoanping.dtsakuracompact.DynamicTrees_Sakura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xueluoanping.dtsakuracompact.pro.info;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        registerColorHandlers();

    }

    @Override
    public void postInit() {
        super.postInit();
    }

    public void registerColorHandlers() {
        for (BlockDynamicLeaves leaves : LeavesPaging.getLeavesMapForModId(info.modid).values()) {
            ModelHelper.regColorHandler(leaves, new IBlockColor() {
                @Override
                public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
                    //boolean inWorld = worldIn != null && pos != null;

                    Block block = state.getBlock();

                    if (TreeHelper.isLeaves(block)) {
                        return ((BlockDynamicLeaves) block).getProperties(state).foliageColorMultiplier(state, worldIn, pos);
                    }
                    return 0x00FF00FF; //Magenta
                }
            });
        }
    }


//    @Override
    public static void spawnParticle(World worldIn,double x, double y, double z, double velX, double velY, double velZ) {
        Minecraft mc = Minecraft.getMinecraft();


        if (mc.effectRenderer != null) {
            int i = mc.gameSettings.particleSetting;
            if (i == 1 && worldIn.rand.nextInt(3) == 0) i = 2;
            Particle particle = null;

            particle = new ParticleMapleRedLeaf(worldIn, x, y, z, velX, velY, velZ);
//			if (particle != null) {
//				mc.effectRenderer.addEffect(particle);
//			}

        }
    }
}
