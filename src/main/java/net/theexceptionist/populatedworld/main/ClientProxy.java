package net.theexceptionist.populatedworld.main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.theexceptionist.populatedworld.main.entity.EntityVillagerSoldier;
import net.theexceptionist.populatedworld.main.entity.RenderVillagerSoldier;
import net.theexceptionist.populatedworld.main.worldgen.WorldModGenerator;

public class ClientProxy implements IProxy{
	
	@Override
	public void registerRenders()
	{
RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        
        renderManager.entityRenderMap.put(EntityVillagerSoldier.class, new RenderVillagerSoldier(renderManager));
   /*
        renderManager.entityRenderMap.put(EntityVillagerArcher.class, new RenderVillagerArcher(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMage.class, new RenderVillagerMage(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerGuardian.class, new RenderVillagerGuardian(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerAlchemist.class, new RenderVillagerAlchemist(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerMerchant.class, new RenderVillagerMerchant(renderManager));
        renderManager.entityRenderMap.put(EntityMerchantGuard.class, new RenderMerchantGuard(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerKnight.class, new RenderVillagerKnight(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerHorse.class, new RenderVillagerHorse(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerHunter.class, new RenderVillagerHunter(renderManager));
        renderManager.entityRenderMap.put(EntityVillagerCreeperHunter.class, new RenderVillagerCreeperHunter(renderManager));   
        renderManager.entityRenderMap.put(EntityVillagerArrow.class, new RenderVillagerArrow(renderManager));
	*/
	}

	@Override
	public void genInit() {
		GameRegistry.registerWorldGenerator(new WorldModGenerator(), 30);
		//Main.logger.info("Initializing Generator");
	}
}
