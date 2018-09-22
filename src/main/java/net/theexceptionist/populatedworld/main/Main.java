package net.theexceptionist.populatedworld.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.theexceptionist.populatedworld.main.entity.EntityVillagerSoldier;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class Main
{

	

	@SidedProxy(serverSide = "net.theexceptionist.populatedworld.main.CommonProxy", clientSide = "net.theexceptionist.populatedworld.main.ClientProxy")
    public static IProxy proxy;
	
	@Mod.Instance(Reference.MODID)
	public static Main instance;
	
	public static Logger logger = LogManager.getLogger(Reference.NAME);
	
	public static int chapel_spawnrate = 5;
	public static int hamlet_spawnrate = 5;
	//Config file
	private static File config_file;
	private static BufferedReader reader;
	private static BufferedWriter writer;
	 private static String[] config_text =
	    	{
	    			"#Spawnrate of chapel\n",
	    			"#out of 1000! So 5 means a 0.5% chance per chunk loaded!\n",
	    			"chapel_spawnrate=5\n",
	    			"hamlet_spawnrate=5\n",
	    			/*"#do not set above max_distance or below 3!\n",
	    			"min_distance=3\n",
	    			"#Spawnrate for the villagers outside the villages, 0 or -1 turns them off!\n",
	    			"merchant_spawn_rate=1\n",
	    			"#\n",
	    			"#mark each biome name with either a 0 or 1 to turn them on/off\n",
	    			"#Ex: Ocean=1 turns on the village in the ocean biome\n",
	    			"#Ex: Ocean=0 turns off the village in the ocean biome\n",
	    			"Ocean=1\n",
	    			"Plains=1\n",
	    			"Desert=1\n",
	    			"Extreme Hills=1\n",
	    			"Forest=1\n",
	    			"Taiga=1\n",
	    			"Swampland=1\n",
	    			"River=1\n",
	    			//"Hell=1\n",
	    			//"Ocean=1\n",
	    			"FrozenOcean=1\n",
	    			"FrozenRiver=1\n",
	    			"Ice Plains=1\n",
	    			"Ice Mountains=1\n",
	    			"MushroomIsland=1\n",
	    			"MushroomIslandShore=1\n",
	    			"Beach=1\n",
	    			"DesertHills=1\n",
	    			"ForestHills=1\n",
	    			"TaigaHills=1\n",
	    			"Extreme Hills Edge=1\n",
	    			"Jungle=1\n",
	    			"JungleHills=1\n",
	    			"JungleEdge=1\n",
	    			"DeepOcean=1\n",
	    			"Stone Beach=1\n",
	    			"Cold Beach=1\n",
	    			"Birch Forest=1\n",
	    			"Birch Forest Hills=1\n",
	    			"Roofed Forest=1\n",
	    			"Cold Taiga=1\n",
	    			"Cold Taiga Hills=1\n",
	    			"Mega Taiga=1\n",
	    			"Mega Taiga Hills=1\n",
	    			"Extreme Hills+=1\n",
	    			"Savanna=1\n",
	    			"Savanna Plateau=1\n",
	    			"Mesa=1\n",
	    			"Mesa Plateau F=1\n",
	    			"Mesa Plateau=1\n",
	    			"Sunflower Plains=1\n",
	    			"Desert M=1\n",
	    			"Taiga M=1\n",
	    			"Swampland M=1\n",
	    			"Ice Plains Spikes=1\n",
	    			"Jungle M=1\n",
	    			"JungleEdge M=1\n",
	    			"Birch Forest M=1\n",
	    			"Birch Forest Hills M=1\n",
	    			"Roofed Forst M=1\n",
	    			"Cold Taiga M=1\n",
	    			"Mega Spruce Taiga=1\n",
	    			"Redwood Taiga Hills M=1\n",
	    			"Extreme Hills+ M=1\n",
	    			"Savanna M=1\n",
	    			"Savanna Plateau M=1\n",
	    			"Mesa (Bryce)=1\n",
	    			"Mesa Plateau F M=1\n",
	    			"Mesa Plateau M=1\n"*/
	    	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		config_file = new File(event.getSuggestedConfigurationFile().getAbsolutePath());
		
		try {
			if(config_file.createNewFile())
			{
				System.out.println(Reference.MODID+"| Config file not found! \nCreating...");	
				try {
					System.out.println("Writing to config file....");
					writer = new BufferedWriter(new FileWriter(config_file));
					
					//writer.
					for(int i = 0; i < config_text.length; i++)
					{
						writer.write(config_text[i]);
					}
					
					System.out.println("Wrote to config file!");
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} finally {
					writer.close();
				}
				
				readConfig();
			}
			else
			{
				readConfig();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("Populated Worlds Initialized!");
		//LogManager.getLogger()
	}
	
	private void readConfig() throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		System.out.println(Reference.MODID+"| Config file found! \nLoading...");
		
		try {
			String line;
			
			System.out.print("Reading from config file.....");
			reader = new BufferedReader(new FileReader(config_file));
			
			while((line = reader.readLine()) != null)
			{
				if(line.substring(0, 1).compareTo("#") == 0) continue;
				
				String[] parts = line.split("=");
				
				if(parts[0].contains("chapel"))
				{
					chapel_spawnrate = Integer.parseInt(parts[1]);
				}
				else if(parts[0].contains("hamlet"))
				{
					hamlet_spawnrate = Integer.parseInt(parts[1]);	
				}
			}
			
			
			System.out.println("Read the config file! New Chapel Spawnrate: "+chapel_spawnrate);//+" New Min: "+min_distance+" New Merchant: "+merchant_spawn);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.genInit();
    	proxy.registerRenders();
    	instance = this;
    	
    	createEntity(EntityVillagerSoldier.class, 1612, "villager_soldier", 361425, 7582224);
    }
    
    @EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
    
    public static void createEntity(Class entityClass, int ID, String entityName, int solidColor, int spotColor){
    	EntityRegistry.registerModEntity(new ResourceLocation(Reference.MODID+":"+entityName), entityClass, entityName, ID, instance, 128, 1, true);
    	EntityRegistry.registerEgg(new ResourceLocation(Reference.MODID+":"+entityName),  solidColor, spotColor);
    }
	
}
