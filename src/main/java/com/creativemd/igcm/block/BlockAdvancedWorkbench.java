package com.creativemd.igcm.block;

import java.util.ArrayList;

import com.creativemd.creativecore.gui.container.SubContainer;
import com.creativemd.creativecore.gui.container.SubGui;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.creativecore.gui.opener.IGuiCreator;

import jline.internal.Nullable;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAdvancedWorkbench extends BlockWorkbench implements IGuiCreator{
	
	public static final int gridSize = 6;
	public static final int outputs = 4;
	
	public static ArrayList<AdvancedGridRecipe> recipes = new ArrayList<AdvancedGridRecipe>();
	
	public BlockAdvancedWorkbench() {
		super();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!world.isRemote)
			GuiHandler.openGui(player, world, pos);
		return true;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public SubGui getGui(EntityPlayer player, ItemStack stack, World world, BlockPos pos, IBlockState state) {
		return new SubGuiAdvancedWorkbench();
	}

	@Override
	public SubContainer getContainer(EntityPlayer player, ItemStack stack, World world, BlockPos pos,
			IBlockState state) {
		return new SubContainerAdvancedWorkbench(player);
	}
	
}
