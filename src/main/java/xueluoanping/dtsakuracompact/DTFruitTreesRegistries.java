package xueluoanping.dtsakuracompact;


import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xueluoanping.dtsakuracompact.systems.featuregen.SakuraFeatures;
import xueluoanping.dtsakuracompact.systems.leaves.SakuraLeavesProperties;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTFruitTreesRegistries {

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(new ResourceLocation(DynamicTrees_Sakura.MOD_ID, "sakura"), SakuraLeavesProperties.TYPE);
    }



    @SubscribeEvent
    public static void onGenFeatureRegistry(final RegistryEvent<GenFeature> event) {
        DynamicTrees_Sakura.LOGGER.debug("start register GenFeature");
        SakuraFeatures.register(event.getRegistry());
    }


}
