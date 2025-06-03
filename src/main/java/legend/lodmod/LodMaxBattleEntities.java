package legend.lodmod;

import legend.core.GameEngine;
import legend.game.combat.MaxBattleEntities;
import legend.game.combat.MaxBattleEntitiesRegistryEvent;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.RegistryDelegate;

public final class LodMaxBattleEntities {
  private LodMaxBattleEntities() { }

  private static final Registrar<MaxBattleEntities, MaxBattleEntitiesRegistryEvent> MAX_BENT_REGISTRAR = new Registrar<>(GameEngine.REGISTRIES.maxBattleEntities, LodMod.MOD_ID);
  
  public static final RegistryDelegate<MaxBattleEntities> MAX_PLAYER_BENTS = MAX_BENT_REGISTRAR.register("max_player_bents", () -> new MaxBattleEntities(9));
  public static final RegistryDelegate<MaxBattleEntities> MAX_MONSTER_BENTS = MAX_BENT_REGISTRAR.register("max_monster_bents", () -> new MaxBattleEntities(12));

  static void register(final MaxBattleEntitiesRegistryEvent event) {
    MAX_BENT_REGISTRAR.registryEvent(event);
  }
}
