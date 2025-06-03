package legend.game.combat;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class MaxBattleEntitiesRegistry extends MutableRegistry<MaxBattleEntities> {
  public MaxBattleEntitiesRegistry() {
    super(new RegistryId("lod_core", "max_battle_entities"));
  }
}