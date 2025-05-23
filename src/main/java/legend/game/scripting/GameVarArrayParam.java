package legend.game.scripting;

import legend.game.combat.Battle;
import legend.game.submap.SMap;
import legend.game.Scus94491BpeSegment_8006;
import legend.game.Scus94491BpeSegment_800b;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.battlestate.StatusConditions20;
import legend.game.modding.events.battle.DragonBlockStaffOffEvent;
import legend.game.modding.events.battle.DragonBlockStaffOnEvent;
import legend.game.submap.SubmapObject210;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;

public class GameVarArrayParam extends Param {
  private final int varIndex;
  private final int arrIndex;

  public GameVarArrayParam(final int varIndex, final int arrIndex) {
    this.varIndex = varIndex;
    this.arrIndex = arrIndex;
  }
  
  @Override
  public int get() {

    if (this.varIndex>=128){
      return this.readStatusConditionsVar(varIndex-128, this.arrIndex);
    }

    return switch(this.varIndex) {
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08[this.arrIndex];
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIds_88[this.arrIndex];
      case 32 -> Scus94491BpeSegment_8006.battleState_8006e398.allBents_e0c[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.allBents_e0c[this.arrIndex].index : -1;
      case 34 -> Scus94491BpeSegment_8006.battleState_8006e398.playerBents_e40[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.playerBents_e40[this.arrIndex].index : -1;
      case 36 -> Scus94491BpeSegment_8006.battleState_8006e398.monsterBents_e50[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.monsterBents_e50[this.arrIndex].index : -1;
      case 41 -> throw new RuntimeException("Not implemented"); //Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.get(this.arrIndex).get();
      case 45 -> this.readBattleVar(this.arrIndex);
      // Indices 6-9 are indices for the four camera positions in a battle
      case 46 -> ((Battle)currentEngineState_8004dd04).currentStageData_800c6718.get(this.arrIndex);
      case 48 -> Scus94491BpeSegment_8006.battleState_8006e398.aliveBents_e78[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.aliveBents_e78[this.arrIndex].index : -1;
      case 50 -> Scus94491BpeSegment_8006.battleState_8006e398.alivePlayerBents_eac[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.alivePlayerBents_eac[this.arrIndex].index : -1;
      case 52 -> Scus94491BpeSegment_8006.battleState_8006e398.aliveMonsterBents_ebc[this.arrIndex] != null ? Scus94491BpeSegment_8006.battleState_8006e398.aliveMonsterBents_ebc[this.arrIndex].index : -1;
      case 64 -> ((SMap)currentEngineState_8004dd04).sobjs_800c6880[this.arrIndex] != null ? ((SMap)currentEngineState_8004dd04).sobjs_800c6880[this.arrIndex].index : 0;
      case 73 -> ((SMap)currentEngineState_8004dd04).indicatorTickCountArray_800c6970[this.arrIndex];
      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8.wmapFlags_15c.getRaw(this.arrIndex);
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8.visitedLocations_17c.getRaw(this.arrIndex);
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.goods_19c[this.arrIndex];
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4[this.arrIndex];
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4[this.arrIndex];

      /*case 202 -> this.readDragoonTurnsRemainingVar(this.arrIndex);//
      case 203 -> this.read2e8Var(this.arrIndex);//
      case 204 -> this.read334Var(this.arrIndex);//
      case 205 -> this.read34cVar(this.arrIndex);//
      case 206 -> this.readStatusVar(this.arrIndex);//0x81 129
      case 207 -> this.read460Var(this.arrIndex);//
      case 208 -> this.readAdditionExtraVar(this.arrIndex);//0xbd */

      default -> throw new IllegalArgumentException("Unknown game data index " + this.varIndex);
    };
  }

  @Override
  public Param set(final int val) {

    if (this.varIndex>=128){
      this.writeStatusConditionsVar(varIndex-128, this.arrIndex, val);
      return this;
    }

    switch(this.varIndex) {
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08[this.arrIndex] = val;
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIds_88[this.arrIndex] = val;
      case 32 -> Scus94491BpeSegment_8006.battleState_8006e398.allBents_e0c[this.arrIndex] = SCRIPTS.getState(val, BattleEntity27c.class);
      case 34 -> Scus94491BpeSegment_8006.battleState_8006e398.playerBents_e40[this.arrIndex] = SCRIPTS.getState(val, PlayerBattleEntity.class);
      case 36 -> Scus94491BpeSegment_8006.battleState_8006e398.monsterBents_e50[this.arrIndex] = SCRIPTS.getState(val, MonsterBattleEntity.class);
      case 41 -> throw new RuntimeException("Not implemented"); //Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.get(this.arrIndex).set(val);
      case 45 -> this.writeCombatVar(this.arrIndex, val);
      case 46 -> ((Battle)currentEngineState_8004dd04).currentStageData_800c6718.set(this.arrIndex, val);
      case 48 -> Scus94491BpeSegment_8006.battleState_8006e398.aliveBents_e78[this.arrIndex] = SCRIPTS.getState(val, BattleEntity27c.class);
      case 50 -> Scus94491BpeSegment_8006.battleState_8006e398.alivePlayerBents_eac[this.arrIndex] = SCRIPTS.getState(val, PlayerBattleEntity.class);
      case 52 -> Scus94491BpeSegment_8006.battleState_8006e398.aliveMonsterBents_ebc[this.arrIndex] = SCRIPTS.getState(val, MonsterBattleEntity.class);
      case 64 -> ((SMap)currentEngineState_8004dd04).sobjs_800c6880[this.arrIndex] = SCRIPTS.getState(val, SubmapObject210.class);
      case 73 -> ((SMap)currentEngineState_8004dd04).indicatorTickCountArray_800c6970[this.arrIndex] = val;
      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8.wmapFlags_15c.setRaw(this.arrIndex, val);
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8.visitedLocations_17c.setRaw(this.arrIndex, val);
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.goods_19c[this.arrIndex] = val;
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4[this.arrIndex] = val;
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4[this.arrIndex] = val;
 
      /*case 202 -> this.writeDragoonTurnsRemainingVar(this.arrIndex, val);
      case 203 -> this.write2e8Var(this.arrIndex, val);
      case 204 -> this.write334Var(this.arrIndex, val);
      case 205 -> this.write34cVar(this.arrIndex, val);
      case 206 -> this.writeStatusVar(this.arrIndex, val);
      case 207 -> this.write460Var(this.arrIndex, val);
      case 208 -> this.writeAdditionExtraVar(this.arrIndex, val);*/

      default -> throw new IllegalArgumentException("Unknown game data index " + this.varIndex);
    }

    return this;
  }

  @Override
  public Param array(final int index) {
    return new GameVarArrayParam(this.varIndex, this.arrIndex + index);
  }

  @Override
  public String toString() {
    return "GameVar[%d][%d] %d".formatted(this.varIndex, this.arrIndex, this.get());
  }

  private int readBattleVar(final int index) {


    
      int indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._294Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[index - indexOffset];
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._2e8Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._2e8.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398._2e8[index - indexOffset];
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._334Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._334.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398._334[index - indexOffset];
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._34cOffset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._34c.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398._34c[index - indexOffset];
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._384Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.status_384.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398.status_384[index - indexOffset].pack();
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._460Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._460.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398._460[index - indexOffset];
      }
      indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._474Offset;
      if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474.length))){
        return Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[index - indexOffset].pack();
      }


    return switch(index) {
      case 0 -> Scus94491BpeSegment_8006.battleState_8006e398._180;
      case 1 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_184;
      case 2 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_188;
      case 3 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_18c;
      case 4 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_190;
      case 5 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_194;
      case 6 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_198;
      case 7 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_19c;
      case 8 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a0;
      case 9 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a4;
      case 10 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a8;
      case 11 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1ac;
      case 12 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b0;
      case 13 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b4;
      case 14 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b8;
      case 15 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1bc;
      case 16 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c0;
      case 17 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c4;
      case 18 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c8;
      case 19 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1cc;
      case 20 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1d0;
      case 21 -> Scus94491BpeSegment_8006.battleState_8006e398._1d4;
      case 22 -> Scus94491BpeSegment_8006.battleState_8006e398._1d8;
      case 23 -> Scus94491BpeSegment_8006.battleState_8006e398._1dc;
      case 24 -> Scus94491BpeSegment_8006.battleState_8006e398._1e0;
      case 25 -> Scus94491BpeSegment_8006.battleState_8006e398._1e4;
      case 26 -> Scus94491BpeSegment_8006.battleState_8006e398._1e8;
      case 27 -> Scus94491BpeSegment_8006.battleState_8006e398._1ec;
      case 28 -> Scus94491BpeSegment_8006.battleState_8006e398._1f0;
      case 29 -> Scus94491BpeSegment_8006.battleState_8006e398._1f4;
      case 30 -> Scus94491BpeSegment_8006.battleState_8006e398._1f8;
      case 31 -> Scus94491BpeSegment_8006.battleState_8006e398._1fc;
      case 32 -> Scus94491BpeSegment_8006.battleState_8006e398._200;
      case 33 -> Scus94491BpeSegment_8006.battleState_8006e398._204;
      case 34 -> Scus94491BpeSegment_8006.battleState_8006e398._208;
      case 35 -> Scus94491BpeSegment_8006.battleState_8006e398._20c;
      case 36 -> Scus94491BpeSegment_8006.battleState_8006e398._210;
      case 37 -> Scus94491BpeSegment_8006.battleState_8006e398._214;
      case 38 -> Scus94491BpeSegment_8006.battleState_8006e398._218;
      case 39 -> Scus94491BpeSegment_8006.battleState_8006e398._21c;
      case 40 -> Scus94491BpeSegment_8006.battleState_8006e398._220;
      case 41 -> Scus94491BpeSegment_8006.battleState_8006e398._224;
      case 42 -> Scus94491BpeSegment_8006.battleState_8006e398._228;
      case 43 -> Scus94491BpeSegment_8006.battleState_8006e398._22c;
      case 44 -> Scus94491BpeSegment_8006.battleState_8006e398._230;
      case 45 -> Scus94491BpeSegment_8006.battleState_8006e398._234;
      case 46 -> Scus94491BpeSegment_8006.battleState_8006e398._238;
      case 47 -> Scus94491BpeSegment_8006.battleState_8006e398._23c;
      case 48 -> Scus94491BpeSegment_8006.battleState_8006e398._240;
      case 49 -> Scus94491BpeSegment_8006.battleState_8006e398._244;
      case 50 -> Scus94491BpeSegment_8006.battleState_8006e398._248;
      case 51 -> Scus94491BpeSegment_8006.battleState_8006e398._24c;
      case 52 -> Scus94491BpeSegment_8006.battleState_8006e398._250;
      case 53 -> Scus94491BpeSegment_8006.battleState_8006e398._254;
      case 54 -> Scus94491BpeSegment_8006.battleState_8006e398._258;
      case 55 -> Scus94491BpeSegment_8006.battleState_8006e398._25c;
      case 56 -> Scus94491BpeSegment_8006.battleState_8006e398._260;
      case 57 -> Scus94491BpeSegment_8006.battleState_8006e398._264;
      case 58 -> Scus94491BpeSegment_8006.battleState_8006e398._268;
      case 59 -> Scus94491BpeSegment_8006.battleState_8006e398._26c;
      case 60 -> Scus94491BpeSegment_8006.battleState_8006e398._270;
      case 61 -> Scus94491BpeSegment_8006.battleState_8006e398._274;
      case 62 -> Scus94491BpeSegment_8006.battleState_8006e398._278;
      case 63 -> Scus94491BpeSegment_8006.battleState_8006e398._27c;
      case 64 -> Scus94491BpeSegment_8006.battleState_8006e398.numCompleteAdditionHits_280;
      case 65 -> Scus94491BpeSegment_8006.battleState_8006e398._284;
      case 66 -> Scus94491BpeSegment_8006.battleState_8006e398.counterAttackStage_288;
      case 67 -> Scus94491BpeSegment_8006.battleState_8006e398._28c;
      case 68 -> Scus94491BpeSegment_8006.battleState_8006e398._290;

      case 71 -> Scus94491BpeSegment_8006.battleState_8006e398._294Offset;
      case 72 -> Scus94491BpeSegment_8006.battleState_8006e398._2a0;
      case 73 -> Scus94491BpeSegment_8006.battleState_8006e398._2a4;
      case 74 -> Scus94491BpeSegment_8006.battleState_8006e398._2a8;
      case 75 -> Scus94491BpeSegment_8006.battleState_8006e398._2ac;
      case 76 -> Scus94491BpeSegment_8006.battleState_8006e398.specialFlag_2b0;
      case 77 -> Scus94491BpeSegment_8006.battleState_8006e398.runAwayFlags_2b4;
      case 78 -> Scus94491BpeSegment_8006.battleState_8006e398._2b8;
      case 79 -> Scus94491BpeSegment_8006.battleState_8006e398._2bc;
      case 80 -> Scus94491BpeSegment_8006.battleState_8006e398._2c0;
      case 81 -> Scus94491BpeSegment_8006.battleState_8006e398._2c4;
      case 82 -> Scus94491BpeSegment_8006.battleState_8006e398._2c8;
      case 83 -> Scus94491BpeSegment_8006.battleState_8006e398._2cc;
      case 84 -> Scus94491BpeSegment_8006.battleState_8006e398._2d0;
      case 85 -> Scus94491BpeSegment_8006.battleState_8006e398._2d4;
      case 86 -> Scus94491BpeSegment_8006.battleState_8006e398._2d8;
      case 87 -> Scus94491BpeSegment_8006.battleState_8006e398._2dc;
      case 88 -> Scus94491BpeSegment_8006.battleState_8006e398._2e0;
      case 89 -> Scus94491BpeSegment_8006.battleState_8006e398._2e4;

      case 92 -> Scus94491BpeSegment_8006.battleState_8006e398._2e8Offset;
      case 93 -> Scus94491BpeSegment_8006.battleState_8006e398._2f4;
      case 94 -> Scus94491BpeSegment_8006.battleState_8006e398._2f8;
      case 95 -> Scus94491BpeSegment_8006.battleState_8006e398._2fc;
      case 96 -> Scus94491BpeSegment_8006.battleState_8006e398._300;
      case 97 -> Scus94491BpeSegment_8006.battleState_8006e398._304;
      case 98 -> Scus94491BpeSegment_8006.battleState_8006e398._308;
      case 99 -> Scus94491BpeSegment_8006.battleState_8006e398._30c;
      case 100 -> Scus94491BpeSegment_8006.battleState_8006e398._310;
      case 101 -> Scus94491BpeSegment_8006.battleState_8006e398._314;
      case 102 -> Scus94491BpeSegment_8006.battleState_8006e398._318;
      case 103 -> Scus94491BpeSegment_8006.battleState_8006e398._31c;
      case 104 -> Scus94491BpeSegment_8006.battleState_8006e398._320;
      case 105 -> Scus94491BpeSegment_8006.battleState_8006e398.additionState_324;
      case 106 -> Scus94491BpeSegment_8006.battleState_8006e398.additionStarbustEffectCountParam_328;
      case 107 -> Scus94491BpeSegment_8006.battleState_8006e398._32c;
      case 108 -> Scus94491BpeSegment_8006.battleState_8006e398._330;

      case 111 -> Scus94491BpeSegment_8006.battleState_8006e398._334Offset;
      case 112 -> Scus94491BpeSegment_8006.battleState_8006e398._340;
      case 113 -> Scus94491BpeSegment_8006.battleState_8006e398._344;
      case 114 -> Scus94491BpeSegment_8006.battleState_8006e398._348;

      case 117 -> Scus94491BpeSegment_8006.battleState_8006e398._34cOffset;
      case 118 -> Scus94491BpeSegment_8006.battleState_8006e398.sequenceVolume_358;
      case 119 -> Scus94491BpeSegment_8006.battleState_8006e398._35c;
      case 120 -> Scus94491BpeSegment_8006.battleState_8006e398._360;
      case 121 -> Scus94491BpeSegment_8006.battleState_8006e398._364;
      case 122 -> Scus94491BpeSegment_8006.battleState_8006e398._368;
      case 123 -> Scus94491BpeSegment_8006.battleState_8006e398._36c;
      case 124 -> Scus94491BpeSegment_8006.battleState_8006e398._370;
      case 125 -> Scus94491BpeSegment_8006.battleState_8006e398._374;
      case 126 -> Scus94491BpeSegment_8006.battleState_8006e398._378;
      case 127 -> Scus94491BpeSegment_8006.battleState_8006e398._37c;
      case 128 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptEffectTableJumpIndex_380;

      case 136 -> Scus94491BpeSegment_8006.battleState_8006e398._384Offset;
      case 137 -> Scus94491BpeSegment_8006.battleState_8006e398._3a4;
      case 138 -> Scus94491BpeSegment_8006.battleState_8006e398._3a8;
      case 139 -> Scus94491BpeSegment_8006.battleState_8006e398._3ac;
      case 140 -> Scus94491BpeSegment_8006.battleState_8006e398._3b0;
      case 141 -> Scus94491BpeSegment_8006.battleState_8006e398._3b4;
      case 142 -> Scus94491BpeSegment_8006.battleState_8006e398._3b8;
      case 143 -> Scus94491BpeSegment_8006.battleState_8006e398._3bc;
      case 144 -> Scus94491BpeSegment_8006.battleState_8006e398._3c0;
      case 145 -> Scus94491BpeSegment_8006.battleState_8006e398._3c4;
      case 146 -> Scus94491BpeSegment_8006.battleState_8006e398._3c8;
      case 147 -> Scus94491BpeSegment_8006.battleState_8006e398.magicItemDamageMultiplier_3cc;
      case 148 -> Scus94491BpeSegment_8006.battleState_8006e398._3d0;
      case 149 -> Scus94491BpeSegment_8006.battleState_8006e398._3d4;
      case 150 -> Scus94491BpeSegment_8006.battleState_8006e398._3d8;
      case 151 -> Scus94491BpeSegment_8006.battleState_8006e398._3dc;
      case 152 -> Scus94491BpeSegment_8006.battleState_8006e398._3e0;
      case 153 -> Scus94491BpeSegment_8006.battleState_8006e398._3e4;
      case 154 -> Scus94491BpeSegment_8006.battleState_8006e398._3e8;
      case 155 -> Scus94491BpeSegment_8006.battleState_8006e398._3ec;
      case 156 -> Scus94491BpeSegment_8006.battleState_8006e398._3f0;
      case 157 -> Scus94491BpeSegment_8006.battleState_8006e398._3f4;
      case 158 -> Scus94491BpeSegment_8006.battleState_8006e398._3f8;
      case 159 -> Scus94491BpeSegment_8006.battleState_8006e398._3fc;
      case 160 -> Scus94491BpeSegment_8006.battleState_8006e398._400;
      case 161 -> Scus94491BpeSegment_8006.battleState_8006e398._404;
      case 162 -> Scus94491BpeSegment_8006.battleState_8006e398._408;
      case 163 -> Scus94491BpeSegment_8006.battleState_8006e398._40c;
      case 164 -> Scus94491BpeSegment_8006.battleState_8006e398._410;
      case 165 -> Scus94491BpeSegment_8006.battleState_8006e398._414;
      case 166 -> Scus94491BpeSegment_8006.battleState_8006e398._418;
      case 167 -> Scus94491BpeSegment_8006.battleState_8006e398._41c;
      case 168 -> Scus94491BpeSegment_8006.battleState_8006e398._420;
      case 169 -> Scus94491BpeSegment_8006.battleState_8006e398._424;
      case 170 -> Scus94491BpeSegment_8006.battleState_8006e398._428;
      case 171 -> Scus94491BpeSegment_8006.battleState_8006e398._42c;
      case 172 -> Scus94491BpeSegment_8006.battleState_8006e398._430;
      case 173 -> Scus94491BpeSegment_8006.battleState_8006e398._434;
      case 174 -> Scus94491BpeSegment_8006.battleState_8006e398._438;
      case 175 -> Scus94491BpeSegment_8006.battleState_8006e398._43c;
      case 176 -> Scus94491BpeSegment_8006.battleState_8006e398._440;
      case 177 -> Scus94491BpeSegment_8006.battleState_8006e398._444;
      case 178 -> Scus94491BpeSegment_8006.battleState_8006e398._448;
      case 179 -> Scus94491BpeSegment_8006.battleState_8006e398._44c;
      case 180 -> Scus94491BpeSegment_8006.battleState_8006e398._450;
      case 181 -> Scus94491BpeSegment_8006.battleState_8006e398._454;
      case 182 -> Scus94491BpeSegment_8006.battleState_8006e398._458;
      case 183 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptsProcessingStatusAfflictions_45c;

      case 186 -> Scus94491BpeSegment_8006.battleState_8006e398._460Offset;
      case 187 -> Scus94491BpeSegment_8006.battleState_8006e398._46c;
      case 188 -> Scus94491BpeSegment_8006.battleState_8006e398._470;

      case 190 -> Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294.length; //max players
      case 191 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384.length-Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294.length; //max monsters
      
      case 196 -> Scus94491BpeSegment_8006.battleState_8006e398._474Offset;
      case 197 -> Scus94491BpeSegment_8006.battleState_8006e398._494;
      case 198 -> Scus94491BpeSegment_8006.battleState_8006e398._498;
      case 199 -> Scus94491BpeSegment_8006.battleState_8006e398._49c;
      case 200 -> Scus94491BpeSegment_8006.battleState_8006e398._4a0;
      case 201 -> Scus94491BpeSegment_8006.battleState_8006e398._4a4;
      case 202 -> Scus94491BpeSegment_8006.battleState_8006e398.magicItemLevel_4a8;
      case 203 -> Scus94491BpeSegment_8006.battleState_8006e398._4ac;
      case 204 -> Scus94491BpeSegment_8006.battleState_8006e398._4b0;
      case 205 -> Scus94491BpeSegment_8006.battleState_8006e398._4b4;
      case 206 -> Scus94491BpeSegment_8006.battleState_8006e398._4b8;
      case 207 -> Scus94491BpeSegment_8006.battleState_8006e398._4bc;
      case 208 -> Scus94491BpeSegment_8006.battleState_8006e398._4c0;
      case 209 -> Scus94491BpeSegment_8006.battleState_8006e398._4c4;
      case 210 -> Scus94491BpeSegment_8006.battleState_8006e398._4c8;
      case 211 -> Scus94491BpeSegment_8006.battleState_8006e398._4cc;
      case 212 -> Scus94491BpeSegment_8006.battleState_8006e398._4d0;
      case 213 -> Scus94491BpeSegment_8006.battleState_8006e398._4d4;
      case 214 -> Scus94491BpeSegment_8006.battleState_8006e398._4d8;
      case 215 -> Scus94491BpeSegment_8006.battleState_8006e398._4dc;
      case 216 -> Scus94491BpeSegment_8006.battleState_8006e398._4e0;
      case 217 -> Scus94491BpeSegment_8006.battleState_8006e398._4e4;
      case 218 -> Scus94491BpeSegment_8006.battleState_8006e398._4e8;
      case 219 -> Scus94491BpeSegment_8006.battleState_8006e398._4ec;
      case 220 -> Scus94491BpeSegment_8006.battleState_8006e398._4f0;
      case 221 -> Scus94491BpeSegment_8006.battleState_8006e398._4f4;
      case 222 -> Scus94491BpeSegment_8006.battleState_8006e398._4f8;
      case 223 -> Scus94491BpeSegment_8006.battleState_8006e398._4fc;
      case 224 -> Scus94491BpeSegment_8006.battleState_8006e398.damageDealt_500;
      case 225 -> Scus94491BpeSegment_8006.battleState_8006e398._504;
      case 226 -> Scus94491BpeSegment_8006.battleState_8006e398._508;
      case 227 -> Scus94491BpeSegment_8006.battleState_8006e398._50c;
      case 228 -> Scus94491BpeSegment_8006.battleState_8006e398.globalMenuBlocks_510;
      case 229 -> Scus94491BpeSegment_8006.battleState_8006e398._514;
      case 230 -> Scus94491BpeSegment_8006.battleState_8006e398._518;
      case 231 -> Scus94491BpeSegment_8006.battleState_8006e398._51c;
      case 232 -> Scus94491BpeSegment_8006.battleState_8006e398._520;
      case 233 -> Scus94491BpeSegment_8006.battleState_8006e398._524;
      case 234 -> Scus94491BpeSegment_8006.battleState_8006e398._528;
      case 235 -> Scus94491BpeSegment_8006.battleState_8006e398._52c;
      case 236 -> Scus94491BpeSegment_8006.battleState_8006e398._530;
      case 237 -> Scus94491BpeSegment_8006.battleState_8006e398._534;
      case 238 -> Scus94491BpeSegment_8006.battleState_8006e398._538;
      case 239 -> Scus94491BpeSegment_8006.battleState_8006e398._53c;
      case 240 -> Scus94491BpeSegment_8006.battleState_8006e398._540;
      case 241 -> Scus94491BpeSegment_8006.battleState_8006e398._544;
      case 242 -> Scus94491BpeSegment_8006.battleState_8006e398._548;
      case 243 -> Scus94491BpeSegment_8006.battleState_8006e398._54c;
      case 244 -> Scus94491BpeSegment_8006.battleState_8006e398.fieldFlags_550;
      case 245 -> Scus94491BpeSegment_8006.battleState_8006e398._554;
      case 246 -> Scus94491BpeSegment_8006.battleState_8006e398.attackTargets_558;
      case 247 -> Scus94491BpeSegment_8006.battleState_8006e398._55c;
      case 248 -> Scus94491BpeSegment_8006.battleState_8006e398._560;
      case 249 -> Scus94491BpeSegment_8006.battleState_8006e398._564;
      case 250 -> Scus94491BpeSegment_8006.battleState_8006e398._568;
      case 251 -> Scus94491BpeSegment_8006.battleState_8006e398._56c;
      case 252 -> Scus94491BpeSegment_8006.battleState_8006e398.monsterMoveId_570;
      case 253 -> Scus94491BpeSegment_8006.battleState_8006e398._574;
      case 254 -> Scus94491BpeSegment_8006.battleState_8006e398._578;
      case 255 -> Scus94491BpeSegment_8006.battleState_8006e398._57c;
      

      default -> throw new IllegalArgumentException("Unknown combat var index " + index);
    };
  }

  private void writeCombatVar(final int index, final int val) {
    int indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._294Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294.length))){
      Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[index - indexOffset] = val;
      return;
    }
    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._2e8Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._2e8.length))){
      Scus94491BpeSegment_8006.battleState_8006e398._2e8[index - indexOffset] = val;
      return;
    }
    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._334Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._334.length))){
      Scus94491BpeSegment_8006.battleState_8006e398._334[index - indexOffset] = val;
      return;
    }
    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._34cOffset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._34c.length))){
      Scus94491BpeSegment_8006.battleState_8006e398._34c[index - indexOffset] = val;
      return;
    }
    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._384Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.status_384.length))){
      Scus94491BpeSegment_8006.battleState_8006e398.status_384[index - indexOffset].unpack(val);
      return;
    }
    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._460Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398._460.length))){
      Scus94491BpeSegment_8006.battleState_8006e398._460[index - indexOffset] = val;
      return;
    }

    indexOffset = Scus94491BpeSegment_8006.battleState_8006e398._474Offset;
    if((index>=indexOffset) && (index<(indexOffset+Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474.length))){
      Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[index - indexOffset].unpack(val);
      return;
    }
    switch(index) {
      case 0 -> Scus94491BpeSegment_8006.battleState_8006e398._180 = val;
      case 1 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_184 = val;
      case 2 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_188 = val;
      case 3 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_18c = val;
      case 4 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_190 = val;
      case 5 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_194 = val;
      case 6 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_198 = val;
      case 7 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_19c = val;
      case 8 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a0 = val;
      case 9 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a4 = val;
      case 10 -> Scus94491BpeSegment_8006.battleState_8006e398.combatantBentIndex_1a8 = val;
      case 11 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1ac = val;
      case 12 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b0 = val;
      case 13 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b4 = val;
      case 14 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1b8 = val;
      case 15 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1bc = val;
      case 16 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c0 = val;
      case 17 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c4 = val;
      case 18 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1c8 = val;
      case 19 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1cc = val;
      case 20 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptIndex_1d0 = val;
      case 21 -> Scus94491BpeSegment_8006.battleState_8006e398._1d4 = val;
      case 22 -> Scus94491BpeSegment_8006.battleState_8006e398._1d8 = val;
      case 23 -> Scus94491BpeSegment_8006.battleState_8006e398._1dc = val;
      case 24 -> Scus94491BpeSegment_8006.battleState_8006e398._1e0 = val;
      case 25 -> Scus94491BpeSegment_8006.battleState_8006e398._1e4 = val;
      case 26 -> Scus94491BpeSegment_8006.battleState_8006e398._1e8 = val;
      case 27 -> Scus94491BpeSegment_8006.battleState_8006e398._1ec = val;
      case 28 -> Scus94491BpeSegment_8006.battleState_8006e398._1f0 = val;
      case 29 -> Scus94491BpeSegment_8006.battleState_8006e398._1f4 = val;
      case 30 -> Scus94491BpeSegment_8006.battleState_8006e398._1f8 = val;
      case 31 -> Scus94491BpeSegment_8006.battleState_8006e398._1fc = val;
      case 32 -> Scus94491BpeSegment_8006.battleState_8006e398._200 = val;
      case 33 -> Scus94491BpeSegment_8006.battleState_8006e398._204 = val;
      case 34 -> Scus94491BpeSegment_8006.battleState_8006e398._208 = val;
      case 35 -> Scus94491BpeSegment_8006.battleState_8006e398._20c = val;
      case 36 -> Scus94491BpeSegment_8006.battleState_8006e398._210 = val;
      case 37 -> Scus94491BpeSegment_8006.battleState_8006e398._214 = val;
      case 38 -> Scus94491BpeSegment_8006.battleState_8006e398._218 = val;
      case 39 -> Scus94491BpeSegment_8006.battleState_8006e398._21c = val;
      case 40 -> Scus94491BpeSegment_8006.battleState_8006e398._220 = val;
      case 41 -> Scus94491BpeSegment_8006.battleState_8006e398._224 = val;
      case 42 -> Scus94491BpeSegment_8006.battleState_8006e398._228 = val;
      case 43 -> Scus94491BpeSegment_8006.battleState_8006e398._22c = val;
      case 44 -> Scus94491BpeSegment_8006.battleState_8006e398._230 = val;
      case 45 -> Scus94491BpeSegment_8006.battleState_8006e398._234 = val;
      case 46 -> Scus94491BpeSegment_8006.battleState_8006e398._238 = val;
      case 47 -> Scus94491BpeSegment_8006.battleState_8006e398._23c = val;
      case 48 -> Scus94491BpeSegment_8006.battleState_8006e398._240 = val;
      case 49 -> Scus94491BpeSegment_8006.battleState_8006e398._244 = val;
      case 50 -> Scus94491BpeSegment_8006.battleState_8006e398._248 = val;
      case 51 -> Scus94491BpeSegment_8006.battleState_8006e398._24c = val;
      case 52 -> Scus94491BpeSegment_8006.battleState_8006e398._250 = val;
      case 53 -> Scus94491BpeSegment_8006.battleState_8006e398._254 = val;
      case 54 -> Scus94491BpeSegment_8006.battleState_8006e398._258 = val;
      case 55 -> Scus94491BpeSegment_8006.battleState_8006e398._25c = val;
      case 56 -> Scus94491BpeSegment_8006.battleState_8006e398._260 = val;
      case 57 -> Scus94491BpeSegment_8006.battleState_8006e398._264 = val;
      case 58 -> Scus94491BpeSegment_8006.battleState_8006e398._268 = val;
      case 59 -> Scus94491BpeSegment_8006.battleState_8006e398._26c = val;
      case 60 -> Scus94491BpeSegment_8006.battleState_8006e398._270 = val;
      case 61 -> Scus94491BpeSegment_8006.battleState_8006e398._274 = val;
      case 62 -> Scus94491BpeSegment_8006.battleState_8006e398._278 = val;
      case 63 -> Scus94491BpeSegment_8006.battleState_8006e398._27c = val;
      case 64 -> Scus94491BpeSegment_8006.battleState_8006e398.numCompleteAdditionHits_280 = val;
      case 65 -> Scus94491BpeSegment_8006.battleState_8006e398._284 = val;
      case 66 -> Scus94491BpeSegment_8006.battleState_8006e398.counterAttackStage_288 = val;
      case 67 -> Scus94491BpeSegment_8006.battleState_8006e398._28c = val;
      case 68 -> Scus94491BpeSegment_8006.battleState_8006e398._290 = val;
      /*case 69 -> Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[0] = val;
      case 70 -> Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[1] = val;
      case 71 -> Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[2] = val;*/
      case 72 -> Scus94491BpeSegment_8006.battleState_8006e398._2a0 = val;
      case 73 -> Scus94491BpeSegment_8006.battleState_8006e398._2a4 = val;
      case 74 -> Scus94491BpeSegment_8006.battleState_8006e398._2a8 = val;
      case 75 -> Scus94491BpeSegment_8006.battleState_8006e398._2ac = val;
      case 76 -> Scus94491BpeSegment_8006.battleState_8006e398.specialFlag_2b0 = val;
      case 77 -> Scus94491BpeSegment_8006.battleState_8006e398.runAwayFlags_2b4 = val;
      case 78 -> Scus94491BpeSegment_8006.battleState_8006e398._2b8 = val;
      case 79 -> Scus94491BpeSegment_8006.battleState_8006e398._2bc = val;
      case 80 -> Scus94491BpeSegment_8006.battleState_8006e398._2c0 = val;
      case 81 -> Scus94491BpeSegment_8006.battleState_8006e398._2c4 = val;
      case 82 -> Scus94491BpeSegment_8006.battleState_8006e398._2c8 = val;
      case 83 -> Scus94491BpeSegment_8006.battleState_8006e398._2cc = val;
      case 84 -> Scus94491BpeSegment_8006.battleState_8006e398._2d0 = val;
      case 85 -> Scus94491BpeSegment_8006.battleState_8006e398._2d4 = val;
      case 86 -> Scus94491BpeSegment_8006.battleState_8006e398._2d8 = val;
      case 87 -> Scus94491BpeSegment_8006.battleState_8006e398._2dc = val;
      case 88 -> Scus94491BpeSegment_8006.battleState_8006e398._2e0 = val;
      case 89 -> Scus94491BpeSegment_8006.battleState_8006e398._2e4 = val;
      /*case 90 -> Scus94491BpeSegment_8006.battleState_8006e398._2e8[0] = val;
      case 91 -> Scus94491BpeSegment_8006.battleState_8006e398._2e8[1] = val;
      case 92 -> Scus94491BpeSegment_8006.battleState_8006e398._2e8[2] = val;*/
      case 93 -> Scus94491BpeSegment_8006.battleState_8006e398._2f4 = val;
      case 94 -> Scus94491BpeSegment_8006.battleState_8006e398._2f8 = val;
      case 95 -> Scus94491BpeSegment_8006.battleState_8006e398._2fc = val;
      case 96 -> Scus94491BpeSegment_8006.battleState_8006e398._300 = val;
      case 97 -> Scus94491BpeSegment_8006.battleState_8006e398._304 = val;
      case 98 -> Scus94491BpeSegment_8006.battleState_8006e398._308 = val;
      case 99 -> Scus94491BpeSegment_8006.battleState_8006e398._30c = val;
      case 100 -> Scus94491BpeSegment_8006.battleState_8006e398._310 = val;
      case 101 -> Scus94491BpeSegment_8006.battleState_8006e398._314 = val;
      case 102 -> Scus94491BpeSegment_8006.battleState_8006e398._318 = val;
      case 103 -> Scus94491BpeSegment_8006.battleState_8006e398._31c = val;
      case 104 -> Scus94491BpeSegment_8006.battleState_8006e398._320 = val;
      case 105 -> Scus94491BpeSegment_8006.battleState_8006e398.additionState_324 = val;
      case 106 -> Scus94491BpeSegment_8006.battleState_8006e398.additionStarbustEffectCountParam_328 = val;
      case 107 -> Scus94491BpeSegment_8006.battleState_8006e398._32c = val;
      case 108 -> Scus94491BpeSegment_8006.battleState_8006e398._330 = val;
      /*case 109 -> Scus94491BpeSegment_8006.battleState_8006e398._334[0] = val;
      case 110 -> Scus94491BpeSegment_8006.battleState_8006e398._334[1] = val;
      case 111 -> Scus94491BpeSegment_8006.battleState_8006e398._334[2] = val;*/
      case 112 -> Scus94491BpeSegment_8006.battleState_8006e398._340 = val;
      case 113 -> Scus94491BpeSegment_8006.battleState_8006e398._344 = val;
      case 114 -> Scus94491BpeSegment_8006.battleState_8006e398._348 = val;
      /*case 115 -> Scus94491BpeSegment_8006.battleState_8006e398._34c[0] = val;
      case 116 -> Scus94491BpeSegment_8006.battleState_8006e398._34c[1] = val;
      case 117 -> Scus94491BpeSegment_8006.battleState_8006e398._34c[2] = val;*/
      case 118 -> Scus94491BpeSegment_8006.battleState_8006e398.sequenceVolume_358 = val;
      case 119 -> Scus94491BpeSegment_8006.battleState_8006e398._35c = val;
      case 120 -> Scus94491BpeSegment_8006.battleState_8006e398._360 = val;
      case 121 -> Scus94491BpeSegment_8006.battleState_8006e398._364 = val;
      case 122 -> Scus94491BpeSegment_8006.battleState_8006e398._368 = val;
      case 123 -> Scus94491BpeSegment_8006.battleState_8006e398._36c = val;
      case 124 -> Scus94491BpeSegment_8006.battleState_8006e398._370 = val;
      case 125 -> Scus94491BpeSegment_8006.battleState_8006e398._374 = val;
      case 126 -> Scus94491BpeSegment_8006.battleState_8006e398._378 = val;
      case 127 -> Scus94491BpeSegment_8006.battleState_8006e398._37c = val;
      case 128 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptEffectTableJumpIndex_380 = val;
      /*case 129 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[0].unpack(val);
      case 130 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[1].unpack(val);
      case 131 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[2].unpack(val);
      case 132 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[3].unpack(val);
      case 133 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[4].unpack(val);
      case 134 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[5].unpack(val);
      case 135 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[6].unpack(val);
      case 136 -> Scus94491BpeSegment_8006.battleState_8006e398.status_384[7].unpack(val);*/
      case 137 -> Scus94491BpeSegment_8006.battleState_8006e398._3a4 = val;
      case 138 -> Scus94491BpeSegment_8006.battleState_8006e398._3a8 = val;
      case 139 -> Scus94491BpeSegment_8006.battleState_8006e398._3ac = val;
      case 140 -> Scus94491BpeSegment_8006.battleState_8006e398._3b0 = val;
      case 141 -> Scus94491BpeSegment_8006.battleState_8006e398._3b4 = val;
      case 142 -> Scus94491BpeSegment_8006.battleState_8006e398._3b8 = val;
      case 143 -> Scus94491BpeSegment_8006.battleState_8006e398._3bc = val;
      case 144 -> Scus94491BpeSegment_8006.battleState_8006e398._3c0 = val;
      case 145 -> Scus94491BpeSegment_8006.battleState_8006e398._3c4 = val;
      case 146 -> Scus94491BpeSegment_8006.battleState_8006e398._3c8 = val;
      case 147 -> Scus94491BpeSegment_8006.battleState_8006e398.magicItemDamageMultiplier_3cc = val;
      case 148 -> Scus94491BpeSegment_8006.battleState_8006e398._3d0 = val;
      case 149 -> Scus94491BpeSegment_8006.battleState_8006e398._3d4 = val;
      case 150 -> Scus94491BpeSegment_8006.battleState_8006e398._3d8 = val;
      case 151 -> Scus94491BpeSegment_8006.battleState_8006e398._3dc = val;
      case 152 -> Scus94491BpeSegment_8006.battleState_8006e398._3e0 = val;
      case 153 -> Scus94491BpeSegment_8006.battleState_8006e398._3e4 = val;
      case 154 -> Scus94491BpeSegment_8006.battleState_8006e398._3e8 = val;
      case 155 -> Scus94491BpeSegment_8006.battleState_8006e398._3ec = val;
      case 156 -> Scus94491BpeSegment_8006.battleState_8006e398._3f0 = val;
      case 157 -> Scus94491BpeSegment_8006.battleState_8006e398._3f4 = val;
      case 158 -> Scus94491BpeSegment_8006.battleState_8006e398._3f8 = val;
      case 159 -> Scus94491BpeSegment_8006.battleState_8006e398._3fc = val;
      case 160 -> Scus94491BpeSegment_8006.battleState_8006e398._400 = val;
      case 161 -> Scus94491BpeSegment_8006.battleState_8006e398._404 = val;
      case 162 -> Scus94491BpeSegment_8006.battleState_8006e398._408 = val;
      case 163 -> Scus94491BpeSegment_8006.battleState_8006e398._40c = val;
      case 164 -> Scus94491BpeSegment_8006.battleState_8006e398._410 = val;
      case 165 -> Scus94491BpeSegment_8006.battleState_8006e398._414 = val;
      case 166 -> Scus94491BpeSegment_8006.battleState_8006e398._418 = val;
      case 167 -> Scus94491BpeSegment_8006.battleState_8006e398._41c = val;
      case 168 -> Scus94491BpeSegment_8006.battleState_8006e398._420 = val;
      case 169 -> Scus94491BpeSegment_8006.battleState_8006e398._424 = val;
      case 170 -> Scus94491BpeSegment_8006.battleState_8006e398._428 = val;
      case 171 -> Scus94491BpeSegment_8006.battleState_8006e398._42c = val;
      case 172 -> Scus94491BpeSegment_8006.battleState_8006e398._430 = val;
      case 173 -> Scus94491BpeSegment_8006.battleState_8006e398._434 = val;
      case 174 -> Scus94491BpeSegment_8006.battleState_8006e398._438 = val;
      case 175 -> Scus94491BpeSegment_8006.battleState_8006e398._43c = val;
      case 176 -> Scus94491BpeSegment_8006.battleState_8006e398._440 = val;
      case 177 -> Scus94491BpeSegment_8006.battleState_8006e398._444 = val;
      case 178 -> Scus94491BpeSegment_8006.battleState_8006e398._448 = val;
      case 179 -> Scus94491BpeSegment_8006.battleState_8006e398._44c = val;
      case 180 -> Scus94491BpeSegment_8006.battleState_8006e398._450 = val;
      case 181 -> Scus94491BpeSegment_8006.battleState_8006e398._454 = val;
      case 182 -> Scus94491BpeSegment_8006.battleState_8006e398._458 = val;
      case 183 -> Scus94491BpeSegment_8006.battleState_8006e398.scriptsProcessingStatusAfflictions_45c = val;
      /*case 184 -> Scus94491BpeSegment_8006.battleState_8006e398._460[0] = val;
      case 185 -> Scus94491BpeSegment_8006.battleState_8006e398._460[1] = val;
      case 186 -> Scus94491BpeSegment_8006.battleState_8006e398._460[2] = val;*/
      case 187 -> Scus94491BpeSegment_8006.battleState_8006e398._46c = val;
      case 188 -> Scus94491BpeSegment_8006.battleState_8006e398._470 = val;
      /*case 189 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[0].unpack(val);
      case 190 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[1].unpack(val);
      case 191 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[2].unpack(val);
      case 192 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[3].unpack(val);
      case 193 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[4].unpack(val);
      case 194 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[5].unpack(val);
      case 195 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[6].unpack(val);
      case 196 -> Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[7].unpack(val);*/
      case 197 -> Scus94491BpeSegment_8006.battleState_8006e398._494 = val;
      case 198 -> Scus94491BpeSegment_8006.battleState_8006e398._498 = val;
      case 199 -> Scus94491BpeSegment_8006.battleState_8006e398._49c = val;
      case 200 -> Scus94491BpeSegment_8006.battleState_8006e398._4a0 = val;
      case 201 -> Scus94491BpeSegment_8006.battleState_8006e398._4a4 = val;
      case 202 -> Scus94491BpeSegment_8006.battleState_8006e398.magicItemLevel_4a8 = val;
      case 203 -> Scus94491BpeSegment_8006.battleState_8006e398._4ac = val;
      case 204 -> Scus94491BpeSegment_8006.battleState_8006e398._4b0 = val;
      case 205 -> Scus94491BpeSegment_8006.battleState_8006e398._4b4 = val;
      case 206 -> Scus94491BpeSegment_8006.battleState_8006e398._4b8 = val;
      case 207 -> Scus94491BpeSegment_8006.battleState_8006e398._4bc = val;
      case 208 -> Scus94491BpeSegment_8006.battleState_8006e398._4c0 = val;
      case 209 -> Scus94491BpeSegment_8006.battleState_8006e398._4c4 = val;
      case 210 -> Scus94491BpeSegment_8006.battleState_8006e398._4c8 = val;
      case 211 -> Scus94491BpeSegment_8006.battleState_8006e398._4cc = val;
      case 212 -> Scus94491BpeSegment_8006.battleState_8006e398._4d0 = val;
      case 213 -> Scus94491BpeSegment_8006.battleState_8006e398._4d4 = val;
      case 214 -> Scus94491BpeSegment_8006.battleState_8006e398._4d8 = val;
      case 215 -> Scus94491BpeSegment_8006.battleState_8006e398._4dc = val;
      case 216 -> Scus94491BpeSegment_8006.battleState_8006e398._4e0 = val;
      case 217 -> Scus94491BpeSegment_8006.battleState_8006e398._4e4 = val;
      case 218 -> Scus94491BpeSegment_8006.battleState_8006e398._4e8 = val;
      case 219 -> Scus94491BpeSegment_8006.battleState_8006e398._4ec = val;
      case 220 -> Scus94491BpeSegment_8006.battleState_8006e398._4f0 = val;
      case 221 -> Scus94491BpeSegment_8006.battleState_8006e398._4f4 = val;
      case 222 -> Scus94491BpeSegment_8006.battleState_8006e398._4f8 = val;
      case 223 -> Scus94491BpeSegment_8006.battleState_8006e398._4fc = val;
      case 224 -> Scus94491BpeSegment_8006.battleState_8006e398.damageDealt_500 = val;
      case 225 -> Scus94491BpeSegment_8006.battleState_8006e398._504 = val;
      case 226 -> Scus94491BpeSegment_8006.battleState_8006e398._508 = val;
      case 227 -> Scus94491BpeSegment_8006.battleState_8006e398._50c = val;
      case 228 -> Scus94491BpeSegment_8006.battleState_8006e398.globalMenuBlocks_510 = val;
      case 229 -> Scus94491BpeSegment_8006.battleState_8006e398._514 = val;
      case 230 -> Scus94491BpeSegment_8006.battleState_8006e398._518 = val;
      case 231 -> Scus94491BpeSegment_8006.battleState_8006e398._51c = val;
      case 232 -> Scus94491BpeSegment_8006.battleState_8006e398._520 = val;
      case 233 -> Scus94491BpeSegment_8006.battleState_8006e398._524 = val;
      case 234 -> Scus94491BpeSegment_8006.battleState_8006e398._528 = val;
      case 235 -> Scus94491BpeSegment_8006.battleState_8006e398._52c = val;
      case 236 -> Scus94491BpeSegment_8006.battleState_8006e398._530 = val;
      case 237 -> Scus94491BpeSegment_8006.battleState_8006e398._534 = val;
      case 238 -> Scus94491BpeSegment_8006.battleState_8006e398._538 = val;
      case 239 -> Scus94491BpeSegment_8006.battleState_8006e398._53c = val;
      case 240 -> Scus94491BpeSegment_8006.battleState_8006e398._540 = val;
      case 241 -> Scus94491BpeSegment_8006.battleState_8006e398._544 = val;
      case 242 -> Scus94491BpeSegment_8006.battleState_8006e398._548 = val;
      case 243 -> Scus94491BpeSegment_8006.battleState_8006e398._54c = val;
      case 244 -> {
        Scus94491BpeSegment_8006.battleState_8006e398.fieldFlags_550 = val;
        if (val == 1) {
          EVENTS.postEvent(new DragonBlockStaffOnEvent());
        } else {
          EVENTS.postEvent(new DragonBlockStaffOffEvent());
        }
      }
      case 245 -> Scus94491BpeSegment_8006.battleState_8006e398._554 = val;
      case 246 -> Scus94491BpeSegment_8006.battleState_8006e398.attackTargets_558 = val;
      case 247 -> Scus94491BpeSegment_8006.battleState_8006e398._55c = val;
      case 248 -> Scus94491BpeSegment_8006.battleState_8006e398._560 = val;
      case 249 -> Scus94491BpeSegment_8006.battleState_8006e398._564 = val;
      case 250 -> Scus94491BpeSegment_8006.battleState_8006e398._568 = val;
      case 251 -> Scus94491BpeSegment_8006.battleState_8006e398._56c = val;
      case 252 -> Scus94491BpeSegment_8006.battleState_8006e398.monsterMoveId_570 = val;
      case 253 -> Scus94491BpeSegment_8006.battleState_8006e398._574 = val;
      case 254 -> Scus94491BpeSegment_8006.battleState_8006e398._578 = val;
      case 255 -> Scus94491BpeSegment_8006.battleState_8006e398._57c = val;

      default -> throw new IllegalArgumentException("Unknown combat var index " + index);
    }
  }

  private int readStatusConditionsVar(final int effectIndex, final int varIndex) {
    final StatusConditions20 conditions = Scus94491BpeSegment_8006.battleState_8006e398.statusConditions_00[effectIndex];

    return switch(varIndex) {
      case 0 -> conditions._00;
      case 1 -> conditions._04;
      case 2 -> conditions._08;
      case 3 -> conditions._0c;
      case 4 -> conditions._10;
      case 5 -> conditions._14;
      case 6 -> conditions.menuBlockFlag_18;
      case 7 -> (conditions.stolenItem_1f & 0xff) << 24 | (conditions.chargingSpirit_1e & 0xff) << 16 | (conditions.pandemoniumTurnsDiedAsDragoon_1d & 0xff) << 8 | conditions.shieldsSigStoneCharmTurns_1c & 0xff;

      default -> throw new IllegalArgumentException("Unknown status condition var index " + varIndex);
    };
  }

  private void writeStatusConditionsVar(final int effectIndex, final int varIndex, final int val) {
    final StatusConditions20 conditions = Scus94491BpeSegment_8006.battleState_8006e398.statusConditions_00[effectIndex];

    switch(varIndex) {
      case 0 -> conditions._00 = val;
      case 1 -> conditions._04 = val;
      case 2 -> conditions._08 = val;
      case 3 -> conditions._0c = val;
      case 4 -> conditions._10 = val;
      case 5 -> conditions._14 = val; // Move ID? (0x20 is pre-move, other values are spell/item IDs)
      case 6 -> conditions.menuBlockFlag_18 = val;
      case 7 -> {
        conditions.shieldsSigStoneCharmTurns_1c = val & 0xff;
        conditions.pandemoniumTurnsDiedAsDragoon_1d = val >>> 8 & 0xff;
        conditions.chargingSpirit_1e = val >>> 16 & 0xff;
        conditions.stolenItem_1f = val >>> 24 & 0xff;
      }

      default -> throw new IllegalArgumentException("Unknown special effect var index " + varIndex);
    }
  }

  /*private int readDragoonTurnsRemainingVar(final int varIndex) {
    return Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[varIndex];
  };
  private void writeDragoonTurnsRemainingVar(final int varIndex, final int val) {
    Scus94491BpeSegment_8006.battleState_8006e398.dragoonTurnsRemaining_294[varIndex] = val;
  };
  private int read2e8Var(final int varIndex) {
    return Scus94491BpeSegment_8006.battleState_8006e398._2e8[varIndex];
  };
  private void write2e8Var(final int varIndex, final int val) {
    Scus94491BpeSegment_8006.battleState_8006e398._2e8[varIndex] = val;
  };
  private int read334Var(final int varIndex) {
    return Scus94491BpeSegment_8006.battleState_8006e398._334[varIndex];
  };
  private void write334Var(final int varIndex, final int val) {
    Scus94491BpeSegment_8006.battleState_8006e398._334[varIndex] = val;
  };
  private int read34cVar(final int varIndex) {
    return Scus94491BpeSegment_8006.battleState_8006e398._34c[varIndex];
  };
  private void write34cVar(final int varIndex, final int val) {
    Scus94491BpeSegment_8006.battleState_8006e398._34c[varIndex] = val;
  };
  private int readStatusVar(final int varIndex) { 
    return Scus94491BpeSegment_8006.battleState_8006e398.status_384[varIndex].pack();
  };
  private void writeStatusVar(final int varIndex, final int val) { 
    Scus94491BpeSegment_8006.battleState_8006e398.status_384[varIndex].unpack(val);
  };
  private int read460Var(final int varIndex) {
    return Scus94491BpeSegment_8006.battleState_8006e398._460[varIndex];
  };
  private void write460Var(final int varIndex, final int val) {
    Scus94491BpeSegment_8006.battleState_8006e398._460[varIndex] = val;
  };
  private int readAdditionExtraVar(final int varIndex) { 
    return Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[varIndex].pack();
  };
  private void writeAdditionExtraVar(final int varIndex, final int val) { 
    Scus94491BpeSegment_8006.battleState_8006e398.additionExtra_474[varIndex].unpack(val);
  };*/

}
