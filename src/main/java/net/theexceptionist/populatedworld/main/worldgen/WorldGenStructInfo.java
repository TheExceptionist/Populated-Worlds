package net.theexceptionist.populatedworld.main.worldgen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.theexceptionist.populatedworld.main.Reference;

public class WorldGenStructInfo {
	public static final int VILLAGER_PRIEST_ID = 0;
	public static final int VILLAGER_ID = 1;
	public static final int IRON_GOLEM_ID = 2;
	public static final int VILLAGER_SOLDIER_ID = 3;

	public static final ResourceLocation[] CHAPEL_RESOURCE = {
			new ResourceLocation(Reference.MODID+":lone_chapel")
	};
	
	public static final int CHAPEL_ID = 0; 
	public static final int[] CHAPEL_MAX_SPAWNS = 
		{
				3,
				3,
				2,
				3
		};
	
	public static final ResourceLocation[] HAMLET_RESOURCE = new ResourceLocation[]{
		  new ResourceLocation(Reference.MODID+":hamlet_bighouse"),
		  new ResourceLocation(Reference.MODID+":hamlet_church"),
		  new ResourceLocation(Reference.MODID+":hamlet_farmhouse"),
		  new ResourceLocation(Reference.MODID+":hamlet_forge"),
		  new ResourceLocation(Reference.MODID+":hamlet_fort"),
		  new ResourceLocation(Reference.MODID+":hamlet_library")
	};
	
	public static final int HAMLET_RESOURCE_BIG_HOUSE_ID = 0; 
	public static final int HAMLET_RESOURCE_CHURCH_ID = 1; 
	public static final int HAMLET_RESOURCE_FARMHOUSE_ID = 2; 
	public static final int HAMLET_RESOURCE_FORGE_ID = 3; 
	public static final int HAMLET_RESOURCE_FORT_ID = 4; 
	public static final int HAMLET_RESOURCE_LIBRARY_ID = 5; 
	
	/*
	 * HAMLET_RESOURCE_BIG_HOUSE
	 * HAMLET_RESOURCE_CHURCH
	 * HAMLET_RESOURCE_FARMHOUSE
	 * HAMLET_RESOURCE_FORGE
	 * HAMLET_RESOURCE_FORT
	 * HAMLET_RESOURCE_LIBRARY
	 */
	
	public static final int HAMLET_ID = 1; 
	public static final int[] HAMLET_MAX_SPAWNS = 
		{
				3,
				3,
				2,
				3
		};
	
	public static final String[] ENTITIES_NAMES = 
		{
				"villager_priest",
				"villager",
				"iron_golem",
				"villager_soldier"
		}; 
	
	
}
