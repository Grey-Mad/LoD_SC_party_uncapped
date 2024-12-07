package legend.game.characters;

import static legend.core.GameEngine.CONFIG;

import legend.game.inventory.Equipment;
import legend.game.scripting.ScriptReadable;
import legend.game.types.EquipmentSlot;

import java.util.EnumMap;
import java.util.Map;
import legend.lodmod.LodMod;

import org.legendofdragoon.modloader.registries.RegistryEntry;
import org.legendofdragoon.modloader.registries.RegistryId;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;


public class CharacterData extends RegistryEntry implements ScriptReadable {
  public CharacterData() {
    super();
    this.equipment_14.clear();
    this.equipment_14.putAll(equipment_14);
    System.arraycopy(additionLevels_1a, 0, this.additionLevels_1a, 0, this.additionLevels_1a.length);
    System.arraycopy(additionXp_22, 0, this.additionXp_22, 0, this.additionXp_22.length);
    partyFlags_04 = 0x0;
  } 

  private int xp_00;
    /**
     * <ul>
     *   <li>0x1 - in party</li>
     *   <li>0x2 - can be put in main party (without this flag a char can only appear in secondary and can't be put into main)</li>
     *   <li>0x20 - can't remove (don't select, can't be taken out of main party)</li>
     *   <li>0x40 - ultimate addition unlocked</li>
     * </ul>
     */
  private int partyFlags_04;
  private int hp_08;
  private int mp_0a;
  private int sp_0c;
  private int dlevelXp_0e;
  /** i.e. poison */
  private int status_10;
  private int level_12;
  private int dlevel_13;
  /** Only used during loading */
  public Object2IntMap<EquipmentSlot> equipmentIds_14 = new Object2IntOpenHashMap<>();
  /** Only used during loading */
  public Map<EquipmentSlot, RegistryId> equipmentRegistryIds_14 = new EnumMap<>(EquipmentSlot.class);
  public Map<EquipmentSlot, Equipment> equipment_14 = new EnumMap<>(EquipmentSlot.class);
  public int selectedAddition_19;
  public int[] additionLevels_1a = new int[8];
  public int[] additionXp_22 = new int[8];

  private int charId;
  public String name;

  
  public int getXp() {
    return this.xp_00;
  }
  public void setXp(final int xp) {
    this.xp_00 = xp;
  }

  public int getPartyFlags() {
    return this.partyFlags_04;
  }
  public void setPartyFlagsFromSerializer(final int partyFlags) {
    this.partyFlags_04 = partyFlags;
  }
  public void setPartyFlags(final int partyFlags) {
    this.partyFlags_04 = partyFlags;

    String name = this.getRegistryId().entryId();
    if ((boolean) CONFIG.getConfig(legend.core.GameEngine.REGISTRIES.config.getEntry("lod", String.join("","force_remove_and_block_", name)).get())){
      this.partyFlags_04=0x0;
    };
    
    if (this.partyFlags_04 != 0x23){
      if ((boolean) CONFIG.getConfig(legend.core.GameEngine.REGISTRIES.config.getEntry("lod", String.join("","force_add_and_keep_", name)).get())){
        this.partyFlags_04=0x3;
      };   
    }
    
  }
  public int getHp() {
    return this.hp_08;
  }
  public void setHp(final int hp) {
    this.hp_08 = hp;
  }
  public int getMp() {
    return this.mp_0a;
  }
  public void setMp(final int mp) {
    this.mp_0a = mp;
  }
  public int getSp() {
    return this.sp_0c;
  }
  public void setSp(final int sp) {
    this.sp_0c = sp;
  }  
  public int getDlevelXp() {
    return this.dlevelXp_0e;
  }
  public void setDlevelXp(final int dlevelxp) {
    this.dlevelXp_0e = dlevelxp;
  }  

  public int getStatus() {
    return this.status_10;
  }
  public void setStatus(final int status) {
    this.status_10 = status;
  }  
  public int getLevel() {
    return this.level_12;
  }
  public void setLevel(final int level) {
    this.level_12 = level;
  }  
  public void levelUp() {
    this.level_12++;
  }  

  public int getDlevel() {
    return this.dlevel_13;
  }
  public void setDlevel(final int dlevel) {
    this.dlevel_13 = dlevel;
  }
  public void dlevelUp() {
    this.dlevel_13++;
  }  

  public void setCharId(final int charId) {
    this.charId = charId;
    this.name = LodMod.CHARACTER_NAMES[charId];
  }  
  public int getCharId() {
    return this.charId;
  }

  public void setCharacterData(CharacterData data) {
    this.xp_00 = data.getXp();
    this.partyFlags_04 =data.getPartyFlags();
    this.hp_08 =data.getHp();
    this.mp_0a =data.getMp();
    this.sp_0c =data.getSp();
    this.dlevelXp_0e =data.getDlevelXp();
    this.status_10 =data.getStatus();
    this.level_12 =data.getLevel();
    this.dlevel_13 =data.getDlevel();
    /** Only used during loading */
    this.equipmentIds_14 = data.equipmentIds_14;
    /** Only used during loading */
    this.equipmentRegistryIds_14 = data.equipmentRegistryIds_14;
    this.equipment_14 = data.equipment_14;
    this.selectedAddition_19 = data.selectedAddition_19;
    this.additionLevels_1a = data.additionLevels_1a;
    this.additionXp_22 = data.additionXp_22;
    this.charId=  data.charId;
    this.name= data.name; 
  }


}
