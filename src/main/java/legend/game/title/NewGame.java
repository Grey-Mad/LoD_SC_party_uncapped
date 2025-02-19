package legend.game.title;

import legend.core.memory.Method;
import legend.game.EngineState;
import legend.game.EngineStateEnum;
import legend.game.characters.CharacterData;
import legend.game.modding.coremod.CoreMod;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.SItem.xpTables;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.core.GameEngine.CONFIG;

public class NewGame extends EngineState {
  private static final int[] characterStartingLevels = {1, 3, 4, 8, 13, 15, 17, 19, 23};
  private static final int[] startingAddition_800ce758 = {0, 8, -1, 14, 29, 8, 23, 19, -1};

  @Method(0x800c7194L)
  private void setUpNewGameData() {
    if(CONFIG.getConfig(CoreMod.PLAYER_COMBATANT_SIZE_CONFIG.get()) == 3){
      gameState_800babc8.charIds_88 = new int[3];
      gameState_800babc8.charIds_88[0] = 0;
      gameState_800babc8.charIds_88[1] = -1;
      gameState_800babc8.charIds_88[2] = -1;
    }else{
      gameState_800babc8.charIds_88 = new int[CONFIG.getConfig(CoreMod.PLAYER_COMBATANT_SIZE_CONFIG.get())];
      for(int  charIndex = 1; charIndex < CONFIG.getConfig(CoreMod.PLAYER_COMBATANT_SIZE_CONFIG.get()); charIndex++)
        gameState_800babc8.charIds_88[charIndex] = -1;
    }

    //LAB_800c723c
    for(int charIndex = 0; charIndex < characterIndices_800bdbb8.length; charIndex++) {
      final CharacterData charData = gameState_800babc8.charData_32c.get(charIndex);
      final int level = characterStartingLevels[charIndex];
      charData.setXp(xpTables[charIndex][level]);
      charData.setHp(levelStuff_80111cfc[charIndex][level].hp_00);
      charData.setMp(magicStuff_80111d20[charIndex][1].mp_00);
      charData.setSp(0);
      charData.setDlevelXp(0);
      charData.setStatus (0);
      charData.setLevel(level);
      charData.setDlevel(1);

      //LAB_800c7294
      for(int additionIndex = 0; additionIndex < 8; additionIndex++) {
        charData.additionLevels_1a[additionIndex] = 0;
        charData.additionXp_22[additionIndex] = 0;
      }

      charData.additionLevels_1a[0] = 1;

      //LAB_800c72d4
      for(int i = 1; i < level; i++) {
        final int index = levelStuff_80111cfc[charIndex][i].addition_02;

        if(index != -1) {
          final int offset = additionOffsets_8004f5ac[charIndex];
          charData.additionLevels_1a[index - offset] = 1;
        }

        //LAB_800c72fc
      }

      //LAB_800c730c
      charData.selectedAddition_19 = startingAddition_800ce758[charIndex];
    }

    gameState_800babc8.charData_32c.get(0).setPartyFlags(0x3);
  }

  @Override
  @Method(0x800c7424L)
  public void tick() {
    this.setUpNewGameData();
    engineStateOnceLoaded_8004dd24 = EngineStateEnum.SUBMAP_05;
  }
}
