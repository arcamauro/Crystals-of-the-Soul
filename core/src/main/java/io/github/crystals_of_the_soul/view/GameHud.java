package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameHud {

    public interface Callbacks {
        void onAttack();
        void onTalk();
        void onResume();
        void onSave();
        void onMainMenu();
        void onExit();
        void onBuyItem(String itemName, int price);
        void onCloseShop();
        void onCrystalContinue();
    }

    private final Stage stage;
    private final Skin skin;

    private Table pauseTable;
    private Table battleTable;
    private Table hudTable;
    private Table shopTable;
    private Table crystalDialogueTable;
    private Label crystalDialogueLabel;

    private Label playerHpLabel;
    private Label playerStatsLabel;
    private Label player2HpLabel;
    private Label goldLabel;
    private Label interactLabel;
    private Label saveConfirmLabel;
    private Label enemyHpLabel;
    private Label dialogueLabel;

    private TextButton attackBtn;
    private TextButton talkBtn;
    private TextButton buyPotionBtn;
    private TextButton buySwordBtn;
    private TextButton buyArmorBtn;
    private TextButton exitShopBtn;

    private Label shopTitleLabel;
    private Label shopWelcomeLabel;
    private String activeItemName;
    private int activePotionPrice;
    private int activeSwordPrice;
    private int activeArmorPrice;

    private Label notificationLabel;
    private float notificationTimer;

    private boolean paused = false;

    public GameHud(int initialHp, Callbacks callbacks) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        buildHud(initialHp);
        buildBattleUI(callbacks);
        buildPauseMenu(callbacks);
        buildShopUI(callbacks);
        buildCrystalDialogue(callbacks);

        stage.addActor(hudTable);
        stage.addActor(battleTable);
        stage.addActor(pauseTable);
        stage.addActor(shopTable);
        stage.addActor(crystalDialogueTable);

        pauseTable.setVisible(false);
        battleTable.setVisible(false);
        shopTable.setVisible(false);
        interactLabel.setVisible(false);
        crystalDialogueTable.setVisible(false);
    }

    private void buildHud(int initialHp) {
        hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(10);

        playerHpLabel = new Label("HP: " + initialHp, skin);
        playerStatsLabel = new Label("ATK: 10 | DEF: 0", skin);
        player2HpLabel = new Label("", skin);
        goldLabel = new Label("Oro: 20", skin);
        interactLabel = new Label("Premi E per interagire", skin);

        hudTable.add(playerHpLabel).row();
        hudTable.add(playerStatsLabel).row();
        hudTable.add(player2HpLabel).row();
        hudTable.add(goldLabel).row();
        hudTable.add(interactLabel);

        notificationLabel = new Label("", skin);
        notificationLabel.setVisible(false);

        hudTable.add(notificationLabel).padTop(10).row();
    }
    public void showNotification(String text) {

        notificationLabel.setText(text);
        notificationLabel.setVisible(true);

        notificationTimer = 2f;
    }
    private void buildBattleUI(final Callbacks callbacks) {
        battleTable = new Table();
        battleTable.setFillParent(true);
        battleTable.bottom();

        dialogueLabel = new Label("", skin);
        dialogueLabel.setWrap(true);
        enemyHpLabel = new Label("", skin);

        attackBtn = new TextButton("Attacca", skin);
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onAttack();
            }
        });

        talkBtn = new TextButton("Parla", skin);
        talkBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onTalk();
            }
        });

        battleTable.add(dialogueLabel).width(500).padBottom(10).colspan(2).row();
        battleTable.add(enemyHpLabel).padBottom(15).colspan(2).row();
        battleTable.add(attackBtn).width(200).height(50).padRight(20).padBottom(20);
        battleTable.add(talkBtn).width(200).height(50).padBottom(20);
    }

    private void buildCrystalDialogue(final Callbacks callbacks) {
        crystalDialogueTable = new Table();
        crystalDialogueTable.setFillParent(true);
        crystalDialogueTable.center();

        crystalDialogueLabel = new Label("", skin);
        crystalDialogueLabel.setWrap(true);
        crystalDialogueLabel.setAlignment(Align.center);

        TextButton continueBtn = new TextButton("Continue", skin);
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onCrystalContinue();
            }
        });

        crystalDialogueTable.add(crystalDialogueLabel).width(520).padBottom(24).row();
        crystalDialogueTable.add(continueBtn).width(200).height(50);
    }

    public void showCrystalDialogue(String message) {
        crystalDialogueLabel.setText(message);
        crystalDialogueTable.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void hideCrystalDialogue() {
        crystalDialogueTable.setVisible(false);
        Gdx.input.setInputProcessor(null);
    }

    public boolean isCrystalDialogueShowing() {
        return crystalDialogueTable.isVisible();
    }

    private void buildPauseMenu(final Callbacks callbacks) {
        pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();

        Label title = new Label("Paused", skin);
        title.setFontScale(2f);

        TextButton resumeBtn = new TextButton("Resume", skin);
        resumeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onResume();
            }
        });

        TextButton saveBtn = new TextButton("Save Game", skin);
        saveBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onSave();
            }
        });

        TextButton menuBtn = new TextButton("Main Menu", skin);
        menuBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onMainMenu();
            }
        });

        TextButton exitBtn = new TextButton("Exit Game", skin);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onExit();
            }
        });

        saveConfirmLabel = new Label("Game Saved!", skin);
        saveConfirmLabel.setVisible(false);

        pauseTable.add(title).padBottom(40).row();
        pauseTable.add(resumeBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(saveBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(menuBtn).width(200).height(50).padBottom(15).row();
        pauseTable.add(exitBtn).width(200).height(50).padBottom(20).row();
        pauseTable.add(saveConfirmLabel);
    }

    public void setAttackEnabled(boolean enabled) {
        attackBtn.setVisible(enabled);
    }

    public void setTalkEnabled(boolean enabled) {
        talkBtn.setVisible(enabled);
    }

    public void showBattle() {
        battleTable.setVisible(true);
    }

    public void hideBattle() {
        battleTable.setVisible(false);
    }

    public void updatePlayerHp(int hp) {
        playerHpLabel.setText("HP: " + hp);
    }

    public void updatePlayerStats(int attack, int defense) {
        playerStatsLabel.setText("ATK: " + attack + " | DEF: " + defense);
    }

    public void updatePlayer2Hp(String text) {
        player2HpLabel.setText(text);
    }

    public void updateEnemyHp(String text) {
        enemyHpLabel.setText(text);
    }

    public void updateDialogue(String text) {
        dialogueLabel.setText(text);
    }

    private void buildShopUI(final Callbacks callbacks) {
        shopTable = new Table();
        shopTable.setFillParent(true);
        shopTable.center();

        shopTitleLabel = new Label("Mercante", skin);
        shopTitleLabel.setFontScale(1.5f);
        shopWelcomeLabel = new Label("Benvenuto!", skin);
        shopWelcomeLabel.setWrap(true);

        buyPotionBtn = new TextButton("Compra", skin);
        buyPotionBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onBuyItem(activeItemName, activePotionPrice);
            }
        });

        buySwordBtn = new TextButton("Compra Spada", skin);
        buySwordBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onBuyItem("Spada", activeSwordPrice);
            }
        });

        buyArmorBtn = new TextButton("Compra Armatura", skin);
        buyArmorBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onBuyItem("Armatura", activeArmorPrice);
            }
        });

        exitShopBtn = new TextButton("Esci", skin);
        exitShopBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callbacks.onCloseShop();
            }
        });

        shopTable.add(shopTitleLabel).padBottom(10).row();
        shopTable.add(shopWelcomeLabel).width(400).padBottom(20).row();
        shopTable.add(buyPotionBtn).width(300).height(45).padBottom(10).row();
        shopTable.add(buySwordBtn).width(300).height(45).padBottom(10).row();
        shopTable.add(buyArmorBtn).width(300).height(45).padBottom(10).row();
        shopTable.add(exitShopBtn).width(150).height(40);
    }

    public void showShop(String title, String welcomeText, String itemName, int potionPrice, int swordPrice, int armorPrice) {
        shopTitleLabel.setText(title);
        shopWelcomeLabel.setText(welcomeText);
        buyPotionBtn.setText("Compra " + itemName + " (" + potionPrice + " Oro)");
        buySwordBtn.setText("Compra Spada (+5 ATK) (" + swordPrice + " Oro)");
        buyArmorBtn.setText("Compra Armatura (+5 DEF) (" + armorPrice + " Oro)");
        this.activeItemName = itemName;
        this.activePotionPrice = potionPrice;
        this.activeSwordPrice = swordPrice;
        this.activeArmorPrice = armorPrice;
        shopTable.setVisible(true);
    }

    public void hideShop() {
        shopTable.setVisible(false);
    }

    public void updateGold(int gold) {
        goldLabel.setText("Oro: " + gold);
    }

    public void setInteractVisible(boolean visible) {
        interactLabel.setVisible(visible);
    }

    public void showSaveConfirm() {
        saveConfirmLabel.setVisible(true);
    }

    public void togglePause() {
        paused = !paused;
        pauseTable.setVisible(paused);
        saveConfirmLabel.setVisible(false);
    }

    public void resetPause() {
        paused = false;
        pauseTable.setVisible(false);
        saveConfirmLabel.setVisible(false);
    }
    private void updateNotifications(float delta) {

        if (notificationTimer <= 0) {
            return;
        }

        notificationTimer -= delta;

        if (notificationTimer <= 0) {

            notificationLabel.setVisible(false);
        }
    }
    public boolean isPaused() { return paused; }

    public Stage getStage() { return stage; }

    public void act(float delta) {updateNotifications(delta); stage.act(delta); }

    public void draw() { stage.draw(); }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
