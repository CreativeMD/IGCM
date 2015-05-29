package com.creativemd.ingameconfigmanager.api.client.representative;

/**
 * Copyright 2015 CreativeMD & N247S
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

import com.creativemd.creativecore.client.rendering.RenderHelper2D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class RepresentativeItemStack extends RepresentativeObject{
	
	public ItemStack stack;
	
	public RepresentativeItemStack(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public void handleRendering(Minecraft mc, FontRenderer fontRenderer, int width, int height) {
		RenderHelper2D.renderItem(stack, width/2-16/2, height/2-16/2);
	}
}
