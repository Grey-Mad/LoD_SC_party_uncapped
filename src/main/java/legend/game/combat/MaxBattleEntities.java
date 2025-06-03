package legend.game.combat;

import legend.game.scripting.ScriptReadable;
import org.legendofdragoon.modloader.registries.RegistryEntry;


public class MaxBattleEntities extends RegistryEntry implements ScriptReadable {
    public final int maxBattleEntities;
    
    public MaxBattleEntities(final int maxBattleEntities) {
        this.maxBattleEntities = maxBattleEntities;
    }

}
