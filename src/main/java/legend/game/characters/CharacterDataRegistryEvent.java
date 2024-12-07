package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class CharacterDataRegistryEvent extends RegistryEvent.Register<CharacterData> {
  public CharacterDataRegistryEvent(final MutableRegistry<CharacterData> registry) {
    super(registry);
  }
}
