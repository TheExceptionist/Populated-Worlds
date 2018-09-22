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
import net.theexceptionist.populatedworld.main.Main;
import net.theexceptionist.populatedworld.main.Reference;
import net.theexceptionist.populatedworld.main.entity.EntityVillagerSoldier;

public class WorldGenVillageStructure {
	private static final PlacementSettings settingsDefault = new PlacementSettings().setRotation(Rotation.NONE);
	private WorldGenStructTemplate chapel;
	private WorldGenStructTemplate hamlet;
	
	protected String name;
	protected int chance;
	protected PlacementSettings settings;
	protected Template[] template; // = new BlockPos(chunkX * 16 + random.nextInt(16), 100, chunkZ * 16 + random.nextInt(16);
	protected ResourceLocation[] resourceLoc;
	protected List<Biome> validBiomes;
//	protected World world;
	protected float integrity;
	protected List<String> genEntities;
	protected int maxEntities;
	protected BlockPos size;
	protected int[] spawnLimits;
	protected int id;
	
	//Dont use this!
	public WorldGenVillageStructure(final String name, final int chance, final float integrity,
			final PlacementSettings settings, final ResourceLocation resourceLoc, final ArrayList<Biome> validBiomes, 
			final ArrayList<String> entitySpawns, final int maxEntities, final int[] limits)
	{
		this.name = name;
		this.chance = chance;
		this.settings = settings;
		settings.setIntegrity(integrity);
		
//		this.resourceLoc = resourceLoc;
		this.validBiomes = validBiomes;
		this.genEntities = entitySpawns;
		this.maxEntities = maxEntities;
		this.spawnLimits = limits;
	}
	
	public WorldGenVillageStructure(final String name, final int chance, final float integrity)
	{
		initStructs();
		this.name = name;
		this.chance = chance;
		
		if(this.name.compareTo(chapel.getName()) == 0){
			this.init(chapel, WorldGenStructInfo.CHAPEL_ID, WorldGenStructInfo.CHAPEL_RESOURCE);
		}
		else if(this.name.compareTo(hamlet.getName()) == 0)
		{
			this.init(hamlet, WorldGenStructInfo.HAMLET_ID, WorldGenStructInfo.HAMLET_RESOURCE);
		}
		
		settings.setIntegrity(integrity);
	}
	
	private void init(final WorldGenStructTemplate temp, final int id, ResourceLocation[] locations)
	{
		this.id = id;
		this.validBiomes = temp.getSpawnBiomes();		
		this.settings = settingsDefault;
		this.genEntities = temp.getSpawnEntities();
		this.maxEntities = temp.getSpawnAttempts();
		this.spawnLimits = temp.getMaxSpawns();
		
		this.resourceLoc = new ResourceLocation[temp.getMaxStructCount()];
		
		for(int i = 0; i < resourceLoc.length; i++){
			this.resourceLoc[i] = locations[i];
		}
	}

	private void initStructs() {
		// TODO Auto-generated method stub
		chapel =  new WorldGenStructTemplate("Lone Chapel", Arrays.<Biome>asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.FOREST),
				Arrays.<String>asList(WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_PRIEST_ID], 
						WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.IRON_GOLEM_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_SOLDIER_ID]),
				 WorldGenStructInfo.CHAPEL_MAX_SPAWNS, WorldGenStructInfo.CHAPEL_ID+1);
		
		hamlet =  new WorldGenStructTemplate("Hamlet", Arrays.<Biome>asList(Biomes.PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.TAIGA, Biomes.FOREST),
				Arrays.<String>asList(WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.IRON_GOLEM_ID], WorldGenStructInfo.ENTITIES_NAMES[WorldGenStructInfo.VILLAGER_SOLDIER_ID]),
				 WorldGenStructInfo.HAMLET_MAX_SPAWNS, WorldGenStructInfo.HAMLET_RESOURCE_LIBRARY_ID+1);
	}

	public void trySpawn(final BlockPos basePos, final World world, final Random random) {
		// TODO Auto-generated method stub
		switch(this.id){
			case WorldGenStructInfo.CHAPEL_ID:
				this.template = new Template[1];
				this.template[0] = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), this.getRandomResource(random));
				break;
			case WorldGenStructInfo.HAMLET_ID:
				this.template = new Template[3];
			
				for(int i = 0; i < template.length; i++)
				{
					this.template[i] = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), this.getRandomResource(random));
				}
				break;
		}
		
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
		
		BlockPos[] spawnPos = new BlockPos[template.length];
		BlockPos nullPos = new BlockPos(0, 0, 0);
		for(int i = 0; i < spawnPos.length; i++){
			if(i == 0)
			{
				spawnPos[i] = basePos;
			}
			else
			{
				spawnPos[i] = getRandomPoint(world, random, spawnPos, i, nullPos);
				
				if(checkCollides(spawnPos))
				{
					spawnPos[i] = nullPos;
				}
			}
		}
		
		for(int i = 0; i < template.length; i++){
			this.size = this.template[i].getSize();
			
			for(Biome b : validBiomes)
			{
				if(b == biomeIn)
				{
					validBiome = true;
				}
			}
			
			if(r < chance && validBiome && spawnPos[i] != nullPos)
			{
				Rotation nextRotation = random.nextInt(2) == 0 ? Rotation.CLOCKWISE_90 : random.nextInt(2) == 0 ? Rotation.
						COUNTERCLOCKWISE_90: random.nextInt(2) == 0 ? Rotation.CLOCKWISE_180 : Rotation.NONE;
				//Main.logger.info("Size: X:"+this.template.getSize().getX()+" Y: "+this.template.getSize().getY()+" Z: "+this.template.getSize().getZ();
				this.fillWithBlocks(world, spawnPos[i], Blocks.AIR.getDefaultState());
				this.template[i].addBlocksToWorld(world, spawnPos[i], new WorldGenBiomeProcessor(biomeIn), settings, 2);
				this.generatePlatform(world, spawnPos[i], WorldGenBiomeProcessor.getBiomeSpecificBlockState(blockstate7, biomeIn));
				this.settings.setRotation(nextRotation);
				this.spawnEntities(world, spawnPos[i], random, random.nextInt(this.maxEntities), random.nextInt(100));
			}
			
			/*int x = random.nextInt(20);
			int z = random.nextInt(20);
			int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY();
			
			spawnPos.add(this.template[i].getSize());
			spawnPos.add(x, y, z);*/
		}
	}

	private boolean checkCollides(BlockPos[] blockPos) {
		boolean collides = false;
		
		for(int i = 1; i < blockPos.length; i++)
		{
			if(blockPos[i] == null) continue;
			BlockPos pos1 = blockPos[i - 1];
			BlockPos size1 = template[i - 1].getSize();
			BlockPos pos2 = blockPos[i];
			BlockPos size2 = template[i].getSize();
			
			collides = posCollides(pos1, size1, pos2, size2);
		}
		
		return collides;
	}

	private boolean posCollides(BlockPos pos1, BlockPos size1, BlockPos pos2, BlockPos size2) {
		BlockPos posAndSize1 = pos1.add(size1);
		BlockPos posAndSize2 = pos2.add(size2);
		
		boolean collidesX = posAndSize1.getX() > pos2.getX() && pos1.getX() < posAndSize2.getX();
		boolean collidesY = posAndSize1.getY() > pos2.getY() && pos1.getY() < posAndSize2.getY();
		boolean collidesZ = posAndSize1.getZ() > pos2.getZ() && pos1.getZ() < posAndSize2.getZ();
		
		return collidesX && collidesY && collidesZ;
	}

	private BlockPos getRandomPoint(World world, Random random, BlockPos[] spawnPos, int i, BlockPos nullBlock) {
		spawnPos[i - 1].add(template[i - 1].getSize());
		//int dx = random.nextInt(20), dz = random.nextInt(20); 
		int dx = random.nextInt(10), dz = random.nextInt(10); 
		
		int x = spawnPos[i - 1].getX() + dx;
		int z = spawnPos[i - 1].getZ() + dz;
		int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY();
		
		return new BlockPos(x, y, z);
	}

	private ResourceLocation getRandomResource(Random rand) {
		return this.resourceLoc[rand.nextInt(resourceLoc.length)];
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
		int[] r = new int[WorldGenStructInfo.ENTITIES_NAMES.length];
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
				case WorldGenStructInfo.VILLAGER_PRIEST_ID:
					r[i] = rand.nextInt(100);
					break;
				case WorldGenStructInfo.VILLAGER_ID:
					r[i] = rand.nextInt(100);
					break;
				case WorldGenStructInfo.VILLAGER_SOLDIER_ID:
					r[i] = rand.nextInt(100);
					break;
				case WorldGenStructInfo.IRON_GOLEM_ID:
					r[i] = rand.nextInt(1000);
					break;
			}
		}
		
		for(int i = 0; i < count; i++)
		{
//			String entityName = this.genEntities.get(rand.nextInt(this.genEntities.size()));
			if(spawns[WorldGenStructInfo.VILLAGER_PRIEST_ID] == 0)
			{
				EntityVillager entityvillager = new EntityVillager(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                entityvillager.setProfession(2);
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);
                spawns[WorldGenStructInfo.VILLAGER_PRIEST_ID]++;
               // Main.logger.info("Priest Spawned!");
			}
			else if(r[WorldGenStructInfo.VILLAGER_PRIEST_ID] < chance && spawns[WorldGenStructInfo.VILLAGER_PRIEST_ID] < spawnLimits[WorldGenStructInfo.VILLAGER_PRIEST_ID])
			{
				EntityVillager entityvillager = new EntityVillager(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                entityvillager.setProfession(2);
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);
                spawns[WorldGenStructInfo.VILLAGER_PRIEST_ID]++;
			}
			if(r[WorldGenStructInfo.VILLAGER_ID] < chance && spawns[WorldGenStructInfo.VILLAGER_ID] < spawnLimits[WorldGenStructInfo.VILLAGER_ID])
			{
				EntityVillager entityvillager = new EntityVillager(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                entityvillager.setProfession(rand.nextInt(5));
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);
                spawns[WorldGenStructInfo.VILLAGER_ID]++;
			}
			if(r[WorldGenStructInfo.IRON_GOLEM_ID] < chance && spawns[WorldGenStructInfo.IRON_GOLEM_ID] < spawnLimits[WorldGenStructInfo.IRON_GOLEM_ID])
			{
				EntityIronGolem entitygolem = new EntityIronGolem(world);
                entitygolem.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                world.spawnEntity(entitygolem);	
                spawns[WorldGenStructInfo.IRON_GOLEM_ID]++;
			}
			if(r[WorldGenStructInfo.VILLAGER_SOLDIER_ID] < chance && spawns[WorldGenStructInfo.VILLAGER_SOLDIER_ID] < spawnLimits[WorldGenStructInfo.VILLAGER_SOLDIER_ID])
			{
				EntityVillagerSoldier entityvillager = new EntityVillagerSoldier(world);
                entityvillager.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
                //entityvillager.setProfession(0);
                entityvillager.finalizeMobSpawn(world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData)null, false);
                world.spawnEntity(entityvillager);	
                spawns[WorldGenStructInfo.VILLAGER_SOLDIER_ID]++;
			}
			
			x++;
		}
	}
	

}
