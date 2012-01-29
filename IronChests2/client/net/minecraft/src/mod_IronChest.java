package net.minecraft.src;

import java.io.File;

import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemIronChest;
import cpw.mods.ironchest.TileEntityIronChest;
import cpw.mods.ironchest.client.GUIChest;
import cpw.mods.ironchest.client.IronChestRenderHelper;
import cpw.mods.ironchest.client.TileEntityIronChestRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForgeClient;

public class mod_IronChest extends BaseModMp {

	public static BlockIronChest ironChestBlock;
	public static boolean compatibilityMode;

	@Override
	public String getVersion() {
		return "2.0";
	}

	@Override
	public void load() {
		File cfgFile = new File(Minecraft.getMinecraftDir(), "config/IronChest.cfg");
		// If our config file exists
		boolean defaultCompatibility = cfgFile.exists();
		Configuration cfg = new Configuration(cfgFile);
		try {
			cfg.load();
			// But doesn't have the compatibilityMode flag, enable compatibility
			// mode
			compatibilityMode = Boolean.parseBoolean(cfg.getOrCreateBooleanProperty("compatibilityMode", Configuration.GENERAL_PROPERTY,
					defaultCompatibility).value);
			ironChestBlock = new BlockIronChest(Integer.parseInt(cfg.getOrCreateBlockIdProperty("blockVeryLargeChest", 181).value));
			IronChestType.initGUIs(cfg);
		} catch (Exception e) {
			ModLoader.getLogger().severe("IronChest was unable to load it's configuration successfully");
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		} finally {
			cfg.save();
		}

		ModLoader.RegisterBlock(ironChestBlock, ItemIronChest.class);
		IronChestType.registerTranslations();
        IronChestType.registerTileEntities(TileEntityIronChestRenderer.class);
        IronChestType.registerRecipes(ironChestBlock);

        ChestItemRenderHelper.instance=new IronChestRenderHelper();
        MinecraftForgeClient.preloadTexture("ic2/sprites/ironchest_block_tex.png");
	}

	public static void openGUI(EntityPlayer player, TileEntityIronChest te) {
		GUIChest.GUI.showGUI(te, player);
	}

}