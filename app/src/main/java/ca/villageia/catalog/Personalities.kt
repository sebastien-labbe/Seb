package ca.villageia.catalog

data class Personality(val id: String, val label: String)

object Personalities {
    val all = listOf(
        Personality("chaleureux", "Chaleureux"),
        Personality("direct", "Direct"),
        Personality("patient", "Patient"),
        Personality("enjoue", "Enjoué"),
        Personality("calme", "Calme"),
        Personality("pedagogue", "Pédagogue"),
        Personality("motivateur", "Motivateur"),
        Personality("humoristique", "Humoristique"),
        Personality("rigoureux", "Rigoureux"),
    )
}

object PersonNames {
    val all = listOf(
        "Marie", "Luc", "Sophie", "Marc", "Julie", "Pierre", "Isabelle", "Jean",
        "Nadia", "Simon", "Camille", "Étienne", "Léa", "Mathieu", "Chloé", "Olivier",
    )
}
