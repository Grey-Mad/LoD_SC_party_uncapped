package legend.game.modding.coremod.config;

import legend.game.saves.BoolConfigEntry;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigStorageLocation;
import legend.game.scripting.Param;
import legend.game.scripting.ScriptReadable;

import static legend.core.GameEngine.CONFIG;

public class ExtendedPartyConfigEntry extends BoolConfigEntry implements ScriptReadable {
  public ExtendedPartyConfigEntry() {
    super(false, ConfigStorageLocation.SAVE, ConfigCategory.EXTENDED_PARTY);
  }

  @Override
  public void read(final int index, final Param out) {
    out.set(CONFIG.getConfig(this).booleanValue()? 1 : 0);
  }
}

