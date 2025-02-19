package legend.lodmod;

import java.util.ArrayList;
import java.util.List;

import org.legendofdragoon.modloader.events.EventListener;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import legend.core.GameEngine;
import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigRegistryEvent;
import legend.game.saves.ConfigStorageLocation;

public class LodExtendedCharacterSettings {
  private LodExtendedCharacterSettings() {
     
  }

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, "lod");
  
  public static List<RegistryDelegate<BoolConfigEntry>> FORCE_ADD_AND_KEEP_LIST = new ArrayList<RegistryDelegate<BoolConfigEntry>>();
  //greytodo:  
  //public static List<RegistryDelegate<BoolConfigEntry>> FORCE_REMOVE_AND_BLOCK_LIST = new ArrayList<RegistryDelegate<BoolConfigEntry>>();
  public static List<RegistryDelegate<BoolConfigEntry>> FORCE_LEVEL_ZERO_ON_START_LIST = new ArrayList<RegistryDelegate<BoolConfigEntry>>();

 public static void fillRegistrar(String[] characterNames){
  for(int i = 0; i < characterNames.length; i++){
    FORCE_ADD_AND_KEEP_LIST.add(i, CONFIG_REGISTRAR.register(String.join("","force_add_and_keep_", characterNames[i]), () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY)));
    //greytodo:
    //FORCE_REMOVE_AND_BLOCK_LIST.add(i, CONFIG_REGISTRAR.register(String.join("","force_remove_and_block_", characterNames[i]), () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY)));
    FORCE_LEVEL_ZERO_ON_START_LIST.add(CONFIG_REGISTRAR.register(String.join("","set_lv_one_at_start_", characterNames[i]), () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY)));
  
  }

 }


  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    CONFIG_REGISTRAR.registryEvent(event);
  }
}
