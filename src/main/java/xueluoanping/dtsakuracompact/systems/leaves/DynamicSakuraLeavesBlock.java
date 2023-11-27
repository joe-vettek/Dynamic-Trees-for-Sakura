package xueluoanping.dtsakuracompact.systems.leaves;


import cn.mcmod.sakura.client.particle.ParticleRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import java.util.Random;

public class DynamicSakuraLeavesBlock extends DynamicLeavesBlock {


    public DynamicSakuraLeavesBlock(LeavesProperties leavesProperties, Properties properties) {
        super(leavesProperties, properties);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(40) == 0) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;

            double d0 = pos.getX() + 0.5D + 0.25D * j;
            double d1 = pos.getY() - 0.15D;
            double d2 = pos.getZ() + 0.5D + 0.25D * k;
            double d3 = rand.nextFloat() * j * 0.1D;
            double d4 = (rand.nextFloat() * 0.055D) + 0.015D;
            double d5 = rand.nextFloat() * k * 0.1D;
            ParticleOptions leaf_particle=null;

            if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:sakura_leaves"))
                leaf_particle= ParticleRegistry.SAKURA_LEAF.get();
            if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:maplegreen_leaves"))
                leaf_particle= ParticleRegistry.GREEN_MAPLE_LEAF.get();
            if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:mapleyellow_leaves"))
                leaf_particle= ParticleRegistry.YELLOW_MAPLE_LEAF.get();
            if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:mapleorange_leaves"))
                leaf_particle= ParticleRegistry.ORANGE_MAPLE_LEAF.get();
            if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:maplered_leaves"))
                leaf_particle= ParticleRegistry.RED_MAPLE_LEAF.get();
            if(leaf_particle!=null)
                worldIn.addParticle(leaf_particle, d0, d1, d2, d3, -d4, d5);
        }
    }
}

