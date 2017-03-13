package com.creativemd.igcm.event;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.gui.opener.GuiHandler;
import com.creativemd.igcm.IGCM;
import com.creativemd.igcm.packets.CraftResultPacket;
import com.creativemd.igcm.utils.WorkbenchSwitchHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConfigEventHandler
{
	
	@SubscribeEvent
	public void onPlayerJoin(PlayerLoggedInEvent event)
	{
		if(!event.player.getEntityWorld().isRemote)
		{
			IGCM.sendAllUpdatePackets(event.player);
			IGCM.logger.info("Send player update packet to " + event.player.getName() + "!");
		}
	}
	
	@SubscribeEvent
	public void onInteract(RightClickBlock event)
	{
		if(IGCM.overrideWorkbench)
		{
			Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
			if(block instanceof BlockWorkbench)
			{
				event.setCanceled(true);
				if(event.getWorld().isRemote)
				{
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setInteger("gui", 2);
					nbt.setInteger("index", 0);
					GuiHandler.openGui(IGCM.guiID, nbt, event.getEntityPlayer());
				}
			}
		}
	}
	
	public static boolean clicked = false;
	
	public static int index = 0;
	
	public static ItemStack[] craftMatrix = new ItemStack[9];
	
	public static ArrayList<ItemStack> recipes = new ArrayList<ItemStack>();
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tick(RenderTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.phase == Phase.END && mc.player != null && mc.player.openContainer instanceof ContainerWorkbench)
		{
			ContainerWorkbench container = (ContainerWorkbench) mc.player.openContainer;
			boolean changed = false;
			for (int i = 0; i < craftMatrix.length; i++) {
				if(craftMatrix[i] == null || !ItemStack.areItemStacksEqual(container.craftMatrix.getStackInSlot(i), craftMatrix[i]))
				{
					changed = true;
					craftMatrix[i] = container.craftMatrix.getStackInSlot(i).copy();
				}
			}
			if(changed)
			{
				index = 0;
				recipes.clear();
				recipes.addAll(WorkbenchSwitchHelper.findMatchingRecipe(container.craftMatrix, mc.world));
				for (int i = 0; i < recipes.size(); i++) {
					if(recipes.get(i).isEmpty())
						recipes.get(i).setCount(1);;
				}
			}
			if(recipes.size() > 0)
			{
				if(recipes.size() > 1)
				{
					GuiButton button = new GuiButton(0, mc.currentScreen.width/2+20, mc.currentScreen.height/2-25, 50, 20, "Switch");
					GL11.glPushMatrix();
					GL11.glDisable(GL11.GL_LIGHTING);
					renderButton(mc.currentScreen, button, false, 0, 0);
					ScaledResolution scaledresolution = new ScaledResolution(mc);
			        int i = scaledresolution.getScaledWidth();
			        int j = scaledresolution.getScaledHeight();
					mc.fontRendererObj.drawString((index+1) + " of " + recipes.size(), i/2+30, j/2-65, 0);
					GL11.glPopMatrix();
					if(!ItemStack.areItemStacksEqual(container.craftResult.getStackInSlot(0), recipes.get(index)))
					{
						//container.craftResult.setInventorySlotContents(0, recipes.get(index).copy());
						PacketHandler.sendPacketToServer(new CraftResultPacket(index, recipes.get(index)));
					}
					if(Mouse.getEventButtonState() && Mouse.getEventButton() == 0)
					{
						if(!clicked)
						{
							//Mouse clicked
							int x = Mouse.getEventX() * i / mc.displayWidth;
					        int y = j - Mouse.getEventY() * j / mc.displayHeight - 1;
							clicked = true;
							if(button.mousePressed(mc, x, y))
							{
								int newindex = index+1;
								if(newindex >= recipes.size())
									newindex = 0;
								PacketHandler.sendPacketToServer(new CraftResultPacket(newindex, recipes.get(newindex)));
							}
						}
					}else
						clicked = false;
				}
			}else
				container.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderButton(GuiScreen screen, GuiButton button, boolean front, int xOffzet, int yOffzet)
	{
		Minecraft mc = Minecraft.getMinecraft();
		int k = 0;//(screen.width - 176) / 2;
		int l = 0;//(screen.height - 166) / 2;
		button.xPosition += xOffzet;
		button.yPosition += yOffzet;
		int oldX = button.xPosition;
		int oldY = button.yPosition;
		button.xPosition = oldX-k;
		button.yPosition = oldY-l;
		ScaledResolution scaledresolution = new ScaledResolution(mc);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        int k2 = Mouse.getX() * i / mc.displayWidth;
        int l2 = j - Mouse.getY() * j / mc.displayHeight - 1;
        if(front)
        	GL11.glTranslated(0, 0, 500);
        button.drawButton(mc, k2-k, l2-l);
        if(front)
        	GL11.glTranslated(0, 0, -500);
		
		button.xPosition = oldX;
		button.yPosition = oldY;
		button.xPosition -= xOffzet;
		button.yPosition -= yOffzet;
	}
}