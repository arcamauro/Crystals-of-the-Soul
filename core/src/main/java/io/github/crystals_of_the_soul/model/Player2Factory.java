package io.github.crystals_of_the_soul.model;

public class Player2Factory {

    public static Player2 create(Player2State state) {
        Player2Behavior b = createBehavior(state.playerClass);
        return new Player2(state, b);
    }

    public static Player2Behavior createBehavior(Player2Class cls) {
        if (cls == null) return (p2, d, c) -> {};
        switch (cls) {
            case ASSASSIN: return new AssassinBehavior();
            case ARCHER:   return new ArcherBehavior();
            case PROTECTOR:return new ProtectorBehavior();
            default:       return (p2, d, c) -> {};
        }
    }
}
