package xueluoanping.dtsakuracompact.proxy;


import cn.mcmod.sakura.SakuraConfig;
import cn.mcmod.sakura.client.SakuraParticleType;
import com.ferreusveritas.dynamictrees.ModConstants;
import com.ferreusveritas.dynamictrees.ModTrees;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.blocks.BlockFruit;
import com.ferreusveritas.dynamictrees.growthlogic.NullLogic;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import xueluoanping.dtsakuracompact.DynamicTrees_Sakura;
import xueluoanping.dtsakuracompact.ModContent;
import xueluoanping.dtsakuracompact.growthlogic.SakuraLogic;
import xueluoanping.dtsakuracompact.growthlogic.UmeLogic;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.trees.TreeUme;

public class CommonProxy {
    public static BlockFruit plum;

    public void preInit() {
        DynamicTrees_Sakura.LOGGER.debug("Hello Sakura!");
        plum = new BlockFruit();//Apple
        TreeRegistry.registerGrowthLogicKit(new ResourceLocation(info.modid, "sakura"), new SakuraLogic());
        TreeRegistry.registerGrowthLogicKit(new ResourceLocation(info.modid, "ume"), new UmeLogic());
    }

    public void init() {

    }

    public void postInit() {
        SakuraConfig.ume_weight = 0;
    }

    public static void spawnParticle(double x, double y, double z, double velX, double velY, double velZ) {
    }

}
