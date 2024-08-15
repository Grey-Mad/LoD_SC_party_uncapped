package legend.game.modding.coremod.config;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;


import java.util.Arrays;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.game.inventory.screens.controls.NumberSpinner;
import legend.game.saves.ConfigCategory;
import legend.game.saves.ConfigEntry;
import legend.game.saves.ConfigStorageLocation;
import legend.game.sound.SoundFile;

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
    
      if(gameState_800babc8.charIds_88.length > val){//greytodo find better way to this? dyanmic array?
        int[] charIds_replacement = new int[val];
        java.lang.System.arraycopy(gameState_800babc8.charIds_88, 0, charIds_replacement, 0,val);
        for(int i=val; i<gameState_800babc8.charIds_88.length; i++){
          if (i != -1){
          gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 |= 0x2;}
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

      if(soundFiles_800bcf80.length > (val+10)){/*greytodo: see about replacing soundFiles_800bcf80 with a dyanimic array*/
        SoundFile[] soundFilesReplacement = new SoundFile[val+10]; 
        Arrays.setAll(soundFilesReplacement, i -> new SoundFile()); 
        java.lang.System.arraycopy(soundFiles_800bcf80, 0, soundFilesReplacement, 0, val+10);
        soundFiles_800bcf80 = soundFilesReplacement;
      }else if(soundFiles_800bcf80.length < (val+10)){
        SoundFile[] soundFilesReplacement = new SoundFile[val+10];
        Arrays.setAll(soundFilesReplacement, i -> new SoundFile()); 
        java.lang.System.arraycopy(soundFiles_800bcf80, 0, soundFilesReplacement, 0, soundFiles_800bcf80.length);
        soundFiles_800bcf80 = soundFilesReplacement;
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
    
    //unlockedUltimateAddition_800bc910 = new boolean[gameState_800babc8.charIds_88.length];
    //spGained_800bc950 = new int[gameState_800babc8.charIds_88.length];
    //livingCharIds_800bc968 = new int[gameState_800babc8.charIds_88.length];

    if(soundFiles_800bcf80.length > (gameState_800babc8.charIds_88.length+10)){ //4 sound files are for the monsters
      SoundFile[] soundFilesReplacement = new SoundFile[gameState_800babc8.charIds_88.length+10]; //4 sound files are for the monsters
      Arrays.setAll(soundFilesReplacement, i -> new SoundFile()); 
      java.lang.System.arraycopy(soundFiles_800bcf80, 0, soundFilesReplacement, 0, gameState_800babc8.charIds_88.length+10); //4 sound files are for the monsters
      soundFiles_800bcf80 = soundFilesReplacement;
    }else if(soundFiles_800bcf80.length < (gameState_800babc8.charIds_88.length+10)){ //4 sound files are for the monsters
      SoundFile[] soundFilesReplacement = new SoundFile[gameState_800babc8.charIds_88.length+10]; //4 sound files are for the monsters
      Arrays.setAll(soundFilesReplacement, i -> new SoundFile()); 
      java.lang.System.arraycopy(soundFiles_800bcf80, 0, soundFilesReplacement, 0, soundFiles_800bcf80.length); //4 sound files are for the monsters
      soundFiles_800bcf80 = soundFilesReplacement;
    }
  }
}
