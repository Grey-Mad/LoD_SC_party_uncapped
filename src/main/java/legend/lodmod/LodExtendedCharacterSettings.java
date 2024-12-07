package legend.lodmod;

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
  private LodExtendedCharacterSettings() { }

  private static final Registrar<ConfigEntry<?>, ConfigRegistryEvent> CONFIG_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.config, "lod");
  
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_DART = CONFIG_REGISTRAR.register("force_remove_and_block_dart", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_LAVITZ = CONFIG_REGISTRAR.register("force_remove_and_block_lavitz", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_SHANA = CONFIG_REGISTRAR.register("force_remove_and_block_shana", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_ROSE = CONFIG_REGISTRAR.register("force_remove_and_block_rose", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_HASCHEL = CONFIG_REGISTRAR.register("force_remove_and_block_haschel", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_ALBERT = CONFIG_REGISTRAR.register("force_remove_and_block_albert", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_MERU = CONFIG_REGISTRAR.register("force_remove_and_block_meru", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_KONGOL = CONFIG_REGISTRAR.register("force_remove_and_block_kongol", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_REMOVE_AND_BLOCK_MIRANDA = CONFIG_REGISTRAR.register("force_remove_and_block_miranda", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));

  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_DART = CONFIG_REGISTRAR.register("force_add_and_keep_dart", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_LAVITZ = CONFIG_REGISTRAR.register("force_add_and_keep_lavitz", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_SHANA = CONFIG_REGISTRAR.register("force_add_and_keep_shana", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_ROSE = CONFIG_REGISTRAR.register("force_add_and_keep_rose", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_HASCHEL = CONFIG_REGISTRAR.register("force_add_and_keep_haschel", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_ALBERT = CONFIG_REGISTRAR.register("force_add_and_keep_albert", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_MERU = CONFIG_REGISTRAR.register("force_add_and_keep_meru", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_KONGOL = CONFIG_REGISTRAR.register("force_add_and_keep_kongol", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  public static final RegistryDelegate<BoolConfigEntry> FORCE_ADD_AND_KEEP_MIRANDA = CONFIG_REGISTRAR.register("force_add_and_keep_miranda", () -> new BoolConfigEntry(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY));
  
  @EventListener
  public static void registerConfig(final ConfigRegistryEvent event) {
    CONFIG_REGISTRAR.registryEvent(event);
  }
}
