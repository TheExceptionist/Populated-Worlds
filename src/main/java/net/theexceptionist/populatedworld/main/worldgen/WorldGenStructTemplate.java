package net.theexceptionist.populatedworld.main.worldgen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.theexceptionist.populatedworld.main.Reference;

public class WorldGenStructTemplate {
	/*public final List<Biome> CHAPEL_SPAWN_BIOMES = Arrays.<Biome>asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.FOREST);
	public final List<String> CHAPEL_SPAWN_ENTITIES = Arrays.<String>asList(ENTITIES_NAMES[VILLAGER_PRIEST_ID], 
			ENTITIES_NAMES[VILLAGER_ID], ENTITIES_NAMES[IRON_GOLEM_ID], ENTITIES_NAMES[VILLAGER_SOLDIER_ID]);
	public final String CHAPEL_NAME = "Lone Chapel";  
	public final int CHAPEL_MAX_ENTITIES = 7;  
	public final int[] CHAPEL_MAX_SPAWNS = 
		{
				3,
				3,
				2,
				3
		};*/
	private final List<Biome> spawnBiomes;
	private final List<String> spawnEntities;//CHAPEL_SPAWN_ENTITIES = Arrays.<String>asList(WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_PRIEST_ID], 
			//WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.IRON_GOLEM_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_SOLDIER_ID]);
	private final String name;  
	private final int[] maxSpawns;
	private int spawnAttempts = 7;  
	private int maxStructCount;
	
	public WorldGenStructTemplate(final String name, final List<Biome> spawnBiomes, List<String> spawnEntities, int[] maxSpawns, final int count)
	{
		this.name = name;
		this.spawnBiomes = spawnBiomes;
		this.spawnEntities = spawnEntities;
		this.maxSpawns = maxSpawns;
		this.maxStructCount = count;
	}

	public int getSpawnAttempts() {
		return spawnAttempts;
	}

	public void setSpawnAttempts(int spawnAttempts) {
		this.spawnAttempts = spawnAttempts;
	}

	public List<Biome> getSpawnBiomes() {
		return spawnBiomes;
	}

	public List<String> getSpawnEntities() {
		return spawnEntities;
	}

	public String getName() {
		return name;
	}

	public int[] getMaxSpawns() {
		return maxSpawns;
	}

	public int getMaxStructCount() {
		return maxStructCount;
	}

	public void setMaxStructCount(int maxStructCount) {
		this.maxStructCount = maxStructCount;
	}
	
}
