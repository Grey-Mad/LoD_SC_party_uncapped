package legend.game.saves.serializers;

import legend.game.saves.ConfigCollection;
import legend.game.saves.SavedGame;
import legend.game.characters.CharacterData;
import legend.game.types.GameState52c;
import legend.game.unpacker.FileData;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.saves.serializers.RetailSerializer.deserializeRetailGameState;

public final class V1Serializer {
  private V1Serializer() { }

  public static final int MAGIC_V1 = 0x76615344; // DSav

  public static FileData fromV1Matcher(final FileData data) {
    if(data.readInt(0) == MAGIC_V1) {
      return data.slice(0x4);
    }

    return null;
  }

  public static SavedGame fromV1(final String name, final FileData data) {
    final GameState52c state = deserializeRetailGameState(data.slice(0x30));
    final CharacterData charData = state.charData_32c.get(state.charIds_88[0]);
    final int maxHp = levelStuff_80111cfc[state.charIds_88[0]][charData.getLevel()].hp_00;
    final int maxMp = magicStuff_80111d20[state.charIds_88[0]][charData.getDlevel()].mp_00;
    return new SavedGame(name, name, data.readUByte(0x2d), data.readUByte(0x2c), state, new ConfigCollection(), maxHp, maxMp);
  }
}
