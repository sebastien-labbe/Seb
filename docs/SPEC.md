# Village IA — Spécification v0.1

Application Android qui rend l'IA accessible au grand public par la métaphore d'un village.
**Bâtiment = projet. Personne = conversation avec l'IA.**

---

## 1. Principes

1. **Aucun jargon IA visible** : pas de « prompt », « modèle », « jeton » dans l'interface par défaut.
2. **Prêt à l'emploi** : un village de départ est rempli au premier lancement ; la personnalisation est optionnelle.
3. **Aucun stéréotype imposé** : n'importe quelle expertise dans n'importe quel bâtiment.
4. **Flèche arrière Android** toujours cohérente : Fiche personne → Intérieur du bâtiment → Village.

---

## 2. Portée par version

| Fonction | v0.1 (MVP) | v0.2 | Plus tard |
|---|---|---|---|
| Village 4×6, ajouter/retirer/déplacer un bâtiment | ✅ | | |
| Catalogue de bâtiments (images provisoires) | ✅ | | Illustrations finales |
| Icône perso depuis une image locale | ✅ | | |
| Intérieur du bâtiment : liste des personnes | ✅ | | |
| Créer/éditer une personne (nom, personnalité, expertise, avatar) + aléatoire | ✅ | | |
| Clavardage texte en continu (streaming) | ✅ | | |
| Instructions du bâtiment éditables | ✅ | | |
| « Carnet » du bâtiment (faits partagés entre ses occupants) | ✅ manuel | Auto (l'IA propose des ajouts) | |
| Entrevue d'accueil (génère instructions + village) | | ✅ | |
| Voix (lecture des réponses, dictée) | | ✅ TTS Android | Voix premium |
| Serveur intermédiaire + abonnement | | ✅ | |
| Mode « Auto » (choix du modèle) | | ✅ | |
| Autres fournisseurs (OpenAI, Google) | | | ✅ |
| Signalement de contenu (exigence Google Play) | | ✅ | |
| Sauvegarde / synchro infonuagique | | | ✅ |

**v0.1 = usage personnel/test seulement.** La clé API est saisie dans un écran « Paramètres développeur » et stockée chiffrée sur l'appareil. Pas de publication sur Play avant la v0.2 (serveur + conformité).

---

## 3. Écrans et navigation

```
Village (accueil)
 ├─ [appui]        → Intérieur du bâtiment
 │    ├─ [appui personne]  → Conversation
 │    │     └─ [icône ⓘ]   → Fiche personne (édition)
 │    ├─ [+]               → Nouvelle personne (Aléatoire | Manuel)
 │    └─ [⚙]               → Réglages du bâtiment (nom, icône, instructions, carnet)
 ├─ [appui long]   → Mode édition (déplacer, retirer)
 ├─ [case vide +]  → Catalogue de bâtiments
 └─ [menu]         → Paramètres
```

### 3.1 Village
- Grille fixe 4 colonnes × 6 rangées (24 cases), fond « gazon ».
- Chaque case : illustration + nom du projet sous l'icône (ex. « Notre domicile »).
- Case vide : « + » discret en mode édition seulement.
- Appui long : mode édition (glisser-déposer, ✕ pour retirer avec confirmation « Les conversations seront supprimées »).

### 3.2 Catalogue
- Grille par catégories : Habitations, Garages, Commerces, Services publics, Santé, Loisirs & nature, Transport & hébergement, Spirituel, **Image perso**.
- Choisir → nom suggéré + instructions par défaut pré-remplies → « Ajouter ». Une personne par défaut est créée.

### 3.3 Intérieur du bâtiment
- En-tête : illustration + nom du projet.
- Liste des personnes en cartes : avatar, nom, expertise, aperçu du dernier message.
- Bouton « + Ajouter quelqu'un ».

### 3.4 Conversation
- Bulles classiques, réponse affichée en continu.
- Champ texte + envoyer. (v0.2 : micro, haut-parleur.)
- Menu : « Recommencer la conversation », « Ajouter au carnet » (sur un message).

### 3.5 Fiche personne
- Avatar (régénérer / choisir), nom, expertise (liste par catégories + recherche), personnalité (choix + texte libre), voix (v0.2).
- Bouton « 🎲 Tout au hasard ».
- Avancé (repliable) : niveau d'intelligence = Rapide / Équilibré / Expert (masque le nom des modèles).

### 3.6 Réglages du bâtiment
- Nom, icône, instructions du projet (texte), carnet (liste de faits éditables).

### 3.7 Paramètres
- Développeur : clé API Anthropic, modèle par défaut, réinitialiser le village.

---

## 4. Modèle de données (Room / SQLite)

```
Building
  id, slotIndex (0–23, unique), typeId | customImageUri,
  name, instructions, createdAt

BuildingNote          // le « carnet »
  id, buildingId, text, createdAt

Person
  id, buildingId, name, avatarSeed, avatarStyle,
  expertiseId, customExpertise?, personalityId, personalityNotes,
  voiceId?, tier (FAST|BALANCED|EXPERT), createdAt

Message
  id, personId, role (USER|ASSISTANT), text, createdAt, tokensIn?, tokensOut?
```

Catalogues (bâtiments, expertises, personnalités) : fichiers JSON embarqués dans l'app, pas en base.

---

## 5. Assemblage des instructions envoyées à l'IA

Prompt système = concaténation, dans l'ordre :

1. **Cadre global** (fixe, invisible) : « Tu es un personnage dans l'application Village IA. Reste dans ton rôle, parle simplement, en français québécois avec tutoiement sauf si l'utilisateur écrit dans une autre langue. Pour toute question médicale, juridique ou financière importante, rappelle de consulter un professionnel. »
2. **Projet** : instructions du bâtiment.
3. **Carnet** : faits du bâtiment (« Ce que tu sais de l'utilisateur dans ce projet : … »).
4. **Personne** : « Tu t'appelles {nom}. Ton expertise : {expertise}. Ta personnalité : {personnalité + notes}. »

Historique : les N derniers messages qui tiennent dans un budget (v0.1 : 30 derniers messages). Résumé automatique des vieux messages : v0.2.

---

## 6. Catalogue des bâtiments (40)

| # | Type | Nom suggéré | Instructions par défaut (résumé) |
|---|---|---|---|
| 1–4 | Maison unifamiliale A–D | Notre domicile | Spécialistes qui connaissent ma vie de famille : devoirs, cuisine, impôts, assurances, factures, entretien de la maison et de la piscine. |
| 5–8 | Bloc appartements A–D | Mon logement | Vie en logement : bail, voisinage, budget, aménagement de petits espaces. |
| 9–12 | Garage A–D | Mon garage | Véhicules, outils, bricolage, projets d'atelier. |
| 13–16 | Commercial A–D | Mon entreprise | Projet d'affaires : stratégie, marketing, ventes, gestion. |
| 17 | Bibliothèque | La bibliothèque | Lecture, recherche d'information, résumés, recommandations de livres. |
| 18 | École | L'école | Aide aux études primaire/secondaire, explications adaptées à l'âge. |
| 19 | Enseignement supérieur | L'université | M'instruire sur différents sujets, en profondeur. |
| 20 | Palais de justice | Le palais de justice | Comprendre mes droits et les démarches juridiques (information, pas avis légal). |
| 21 | Clinique médicale | La clinique | Questions de santé courantes, préparer un rendez-vous (pas de diagnostic). |
| 22 | Hôpital | L'hôpital | Comprendre une condition, un traitement, accompagner un proche. |
| 23 | Pharmacie | La pharmacie | Médicaments, interactions, produits en vente libre (vérifier avec un pharmacien). |
| 24 | Épicerie | L'épicerie | Listes d'épicerie, menus de la semaine, budget alimentaire. |
| 25 | Bureau comptable | Le comptable | Budget, impôts, placements, factures. |
| 26 | Bar | Le bar | Discussions détendues, sorties, cocktails, se changer les idées. |
| 27 | Boutique de vêtements | La boutique | Style, garde-robe, conseils mode. |
| 28 | Cantine | La cantine | Recettes rapides, cuisine du quotidien. |
| 29 | Parc | Le parc | Activités en famille, idées de sorties avec les enfants. |
| 30 | Lac | Le lac | Plein air, pêche, nautisme, chalet. |
| 31 | Forêt | La forêt | Randonnée, camping, nature, jardinage. |
| 32 | Centre sportif | Le centre sportif | Entraînement, nutrition sportive, objectifs de forme. |
| 33 | Aéroport | L'aéroport | Planifier des voyages : itinéraires, bagages, documents. |
| 34 | Hôtel | L'hôtel | Hébergement, organisation d'événements, réceptions. |
| 35 | Concessionnaire auto | Le concessionnaire | Achat/location de véhicule, comparaisons, négociation. |
| 36 | Église | L'église | Réflexion, spiritualité, valeurs, deuil, soutien moral. |
| 37+ | Image perso | (à nommer) | Instructions vides, à rédiger ou générer par l'entrevue (v0.2). |

Chaque type vient avec **une personne par défaut** dont l'expertise est cohérente (ex. Clinique → Médecin de famille), modifiable.

---

## 7. Expertises (extrait, ~120 au total)

- **Éducation** : enseignant primaire, enseignant secondaire, tuteur de maths, professeur de langues, orthopédagogue.
- **Maison & métiers** : plombier, électricien, menuisier, couvreur, paysagiste, spécialiste piscine, entrepreneur général.
- **Auto & mécanique** : mécanicien automobile, carrossier, mécanicien de petits moteurs.
- **Santé** : médecin de famille, infirmier, pharmacien, nutritionniste, physiothérapeute, psychologue.
- **Finances & droit** : comptable, planificateur financier, fiscaliste, avocat, notaire, courtier d'assurances.
- **Technologie** : technicien TI, programmeur, réparateur d'électronique, spécialiste cybersécurité.
- **Cuisine** : chef cuisinier, pâtissier, sommelier.
- **Mode & image** : conseiller en mode, coiffeur, maquilleur.
- **Sport & bien-être** : coach d'entraînement, yoga, coach de vie.
- **Bureau** : secrétaire, gestionnaire de projet, rédacteur, traducteur, RH.
- **Sciences** : scientifique (généraliste), ingénieur, historien, philosophe.
- **Arts & loisirs** : musicien, artiste, photographe, guide de voyage, conteur.
- **Proches** : ami·e, confident·e, grand-parent, partenaire d'entraînement, compagnon de jeu.
- **Autre** : texte libre.

---

## 8. Personnalités (aléatoire possible)

Chaleureux, Direct, Patient, Enjoué, Calme, Pédagogue, Motivateur, Humoristique, Rigoureux.
Plus un champ libre (« Parle comme un vieux Gaspésien », etc.).

---

## 9. Architecture technique

- **Langage / UI** : Kotlin, Jetpack Compose, Material 3.
- **Navigation** : Navigation Compose (la pile gère la flèche arrière nativement).
- **Données** : Room ; DataStore pour les préférences ; clé API chiffrée (Android Keystore).
- **Réseau** : Ktor client ou OkHttp, API Anthropic Messages avec streaming SSE.
- **Images** : Coil (images locales via le sélecteur de photos Android, sans permission de stockage).
- **Avatars** : génération locale à partir d'une graine (style DiceBear, licence à valider) pour v0.1.
- **Architecture** : MVVM, un module `app` ; couche `ai/` derrière une interface `ChatProvider` pour ajouter d'autres fournisseurs plus tard.
- **Min SDK** : 26 (Android 8). Cible : SDK actuel.

### Correspondance des niveaux d'intelligence (v0.1)

| Niveau | Modèle |
|---|---|
| Rapide (défaut) | `claude-haiku-5-5` |
| Équilibré | `claude-sonnet-5-5` |
| Expert | `claude-opus-5-5` |

À valider au moment de coder (identifiants et prix évoluent).

---

## 10. Hors code — à prévoir

- **Illustrations** : 36 bâtiments dans un style cohérent (v0.1 : images provisoires simples).
- **Loi 25** : consentement explicite, politique de confidentialité, mention que les données transitent par un fournisseur américain — avant toute diffusion publique.
- **Google Play** : politique sur l'IA générative (signalement de contenu), déclaration de sécurité des données.
- **Politiques du fournisseur** : vérifier les exigences d'Anthropic pour les usages grand public en santé, droit et finances.
- **Coûts** : modéliser le coût par utilisateur avant de fixer un prix d'abonnement.

---

## 11. Critères d'acceptation v0.1

1. Au premier lancement, le village contient 4 bâtiments avec une personne chacun.
2. Ajouter, déplacer, retirer un bâtiment ; 24 maximum.
3. Ajouter un bâtiment depuis une image locale.
4. Créer une personne en mode aléatoire en un seul appui.
5. Converser en texte ; la réponse respecte le rôle, l'expertise et les instructions du bâtiment.
6. Un fait ajouté au carnet est connu des autres personnes du même bâtiment, pas des autres bâtiments.
7. La flèche arrière suit toujours Fiche → Bâtiment → Village → quitter.
8. Les données persistent après fermeture de l'app.
