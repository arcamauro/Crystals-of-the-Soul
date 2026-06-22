package io.github.crystals_of_the_soul.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameHud {

    public interface Callbacks {
        void onAttack();
        void onTalk();
        void onResume();
        void onSave();
        void onMainMenu();
        void onExit();
    }

    private final Stage stage;
    private final Skin skin;

    private Table pauseTable;
    private Table battleTable;
    private Table hudTable;

    private Label playerHpLabel;
    private Label player2HpLabel;
    private Label interactLabel;
    private Label saveConfirmLabel;
    private Label enemyHpLabel;
    private Label dialogueLabel;

    private TextButton attackBtn;
    private TextButton talkBtn;

    private Label notificationLabel;
    private float notificationTimer;

    private boolean paused = false;

    public GameHud(int initialHp, Callbacks callbacks) {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        buildHud(initialHp);
        buildBattleUI(callbacks);
        buildPauseMenu(callbacks);

        stage.addActor(hudTable);
        stage.addActor(battleTable);
        stage.addActor(pauseTable);

        pauseTable.setVisible(false);
        battleTable.setVisible(false);
        interactLabel.setVisible(false);
    }

    private void buildHud(int initialHp) {
        hudTable = new Table();
        hudTable.setFillParent(true);
        hudTable.top().left().pad(10);

        playerHpLabel = new Label("HP: " + initialHp, skin);
        player2HpLabel = new Label("", skin);
        interactLabel = new Label("Premi E per interagire", skin);

        hudTable.add(playerHpLabel).row();
        hudTable.add(player2HpLabel).row();
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

    public void updatePlayer2Hp(String text) {
        player2HpLabel.setText(text);
    }

    public void updateEnemyHp(String text) {
        enemyHpLabel.setText(text);
    }

    public void updateDialogue(String text) {
        dialogueLabel.setText(text);
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
