package ca.villageia.catalog

data class Expertise(val id: String, val label: String, val category: String)

object Expertises {
    private fun cat(name: String, vararg p: Pair<String, String>) = p.map { Expertise(it.first, it.second, name) }

    val all: List<Expertise> = listOf(
        cat("Proches",
            "ami" to "Ami·e", "confident" to "Confident·e", "grand_parent" to "Grand-parent",
            "partenaire_entrainement" to "Partenaire d'entraînement", "compagnon_jeu" to "Compagnon de jeu"),
        cat("Éducation",
            "enseignant_primaire" to "Enseignant au primaire", "enseignant_secondaire" to "Enseignant au secondaire",
            "tuteur_maths" to "Tuteur en mathématiques", "prof_langues" to "Professeur de langues",
            "orthopedagogue" to "Orthopédagogue", "professeur_univ" to "Professeur d'université",
            "bibliothecaire" to "Bibliothécaire"),
        cat("Maison et métiers",
            "plombier" to "Plombier", "electricien" to "Électricien", "menuisier" to "Menuisier",
            "couvreur" to "Couvreur", "paysagiste" to "Paysagiste", "peintre" to "Peintre en bâtiment",
            "specialiste_piscine" to "Spécialiste en entretien de piscine",
            "entrepreneur_general" to "Entrepreneur général", "reparateur_electromenager" to "Réparateur d'électroménagers"),
        cat("Auto et mécanique",
            "mecanicien_auto" to "Mécanicien automobile", "carrossier" to "Carrossier",
            "mecanicien_petits_moteurs" to "Mécanicien de petits moteurs", "conseiller_auto" to "Conseiller en achat de véhicule"),
        cat("Santé",
            "medecin_famille" to "Médecin de famille", "infirmier" to "Infirmier", "pharmacien" to "Pharmacien",
            "nutritionniste" to "Nutritionniste", "physiotherapeute" to "Physiothérapeute",
            "psychologue" to "Psychologue", "dentiste" to "Dentiste"),
        cat("Finances et droit",
            "comptable" to "Comptable", "planificateur_financier" to "Planificateur financier",
            "fiscaliste" to "Fiscaliste", "avocat" to "Avocat", "notaire" to "Notaire",
            "courtier_assurances" to "Courtier d'assurances"),
        cat("Technologie",
            "technicien_ti" to "Technicien TI", "programmeur" to "Programmeur",
            "reparateur_electronique" to "Réparateur d'électronique", "cybersecurite" to "Spécialiste en cybersécurité"),
        cat("Cuisine",
            "chef_cuisinier" to "Chef cuisinier", "patissier" to "Pâtissier", "sommelier" to "Sommelier"),
        cat("Mode et image",
            "conseiller_mode" to "Conseiller en mode", "coiffeur" to "Coiffeur", "maquilleur" to "Maquilleur"),
        cat("Sport et bien-être",
            "coach_entrainement" to "Coach d'entraînement", "professeur_yoga" to "Professeur de yoga",
            "coach_vie" to "Coach de vie"),
        cat("Bureau",
            "secretaire" to "Secrétaire", "gestionnaire_projet" to "Gestionnaire de projet",
            "redacteur" to "Rédacteur", "traducteur" to "Traducteur",
            "ressources_humaines" to "Conseiller en ressources humaines"),
        cat("Sciences",
            "scientifique" to "Scientifique", "ingenieur" to "Ingénieur",
            "historien" to "Historien", "philosophe" to "Philosophe"),
        cat("Arts et loisirs",
            "musicien" to "Musicien", "artiste" to "Artiste", "photographe" to "Photographe",
            "conteur" to "Conteur", "guide_voyage" to "Guide de voyage",
            "animateur_loisirs" to "Animateur de loisirs", "guide_plein_air" to "Guide de plein air",
            "concierge" to "Concierge"),
    ).flatten()

    val byCategory: Map<String, List<Expertise>> = all.groupBy { it.category }
    fun label(id: String) = all.firstOrNull { it.id == id }?.label ?: id
}
