package xueluoanping.dtsakuracompact;

import cn.mcmod.sakura.SakuraMain;
import com.ferreusveritas.dynamictrees.ModConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid= info.modid, name= info.name, dependencies = info.dependencies, updateJSON = "")
public class DynamicTrees_Sakura {
	

	public static final Logger LOGGER = LogManager.getLogger(info.modid);
	@Mod.Instance
	public static DynamicTrees_Sakura instance;
	
	@SidedProxy(clientSide = "xueluoanping.dtsakuracompact.proxy.ClientProxy", serverSide = "xueluoanping.dtsakuracompact.proxy.CommonProxy") //com.
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		proxy.postInit();
	}
	
}
