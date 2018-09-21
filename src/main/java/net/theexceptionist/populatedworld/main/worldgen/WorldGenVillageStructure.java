package net.theexceptionist.populatedworld.main.worldgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.theexceptionist.populatedworld.main.Reference;
import net.theexceptionist.populatedworld.main.entity.EntityVillagerSoldier;

public class WorldGenVillageStructure {
	public static final int VILLAGER_PRIEST_ID = 0;
	public static final int VILLAGER_ID = 1;
	public static final int IRON_GOLEM_ID = 2;
	public static final int VILLAGER_SOLDIER_ID = 3;
	
	public static final String[] ENTITIES_NAMES = 
		{
				"villager_priest",
				"villager",
				"iron_golem",
				"villager_soldier"
		};
	
	public static final ResourceLocation CHAPEL_RESOURCE = new ResourceLocation(Reference.MODID+":lone_chapel");
	public static final List<Biome> CHAPEL_SPAWN_BIOMES = Arrays.<Biome>asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.FOREST);
	public static final List<String> CHAPEL_SPAWN_ENTITIES = Arrays.<String>asList(ENTITIES_NAMES[VILLAGER_PRIEST_ID], 
			ENTITIES_NAMES[VILLAGER_ID], ENTITIES_NAMES[IRON_GOLEM_ID], ENTITIES_NAMES[VILLAGER_SOLDIER_ID]);
	public static final String CHAPEL_NAME = "Lone Chapel";  
	public static final int CHAPEL_MAX_ENTITIES = 7;  
	public static final int[] CHAPEL_MAX_SPAWNS = 
		{
				3,
				3,
				2,
				3
		};  
	
	private static final PlacementSettings settingsDefault = new PlacementSettings().setRotation(Rotation.NONE);
	
	protected String name;
	protected int chance;
	protected PlacementSettings settings;
	protected Template template; // = new BlockPos(chunkX * 16 + random.nextInt(16), 100, chunkZ * 16 + random.nextInt(16);
	protected ResourceLocation resourceLoc;
	protected List<Biome> validBiomes;
//	protected World world;
	protected float integrity;
	protected List<String> genEntities;
	protected int maxEntities;
	protected BlockPos size;
	protected int[] spawnLimits;
	
	public WorldGenVillageStructure(final String name, final int chance, final float integrity,
			final PlacementSettings settings, final ResourceLocation resourceLoc, final ArrayList<Biome> validBiomes, 
			final ArrayList<String> entitySpawns, final int maxEntities, final int[] limits)
	{
		this.name = name;
		this.chance = chance;
		this.settings = settings;
		settings.setIntegrity(integrity);
		
		this.resourceLoc = resourceLoc;
		this.validBiomes = validBiomes;
		this.genEntities = entitySpawns;
		this.maxEntities = maxEntities;
		this.spawnLimits = limits;
	}
	
	public WorldGenVillageStructure(final String name, final int chance, final float integrity)
	{
		this.name = name;
		this.chance = chance;
		
		if(this.name.compareTo(CHAPEL_NAME) == 0){

			this.resourceLoc = CHAPEL_RESOURCE;
			this.validBiomes = CHAPEL_SPAWN_BIOMES;		
			this.settings = settingsDefault;
			this.genEntities = CHAPEL_SPAWN_ENTITIES;
			this.maxEntities = CHAPEL_MAX_ENTITIES;
			this.spawnLimits = CHAPEL_MAX_SPAWNS;
		}
		
		settings.setIntegrity(integrity);
	}

	public void trySpawn(final BlockPos basePos, final World world, final Random random) {
		// TODO Auto-generated method stub
		if(this.template == null) this.template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), CHAPEL_RESOURCE);
		
		int r = random.nextInt(1000);
		boolean validBiome = false;
		Biome biomeIn = world.getBiome(basePos);
		
		IBlockState blockstate = Blocks.LOG.getDefaultState();
		IBlockState blockstate1 = Blocks.COBBLESTONE.getDefaultState();
		IBlockState blockstate2 = Blocks.PLANKS.getDefaultState();
		IBlockState blockstate3 = Blocks.OAK_STAIRS.getDefaultState();
		IBlockState blockstate4 = Blocks.STONE_STAIRS.getDefaultState();
		IBlockState blockstate5 = Blocks.GRASS.getDefaultState();
		IBlockState blockstate6 = Blocks.GRAVEL.getDefaultState();
		IBlockState blockstate7 = Blocks.STONEBRICK.getDefaultState();
		
		this.size = this.template.getSize();
		
		for(Biome b : validBiomes)
		{
			if(b == biomeIn)
			{
				validBiome = true;
			}
		}
		
		if(r < chance && validBiome)
		{
			//Main.logger.info("Size: X:"+this.template.getSize().getX()+" Y: "+this.template.getSize().getY()+" Z: "+this.template.getSize().getZ();
			this.fillWithBlocks(world, basePos, Blocks.AIR.getDefaultState());
			this.template.addBlocksToWorld(world, basePos, new WorldGenBiomeProcessor(biomeIn), settings, 2);
			this.generatePlatform(world, basePos, WorldGenBiomeProcessor.getBiomeSpecificBlockState(blockstate7, biomeIn));
            
			this.spawnEntities(world, basePos, random, random.nextInt(this.maxEntities), random.nextInt(100));
		}
	}

	private void generatePlatform(World world, BlockPos basePos, IBlockState defaultState) {
		// TODO Auto-generated method stub
		int x = basePos.getX();
		int y = basePos.getY() - 1;
		int z = basePos.getZ();
		
		for(int xx = x; xx < x + size.getX(); xx++)
		{
			for(int zz = z; zz < z + size.getZ(); zz++){
				this.replaceAirAndLiquidDownwards(world, defaultState, xx, y, zz);
			}
		}
	}

	private void replaceAirAndLiquidDownwards(World world, IBlockState defaultState, int x, int y, int z) {
		while ((world.isAirBlock(new BlockPos(x, y, z)) || world.getBlockState(new BlockPos(x, y, z)).getMaterial().isLiquid()) && y > 1)
        {
            world.setBlockState(new BlockPos(x, y, z), defaultState, 2);
            --y;
        }
	}

	private void fillWithBlocks(World world, BlockPos basePos, IBlockState defaultState) {
		int x = basePos.getX();
		int y = basePos.getY();
		int z = basePos.getZ();
		int targetX = x + size.getX();
		int targetY = y + size.getY();
		//Add one to make sure structure beginning is unblocked
		int targetZ = z + size.getZ() + 1;
		
		for(int xx = x; xx < targetX; xx++)
		{
			for(int yy = y; yy < targetY; yy++)
			{
				for(int zz = z; zz < targetZ; zz++)
				{
					world.setBlockState(new BlockPos(xx, yy, zz), defaultState);
				}
			}
		}
	}

	private void spawnEntities(final World world, final BlockPos spawnPos, final Random rand, final int count, final int chance) {
		int x = spawnPos.getX();
		int y = spawnPos.getY();
		int z = spawnPos.getZ() - 1;
		int[] r = new int[ENTITIES_NAMES.length];
		int[] spawns =
				{
						0,
						0,
						0,
						0
				};
		
		for(int i = 0; i < r.length; i++)
		{
			switch(i){
				case VILLAGER_PRIEST_ID:
					r[i] = rand.nextInt(100);
					break;
				case VILLAGER_ID:
					r[i] = rand.nextInt(100);
					break;
				case VILLAGER_SOLDIER_ID:
					r[i] = rand.nextInt(100);
					break;
				case IRON_GOLEM_ID:
					r[i] = rand.nextInt(1000);
					break;
			}
		}
		
		for(int i = 0; i < count; i++)
		{
//			String entityName = this.genEntities.get(rand.nextInt(this.genEntities.size()));
			
			if(r[VILLAGER_PRIEST_ID] < chance && spawns[VILLAGER_PRIEST_ID] < spawnLimits[VILLAGER_PRIEST_ID])
			{
				EntityVillager entityvillager = new EntityVillager(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                entityvillager.setProfession(2);
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);
                spawns[VILLAGER_PRIEST_ID]++;
			}
			if(r[VILLAGER_ID] < chance && spawns[VILLAGER_ID] < spawnLimits[VILLAGER_ID])
			{
				EntityVillager entityvillager = new EntityVillager(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                entityvillager.setProfession(rand.nextInt(5));
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);
                spawns[VILLAGER_ID]++;
			}
			if(r[IRON_GOLEM_ID] < chance && spawns[IRON_GOLEM_ID] < spawnLimits[IRON_GOLEM_ID])
			{
				EntityIronGolem entitygolem = new EntityIronGolem(world);
                entitygolem.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                world.spawnEntity(entitygolem);	
                spawns[IRON_GOLEM_ID]++;
			}
			if(r[VILLAGER_SOLDIER_ID] < chance && spawns[VILLAGER_SOLDIER_ID] < spawnLimits[VILLAGER_SOLDIER_ID])
			{
				EntityVillagerSoldier entityvillager = new EntityVillagerSoldier(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                //entityvillager.setProfession(0);
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);	
                spawns[VILLAGER_SOLDIER_ID]++;
			}
			
			x++;
		}
	}
	

}
