package io.github.crystals_of_the_soul.states;

public enum EndingType {
    HERO(
        "Redenzione",
        "Hai espiato le tue colpe e quelle degli altri esiliati. " +
        "Il dungeon si apre davanti a te e il tuo villaggio ti accoglie come eroe. " +
        "Le anime liberate ti seguono verso la luce."
    ),
    CITIZEN(
        "Ritorno",
        "Hai superato il dungeon con saggezza. " +
        "Torni al tuo villaggio, non come eroe, ma come uomo comune. " +
        "Nessuno ricorderà il tuo nome, ma tu sai cosa hai fatto."
    ),
    WANDERER(
        "Esilio",
        "Hai lasciato troppe ombre nel dungeon. " +
        "Le porte del villaggio restano chiuse per te. " +
        "Vagherai tra i piani del dungeon per l'eternità, né vivo né morto."
    ),
    GUARDIAN(
        "Il Nuovo Guardiano",
        "Sei diventato ciò che temevi di più. " +
        "Il dungeon ti ha consumato e ora sei tu il suo guardiano. " +
        "Nessuno uscirà finché ci sei tu."
    ),
    CHOICE(
        "Il Bivio",
        "Il tuo cammino è stato equo — tante vite spezzate, tante risparmiate. " +
        "Il dungeon ti offre una scelta: tornare al mondo che ti ha esiliato, " +
        "o restare come guardiano di queste anime dimenticate."
    );

    private final String title;
    private final String narrative;

    EndingType(String title, String narrative) {
        this.title = title;
        this.narrative = narrative;
    }

    public String getTitle() { return title; }
    public String getNarrative() { return narrative; }
}