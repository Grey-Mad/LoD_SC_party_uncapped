package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.characters.Element;
import legend.game.characters.TurnBasedPercentileBuff;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.AttackEvent;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEntityStat;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.environment.BattleMenuBackgroundUvMetrics04;
import legend.game.combat.types.AttackType;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.ui.BattleDisplayStats144;
import legend.game.combat.ui.BattleDisplayStatsDigit10;
import legend.game.combat.ui.BattleHudCharacterDisplay3c;
import legend.game.combat.ui.BattleMenuStruct58;
import legend.game.combat.ui.CombatItem02;
import legend.game.combat.ui.FloatingNumberC4;
import legend.game.combat.ui.FloatingNumberDigit20;
import legend.game.combat.ui.SpellAndItemMenuA4;
import legend.game.combat.ui.UiBox;
import legend.game.inventory.Item;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.events.battle.BattleDescriptionEvent;
import legend.game.modding.events.battle.MonsterStatsEvent;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import legend.game.types.SpellStats0c;
import legend.game.types.Translucency;
import legend.lodmod.LodMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8004.itemStats_8004f2ac;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.characterStatsLoaded_800be5d0;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.combat.Bttl_800c.activePartyBattleHudCharacterDisplays_800c6c40;
import static legend.game.combat.Bttl_800c.aliveBentCount_800c669c;
import static legend.game.combat.Bttl_800c.aliveMonsterCount_800c6758;
import static legend.game.combat.Bttl_800c.battleMenu_800c6c34;
import static legend.game.combat.Bttl_800c.battleUiElementClutVramXy_800c7114;
import static legend.game.combat.Bttl_800c.charCount_800c677c;
import static legend.game.combat.Bttl_800c.combatItems_800c6988;
import static legend.game.combat.Bttl_800c.currentEnemyNames_800c69d0;
import static legend.game.combat.Bttl_800c.digitOffsetX_800c7014;
import static legend.game.combat.Bttl_800c.digitOffsetY_800c7014;
import static legend.game.combat.Bttl_800c.displayStats_800c6c2c;
import static legend.game.combat.Bttl_800c.dragoonSpaceElement_800c6b64;
import static legend.game.combat.Bttl_800c.floatingNumbers_800c6b5c;
import static legend.game.combat.Bttl_800c.floatingTextType1DigitUs_800c7028;
import static legend.game.combat.Bttl_800c.floatingTextType1Digits;
import static legend.game.combat.Bttl_800c.floatingTextType3DigitUs_800c70e0;
import static legend.game.combat.Bttl_800c.melbuMonsterNameIndices_800c6e90;
import static legend.game.combat.Bttl_800c.melbuMonsterNames_800c6ba8;
import static legend.game.combat.Bttl_800c.melbuStageToMonsterNameIndices_800c6f30;
import static legend.game.combat.Bttl_800c.monsterBents_800c6b78;
import static legend.game.combat.Bttl_800c.monsterCount_800c6b9c;
import static legend.game.combat.Bttl_800c.protectedItems_800c72cc;
import static legend.game.combat.Bttl_800c.spellAndItemMenu_800c6b60;
import static legend.game.combat.Bttl_800c.spellStats_800fa0b8;
import static legend.game.combat.Bttl_800c.targetAllItemIds_800c7124;
import static legend.game.combat.Bttl_800c.textboxColours_800c6fec;
import static legend.game.combat.Bttl_800e.initializeBattleHudCharacterDisplay;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;
import static legend.game.combat.Monsters.monsterNames_80112068;
import static legend.game.combat.Monsters.monsterStats_8010ba98;

public final class Bttl_800f {
  private Bttl_800f() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Bttl_800f.class);

  public static void deleteFloatingTextDigits() {
    for(int i = 0; i < floatingTextType1Digits.length; i++) {
      if(floatingTextType1Digits[i] != null) {
        floatingTextType1Digits[i].delete();
        floatingTextType1Digits[i] = null;
      }
    }

    for(int i = 0; i < 10; i++) {
      if(type1FloatingDigits[i] != null) {
        type1FloatingDigits[i].delete();
        type1FloatingDigits[i] = null;
      }

      if(type3FloatingDigits[i] != null) {
        type3FloatingDigits[i].delete();
        type3FloatingDigits[i] = null;
      }

      if(miss != null) {
        miss.delete();
        miss = null;
      }
    }
  }

  /**
   * @param numberIndex which UI element it is, controls positioning
   */
  @Method(0x800f1550L)
  public static void renderNumber(final int charSlot, final int numberIndex, int value, final int colour) {
    if(floatingTextType1Digits[0] == null) {
      for(int i = 0; i < 10; i++) {
        floatingTextType1Digits[i] = buildUiTextureElement(
          "Floating text type 1 digit " + i,
          floatingTextType1DigitUs_800c7028[i], 32,
          8, 8,
          0x80
        );
      }
    }

    final int digitCount = MathHelper.digitCount(value);

    //LAB_800f16e4
    final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];

    final int[] digits = new int[digitCount];

    for(int i = 0; i < displayStats.digits_04[numberIndex].length; i++) {
      displayStats.digits_04[numberIndex][i].digitValue_00 = -1;
    }

    //LAB_800f171c
    Arrays.fill(digits, -1);

    int divisor = 1;

    //LAB_800f1768
    for(int i = 0; i < digitCount - 1; i++) {
      divisor *= 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < digitCount; i++) {
      digits[i] = value / divisor;
      value %= divisor;
      divisor /= 10;
    }

    //LAB_800f1800
    //LAB_800f1828
    final int rightAlignOffset = 4 - digitCount;

    //LAB_800f1848
    //LAB_800f184c
    //LAB_800f18cc
    for(int i = 0; i < digitCount; i++) {
      final BattleDisplayStatsDigit10 digit = displayStats.digits_04[numberIndex][i];

      if(numberIndex == 1 || numberIndex == 3 || numberIndex == 4) {
        //LAB_800f18f0
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + i * 5;
      } else {
        digit.x_02 = digitOffsetX_800c7014[numberIndex] + (i + rightAlignOffset) * 5;
      }

      //LAB_800f1920
      digit.y_04 = digitOffsetY_800c7014[numberIndex];

      if(colour == 1) {
        digit.colour.set(1.0f, 1.0f, 1.0f);
      } else if(colour == 2) {
        digit.colour.set(248 / 240.0f, 224 / 240.0f, 72 / 240.0f);
      } else if(colour == 3) {
        digit.colour.set(224 / 240.0f, 72 / 240.0f, 0.0f);
      }

      //LAB_800f1998
      //LAB_800f199c
      digit.digitValue_00 = digits[i];
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public static void FUN_800f1a00(final long a0) {
    if(a0 != 1) {
      //LAB_800f1a10
      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);

        if(v1.charIndex_00.get() != -1) {
          v1._14.get(2).set(0);
          v1.flags_06.and(0xffff_fffe).and(0xffff_fffd);
        }

        //LAB_800f1a4c
      }

      return;
    }

    //LAB_800f1a64
    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);
      if(v1.charIndex_00.get() != -1) {
        v1._14.get(2).set(0);
        v1.flags_06.or(0x3);
      }

      //LAB_800f1a90
    }
  }

  @Method(0x800f1aa8L)
  public static boolean checkHit(final int attackerIndex, final int defenderIndex, final AttackType attackType) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerIndex].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[defenderIndex].innerStruct_00;
    final boolean isMonster = attacker instanceof MonsterBattleEntity;

    int effectAccuracy;
    if(attackType == AttackType.PHYSICAL) {
      setTempSpellStats(attacker);

      if(isMonster) {
        effectAccuracy = attacker.spell_94.accuracy_05;
      } else {
        //LAB_800f1bf4
        effectAccuracy = attacker.attackHit_3c;
      }
    } else if(attackType == AttackType.DRAGOON_MAGIC_STATUS_ITEMS) {
      //LAB_800f1c08
      setTempSpellStats(attacker);
      effectAccuracy = attacker.spell_94.accuracy_05;
    } else {
      //LAB_800f1c38
      setTempItemMagicStats(attacker);
      effectAccuracy = 100;
    }

    //LAB_800f1c44
    final int hitStat = (byte)attacker.getStat(attackType.tempHitStat);
    effectAccuracy = effectAccuracy * (hitStat + 100) / 100;

    final int avoidChance;
    if(attackType == AttackType.PHYSICAL) {
      avoidChance = defender.attackAvoid_40;
    } else {
      //LAB_800f1c9c
      avoidChance = defender.magicAvoid_42;
    }

    boolean effectHit = false;

    //LAB_800f1ca8
    final int modifiedAvoidChance = (avoidChance * ((byte)attacker.getStat(attackType.tempAvoidStat) + 100)) / 100;
    if(modifiedAvoidChance < effectAccuracy && effectAccuracy - modifiedAvoidChance >= (simpleRand() * 101 >> 16)) {
      effectHit = true;

      if(isMonster) {
        setTempSpellStats(attacker);
      }
    }

    //LAB_800f1d28
    if((attacker.getStat(attackType.alwaysHitStat) & attackType.alwaysHitMask) != 0) {
      effectHit = true;
    }

    //LAB_800f1d5c
    return effectHit;
  }

  public static int adjustDamageForPower(final int damage, final int attackerStat, final int defenderStat) {
    float base = 1.0f;

    if(attackerStat < 0) {
      base /= 2.0f;
    }

    if(defenderStat > 0) {
      base /= 2.0f;
    }

    if(attackerStat > 0) {
      base += 0.5f;
    }

    if(defenderStat < 0) {
      base += 0.5f;
    }

    return (int)(damage * base);
  }

  /**
   * @param magicType item (0), spell (1)
   */
  @Method(0x800f204cL)
  public static int calculateMagicDamage(final BattleEntity27c attacker, final BattleEntity27c defender, final int magicType) {
    // Stat mod item
    if(magicType == 0 && attacker.item_d4.type_0b != 0) {
      //LAB_800f2404
      //LAB_800f2410
      int s1;
      // HP, MP, SP, revive, cure status, cause status
      for(s1 = 0; s1 < 8; s1++) {
        if((attacker.item_d4.type_0b & 0x80 >> s1) != 0) {
          break;
        }
      }

      //LAB_800f2430
      final int value = switch(s1) {
        case 0 -> {
          //LAB_800f2454
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();
        }

        case 1 -> {
          //LAB_800f2464
          attacker.status_0e |= 0x800;
          yield defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();
        }

        //LAB_800f2478
        case 6 -> defender.stats.getStat(CoreMod.HP_STAT.get()).getMax();

        //LAB_800f2484
        case 7 -> defender.stats.getStat(CoreMod.MP_STAT.get()).getMax();

        //LAB_800f2490
        default -> 0;
      };

      //LAB_800f2494
      //LAB_800f24bc
      return value * attacker.item_d4.percentage_09 / 100;
    }

    //LAB_800f2140
    int damage;
    if(attacker.spell_94 != null && (attacker.spell_94.flags_01 & 0x4) != 0) {
      damage = defender.stats.getStat(CoreMod.HP_STAT.get()).getMax() * attacker.spell_94.multi_04 / 100;

      final List<BattleEntity27c> targets = new ArrayList<>();
      if((attacker.spell_94.targetType_00 & 0x8) != 0) { // Attack all
        if(attacker instanceof PlayerBattleEntity) {
          for(int i = 0; i < charCount_800c677c.get(); i++) {
            targets.add(battleState_8006e398.charBents_e40[i].innerStruct_00);
          }
        } else {
          for(int i = 0; i < aliveMonsterCount_800c6758.get(); i++) {
            targets.add(battleState_8006e398.aliveMonsterBents_ebc[i].innerStruct_00);
          }
        }
      } else { // Attack single
        targets.add(defender);
      }

      for(final BattleEntity27c target : targets) {
        applyBuffOrDebuff(attacker, target);
      }

      //LAB_800f2224
      attacker.status_0e |= 0x800;
    } else {
      final Element attackElement = magicType == 1 ? attacker.spell_94.element_08 : attacker.item_d4.element_01;
      final AttackType attackType = magicType == 1 ? AttackType.DRAGOON_MAGIC_STATUS_ITEMS : AttackType.ITEM_MAGIC;

      //LAB_800f2238
      damage = attacker.calculateMagicDamage(defender, magicType);
      damage = attackElement.adjustAttackingElementalDamage(attackType, damage, defender.getElement());
      damage = defender.getElement().adjustDefendingElementalDamage(attackType, damage, attackElement);
      damage = adjustDamageForPower(damage, attacker.powerMagicAttack_b6, defender.powerMagicDefence_ba);

      if(dragoonSpaceElement_800c6b64 != null) {
        damage = attackElement.adjustDragoonSpaceDamage(attackType, damage, dragoonSpaceElement_800c6b64);
      }
    }

    //LAB_800f24c0
    if(damage < 0) {
      damage = 0;
    }

    //LAB_800f24d0
    return damage;
  }

  @ScriptDescription("Perform a battle entity's physical attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2500L)
  public static FlowControl scriptPhysicalAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    final int damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.PHYSICAL, CoreMod.PHYSICAL_DAMAGE_FORMULA.calculate(attacker, defender))).damage;

    script.params_20[2].set(damage);
    script.params_20[3].set(determineAttackSpecialEffects(attacker, defender, AttackType.PHYSICAL));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's magic or status attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spellId", description = "The attacker's spell ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2694L)
  public static FlowControl scriptDragoonMagicStatusItemAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.spellId_4e = script.params_20[2].get();

    attacker.clearTempWeaponAndSpellStats();
    setTempSpellStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 1);
    damage = applyMagicDamageMultiplier(attacker, damage, 0);
    damage = Math.max(1, damage);

    //LAB_800f272c
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.DRAGOON_MAGIC_STATUS_ITEMS);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.spell_94.element_08);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS, damage)).damage;

    //LAB_800f27ec
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.DRAGOON_MAGIC_STATUS_ITEMS));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Perform a battle entity's item attack against another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "damage", description = "The amount of damage done")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "specialEffects", description = "Status effect bitset (or -1 for none)")
  @Method(0x800f2838L)
  public static FlowControl scriptItemMagicAttack(final RunningScript<?> script) {
    final BattleEntity27c attacker = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BattleEntity27c defender = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

    attacker.clearTempWeaponAndSpellStats();
    setTempItemMagicStats(attacker);

    int damage = calculateMagicDamage(attacker, defender, 0);
    damage = applyMagicDamageMultiplier(attacker, damage, 1);
    damage = Math.max(1, damage);

    //LAB_800f28c8
    if((attacker.status_0e & 0x800) != 0) {
      attacker.status_0e &= 0xf7ff;
    } else {
      damage = defender.applyDamageResistanceAndImmunity(damage, AttackType.ITEM_MAGIC);
      damage = defender.applyElementalResistanceAndImmunity(damage, attacker.item_d4.element_01);
    }

    damage = EVENTS.postEvent(new AttackEvent(attacker, defender, AttackType.ITEM_MAGIC, damage)).damage;

    //LAB_800f2970
    script.params_20[3].set(damage);
    script.params_20[4].set(determineAttackSpecialEffects(attacker, defender, AttackType.ITEM_MAGIC));
    applyItemSpecialEffects(attacker, defender);
    return FlowControl.CONTINUE;
  }

  private static final Obj[] type1FloatingDigits = new Obj[10];
  private static final Obj[] type3FloatingDigits = new Obj[10];
  private static Obj miss;

  @Method(0x800f3354L)
  public static void addFloatingNumber(final int numIndex, final int onHitTextType, final int onHitClutCol, final int number, final float x, final float y, int ticks, int colour) {
    if(miss == null) {
      for(int i = 0; i < 10; i++) {
        final QuadBuilder builder1 = new QuadBuilder("Type 1 Floating Digit " + i)
          .uv(floatingTextType1DigitUs_800c7028[i], 32)
          .size(8.0f, 8.0f);
        setGpuPacketClutAndTpageAndQueue(builder1, 0x80, null);
        type1FloatingDigits[i] = builder1.build();

        final QuadBuilder builder3 = new QuadBuilder("Type 3 Floating Digit " + i)
          .uv(floatingTextType3DigitUs_800c70e0[i], 40)
          .size(8.0f, 16.0f);
        setGpuPacketClutAndTpageAndQueue(builder3, 0x80, null);
        type3FloatingDigits[i] = builder3.build();
      }

      final QuadBuilder builderMiss = new QuadBuilder("Miss Floating Digit")
        .uv(72, 128)
        .size(36.0f, 16.0f);
      setGpuPacketClutAndTpageAndQueue(builderMiss, 0x80, null);
      miss = builderMiss.build();
    }

    final FloatingNumberC4 num = floatingNumbers_800c6b5c[numIndex];
    final int[] damageDigits = new int[num.digits_24.length];

    final int floatingTextType;  // 0=floating numbers, 1=MP cost, 2=miss
    final int clutCol; //TODO: confirm this, it may not be this exactly
    if(number != -1) {
      floatingTextType = onHitTextType;
      clutCol = onHitClutCol;
    } else {
      floatingTextType = 2;
      clutCol = 2;
      colour = 14;
    }

    switch(colour) {
      case 0 -> num.colour.set(1.0333333f, 0.6666667f, 0.0f); // Orange
      case 1 -> num.colour.set(1.0f, 1.0f, 1.0f); // Whiter
      case 2, 6 -> num.colour.set(1.0f, 1.0f, 1.0f); // White
      case 3, 7 -> num.colour.set(0.4f, 0.93333334f, 1.0333333f); // Cyan
      case 4, 8 -> num.colour.set(1.0333333f, 0.93333334f, 0.3f); // Yellow
      case 5, 9, 14 -> num.colour.set(0.93333334f, 0.3f, 0.0f); // Red
      case 10, 12 -> num.colour.set(0.56666666f, 0.53333336f, 1.0333333f); // Violet
      case 11, 13 -> num.colour.set(0.3f, 1.0333333f, 0.3f); // Green
    }

    //LAB_800f34d4
    num.flags_02 = 0;
    num.bentIndex_04 = -1;
    num.translucent_08 = false;
    num.shade_0c = 0x80;
    num._18 = -1;
    num.ticksRemaining_14 = -1;

    //LAB_800f3528
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].flags_00 = 0;
      num.digits_24[i]._04 = 0;
      num.digits_24[i]._08 = 0;
      num.digits_24[i].digit_0c = -1;
    }

    num.state_00 = 1;

    if(ticks == 0) {
      num.flags_02 |= 0x1;
    }

    //LAB_800f3588
    num.flags_02 |= 0x8000;
    num._10 = clutCol;

    if(clutCol == 2 && ticks == 0) {
      ticks = 60 / vsyncMode_8007a3b8 * 2;
    }

    //LAB_800f35dc
    //LAB_800f35e4
    //LAB_800f3608
    int damage = MathHelper.clamp(number, 0, 999999999);

    //LAB_800f3614
    num.x_1c = x;
    num.y_20 = y;

    //LAB_800f3654
    for(int i = 0; i < num.digits_24.length; i++) {
      num.digits_24[i].digit_0c = -1;
      damageDigits[i] = -1;
    }

    //LAB_800f36a0
    //Sets what places to render
    int currDigitPlace = (int)Math.pow(10, num.digits_24.length - 1);
    for(int i = 0; i < num.digits_24.length; i++) {
      damageDigits[i] = damage / currDigitPlace;
      damage %= currDigitPlace;
      currDigitPlace /= 10;
    }

    //LAB_800f36dc
    //LAB_800f36ec
    int digitIdx;
    for(digitIdx = 0; digitIdx < num.digits_24.length - 1; digitIdx++) {
      if(damageDigits[digitIdx] != 0) {
        break;
      }
    }

    //LAB_800f370c
    //LAB_800f3710
    int displayPosX;
    if(floatingTextType == 1) {
      //LAB_800f3738
      displayPosX = -(num.digits_24.length - digitIdx) * 5 / 2;
    } else if(floatingTextType == 2) {
      //LAB_800f3758
      displayPosX = -18;
    } else {
      //LAB_800f372c
      displayPosX = -(num.digits_24.length - digitIdx) * 4;
    }

    //LAB_800f375c
    //LAB_800f37ac
    int digitStructIdx;
    for(digitStructIdx = 0; digitStructIdx < num.digits_24.length && digitIdx < num.digits_24.length; digitStructIdx++) {
      final FloatingNumberDigit20 digit = num.digits_24[digitStructIdx];
      digit.flags_00 = 0x8000;
      digit.x_0e = displayPosX;
      digit.y_10 = 0;

      if(clutCol == 2) {
        digit.flags_00 = 0;
        digit._04 = digitStructIdx;
        digit._08 = 0;
      }

      //LAB_800f37d8
      if(floatingTextType == 1) {
        //LAB_800f382c
        digit.obj = type1FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 5;
      } else if(floatingTextType == 2) {
        //LAB_800f386c
        digit.obj = miss;
        displayPosX += 36;
      } else {
        //LAB_800f37f4
        digit.obj = type3FloatingDigits[damageDigits[digitIdx]];
        displayPosX += 8;
      }

      //LAB_800f3898
      digit.digit_0c = damageDigits[digitIdx];

      digitIdx++;
    }

    //LAB_800f38e8
    num.ticksRemaining_14 = digitStructIdx + 12;
    num._18 = ticks + 4; //TODO: ID duration meaning
  }

  @Method(0x800f3940L)
  public static void tickFloatingNumbers() {
    //LAB_800f3978
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          if(num.bentIndex_04 != -1) {
            final ScriptState<?> state = scriptStatePtrArr_800bc1c0[num.bentIndex_04];
            final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

            final float x;
            final float y;
            final float z;
            if(bent instanceof final MonsterBattleEntity monster) {
              x = -monster.targetArrowPos_78.x * 100.0f;
              y = -monster.targetArrowPos_78.y * 100.0f;
              z = -monster.targetArrowPos_78.z * 100.0f;
            } else {
              //LAB_800f3a3c
              x = 0;
              y = -640;
              z = 0;
            }

            //LAB_800f3a44
            final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);
            num.x_1c = clampX(screenCoords.x + centreScreenX_1f8003dc);
            num.y_20 = clampY(screenCoords.y + centreScreenY_1f8003de);
          }

          //LAB_800f3ac8
          if(num.state_00 == 1) {
            //LAB_800f3b24
            if(num._10 == 0x2) {
              num.state_00 = 2;
            } else {
              //LAB_800f3b44
              num.state_00 = 98;
            }
          } else if(num.state_00 == 2) {
            //LAB_800f3b50
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];

              if(digit.digit_0c == -1) {
                break;
              }

              if((digit.flags_00 & 0x1) != 0) {
                if((digit.flags_00 & 0x2) != 0) {
                  if(digit._08 < 5) {
                    digit.y_10 += digit._08;
                    digit._08++;
                  }
                } else {
                  //LAB_800f3bb0
                  digit.flags_00 |= 0x8002;
                  digit._04 = digit.y_10;
                  digit._08 = -4;
                }
              } else {
                //LAB_800f3bc8
                if(digit._08 == digit._04) {
                  digit.flags_00 |= 0x1;
                }

                //LAB_800f3be0
                digit._08++;
              }
            }

            //LAB_800f3c00
            num.ticksRemaining_14--;
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 98;
              num.ticksRemaining_14 = num._18;
            }
          } else if(num.state_00 == 97) {
            //LAB_800f3c34
            if(num.ticksRemaining_14 <= 0) {
              num.state_00 = 100;
            } else {
              //LAB_800f3c50
              num.ticksRemaining_14--;
              num.shade_0c = (num.shade_0c - (num._18 & 0xff)) & 0xff;
            }
          } else if(num.state_00 == 100) {
            //LAB_800f3d38
            num.state_00 = 0;
            num.flags_02 = 0;
            num.bentIndex_04 = -1;
            num.translucent_08 = false;
            num.shade_0c = 0x80;
            num.ticksRemaining_14 = -1;
            num._18 = -1;

            //LAB_800f3d60
            for(int n = 0; n < num.digits_24.length; n++) {
              final FloatingNumberDigit20 digit = num.digits_24[n];
              digit.flags_00 = 0;
              digit._04 = 0;
              digit._08 = 0;
              digit.digit_0c = -1;
            }
            //LAB_800f3b04
          } else if(num.state_00 < 99) {
            //LAB_800f3c88
            if((num.flags_02 & 0x1) != 0) {
              num.state_00 = 99;
            } else {
              //LAB_800f3ca4
              num.ticksRemaining_14--;

              if(num.ticksRemaining_14 <= 0) {
                if(num._10 > 0 && num._10 < 3) {
                  num.state_00 = 97;
                  num.translucent_08 = true;
                  num.shade_0c = 0x60;

                  final int ticksRemaining = 60 / vsyncMode_8007a3b8 / 2;
                  num.ticksRemaining_14 = ticksRemaining;
                  num._18 = 96 / ticksRemaining;
                } else {
                  //LAB_800f3d24
                  //LAB_800f3d2c
                  num.state_00 = 100;
                }
              }
            }
          }
        }
      }
    }
  }

  @Method(0x800f3dbcL)
  public static void drawFloatingNumbers() {
    //LAB_800f3e20
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if((num.flags_02 & 0x8000) != 0) {
        if(num.state_00 != 0) {
          //LAB_800f3e80
          for(int i = 0; i < num.digits_24.length; i++) {
            final FloatingNumberDigit20 digit = num.digits_24[i];

            if(digit.digit_0c == -1) {
              break;
            }

            if((digit.flags_00 & 0x8000) != 0) {
              //LAB_800f3ec0
              num.transforms.transfer.set(digit.x_0e + num.x_1c, digit.y_10 + num.y_20, 28.0f);
              RENDERER.queueOrthoOverlayModel(digit.obj, num.transforms)
                .colour(num.colour);

              if((num.state_00 & 97) == 0) {
                //LAB_800f4118
                break;
              }

              //LAB_800f4110
            }
          }
        }
      }
      //LAB_800f4134
    }
  }

  @Method(0x800f417cL)
  public static void FUN_800f417c() {
    //LAB_800f41ac
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      final BattleHudCharacterDisplay3c s1 = activePartyBattleHudCharacterDisplays_800c6c40.get(i);

      if(s1.charIndex_00.get() == -1 && characterStatsLoaded_800be5d0) {
        initializeBattleHudCharacterDisplay(i);
      }

      //LAB_800f41dc
    }

    //LAB_800f41f4
    //LAB_800f41f8
    short x = 63;

    //LAB_800f4220
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattleDisplayStats144 displayStats = displayStats_800c6c2c[charSlot];
      final BattleHudCharacterDisplay3c v1 = activePartyBattleHudCharacterDisplays_800c6c40.get(charSlot);

      if(v1.charIndex_00.get() != -1) {
        v1.x_08.set(x);
        displayStats.x_00 = x;
      }

      //LAB_800f4238
      x += 94;
    }
  }

  @Method(0x800f4268L)
  public static void addFloatingNumberForBent(final int bentIndex, final int damage, final int s4) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[bentIndex];
    final BattleEntity27c bent = (BattleEntity27c)state.innerStruct_00;

    final float x;
    final float y;
    final float z;
    if(bent instanceof final MonsterBattleEntity monster) {
      // ZYX is the correct order
      x = -monster.targetArrowPos_78.z * 100.0f;
      y = -monster.targetArrowPos_78.y * 100.0f;
      z = -monster.targetArrowPos_78.x * 100.0f;
    } else {
      //LAB_800f4314
      x = 0;
      y = -640;
      z = 0;
    }

    //LAB_800f4320
    final Vector2f screenCoords = perspectiveTransformXyz(bent.model_148, x, y, z);

    //LAB_800f4394
    FUN_800f89f4(bentIndex, 0, 2, damage, clampX(screenCoords.x + centreScreenX_1f8003dc), clampY(screenCoords.y + centreScreenY_1f8003de), 60 / vsyncMode_8007a3b8 / 4, s4);
  }

  @ScriptDescription("Gives SP to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to add")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "total", description = "The total SP after adding the amount requested")
  @Method(0x800f43dcL)
  public static FlowControl scriptGiveSp(final RunningScript<?> script) {
    //LAB_800f43f8
    //LAB_800f4410
    int charSlot;
    for(charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      if(battleState_8006e398.charBents_e40[charSlot].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f4430
    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() + script.params_20[1].get());
    spGained_800bc950[charSlot] += script.params_20[1].get();

    //LAB_800f4500
    script.params_20[2].set(sp.getCurrent());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Consumes SP from a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount of SP to take away")
  @Method(0x800f4518L)
  public static FlowControl scriptConsumeSp(final RunningScript<?> script) {
    //LAB_800f4534
    //LAB_800f454c
    int i;
    for(i = 0; i < charCount_800c677c.get(); i++) {
      if(battleState_8006e398.charBents_e40[i].index == script.params_20[0].get()) {
        break;
      }
    }

    //LAB_800f456c
    final BattleHudCharacterDisplay3c charDisplay = activePartyBattleHudCharacterDisplays_800c6c40.get(i);
    charDisplay.unused_0c.set((short)0);
    charDisplay.unused_0e.set((short)script.params_20[1].get());

    final PlayerBattleEntity player = (PlayerBattleEntity)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VitalsStat sp = player.stats.getStat(CoreMod.SP_STAT.get());

    sp.setCurrent(sp.getCurrent() - script.params_20[2].get());

    if(sp.getCurrent() == 0) {
      charDisplay.flags_06.and(0xfff3);
    }

    //LAB_800f45f8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, this might handle players selecting an attack target")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemOrSpellId", description = "The item or spell ID selected")
  @Method(0x800f4600L)
  public static FlowControl FUN_800f4600(final RunningScript<?> script) {
    final SpellAndItemMenuA4 menu = spellAndItemMenu_800c6b60;
    int itemOrSpellId = menu.itemOrSpellId_1c;
    if(menu.charIndex_08 == 8 && menu.menuType_0a == 1) {
      if(itemOrSpellId == 10) {
        itemOrSpellId = 65;
      }

      //LAB_800f46ec
      if(itemOrSpellId == 11) {
        itemOrSpellId = 66;
      }

      //LAB_800f46f8
      if(itemOrSpellId == 12) {
        itemOrSpellId = 67;
      }
    }

    //LAB_800f4704
    //LAB_800f4708
    script.params_20[0].set(menu._a0);
    script.params_20[1].set(battleMenu_800c6c34.target_48);
    script.params_20[2].set(itemOrSpellId);

    //LAB_800f4770
    PlayerBattleEntity playerBent = null;
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      playerBent = battleState_8006e398.charBents_e40[i].innerStruct_00;

      if(playerBent.charId_272 == menu.charIndex_08) {
        break;
      }
    }

    //LAB_800f47ac
    playerBent.spellId_4e = itemOrSpellId;

    if(menu._a0 == 1 && menu.menuType_0a == 0) {
      //LAB_800f47e4
      for(int i = 0; i < 17; i++) {
        if(targetAllItemIds_800c7124[i] == itemOrSpellId + 0xc0) {
          //LAB_800f4674
          script.params_20[1].set(-1);
          break;
        }
      }
    }

    //LAB_800f4800
    //LAB_800f4804
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the item/spell attack target")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetMode")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index (or -1 if attack all)")
  @Method(0x800f480cL)
  public static FlowControl scriptGetItemOrSpellAttackTarget(final RunningScript<?> script) {
    BattleEntity27c a1 = null;
    final int[] sp0x10 = {0, 0, 1, 0, 2, 1, 1, 1};

    int targetMode = script.params_20[0].get();

    final BattleMenuStruct58 menu = battleMenu_800c6c34;

    //LAB_800f489c
    for(int a0 = 0; a0 < charCount_800c677c.get(); a0++) {
      a1 = battleState_8006e398.charBents_e40[a0].innerStruct_00;

      if(menu.charIndex_04 == a1.charId_272) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1.specialEffectFlag_14 & 0x8) != 0) { // "Attack all"
      targetMode = 3;
    }

    //LAB_800f48f4
    int ret = menu.handleTargeting(sp0x10[targetMode * 2], sp0x10[targetMode * 2 + 1] != 0);
    if(ret == 0) { // No buttons pressed
      return FlowControl.PAUSE_AND_REWIND;
    }

    if(ret == 1) { // Pressed X
      //LAB_800f4930
      ret = menu.target_48;
    } else { // Pressed O
      //LAB_800f4944
      //LAB_800f4948
      ret = -1;
    }

    //LAB_800f4950
    script.params_20[1].set(ret);

    //LAB_800f4954
    return FlowControl.CONTINUE;
  }

  /** Background of battle menu icons */
  @Method(0x800f74f4L)
  public static Obj buildBattleMenuBackground(final String name, final BattleMenuBackgroundUvMetrics04 menuBackgroundMetrics, final int x, final int y, final int w, final int h, final int baseClutOffset, @Nullable final Translucency transMode, final int uvShiftType) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, 0, 0, w, h, false);

    // Modified 1 and 3 from retail to properly align bottom row of pixels
    if(uvShiftType == 0) {
      //LAB_800f7628
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01)
        .uvSize(menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 1) {
      //LAB_800f7654
      builder
        .uv(menuBackgroundMetrics.u_00, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
      //LAB_800f7610
    } else if(uvShiftType == 2) {
      //LAB_800f7680
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01)
        .uvSize(-menuBackgroundMetrics.w_02, menuBackgroundMetrics.h_03);
    } else if(uvShiftType == 3) {
      //LAB_800f76d4
      builder
        .uv(menuBackgroundMetrics.u_00 + menuBackgroundMetrics.w_02 - 1, menuBackgroundMetrics.v_01 + menuBackgroundMetrics.h_03)
        .uvSize(-menuBackgroundMetrics.w_02, -menuBackgroundMetrics.h_03);
    }

    //LAB_800f7724
    //LAB_800f772c
    setGpuPacketClutAndTpageAndQueue(builder, baseClutOffset, transMode);
    return builder.build();
  }

  @Method(0x800f7a74L)
  public static void setTempItemMagicStats(final BattleEntity27c bent) {
    //LAB_800f7a98
    bent.item_d4 = itemStats_8004f2ac[bent.itemId_52];
    bent._ec = 0;
    bent._ee = 0;
    bent._f0 = 0;
    bent._f2 = 0;
  }

  @Method(0x800f7b68L)
  public static void setTempSpellStats(final BattleEntity27c bent) {
    //LAB_800f7b8c
    if(bent.spellId_4e != -1 && bent.spellId_4e <= 127) {
      bent.spell_94 = EVENTS.postEvent(new SpellStatsEvent(bent.spellId_4e, spellStats_800fa0b8[bent.spellId_4e])).spell;
    } else {
      if(bent.spellId_4e > 127) {
        LOGGER.error("Retail bug: spell index out of bounds (%d). This is known to happen during Shana/Miranda's dragoon attack.", bent.spellId_4e);
      }

      bent.spell_94 = new SpellStats0c();
    }

    //LAB_800f7c54
  }

  @Method(0x800f7c5cL)
  public static int determineAttackSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender, final AttackType attackType) {
    final BattleEntityStat[] statusEffectChances = {BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.SPELL_STATUS_CHANCE, BattleEntityStat.ON_HIT_STATUS_CHANCE}; // onHitStatusChance, statusChance, onHitStatusChance, statusChance, statusChance, onHitStatusChance
    final BattleEntityStat[] statusEffectStats = {BattleEntityStat.EQUIPMENT_ON_HIT_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.SPELL_STATUS_TYPE, BattleEntityStat.ITEM_STATUS}; // onHitStatus, statusType, itemStatus, statusType, statusType, itemStatus
    final BattleEntityStat[] specialEffectStats = {BattleEntityStat.SPECIAL_EFFECT_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.SPELL_FLAGS, BattleEntityStat.ITEM_TARGET}; // specialEffectFlag, spellFlags, itemTarget, spellFlags, spellFlags, itemTarget
    final int[] specialEffectMasks = {0x40, 0xf0, 0x80, 0xf0, 0xf0, 0x80};

    final boolean isAttackerMonster = attacker instanceof MonsterBattleEntity;

    final int index = (!isAttackerMonster ? 0 : 3) + attackType.ordinal(); //TODO

    final int effectChance = attackType == AttackType.ITEM_MAGIC ? 101 : attacker.getStat(statusEffectChances[index]);

    //LAB_800f7e98
    int effect = -1;
    if(simpleRand() * 101 >> 16 < effectChance) {
      final int statusType = attacker.getStat(statusEffectStats[index]);

      if((statusType & 0xff) != 0) {
        //LAB_800f7eec
        int statusIndex;
        for(statusIndex = 0; statusIndex < 8; statusIndex++) {
          if((statusType & (0x80 >> statusIndex)) != 0) {
            break;
          }
        }

        //LAB_800f7f0c
        effect = 0x80 >> statusIndex;
      }

      //LAB_800f7f14
      final int specialEffects = attacker.getStat(specialEffectStats[index]) & specialEffectMasks[index];
      if(specialEffects != 0) {
        if(
          !isAttackerMonster && attackType != AttackType.DRAGOON_MAGIC_STATUS_ITEMS ||
          specialEffects == 0x80
        ) {
          effect = 0;
        } else if(specialEffects == 0x10) {
          // I think this is vestigial, there are no spells with flag 0x10
          throw new RuntimeException("Flag 0x10 found");
//          if((attacker.spellElement_a4 & (isDefenderMonster ? defender.elementFlag_1c : characterElements_800c706c[defender.charIndex_272].get())) != 0) {
//            effect = 0;
//          }
        }

        //LAB_800f7fc8
        if((defender.specialEffectFlag_14 & 0x80) != 0) { // Resistance
          effect = -1;
        }
      }
    }

    //LAB_800f7fe0
    //LAB_800f7fe4
    return effect;
  }

  @Method(0x800f83c8L)
  public static void prepareItemList() {
    //LAB_800f83dc
    combatItems_800c6988.clear();

    //LAB_800f8420
    for(int itemSlot1 = 0; itemSlot1 < gameState_800babc8.items_2e9.size(); itemSlot1++) {
      final Item item = gameState_800babc8.items_2e9.get(itemSlot1);

      boolean found = false;

      //LAB_800f843c
      for(final CombatItem02 combatItem : combatItems_800c6988) {
        if(combatItem.item == item) {
          found = true;
          combatItem.count++;
          break;
        }
      }

      if(!found) {
        combatItems_800c6988.add(new CombatItem02(item));
      }
    }
  }

  @Method(0x800f84c8L)
  public static void loadBattleHudTextures() {
    loadDrgnDir(0, 4113, Bttl_800e::battleHudTexturesLoadedCallback);
  }

  @Method(0x800f8568L)
  public static LodString getTargetEnemyName(final BattleEntity27c target, final LodString targetName) {
    // Seems to be special-case handling to replace Tentacle, since the Melbu fight has more enemies than the engine can handle
    if(target.charId_272 == 0x185) {
      final int stageProgression = battleState_8006e398.stageProgression_eec;
      if(stageProgression == 0 || stageProgression == 4 || stageProgression == 6) {
        return melbuMonsterNames_800c6ba8[melbuStageToMonsterNameIndices_800c6f30[battleState_8006e398.stageProgression_eec]];
      }
    }

    return targetName;
  }

  @Method(0x800f8670L)
  public static void loadMonster(final ScriptState<MonsterBattleEntity> state) {
    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      melbuMonsterNames_800c6ba8[i] = monsterNames_80112068[melbuMonsterNameIndices_800c6e90[i]];
    }

    final MonsterBattleEntity monster = state.innerStruct_00;
    currentEnemyNames_800c69d0[monsterCount_800c6b9c.get()] = monsterNames_80112068[monster.charId_272];

    //LAB_800eefa8
    monsterBents_800c6b78.get(monsterCount_800c6b9c.get()).set(state.index);
    monsterCount_800c6b9c.incr();

    //LAB_800eefcc
    final MonsterStats1c monsterStats = monsterStats_8010ba98[monster.charId_272];

    final MonsterStatsEvent statsEvent = EVENTS.postEvent(new MonsterStatsEvent(monster.charId_272));

    final VitalsStat monsterHp = monster.stats.getStat(CoreMod.HP_STAT.get());
    monsterHp.setCurrent(statsEvent.hp);
    monsterHp.setMaxRaw(statsEvent.maxHp);
    monster.specialEffectFlag_14 = statsEvent.specialEffectFlag;
//    monster.equipmentType_16 = 0;
    monster.equipment_02_18 = 0;
    monster.equipmentEquipableFlags_1a = 0;
    monster.displayElement_1c = statsEvent.elementFlag;
    monster.equipment_05_1e = monsterStats._0e;
    monster.equipmentElementalImmunity_22.set(statsEvent.elementalImmunityFlag);
    monster.equipmentStatusResist_24 = statsEvent.statusResistFlag;
    monster.equipment_09_26 = 0;
    monster.equipmentAttack1_28 = 0;
    monster._2e = 0;
    monster.equipmentIcon_30 = 0;
    monster.stats.getStat(CoreMod.SPEED_STAT.get()).setRaw(statsEvent.speed);
    monster.attack_34 = statsEvent.attack;
    monster.magicAttack_36 = statsEvent.magicAttack;
    monster.defence_38 = statsEvent.defence;
    monster.magicDefence_3a = statsEvent.magicDefence;
    monster.attackHit_3c = 0;
    monster.magicHit_3e = 0;
    monster.attackAvoid_40 = statsEvent.attackAvoid;
    monster.magicAvoid_42 = statsEvent.magicAvoid;
    monster.onHitStatusChance_44 = 0;
    monster.equipment_19_46 = 0;
    monster.equipment_1a_48 = 0;
    monster.equipmentOnHitStatus_4a = 0;
    monster.targetArrowPos_78.set(monsterStats.targetArrowX_12, monsterStats.targetArrowY_13, monsterStats.targetArrowZ_14);
    monster.hitCounterFrameThreshold_7e = monsterStats.hitCounterFrameThreshold_15;
    monster._80 = monsterStats._16;
    monster._82 = monsterStats._17;
    monster._84 = monsterStats._18;
    monster._86 = monsterStats._19;
    monster._88 = monsterStats._1a;
    monster._8a = monsterStats._1b;

    monster.damageReductionFlags_6e = monster.specialEffectFlag_14;
    monster._70 = monster.equipment_05_1e;
    monster.monsterElement_72 = monster.displayElement_1c;
    monster.monsterElementalImmunity_74.set(monster.equipmentElementalImmunity_22);
    monster.monsterStatusResistFlag_76 = monster.equipmentStatusResist_24;

    if((monster.damageReductionFlags_6e & 0x8) != 0) {
      monster.physicalImmunity_110 = true;
    }

    //LAB_800ef25c
    if((monster.damageReductionFlags_6e & 0x4) != 0) {
      monster.magicalImmunity_112 = true;
    }
  }

  @Method(0x800f8854L)
  public static void applyItemSpecialEffects(final BattleEntity27c attacker, final BattleEntity27c defender) {
    setTempItemMagicStats(attacker);

    final int turnCount = attacker != defender ? 3 : 4;

    if(attacker.item_d4.powerDefence != 0) {
      defender.powerDefence_b8 = attacker.item_d4.powerDefence;
      defender.powerDefenceTurns_b9 = turnCount;
    }

    if(attacker.item_d4.powerMagicDefence != 0) {
      defender.powerMagicDefence_ba = attacker.item_d4.powerMagicDefence;
      defender.powerMagicDefenceTurns_bb = turnCount;
    }

    if(attacker.item_d4.powerAttack != 0) {
      defender.powerAttack_b4 = attacker.item_d4.powerAttack;
      defender.powerAttackTurns_b5 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttack != 0) {
      defender.powerMagicAttack_b6 = attacker.item_d4.powerMagicAttack;
      defender.powerMagicAttackTurns_b7 = turnCount;
    }

    if(attacker.item_d4.powerAttackHit != 0) {
      defender.tempAttackHit_bc = attacker.item_d4.powerAttackHit;
      defender.tempAttackHitTurns_bd = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackHit != 0) {
      defender.tempMagicHit_be = attacker.item_d4.powerMagicAttackHit;
      defender.tempMagicHitTurns_bf = turnCount;
    }

    if(attacker.item_d4.powerAttackAvoid != 0) {
      defender.tempAttackAvoid_c0 = attacker.item_d4.powerAttackAvoid;
      defender.tempAttackAvoidTurns_c1 = turnCount;
    }

    if(attacker.item_d4.powerMagicAttackAvoid != 0) {
      defender.tempMagicAvoid_c2 = attacker.item_d4.powerMagicAttackAvoid;
      defender.tempMagicAvoidTurns_c3 = turnCount;
    }

    if(attacker.item_d4.physicalImmunity) {
      defender.tempPhysicalImmunity_c4 = 1;
      defender.tempPhysicalImmunityTurns_c5 = turnCount;
    }

    if(attacker.item_d4.magicalImmunity) {
      defender.tempMagicalImmunity_c6 = 1;
      defender.tempMagicalImmunityTurns_c7 = turnCount;
    }

    if(attacker.item_d4.speedDown != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedDown, turnCount));
    }

    if(attacker.item_d4.speedUp != 0) {
      defender.stats.getStat(CoreMod.SPEED_STAT.get()).addMod(new TurnBasedPercentileBuff(attacker.item_d4.speedUp, turnCount));
    }

    if(defender instanceof final PlayerBattleEntity playerDefender) {
      if(attacker.item_d4.spPerPhysicalHit != 0) {
        playerDefender.tempSpPerPhysicalHit_cc = attacker.item_d4.spPerPhysicalHit;
        playerDefender.tempSpPerPhysicalHitTurns_cd = turnCount;
      }

      if(attacker.item_d4.mpPerPhysicalHit != 0) {
        playerDefender.tempMpPerPhysicalHit_ce = attacker.item_d4.mpPerPhysicalHit;
        playerDefender.tempMpPerPhysicalHitTurns_cf = turnCount;
      }

      if(attacker.item_d4.spPerMagicalHit != 0) {
        playerDefender.tempSpPerMagicalHit_d0 = attacker.item_d4.spPerMagicalHit;
        playerDefender.tempSpPerMagicalHitTurns_d1 = turnCount;
      }

      if(attacker.item_d4.mpPerMagicalHit != 0) {
        playerDefender.tempMpPerMagicalHit_d2 = attacker.item_d4.mpPerMagicalHit;
        playerDefender.tempMpPerMagicalHitTurns_d3 = turnCount;
      }
    }

    defender.recalculateSpeedAndPerHitStats();
  }

  @Method(0x800f89f4L)
  public static boolean FUN_800f89f4(final int bentIndex, final int a1, final int a2, final int rawDamage, final float x, final float y, final int a6, final int a7) {
    //LAB_800f8a30
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, a1, a2, rawDamage, x, y, a6, a7);
        num.bentIndex_04 = bentIndex;
        return true;
      }

      //LAB_800f8a74
    }

    //LAB_800f8a84
    return false;
  }

  @Method(0x800f8aa4L)
  public static void renderDamage(final int bentIndex, final int damage) {
    addFloatingNumberForBent(bentIndex, damage, 8);
  }

  /**
   * @param textType <ol start="0">
   *                   <li>Player names</li>
   *                   <li>Player names</li>
   *                   <li>Combat item names</li>
   *                   <li>Dragoon spells</li>
   *                   <li>Item descriptions</li>
   *                   <li>Spell descriptions</li>
   *                 </ol>
   */
  @Method(0x800f8ac4L)
  public static void renderText(final int textType, final int textIndex, final int x, final int y) {
    final LodString str;
    if(textType == 4) {
      str = new LodString(itemStats_8004f2ac[textIndex].combatDescription);
    } else if(textType == 5) {
      str = new LodString(spellStats_800fa0b8[textIndex].combatDescription);
    } else {
      throw new IllegalArgumentException("Only supports textType 4/5");
    }

    final BattleDescriptionEvent event = EVENTS.postEvent(new BattleDescriptionEvent(textType, textIndex, str));
    Scus94491BpeSegment_8002.renderText(event.string, x - textWidth(event.string) / 2, y - 6, TextColour.WHITE, 0);
  }

  @Method(0x800f8cd8L)
  public static Obj buildBattleMenuElement(final String name, final int x, final int y, final int u, final int v, final int w, final int h, final int clut, @Nullable final Translucency transMode) {
    final QuadBuilder builder = new QuadBuilder(name)
      .monochrome(0.5f);

    setGpuPacketParams(builder, x, y, u, v, w, h, true);
    setGpuPacketClutAndTpageAndQueue(builder, clut, transMode);

    return builder.build();
  }

  @Method(0x800f8dfcL)
  public static Obj buildUiTextureElement(final String name, final int u, final int v, final int w, final int h, final int clut) {
    final QuadBuilder builder = new QuadBuilder(name)
      .size(w, h)
      .uv(u, v);

    setGpuPacketClutAndTpageAndQueue(builder, clut, null);

    return builder.build();
  }

  @Method(0x800f8facL)
  public static void setGpuPacketParams(final QuadBuilder cmd, final int x, final int y, final int u, final int v, final int w, final int h, final boolean textured) {
    cmd
      .pos(x, y, 0.0f)
      .size(w, h);

    if(textured) {
      cmd
        .uv(u, v);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void setGpuPacketClutAndTpageAndQueue(final QuadBuilder cmd, int clut, @Nullable final Translucency transparencyMode) {
    final int clutIndex;
    if(clut >= 0x80) {
      clutIndex = 1;
      clut -= 0x80;
    } else {
      //LAB_800f9080
      clutIndex = 0;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    final int clutX = battleUiElementClutVramXy_800c7114[clutIndex].x + clut & 0x3f0;
    final int clutY = battleUiElementClutVramXy_800c7114[clutIndex].y + clut % 16;

    cmd
      .bpp(Bpp.BITS_4)
      .clut(clutX, clutY)
      .vramPos(704, 256);

    if(transparencyMode != null) {
      cmd.translucency(transparencyMode);
    }
  }

  @Method(0x800f9380L)
  public static void applyBuffOrDebuff(final BattleEntity27c attacker, final BattleEntity27c defender) {
    final BattleEntityStat[] stats = {BattleEntityStat.POWER_DEFENCE, BattleEntityStat.POWER_MAGIC_DEFENCE, BattleEntityStat.POWER_ATTACK, BattleEntityStat.POWER_MAGIC_ATTACK};

    for(int i = 0; i < 8; i++) {
      // This has been intentionally changed to attacker.buffType. Defender.buffType was always set to attacker.buffType anyway.
      if((attacker.spell_94.buffType_0a & (0x80 >> i)) != 0) {
        final int turnCount = attacker.charId_272 != defender.charId_272 ? 3 : 4;
        final int amount = i < 4 ? 50 : -50;

        defender.setStat(stats[i % 4], turnCount << 8 | (amount & 0xff));
      }
    }
  }

  /**
   * @param magicType spell (0), item (1)
   */
  @Method(0x800f946cL)
  public static int applyMagicDamageMultiplier(final BattleEntity27c attacker, final int damage, final int magicType) {
    final int damageMultiplier;
    if(magicType == 0) {
      damageMultiplier = spellStats_800fa0b8[attacker.spellId_4e].damageMultiplier_03;
    } else {
      //LAB_800f949c
      damageMultiplier = attacker.item_d4.damageMultiplier_02;
    }

    if(damageMultiplier == 0x1) {
      //LAB_800f9570
      return damage * 8;
    }

    if(damageMultiplier == 0x2) {
      //LAB_800f9564
      return damage * 6;
    }

    //LAB_800f94d8
    if(damageMultiplier == 0x4) {
      //LAB_800f955c
      return damage * 5;
    }

    //LAB_800f94a0
    if(damageMultiplier == 0x8) {
      //LAB_800f9554
      return damage * 4;
    }

    if(damageMultiplier == 0x10) {
      //LAB_800f954c
      return damage * 3;
    }

    //LAB_800f94ec
    if(damageMultiplier == 0x20) {
      //LAB_800f9544
      return damage * 2;
    }

    //LAB_800f9510
    if(damageMultiplier == 0x40) {
      //LAB_800f9534
      return damage + damage / 2;
    }

    if(damageMultiplier == 0x80) {
      return damage / 2;
    }

    //LAB_800f9578
    //LAB_800f957c
    return damage;
  }

  @ScriptDescription("Checks if a battle entity's physical attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f95d0L)
  public static FlowControl scriptCheckPhysicalHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.PHYSICAL) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's spell or status attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9618L)
  public static FlowControl scriptCheckSpellOrStatusHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.DRAGOON_MAGIC_STATUS_ITEMS) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if a battle entity's item magic attack hits another battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "defenderIndex", description = "The BattleEntity27c defender script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hit", description = "True if attack hit, false otherwise")
  @Method(0x800f9660L)
  public static FlowControl scriptCheckItemHit(final RunningScript<?> script) {
    script.params_20[2].set(checkHit(script.params_20[0].get(), script.params_20[1].get(), AttackType.ITEM_MAGIC) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f96a8L)
  public static FlowControl scriptSetTempSpellStats(final RunningScript<?> script) {
    setTempSpellStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets a battle entity's position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The BattleEntity27c attacker script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x800f96d4L)
  public static FlowControl scriptGetBentPos(final RunningScript<?> script) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(Math.round(bent.model_148.coord2_14.coord.transfer.x));
    script.params_20[2].set(Math.round(bent.model_148.coord2_14.coord.transfer.y));
    script.params_20[3].set(Math.round(bent.model_148.coord2_14.coord.transfer.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a floating number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "number", description = "The number")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X coordinate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y coordinate")
  @Method(0x800f9730L)
  public static FlowControl scriptAddFloatingNumber(final RunningScript<?> script) {
    //LAB_800f9758
    for(int i = 0; i < floatingNumbers_800c6b5c.length; i++) {
      final FloatingNumberC4 num = floatingNumbers_800c6b5c[i];

      if(num.state_00 == 0) {
        addFloatingNumber(i, 0, 0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), 60 / vsyncMode_8007a3b8 * 5, 0);
        break;
      }

      //LAB_800f97b8
    }

    //LAB_800f97c8
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Initialized the battle menu for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "menuType", description = "0 = items, 1 = spells, 2 = ?")
  @Method(0x800f97d8L)
  public static FlowControl scriptInitSpellAndItemMenu(final RunningScript<?> script) {
    spellAndItemMenu_800c6b60.clearSpellAndItemMenu();
    spellAndItemMenu_800c6b60.initSpellAndItemMenu(((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).charId_272, (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Render recovery amount for a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "amount", description = "The amount recovered")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourIndex", description = "Which colour to use (indices are unknown)")
  @Method(0x800f984cL)
  public static FlowControl scriptRenderRecover(final RunningScript<?> script) {
    addFloatingNumberForBent(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Caches selected spell's stats on a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9884L)
  public static FlowControl scriptSetTempItemMagicStats(final RunningScript<?> script) {
    setTempItemMagicStats((BattleEntity27c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Takes a specific (or random) item from the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID (or -1 to take a random item)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemTaken", description = "The item ID that was taken (or -1 if none could be taken)")
  @Method(0x800f98b0L)
  public static FlowControl scriptTakeItem(final RunningScript<?> script) {
    int itemId = script.params_20[0].get();

    if(gameState_800babc8.items_2e9.isEmpty()) {
      script.params_20[1].set(-1);
      return FlowControl.CONTINUE;
    }

    Item item = null;
    if(itemId == -1) {
      item = gameState_800babc8.items_2e9.get((simpleRand() * gameState_800babc8.items_2e9.size()) >> 16);
      itemId = LodMod.idItemMap.getInt(item.getRegistryId());

      //LAB_800f996c
      for(int i = 0; i < 10; i++) {
        if(itemId == protectedItems_800c72cc[i]) {
          //LAB_800f999c
          item = null;
          itemId = -1;
          break;
        }
      }
    }

    //LAB_800f9988
    //LAB_800f99a4
    if(item != null && takeItemId(item) != 0) {
      itemId = -1;
    }

    //LAB_800f99c0
    script.params_20[1].set(itemId);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gives a specific item to the player")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "itemId", description = "The item ID")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "itemGiven", description = "The item ID that was given (or -1 if none could be given)")
  @Method(0x800f99ecL)
  public static FlowControl scriptGiveItem(final RunningScript<?> script) {
    final int givenItem;

    final int itemId = script.params_20[0].get();
    final boolean given;
    if(itemId < 192) {
      given = giveEquipment(REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(itemId)).get());
    } else {
      given = giveItem(REGISTRIES.items.getEntry(LodMod.itemIdMap.get(itemId - 192)).get());
    }

    if(given) {
      givenItem = itemId;
    } else {
      givenItem = -1;
    }

    //LAB_800f9a2c
    script.params_20[1].set(givenItem);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to targeting")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetType", description = "0 = characters, 1 = monsters, 2 = any")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetBentIndex", description = "The targeted BattleEntity27c script index")
  @Method(0x800f9a50L)
  public static FlowControl FUN_800f9a50(final RunningScript<?> script) {
    final int targetType = script.params_20[0].get();
    final int targetBent = script.params_20[1].get();

    final ScriptState<? extends BattleEntity27c>[] bents;
    final int count;
    if(targetType == 0) {
      bents = battleState_8006e398.charBents_e40;
      count = charCount_800c677c.get();
    } else if(targetType == 1) {
      //LAB_800f9a94
      bents = battleState_8006e398.aliveMonsterBents_ebc;
      count = aliveMonsterCount_800c6758.get();
    } else {
      //LAB_800f9aac
      bents = battleState_8006e398.aliveBents_e78;
      count = aliveBentCount_800c669c.get();
    }

    //LAB_800f9abc
    //LAB_800f9adc
    for(int i = 0; i < count; i++) {
      if(targetBent == bents[i].index) {
        if(targetType == 0) {
          battleMenu_800c6c34._800c6980 = i;
        } else if(targetType == 1) {
          //LAB_800f9b0c
          battleMenu_800c6c34._800c697e = i;
        }

        break;
      }

      //LAB_800f9b14
    }

    //LAB_800f9b24
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if any floating number is on the screen")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "onScreen", description = "True if any floating number is on screen, false otherwise")
  @Method(0x800f9b2cL)
  public static FlowControl scriptIsFloatingNumberOnScreen(final RunningScript<?> script) {
    //LAB_800f9b3c
    int found = 0;
    for(final FloatingNumberC4 num : floatingNumbers_800c6b5c) {
      if(num.state_00 != 0) {
        found = 1;
        break;
      }

      //LAB_800f9b58
    }

    //LAB_800f9b64
    script.params_20[0].set(found);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the active dragoon space element")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID whose element should be used")
  @Method(0x800f9b78L)
  public static FlowControl scriptSetDragoonSpaceElementIndex(final RunningScript<?> script) {
    final int characterId = script.params_20[0].get();

    dragoonSpaceElement_800c6b64 = null;

    if(characterId != -1) {
      if(characterId == 9) { //TODO stupid special case handling for DD Dart
        dragoonSpaceElement_800c6b64 = CoreMod.DIVINE_ELEMENT.get();
      } else {
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          if(battleState_8006e398.charBents_e40[i].innerStruct_00.charId_272 == characterId) {
            dragoonSpaceElement_800c6b64 = battleState_8006e398.charBents_e40[i].innerStruct_00.element;
            break;
          }
        }
      }
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800f9b94L)
  public static FlowControl FUN_800f9b94(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9bd4L)
  public static FlowControl FUN_800f9bd4(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, related to menu")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800f9c00L)
  public static FlowControl FUN_800f9c00(final RunningScript<?> script) {
    // Unused menu-related code
    return FlowControl.CONTINUE;
  }

  private static UiBox scriptUi;

  @ScriptDescription("Renders the battle HUD background")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position (centre)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "width", description = "The width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "height", description = "The height")
  @Method(0x800f9c2cL)
  public static FlowControl scriptRenderBattleHudBackground(final RunningScript<?> script) {
    final int colourIndex = script.params_20[4].get();
    final int r = textboxColours_800c6fec[colourIndex][0];
    final int g = textboxColours_800c6fec[colourIndex][1];
    final int b = textboxColours_800c6fec[colourIndex][2];

    // This is kinda dumb since we'll have to upload a new box each frame, but there isn't a great
    // way to deal with it. Maybe check to see if any of the params have changed before deleting?

    if(scriptUi != null) {
      scriptUi.delete();
    }

    scriptUi = new UiBox(
      "Scripted Battle UI",
      (short)script.params_20[0].get() - script.params_20[2].get() / 2,
      (short)script.params_20[1].get() - script.params_20[3].get() / 2,
      (short)script.params_20[2].get(),
      (short)script.params_20[3].get()
    );

    scriptUi.render(r / 255.0f, g / 255.0f, b / 255.0f);

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, disables menu icons if certain flags are set")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "iconIndicesBitset", description = "The icons to disable if their flag matches a certain value (unknown)")
  @Method(0x800f9cacL)
  public static FlowControl scriptSetDisabledMenuIcons(final RunningScript<?> script) {
    final int disabledIconsBitset = script.params_20[0].get();
    battleMenu_800c6c34.setDisabledIcons(disabledIconsBitset);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Called after any battle entity finishes its turn, ticks temporary stats and calls turnFinished on custom battle entities")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c script index")
  @Method(0x800f9d7cL)
  public static FlowControl scriptFinishBentTurn(final RunningScript<?> script) {
    final int bentIndex = script.params_20[0].get();
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    bent.turnFinished();
    bent.recalculateSpeedAndPerHitStats();
    return FlowControl.CONTINUE;
  }

  @Method(0x800f9e50L)
  public static PlayerBattleEntity setActiveCharacterSpell(final int spellId) {
    final int charIndex = spellAndItemMenu_800c6b60.charIndex_08;

    //LAB_800f9e8c
    for(int charSlot = 0; charSlot < charCount_800c677c.get(); charSlot++) {
      final ScriptState<PlayerBattleEntity> playerState = battleState_8006e398.charBents_e40[charSlot];
      final PlayerBattleEntity player = playerState.innerStruct_00;

      if(charIndex == player.charId_272) {
        //LAB_800f9ec8
        player.spellId_4e = spellId;
        setTempSpellStats(player);
        return player;
      }
    }

    throw new IllegalStateException();
  }

  @Method(0x800f9ee8L)
  public static void drawLine(final int x1, final int y1, final int x2, final int y2, final int r, final int g, final int b, final boolean translucent) {
    final GpuCommandLine cmd = new GpuCommandLine()
      .rgb(0, r, g, b)
      .rgb(1, r, g, b)
      .pos(0, x1, y1)
      .pos(1, x2, y2);

    if(translucent) {
      cmd.translucent(Translucency.B_PLUS_F);
    }

    GPU.queueCommand(31, cmd);
  }

  @Method(0x800fa068L)
  public static float clampX(final float x) {
    return MathHelper.clamp(x, 20.0f, 300.0f);
  }

  @Method(0x800fa090L)
  public static float clampY(final float y) {
    return MathHelper.clamp(y, 20.0f, 220.0f);
  }
}
