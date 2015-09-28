package com.creativemd.ingameconfigmanager.mod.block;

import java.util.ArrayList;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.IGuiCreator;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.recipe.Recipe;
import com.creativemd.creativecore.core.CreativeCore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockAdvancedWorkbench extends Block implements IGuiCreator{
	
	public static final int gridSize = 6;
	public static final int outputs = 4;
	
	public static ArrayList<AdvancedGridRecipe> recipes = new ArrayList<AdvancedGridRecipe>();
	
	public BlockAdvancedWorkbench() {
		super(Material.wood);
		setStepSound(soundTypeWood);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.crafting_table.getIcon(side, meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registry)
    {
		
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
		if(!world.isRemote)
			((EntityPlayerMP)player).openGui(CreativeCore.instance, 0, world, x, y, z);
		return true;
    }

	@Override
	public SubGui getGui(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) {
		return new SubGuiAdvancedWorkbench();
	}

	@Override
	public SubContainer getContainer(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) {
		return new SubContainerAdvancedWorkbench(player);
	}
	
}
