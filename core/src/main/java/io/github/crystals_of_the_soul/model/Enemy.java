package io.github.crystals_of_the_soul.model;

/**
 * Classe base astratta per tutti i nemici del gioco.
 * Definisce l'interfaccia comune tra nemici normali e boss.
 */
public abstract class Enemy {

    protected float x, y;
    protected int hp;
    protected int maxHp;

    public Enemy(float x, float y, int maxHp) {
        this.x = x;
        this.y = y;
        this.hp = maxHp;
        this.maxHp = maxHp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    /**
     * Chiamato quando il giocatore attacca.
     * Le sottoclassi possono sovrascrivere per comportamenti speciali.
     */
    public abstract void onAttack();

    /**
     * Chiamato quando il giocatore parla.
     * Le sottoclassi possono sovrascrivere per comportamenti speciali.
     */
    public abstract void onTalk();

    /**
     * Restituisce il dialogo corrente del nemico.
     * Cambia in base allo stato interno — il giocatore deve interpretarlo.
     */
    public abstract String getDialogue();

    /**
     * Verifica se il nemico è stato sconfitto.
     * La condizione di vittoria è nascosta al giocatore.
     */
    public abstract boolean isDefeated();

    /**
     * Danno inflitto al giocatore durante il turno del nemico.
     */
    public abstract int getDamage();

    /**
     * Nome del nemico mostrato nell'UI durante la battaglia.
     */
    public abstract String getName();

    public int getHp() { return hp; }
    public float getX() { return x; }
    public float getY() { return y; }
}
