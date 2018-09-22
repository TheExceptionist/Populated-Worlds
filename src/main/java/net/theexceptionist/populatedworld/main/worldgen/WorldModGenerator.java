package net.theexceptionist.populatedworld.main.worldgen;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.theexceptionist.populatedworld.main.Main;
import net.theexceptionist.populatedworld.main.Reference;

public class WorldModGenerator implements IWorldGenerator{
	private final WorldGenVillageStructure chapelGenerator;
	private final WorldGenVillageStructure hamletGenerator;
	
	public WorldModGenerator()
	{
		this.chapelGenerator = new WorldGenVillageStructure("Lone Chapel", Main.chapel_spawnrate, 0.99f);
		this.hamletGenerator = new WorldGenVillageStructure("Hamlet", Main.hamlet_spawnrate, 0.99f);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int x = chunkX * 16 + random.nextInt(8) + 8;
		int z = chunkZ * 16 + random.nextInt(8) + 8;
		int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 80, z)).getY();
		final BlockPos basePos = new BlockPos(x, y, z);
		
		
		if(random.nextInt(100) < 25) 
		{
			chapelGenerator.trySpawn(basePos, world, random);
		}
		else if(random.nextInt(100) < 10) 
		{
			hamletGenerator.trySpawn(basePos, world, random);
		}
	}

}
