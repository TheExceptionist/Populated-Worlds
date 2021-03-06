package net.theexceptionist.populatedworld.main.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIGuardArea extends EntityAIBase
{
    private final EntityCreature entity;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;

    public EntityAIGuardArea(EntityCreature entityIn)
    {
        this.entity = entityIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        BlockPos blockpos = new BlockPos(this.entity);

        if (this.entity.world.isDaytime())
        {
            if (this.entity.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (this.insidePosX != -1 && this.entity.getDistanceSq((double)this.insidePosX, this.entity.posY, (double)this.insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                Village village = this.entity.world.getVillageCollection().getNearestVillage(blockpos, 14);
                
                if (village == null)
                {
                    return false;
                }
                else
                {
                    this.doorInfo = village.getDoorInfo(blockpos);
                    return this.doorInfo != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.insidePosX = -1;
        BlockPos blockpos = this.doorInfo.getDoorBlockPos();
        int i = blockpos.getX() + (this.entity.world.rand.nextInt(2) == 0 ? 1 : -1);
        int j = blockpos.getY();
        int k = blockpos.getZ() - 1;
        int choice = this.entity.world.rand.nextInt(3);

        if(choice == 0){
	        if (this.entity.getDistanceSq(blockpos) > 256.0D)
	        {
	            /*Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entity, 14, 3, new Vec3d((double)i + 0.5D, (double)j, (double)k + 0.5D));
	
	            if (vec3d != null)
	            {*/
	            this.entity.getNavigator().tryMoveToXYZ(i, j, k, 1.0D);
	            //}
	        }
	        else
	        {
	            this.entity.getNavigator().tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
	        }
        }
        else if(choice == 1)
        {
        	BlockPos pos = this.entity.getPosition();
        	int xChange = this.entity.world.rand.nextInt(30);
        	int zChange = this.entity.world.rand.nextInt(30);
        	BlockPos target = new BlockPos(pos.getX() + xChange, this.entity.world.getTopSolidOrLiquidBlock(new BlockPos(pos.getX() + xChange, pos.getY(), pos.getZ() + zChange)).getY(), pos.getZ() + zChange);
        	
        	int light = this.entity.world.getLightFromNeighbors(target);
        	
        	if(light < 7)
        	{
        	     this.entity.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), 1.0D);//.tryMoveToXYZ((double)i + 0.5D, (double)j, (double)k + 0.5D, 1.0D);
        	}
        	else
        	{
        		return;
        	}
        }
        else
        {
        	int radius = 30;
        	double x = this.entity.posX + this.entity.world.rand.nextInt((int) (radius) - radius/2);
        	double y = this.entity.posY + this.entity.world.rand.nextInt((int) (3) - 1);
        	double z = this.entity.posZ + this.entity.world.rand.nextInt((int) (radius) - radius/2);
        	
        	IBlockState block = this.entity.world.getBlockState(new BlockPos(x, y, z));
			
			if(block == Blocks.OAK_FENCE.getDefaultState() || block == Blocks.ACACIA_FENCE.getDefaultState() || block == Blocks.BIRCH_FENCE.getDefaultState() || block == Blocks.JUNGLE_FENCE.getDefaultState() || block == Blocks.SPRUCE_FENCE.getDefaultState() || block == Blocks.DARK_OAK_FENCE.getDefaultState())
			{
				this.entity.getNavigator().tryMoveToXYZ(x, y, z, 1.0D);
				return;
			}
        }
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.insidePosX = this.doorInfo.getInsideBlockPos().getX();
        this.insidePosZ = this.doorInfo.getInsideBlockPos().getZ();
        this.doorInfo = null;
    }
}
