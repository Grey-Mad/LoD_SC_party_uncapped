package legend.game.combat;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class MaxBattleEntitiesRegistryEvent extends RegistryEvent.Register<MaxBattleEntities> {
  public MaxBattleEntitiesRegistryEvent(final MutableRegistry<MaxBattleEntities> registry) {
    super(registry);
  }
}
