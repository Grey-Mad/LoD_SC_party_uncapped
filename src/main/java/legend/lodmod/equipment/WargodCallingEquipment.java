package legend.lodmod.equipment;

import legend.game.characters.ElementSet;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Equipment;
import legend.game.inventory.ItemIcon;
import legend.game.types.EquipmentSlot;
import legend.lodmod.LodMod;

import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;

public class WargodCallingEquipment extends Equipment {
  public WargodCallingEquipment(final int price) {
    super(price, 0, EquipmentSlot.ACCESSORY, 0x20, 0xf5, LodMod.NO_ELEMENT.get(), 0, new ElementSet(), new ElementSet(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, false, false, false, 0, 0, 0, 0, 0, ItemIcon.AMULET, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
  }

  @Override
  public void applyEffect(final BattleEntity27c wearer) {
    battleState_8006e398.additionExtra_474[wearer.bentSlot_274].flag_00 |= 0x2;
  }
}
