package ca.villageia.catalog

enum class Category(val label: String) {
    HABITATIONS("Habitations"), GARAGES("Garages"), COMMERCES("Commerces"),
    SERVICES("Services publics"), SANTE("Santé"), LOISIRS("Loisirs & nature"),
    TRANSPORT("Transport & hébergement"), SPIRITUEL("Spirituel"),
}

/** Les emojis sont des visuels provisoires, à remplacer par les illustrations finales. */
data class BuildingType(
    val id: String,
    val category: Category,
    val emoji: String,
    val defaultName: String,
    val defaultInstructions: String,
    val defaultExpertiseId: String,
)

object BuildingCatalog {
    private fun variants(
        prefix: String, cat: Category, emojis: List<String>,
        name: String, instr: String, expertise: String,
    ) = emojis.mapIndexed { i, e -> BuildingType("${prefix}_${'a' + i}", cat, e, name, instr, expertise) }

    private fun one(
        id: String, cat: Category, emoji: String,
        name: String, instr: String, expertise: String,
    ) = listOf(BuildingType(id, cat, emoji, name, instr, expertise))

    val all: List<BuildingType> = listOf(
        variants("maison", Category.HABITATIONS, listOf("🏠", "🏡", "🏘️", "🛖"), "Notre domicile",
            "Dans ce projet, je veux parler à des spécialistes qui connaîtront ma vie et m'aideront dans les projets à la maison : aider avec les devoirs des enfants, cuisiner, faire mes impôts, mes réclamations d'assurance, gérer mes factures, entretenir la maison et la piscine.",
            "ami"),
        variants("bloc", Category.HABITATIONS, listOf("🏢", "🏬", "🏣", "🏤"), "Mon logement",
            "Dans ce projet, je veux de l'aide pour la vie en logement : bail, voisinage, budget, aménagement de petits espaces.",
            "coach_vie"),
        variants("garage", Category.GARAGES, listOf("🔧", "🚘", "🛠️", "🅿️"), "Mon garage",
            "Dans ce projet, je veux de l'aide pour mes véhicules, mes outils, le bricolage et mes projets d'atelier.",
            "mecanicien_auto"),
        variants("commerce", Category.COMMERCES, listOf("🏪", "🏦", "🏭", "🏗️"), "Mon entreprise",
            "Dans ce projet, je veux de l'aide pour mon entreprise : stratégie, marketing, ventes et gestion.",
            "gestionnaire_projet"),
        one("bibliotheque", Category.SERVICES, "📚", "La bibliothèque",
            "Dans ce projet, je veux de l'aide pour lire, chercher de l'information, résumer et trouver des livres.",
            "bibliothecaire"),
        one("ecole", Category.SERVICES, "🏫", "L'école",
            "Dans ce projet, je veux de l'aide pour les études primaires et secondaires, avec des explications adaptées à l'âge.",
            "enseignant_primaire"),
        one("universite", Category.SERVICES, "🎓", "L'université",
            "Dans ce projet, je veux m'instruire sur différents sujets, en profondeur.",
            "professeur_univ"),
        one("justice", Category.SERVICES, "⚖️", "Le palais de justice",
            "Dans ce projet, je veux comprendre mes droits et les démarches juridiques. Information générale seulement, pas d'avis légal.",
            "avocat"),
        one("clinique", Category.SANTE, "🏥", "La clinique",
            "Dans ce projet, je veux parler de santé courante et préparer mes rendez-vous. Pas de diagnostic.",
            "medecin_famille"),
        one("hopital", Category.SANTE, "🚑", "L'hôpital",
            "Dans ce projet, je veux comprendre une condition ou un traitement et accompagner un proche.",
            "infirmier"),
        one("pharmacie", Category.SANTE, "💊", "La pharmacie",
            "Dans ce projet, je veux de l'information sur les médicaments, les interactions et les produits en vente libre. Je vérifierai avec un pharmacien.",
            "pharmacien"),
        one("epicerie", Category.COMMERCES, "🛒", "L'épicerie",
            "Dans ce projet, je veux de l'aide pour mes listes d'épicerie, mes menus de la semaine et mon budget alimentaire.",
            "nutritionniste"),
        one("comptable", Category.COMMERCES, "🧾", "Le comptable",
            "Dans ce projet, je veux de l'aide pour mon budget, mes impôts, mes placements et mes factures.",
            "comptable"),
        one("bar", Category.COMMERCES, "🍺", "Le bar",
            "Dans ce projet, je veux des discussions détendues, des idées de sorties et me changer les idées.",
            "ami"),
        one("boutique", Category.COMMERCES, "👗", "La boutique",
            "Dans ce projet, je veux des conseils de style et de garde-robe.",
            "conseiller_mode"),
        one("cantine", Category.COMMERCES, "🍟", "La cantine",
            "Dans ce projet, je veux des recettes rapides et de la cuisine du quotidien.",
            "chef_cuisinier"),
        one("parc", Category.LOISIRS, "🛝", "Le parc",
            "Dans ce projet, je veux des idées d'activités et de sorties en famille avec les enfants.",
            "animateur_loisirs"),
        one("lac", Category.LOISIRS, "🏞️", "Le lac",
            "Dans ce projet, je veux de l'aide pour le plein air, la pêche, le nautisme et le chalet.",
            "guide_plein_air"),
        one("foret", Category.LOISIRS, "🌲", "La forêt",
            "Dans ce projet, je veux de l'aide pour la randonnée, le camping, la nature et le jardinage.",
            "guide_plein_air"),
        one("sport", Category.LOISIRS, "🏋️", "Le centre sportif",
            "Dans ce projet, je veux de l'aide pour l'entraînement, la nutrition sportive et mes objectifs de forme.",
            "coach_entrainement"),
        one("aeroport", Category.TRANSPORT, "✈️", "L'aéroport",
            "Dans ce projet, je veux planifier mes voyages : itinéraires, bagages, documents.",
            "guide_voyage"),
        one("hotel", Category.TRANSPORT, "🏨", "L'hôtel",
            "Dans ce projet, je veux de l'aide pour l'hébergement et l'organisation d'événements et de réceptions.",
            "concierge"),
        one("auto", Category.COMMERCES, "🚗", "Le concessionnaire",
            "Dans ce projet, je veux de l'aide pour acheter ou louer un véhicule : comparaisons et négociation.",
            "conseiller_auto"),
        one("eglise", Category.SPIRITUEL, "⛪", "L'église",
            "Dans ce projet, je veux réfléchir à la spiritualité, aux valeurs, au deuil et trouver du soutien moral.",
            "confident"),
    ).flatten()

    private val byId = all.associateBy { it.id }
    fun get(id: String?): BuildingType? = id?.let { byId[it] }
    val byCategory: Map<Category, List<BuildingType>> = all.groupBy { it.category }
}
