package ca.villageia.catalog

data class Expertise(val id: String, val label: String)

// Liste partielle : seulement les expertises utilisées par défaut. À étendre (~120, voir SPEC §7).
object Expertises {
    val all = listOf(
        Expertise("ami", "Ami·e"),
        Expertise("coach_vie", "Coach de vie"),
        Expertise("confident", "Confident·e"),
        Expertise("mecanicien_auto", "Mécanicien automobile"),
        Expertise("conseiller_auto", "Conseiller en achat de véhicule"),
        Expertise("gestionnaire_projet", "Gestionnaire de projet"),
        Expertise("bibliothecaire", "Bibliothécaire"),
        Expertise("enseignant_primaire", "Enseignant au primaire"),
        Expertise("professeur_univ", "Professeur d'université"),
        Expertise("avocat", "Avocat"),
        Expertise("medecin_famille", "Médecin de famille"),
        Expertise("infirmier", "Infirmier"),
        Expertise("pharmacien", "Pharmacien"),
        Expertise("nutritionniste", "Nutritionniste"),
        Expertise("comptable", "Comptable"),
        Expertise("conseiller_mode", "Conseiller en mode"),
        Expertise("chef_cuisinier", "Chef cuisinier"),
        Expertise("animateur_loisirs", "Animateur de loisirs"),
        Expertise("guide_plein_air", "Guide de plein air"),
        Expertise("coach_entrainement", "Coach d'entraînement"),
        Expertise("guide_voyage", "Guide de voyage"),
        Expertise("concierge", "Concierge"),
    )
    fun label(id: String) = all.firstOrNull { it.id == id }?.label ?: id
}
