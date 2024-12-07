package legend.game.characters;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class CharacterDataRegistry extends MutableRegistry<CharacterData> {
  public CharacterDataRegistry() {
    super(new RegistryId("lod_core", "characters_data"));
  }
}
