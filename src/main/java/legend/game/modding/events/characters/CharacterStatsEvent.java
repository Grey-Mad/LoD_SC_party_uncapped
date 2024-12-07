package legend.game.modding.events.characters;

import legend.game.characters.CharacterData;
import legend.game.types.LevelStuff08;
import legend.game.types.MagicStuff08;
import org.legendofdragoon.modloader.events.Event;

import static legend.game.SItem.levelStuff_80111cfc;
import static legend.game.SItem.magicStuff_80111d20;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class CharacterStatsEvent extends Event {
  public final int characterId;

  // Basic stats
  public int xp;
  public int hp;
  public int mp;
  public int sp;
  public int dxp;
  public int flags;
  public int level;
  public int dlevel;

  // Current level stats
  public int maxHp;
  /** The addition that unlocks at this level, if there is one */
  public int addition;
  public int bodySpeed;
  public int bodyAttack;
  public int bodyMagicAttack;
  public int bodyDefence;
  public int bodyMagicDefence;

  // Current dlevel stats
  public int maxMp;
  /** The spell that unlocks at this dlevel, if there is one */
  public int spellId;
  public int dragoonAttack;
  public int dragoonMagicAttack;
  public int dragoonDefence;
  public int dragoonMagicDefence;

  public CharacterStatsEvent(final int characterId) {
    this.characterId = characterId;

    final CharacterData charData = gameState_800babc8.charData_32c.get(characterId);
    this.xp = charData.getXp();
    this.hp = charData.getHp();
    this.mp = charData.getMp();
    this.sp = charData.getSp();
    this.dxp = charData.getDlevelXp();
    this.flags = charData.getStatus();
    this.level = charData.getLevel();
    this.dlevel = charData.getDlevel();

    final LevelStuff08 levelStuff = levelStuff_80111cfc[characterId][this.level];
    this.maxHp = levelStuff.hp_00;
    this.addition = levelStuff.addition_02;
    this.bodySpeed = levelStuff.bodySpeed_03;
    this.bodyAttack = levelStuff.bodyAttack_04;
    this.bodyMagicAttack = levelStuff.bodyMagicAttack_05;
    this.bodyDefence = levelStuff.bodyDefence_06;
    this.bodyMagicDefence = levelStuff.bodyMagicDefence_07;

    final MagicStuff08 magicStuff = magicStuff_80111d20[characterId][this.dlevel];
    this.maxMp = magicStuff.mp_00;
    this.spellId = magicStuff.spellIndex_02;
    this.dragoonAttack = magicStuff.dragoonAttack_04;
    this.dragoonMagicAttack = magicStuff.dragoonMagicAttack_05;
    this.dragoonDefence = magicStuff.dragoonDefence_06;
    this.dragoonMagicDefence = magicStuff.dragoonMagicDefence_07;
  }
}
