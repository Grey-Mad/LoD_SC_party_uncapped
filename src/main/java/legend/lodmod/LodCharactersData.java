package legend.lodmod;

import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

import legend.core.GameEngine;
import legend.game.characters.CharacterData;
import legend.game.characters.CharacterDataRegistryEvent;

import java.util.ArrayList;
import java.util.List;

public final class LodCharactersData {
  private LodCharactersData() { 

  }

  private static final Registrar<CharacterData, CharacterDataRegistryEvent> CHARACTER_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.charactersData, LodMod.MOD_ID);
  public static List<RegistryDelegate<CharacterData>> CHARACTER_REGISTRAR_LIST = new ArrayList<RegistryDelegate<CharacterData>>();
  
 public static void fillRegistrar(String[] characterNames){
  for(int i = 0; i < characterNames.length; i++){
    CHARACTER_REGISTRAR_LIST.add(i, CHARACTER_REGISTRAR.register(characterNames[i], () -> new CharacterData()));
  }
 }

  static void register(final CharacterDataRegistryEvent event) {
    CHARACTER_REGISTRAR.registryEvent(event);
  }
}
