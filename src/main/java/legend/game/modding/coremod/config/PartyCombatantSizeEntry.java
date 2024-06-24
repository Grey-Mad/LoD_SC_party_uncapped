package legend.game.modding.coremod.config;

import org.legendofdragoon.modloader.registries.RegistryId;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;

public class PartyCombatantSizeEntry extends ConfigEntry<Integer> {
  public PartyCombatantSizeEntry() {
    super(3, ConfigStorageLocation.CAMPAIGN, ConfigCategory.GAMEPLAY, PartyCombatantSizeEntry::serializer, PartyCombatantSizeEntry::deserializer);

    this.setEditControl((number, gameState) -> {
      final NumberSpinner<Integer> spinner = NumberSpinner.intSpinner(number, 3, 7);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  private static byte[] serializer(final int val) {
    final byte[] data = new byte[4];
    MathHelper.set(data, 0, 4, val);
    return data;
  }

  private static int deserializer(final byte[] data) {
    if(data.length == 4) {
      return IoHelper.readInt(data, 0);
    }
    return 3;
  }

  public static void callOnChangeValued(final String entryId) {
    
  }
}
