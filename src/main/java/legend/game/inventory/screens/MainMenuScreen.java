package legend.game.inventory.screens;

import legend.game.EngineStateEnum;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.inventory.screens.controls.DragoonSpirits;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.types.MessageBoxResult;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.RENDERER;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.canSave_8011dc88;
import static legend.game.SItem.chapterNames_80114248;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.submapNames_8011c108;
import static legend.game.SItem.worldMapNames_8011c1ec;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getTimestampPart;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8004.engineState_8004dd20;
import static legend.game.Scus94491BpeSegment_8005.standingInSavePoint_8005a368;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.fullScreenEffect_800bb140;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.loadingNewGameState_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba4;
import static legend.game.Scus94491BpeSegment_800b.renderablePtr_800bdba8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.submapId_800bd808;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  private final CharacterCard[] charCards = new CharacterCard[3];
  private final List<Button> menuButtons = new ArrayList<>();

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;

    loadingNewGameState_800bdc34 = false;
    loadCharacterStats();

    if(engineState_8004dd20 == EngineStateEnum.WORLD_MAP_08) {
      gameState_800babc8.isOnWorldMap_4e4 = true;
      canSave_8011dc88 = true;
    } else {
      gameState_800babc8.isOnWorldMap_4e4 = false;
      canSave_8011dc88 = CONFIG.getConfig(CoreMod.SAVE_ANYWHERE_CONFIG.get()) || standingInSavePoint_8005a368;
    }

    this.addControl(new Background());
    this.addControl(Glyph.glyph(71)).setPos( 16,  16); // Chapter box
    this.addControl(Glyph.glyph(72)).setPos( 18,  60); // Menu box
    this.addControl(Glyph.glyph(73)).setPos( 19, 175); // Bottom box
    this.addControl(Glyph.glyph(75)).setPos(194,  83); // Line between char 0 and 1
    this.addControl(Glyph.glyph(75)).setPos(194, 155); // Line between char 1 and 2

    this.addControl(new DragoonSpirits(gameState_800babc8.goods_19c[0])).setPos(40, 197); // Dragoon spirits

    this.addButton("Use Item", this::showUseItemScreen);
    this.addButton("Equipment", this::showEquipmentScreen);
    this.addButton("Inventory", this::showItemListScreen);
    this.addButton("Goods", this::showGoodsScreen);
    this.addButton("Diiig", this::showDabasScreen);
    this.addButton("", () -> { }).hide();
    this.addButton("Quit", () -> menuStack.pushScreen(new MessageBoxScreen("Quit to main menu?", 2, result -> {
      if(result == MessageBoxResult.YES) {
        this.menuEscape();
        whichMenu_800bdc38 = WhichMenu.QUIT;
      }
    })));
    this.addButton("Status", this::showStatusScreen);
    this.addButton("Addition", this::showAdditionsScreen);
    this.addButton("Replace", this::showCharSwapScreen);
    this.addButton("Options", this::showOptionsScreen);
    this.addButton("", () -> { }).hide();
    this.addButton("", () -> { }).hide();
    this.addButton("Save", this::showSaveScreen).setDisabled(!canSave_8011dc88);

    for(int i = 0; i < 3; i++) {
      this.addCharCard(i);
    }

    this.setFocus(this.menuButtons.getFirst());
  }

  private Button addButton(final String text, final Runnable onClick) {
    final int index = this.menuButtons.size();

    final Button button = this.addControl(new Button(text));
    button.setPos(21 + index / 7 * 74, 79 + (index % 7) * 13);
    button.setWidth(72);

    button.onHoverIn(() -> {
      playMenuSound(1);
      this.setFocus(button);
    });

    button.onLostFocus(() -> button.setTextColour(TextColour.BROWN));
    button.onGotFocus(() -> button.setTextColour(TextColour.RED));

    button.onPressed(onClick::run);

    button.onPressedWithRepeatPulse(inputAction -> {
      switch(inputAction) {
        case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
          for(int i = 1; i < this.menuButtons.size(); i++) {
            final Button otherButton = this.menuButtons.get(Math.floorMod(index + i, this.menuButtons.size()));

            if(!otherButton.isDisabled() && otherButton.isVisible()) {
              playMenuSound(1);
              this.setFocus(otherButton);
              break;
            }
          }
        }
        case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> {
          for(int i = 1; i < this.menuButtons.size(); i++) {
            final Button otherButton = this.menuButtons.get(Math.floorMod(index - i, this.menuButtons.size()));

            if(!otherButton.isDisabled() && otherButton.isVisible()) {
              playMenuSound(1);
              this.setFocus(otherButton);
              break;
            }
          }
        }
        case DPAD_RIGHT, JOYSTICK_LEFT_BUTTON_RIGHT -> {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index + this.menuButtons.size() / 2, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            this.setFocus(otherButton);
          }
        }
        case DPAD_LEFT, JOYSTICK_LEFT_BUTTON_LEFT -> {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index - this.menuButtons.size() / 2, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            playMenuSound(1);
            this.setFocus(otherButton);
          }
        }
      }

      return InputPropagation.HANDLED;
    });

    this.menuButtons.add(button);
    return button;
  }

  private void addCharCard(final int slot) {
    final int id = gameState_800babc8.charIds_88[slot];
    this.charCards[slot] = this.addControl(new CharacterCard(id));
    this.charCards[slot].setPos(186, 16 + slot * 72);
  }

  @Override
  public void setFocus(@Nullable final Control control) {
    // Don't allow complete unfocusing
    if(control instanceof Button) {
      super.setFocus(control);
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        cacheCharacterSlots();
        startFadeEffect(2, 10);

        for(int i = 0; i < 3; i++) {
          this.charCards[i].setCharId(gameState_800babc8.charIds_88[i]);
        }

        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);

        this.renderInventoryMenu(0xff);
        this.loadingStage++;
      }

      case 2 -> this.renderInventoryMenu(0);

      // Fade out
      case 100 -> {
        this.renderInventoryMenu(0);
        startFadeEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        this.renderInventoryMenu(0);

        if(fullScreenEffect_800bb140.currentColour_28 >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  private void renderInventoryMenu(final long a2) {
    final boolean allocate = a2 == 0xff;
    if(allocate) {
      renderCharacter(146, 184, 10);
      renderCharacter(164, 184, 10);
    }

    this.renderNumber( 67, 184, gameState_800babc8.gold_94, 8); // Gold
    this.renderNumber(166, 204, gameState_800babc8.stardust_9c, 2); // Stardust
    this.renderNumber(128, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 0), 3);
    this.renderNumber(152, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 1), 2, 0x1);
    this.renderNumber(170, 184, getTimestampPart(gameState_800babc8.timestamp_a0, 2), 2, 0x1);
    renderText(chapterNames_80114248[gameState_800babc8.chapterIndex_98], 94, 24, UI_TEXT_CENTERED);

    final String name;
    if(engineState_8004dd20 == EngineStateEnum.SUBMAP_05) {
      name = submapNames_8011c108[submapId_800bd808];
    } else {
      name = worldMapNames_8011c1ec[continentIndex_800bf0b0];
    }

    // The retail lines between the buttons are too short, so we just draw more line where the texture ends
    final Matrix4f transforms = new Matrix4f();
    for(int i = 0; i < 6; i++) {
      final int x = 106;
      final int y = 93 + i * 13;
      RENDERER.queueLine(transforms, textZ_800bdf00 * 4.0f, new Vector2f(x, y), new Vector2f(x + 59, y))
        .translucency(Translucency.B_MINUS_F)
        .monochrome(0.05f)
      ;
    }

    renderText(name, 90, 38, UI_TEXT_CENTERED);
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void fadeOutArrows() {
    if(renderablePtr_800bdba4 != null) {
      fadeOutArrow(renderablePtr_800bdba4);
      renderablePtr_800bdba4 = null;
    }

    //LAB_800fca40
    if(renderablePtr_800bdba8 != null) {
      fadeOutArrow(renderablePtr_800bdba8);
      renderablePtr_800bdba8 = null;
    }

    //LAB_800fca60
    if(saveListUpArrow_800bdb94 != null) {
      fadeOutArrow(saveListUpArrow_800bdb94);
      saveListUpArrow_800bdb94 = null;
    }

    //LAB_800fca80
    if(saveListDownArrow_800bdb98 != null) {
      fadeOutArrow(saveListDownArrow_800bdb98);
      saveListDownArrow_800bdb98 = null;
    }
  }

  private void showStatusScreen() {
    this.showScreen(StatusScreen::new);
  }

  private void showEquipmentScreen() {
    this.showScreen(EquipmentScreen::new);
  }

  private void showAdditionsScreen() {
    this.showScreen(AdditionsScreen::new);
  }

  private void showCharSwapScreen() {
    this.showScreen(CharSwapScreen::new);
  }

  private void showOptionsScreen() {
    menuStack.pushScreen(new OptionsCategoryScreen(CONFIG, EnumSet.allOf(ConfigStorageLocation.class), () -> {
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));
      ConfigStorage.saveConfig(CONFIG, ConfigStorageLocation.CAMPAIGN, gameState_800babc8.campaign.path.resolve("campaign_config.dcnf"));
      menuStack.popScreen();

  


      
      this.loadingStage = 0;
    }));
  }


  
  private void showSaveScreen() {
    if(canSave_8011dc88) {
      menuStack.pushScreen(new SaveGameScreen(() -> {
        menuStack.popScreen();
        this.fadeOutArrows();
        this.loadingStage = 0;
      }));
    } else {
      playMenuSound(40);
    }
  }

  private void showUseItemScreen() {
    this.showScreen(UseItemScreen::new);
  }

  private void showItemListScreen() {
    this.showScreen(ItemListScreen::new);
  }

  private void showGoodsScreen() {
    this.showScreen(GoodsScreen::new);
  }

  private void showDabasScreen() {
    this.showScreen(DabasScreen::new);
  }

  private void showScreen(final Function<Runnable, MenuScreen> screen) {
    menuStack.pushScreen(screen.apply(() -> {
      menuStack.popScreen();
      this.loadingStage = 0;
    }));
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      if(inputAction == InputAction.BUTTON_EAST) {
        this.menuEscape();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }
}
