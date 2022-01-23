package xueluoanping.dtsakuracompact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.function.BinaryOperator;

import cn.mcmod.sakura.SakuraConfig;
import cn.mcmod.sakura.SakuraMain;
import cn.mcmod.sakura.block.BlockLoader;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveGreen;
import cn.mcmod.sakura.block.tree.BlockMapleLeaveRed;
import cn.mcmod.sakura.block.tree.BlockSakuraLeave;
import cn.mcmod.sakura.client.SakuraParticleType;
import cn.mcmod.sakura.client.TileEntityRenderHelper;
import cn.mcmod.sakura.client.particle.ParticleMapleRedLeaf;
import cn.mcmod.sakura.client.render.tileentity.*;
import cn.mcmod.sakura.tileentity.*;
import cn.mcmod_mmf.mmlib.register.BlockRegister;
import com.ferreusveritas.dynamictrees.ModItems;
import com.ferreusveritas.dynamictrees.ModRecipes;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.WorldGenRegistry.BiomeDataBasePopulatorRegistryEvent;
import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.*;
import com.ferreusveritas.dynamictrees.items.DendroPotion.DendroPotionType;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.trees.TreeFamily;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegistryManager;
import xueluoanping.dtsakuracompact.block.BlockMapleSpile_extened;
import xueluoanping.dtsakuracompact.pro.info;
import xueluoanping.dtsakuracompact.proxy.ClientProxy;
import xueluoanping.dtsakuracompact.proxy.CommonProxy;

import xueluoanping.dtsakuracompact.trees.TreeMaple;
import xueluoanping.dtsakuracompact.trees.TreeSakura;
import xueluoanping.dtsakuracompact.trees.TreeUme;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleGreen;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleOrange;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleRed;
import xueluoanping.dtsakuracompact.trees.species.SpeciesMapleYellow;
import xueluoanping.dtsakuracompact.worldgen.BiomeDataBasePopulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import static com.ferreusveritas.dynamictrees.ModTabs.dynamicTreesTab;

@Mod.EventBusSubscriber(modid = info.modid)
@ObjectHolder(info.modid)
public class ModContent {

    public static ArrayList<TreeFamily> trees = new ArrayList<TreeFamily>();
    // extra species added by this mod
    public static ArrayList<Species> vanillaSpecies = new ArrayList<>();
    public static Species mapleGreen;
    public static Species mapleOrange;
    public static Species mapleYellow;
    public static ILeavesProperties sakuraLeavesProperties;
    public static ILeavesProperties maplegreenLeavesProperties;
    public static ILeavesProperties mapleorangeLeavesProperties;
    public static ILeavesProperties mapleredLeavesProperties;
    public static ILeavesProperties mapleyellowLeavesProperties;
    public static ILeavesProperties umeLeavesProperties;
    public static boolean failedToLoad = false;
    static boolean messageSent = false;

    public static Block mapleSpile_extened = new BlockMapleSpile_extened();
    public static ItemBlock mapleSpileItem;

    @SubscribeEvent
    public static void onEvent(EntityJoinWorldEvent event) {
        if (!messageSent && (event.getEntity() instanceof EntityPlayer)) {
            if (SakuraConfig.ume_weight != 0) {
                event.getEntity().sendMessage(new TextComponentString("Dynamic Trees for Sakura: To prevent non-dynamic trees from spawning please disable Sakura's ume_weight in Sakura world settings."));
                messageSent = true;
            }
        }

    }

    @SubscribeEvent
    public static void registerDataBasePopulators(final BiomeDataBasePopulatorRegistryEvent event) {
        event.register(new BiomeDataBasePopulator());
    }


    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        try {

            sakuraLeavesProperties = setUpLeaves(TreeSakura.leavesBlock, TreeSakura.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);
            maplegreenLeavesProperties = setUpLeaves(SpeciesMapleGreen.leavesBlock, SpeciesMapleGreen.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);
            mapleorangeLeavesProperties = setUpLeaves(SpeciesMapleOrange.leavesBlock, SpeciesMapleOrange.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);
            mapleredLeavesProperties = setUpLeaves(SpeciesMapleRed.leavesBlock, SpeciesMapleRed.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);
            mapleyellowLeavesProperties = setUpLeaves(SpeciesMapleYellow.leavesBlock, SpeciesMapleYellow.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);
            umeLeavesProperties = setUpLeaves(TreeUme.leavesBlock, TreeUme.leavesBlock.getDefaultState(), 0, "deciduous", 3, 13);

            LeavesPaging.getLeavesBlockForSequence(info.modid, 0, sakuraLeavesProperties);
            LeavesPaging.getLeavesBlockForSequence(info.modid, 4, maplegreenLeavesProperties);
            LeavesPaging.getLeavesBlockForSequence(info.modid, 5, mapleorangeLeavesProperties);
            LeavesPaging.getLeavesBlockForSequence(info.modid, 6, mapleredLeavesProperties);
            LeavesPaging.getLeavesBlockForSequence(info.modid, 7, mapleyellowLeavesProperties);
            LeavesPaging.getLeavesBlockForSequence(info.modid, 8, umeLeavesProperties);
            TreeFamily sakuraTree = new TreeSakura();
            TreeFamily mapleTree = new TreeMaple();
            TreeFamily umeTree = new TreeUme();
            mapleGreen = new SpeciesMapleGreen(mapleTree);
            vanillaSpecies.add(mapleGreen);
            mapleOrange = new SpeciesMapleOrange(mapleTree);
            vanillaSpecies.add(mapleOrange);
            mapleYellow = new SpeciesMapleYellow(mapleTree);
            vanillaSpecies.add(mapleYellow);
//            Species.REGISTRY.registerAll(vanillaSpecies.toArray(new Species[0]));
            Collections.addAll(trees, new TreeSakura(), mapleTree, new TreeUme());

        } catch (Exception e) {
            failedToLoad = true;

        }
        ///Collections.addAll(trees);


        trees.forEach(tree -> tree.registerSpecies(Species.REGISTRY));
        ArrayList<Block> treeBlocks = new ArrayList<>();
        trees.forEach(tree -> tree.getRegisterableBlocks(treeBlocks));
        treeBlocks.addAll(LeavesPaging.getLeavesMapForModId(info.modid).values());
        registry.registerAll(treeBlocks.toArray(new Block[treeBlocks.size()]));
        registry.register(CommonProxy.plum);
        registry.register(mapleSpile_extened.setRegistryName(info.modid, "maple_spile").setCreativeTab(dynamicTreesTab).setUnlocalizedName("dtsakuracompact.maple_spile"));
//        DynamicTrees_Sakura.LOGGER.debug(mapleSpile_extened.getUnlocalizedName());
        initTileEntity();

    }

    public static ILeavesProperties setUpLeaves(Block leavesBlock, IBlockState leavesState, int leavesMeta, String cellKit, int smother, int light) {
        ILeavesProperties leavesProperties;
        leavesProperties = new LeavesProperties(
                leavesState,
                new ItemStack(leavesBlock, 1, leavesMeta),
                TreeRegistry.findCellKit(cellKit)) {
            @Override
            public boolean updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

                int j = rand.nextInt(2) * 2 - 1;
                int k = rand.nextInt(2) * 2 - 1;

                double d0 = pos.getX() + 0.5D + 0.25D * j;
                double d1 = pos.getY() - 0.15D;
                double d2 = pos.getZ() + 0.5D + 0.25D * k;
                double d3 = rand.nextFloat() * j * 0.1D;
                double d4 = ((rand.nextFloat()) * 0.055D) + 0.015D;
                double d5 = rand.nextFloat() * k * 0.1D;
                if (state.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves0"))
                    SakuraMain.proxy.spawnParticle(SakuraParticleType.LEAVESSAKURA, d0, d1, d2, d3, -d4, d5);
                if (state.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
                        && state.getBlock().getMetaFromState(state) / 4 == 0)
                    SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEGREEN, d0, d1, d2, d3, -d4, d5);
                if (state.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
                        && state.getBlock().getMetaFromState(state) / 4 == 1)
                    SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEORANGE, d0, d1, d2, d3, -d4, d5);
                if (state.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
                        && state.getBlock().getMetaFromState(state) / 4 == 2)
                    SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLERED, d0, d1, d2, d3, -d4, d5);
                if (state.getBlock().getRegistryName().toString().equals("dtsakuracompact:leaves1")
                        && state.getBlock().getMetaFromState(state) / 4 == 3)
                    SakuraMain.proxy.spawnParticle(SakuraParticleType.MAPLEYELLOW, d0, d1, d2, d3, -d4, d5);
//                DynamicTrees_Sakura.LOGGER.debug("helloDTS" + rand.nextInt(2)+pos.toString()+state.getBlock().getRegistryName().toString()+state.toString());
                return super.updateTick(worldIn, pos, state, rand);
            }

            @Override
            public int getSmotherLeavesMax() {
                return smother;
            } //Default: 4

            @Override
            public int getLightRequirement() {
                return light;
            } //Default: 13

            @Override
            public ItemStack getPrimitiveLeavesItemStack() {
                return new ItemStack(leavesBlock, 1, leavesMeta);
            }

        };
        return leavesProperties;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        ArrayList<Item> treeItems = new ArrayList<>();
        trees.forEach(tree -> tree.getRegisterableItems(treeItems));
        registry.registerAll(treeItems.toArray(new Item[treeItems.size()]));
        mapleSpileItem = new ItemBlock(mapleSpile_extened);
        registry.register(mapleSpileItem.setRegistryName(info.modid, "maple_spile"));

    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

        if (!failedToLoad) {
            setUpSeedRecipes("sakura", new ItemStack(TreeSakura.saplingBlock, 1, 0));
            setUpSeedRecipes("maplegreen", new ItemStack(SpeciesMapleGreen.saplingBlock, 1, 0));
            setUpSeedRecipes("mapleorange", new ItemStack(SpeciesMapleOrange.saplingBlock, 1, 0));
            setUpSeedRecipes("maplered", new ItemStack(SpeciesMapleRed.saplingBlock, 1, 0));
            setUpSeedRecipes("mapleyellow", new ItemStack(SpeciesMapleYellow.saplingBlock, 1, 0));
            setUpSeedRecipes("ume", new ItemStack(TreeUme.fruit, 1, 166));

            RegistryManager.ACTIVE.getRegistry(GameData.RECIPES)
                    .remove(new ResourceLocation(SakuraMain.MODID, "maple_spile"));
            GameRegistry.addShapelessRecipe(new ResourceLocation(info.modid, "spile_re"),
                    (ResourceLocation) null,new ItemStack(mapleSpileItem, 1),
                    Ingredient.fromItem(Item.getItemFromBlock(BlockLoader.MAPLE_SPILE)));
        }


    }

    public static void setUpSeedRecipes(String name, ItemStack treeSapling) {
        Species treeSpecies = TreeRegistry.findSpecies(new ResourceLocation(info.modid, name));

        ItemStack treeSeed = treeSpecies.getSeedStack(1);
//        DynamicTrees_Sakura.LOGGER.debug(name + "|||" + treeSeed.getUnlocalizedName());
        ItemStack treeTransformationPotion = ModItems.dendroPotion.setTargetTree(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSpecies.getFamily());
        BrewingRecipeRegistry.addRecipe(new ItemStack(ModItems.dendroPotion, 1, DendroPotionType.TRANSFORM.getIndex()), treeSeed, treeTransformationPotion);

        ModRecipes.createDirtBucketExchangeRecipes(treeSapling, treeSeed, true);

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (TreeFamily tree : trees) {
            ModelHelper.regModel(tree.getDynamicBranch());
            ModelHelper.regModel(tree.getCommonSpecies().getSeed());
            ModelHelper.regModel(tree);
            if (tree instanceof TreeMaple) {
                ModelHelper.regModel(((TreeMaple) tree).mapleGreen.getSeed());
                ModelHelper.regModel(((TreeMaple) tree).mapleOrange.getSeed());
                ModelHelper.regModel(((TreeMaple) tree).mapleYellow.getSeed());

            }
        }
        LeavesPaging.getLeavesMapForModId(info.modid).forEach((key, leaves) -> ModelLoader.setCustomStateMapper(leaves, new StateMap.Builder().ignore(BlockLeaves.DECAYABLE).build()));
        render();
        registerRender(mapleSpile_extened);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block) {
        BlockRegister.getInstance().registerRender(block);
    }


    public static void initTileEntity() {
//        registerTileEntity(TileEntityMapleCauldron_extended.class, "maple_cauldron_extended");
    }

    private static void registerTileEntity(Class<? extends TileEntity> cls, String baseName) {
        GameRegistry.registerTileEntity(cls, new ResourceLocation(info.modid, baseName));
    }

    @SideOnly(Side.CLIENT)
    public static void render() {
//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMapleCauldron.class, new RenderTileEntityMapleCauldron());
    }
}
