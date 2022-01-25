package xueluoanping.dtsakuracompact.block;

import cn.mcmod.sakura.SakuraMain;
import cn.mcmod.sakura.client.SakuraParticleType;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.BlockDynamicLeaves;
import com.ferreusveritas.dynamictrees.blocks.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.LeavesPropertiesJson;
import com.ferreusveritas.dynamictrees.blocks.LeavesStateMapper;
import com.ferreusveritas.dynamictrees.util.JsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//Changed the randomTickDisplay
public class LeavesPaging extends com.ferreusveritas.dynamictrees.blocks.LeavesPaging {

	///////////////////////////////////////////
	//BLOCK PAGING
	///////////////////////////////////////////

	private static HashMap<String, HashMap<Integer, BlockDynamicLeaves>> modLeavesArray = new HashMap<>();
	private static HashMap<String, Integer> modLastSeq = new HashMap<>();

	private static String autoModId(@Nullable String modid) {
		if (modid == null || "".equals(modid)) {
			ModContainer mc = Loader.instance().activeModContainer();
			modid = mc == null ? ModConstants.MODID : mc.getModId();
		}
		return modid;
	}
	public static BlockDynamicLeaves getLeavesBlockForSequence(@Nullable String modid, int seq, @Nonnull ILeavesProperties leavesProperties) {
		BlockDynamicLeaves leaves = getLeavesBlockForSequence(modid, seq);
		int tree = seq & 3;
		leavesProperties.setDynamicLeavesState(leaves.getDefaultState().withProperty(BlockDynamicLeaves.TREE, tree));
		leaves.setProperties(tree, leavesProperties);
		return leaves;
	}
	/**
	 * A convenience function for packing 4 {@link BlockDynamicLeaves} blocks into one Minecraft block using metadata.
	 * Changed the randomTickDisplay
	 *
	 * @param modid
	 * @param seq
	 * @return
	 */
	private static BlockDynamicLeaves getLeavesBlockForSequence(@Nullable String modid, int seq) {
		int key = seq / 4;
		String regname = "leaves" + key;

		return getLeavesMapForModId(modid).computeIfAbsent(key, k -> (BlockDynamicLeaves) new BlockDynamicLeaves(){
			@Override
			public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
				if(rand.nextInt(40)<1){int j = rand.nextInt(2) * 2 - 1;
					int k = rand.nextInt(2) * 2 - 1;

					double d0 = pos.getX() + 0.5D + 0.25D * j;
					double d1 = pos.getY() - 0.15D;
					double d2 = pos.getZ() + 0.5D + 0.25D * k;
					double d3 = rand.nextFloat() * j * 0.1D;
					double d4 = ((rand.nextFloat()) * 0.055D) + 0.015D;
					double d5 = rand.nextFloat() * k * 0.1D;
					if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves0"))
						SakuraMain.proxy.spawnParticle(SakuraParticleType.LEAVESSAKURA, d0, d1, d2, d3, -d4, d5);
					if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
							&& stateIn.getBlock().getMetaFromState(stateIn) / 4 == 0)
						SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEGREEN, d0, d1, d2, d3, -d4, d5);
					if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
							&& stateIn.getBlock().getMetaFromState(stateIn) / 4 == 1)
						SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEORANGE, d0, d1, d2, d3, -d4, d5);
					if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
							&& stateIn.getBlock().getMetaFromState(stateIn) / 4 == 2)
						SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLERED, d0, d1, d2, d3, -d4, d5);
					if (stateIn.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
							&& stateIn.getBlock().getMetaFromState(stateIn) / 4 == 3)
						SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEYELLOW, d0, d1, d2, d3, -d4, d5);}
				super.randomDisplayTick(stateIn, worldIn, pos, rand);
			}
		}.setDefaultNaming(autoModId(modid), regname));
	}



}
