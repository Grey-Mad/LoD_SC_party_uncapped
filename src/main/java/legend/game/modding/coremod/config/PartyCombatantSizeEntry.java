package legend.game.modding.coremod.config;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.livingCharIds_800bc968;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.unlockedUltimateAddition_800bc910;

import java.util.ArrayList;
import java.util.Arrays;

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
      final NumberSpinner<Integer> spinner = NumberSpinner.intSpinner(number, 3, 9);
      spinner.onChange(val -> gameState.setConfig(this, val));
      return spinner;
    });
  }

  private static byte[] serializer(final int val) {
    final byte[] data = new byte[4];
    MathHelper.set(data, 0, 4, val);
    
      if(gameState_800babc8.charIds_88.length > val){//greytodo find better way to this? dyanmic array?
        int[] charIds_replacement = new int[val];
        java.lang.System.arraycopy(gameState_800babc8.charIds_88, 0, charIds_replacement, 0,val);
        for(int i=val; i<gameState_800babc8.charIds_88.length; i++){
          if (i != -1 && gameState_800babc8.charIds_88[i] != -1){
            gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 = (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 | 0x2);}
        }
        gameState_800babc8.charIds_88 = charIds_replacement;
        updateStateGamestateCharIdsDepentedValues();
      }else if(gameState_800babc8.charIds_88.length < val){
        int[] charIds_replacement = new int[val];
        java.lang.System.arraycopy(gameState_800babc8.charIds_88, 0, charIds_replacement, 0, gameState_800babc8.charIds_88.length);
        for(int i=gameState_800babc8.charIds_88.length; i<val; i++){
          charIds_replacement[i]=-1;
        }
        gameState_800babc8.charIds_88 = charIds_replacement;
        updateStateGamestateCharIdsDepentedValues();
      }
      
    return data;
  }

  private static int deserializer(final byte[] data) {
   
    //legend.game.Scus94491BpeSegment_800b.gameState_800babc8

    if(data.length == 4) {
      return IoHelper.readInt(data, 0);
    }
    return 3;
  }

  public static void callOnChangeValued(final String entryId) {
   
  }

  private static void updateStateGamestateCharIdsDepentedValues() { 
    unlockedUltimateAddition_800bc910=new ArrayList<Boolean>(Arrays.asList(new Boolean[gameState_800babc8.charIds_88.length]));
    spGained_800bc950=new ArrayList<Integer>(Arrays.asList(new Integer[gameState_800babc8.charIds_88.length]));
    livingCharIds_800bc968=new ArrayList<Integer>(Arrays.asList(new Integer[gameState_800babc8.charIds_88.length]));    
  }
}
