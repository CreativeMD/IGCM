package com.creativemd.igcm.api.segments.advanced;

import com.creativemd.igcm.api.machine.RecipeMachine;
import com.creativemd.igcm.api.segments.ValueSegment;

public abstract class RecipeSegment<T> extends ValueSegment<T> {
	
	public RecipeMachine machine;
	public Object recipe;
	
	public RecipeSegment(String id, T defaultValue, RecipeMachine machine, Object recipe) {
		super(id, defaultValue);
		this.machine = machine;
		this.recipe = recipe;
		addSubSegments();
	}
	
	public abstract void addSubSegments();
	
	@Override
	public void onSegmentLoaded(int x, int y, int maxWidth) {
		super.onSegmentLoaded(x, y, maxWidth);
		machine.onControlCreated(!(value instanceof Boolean) ? value : recipe, this instanceof AddRecipeSegment, x, y, maxWidth, getGuiControls(), getContainerControls());
	}
	
}
