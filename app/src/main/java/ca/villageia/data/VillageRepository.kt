package ca.villageia.data

import androidx.room.withTransaction
import ca.villageia.catalog.BuildingType
import ca.villageia.catalog.Expertises
import ca.villageia.catalog.Personalities
import ca.villageia.catalog.PersonNames
import java.util.UUID

class VillageRepository(private val db: AppDatabase) {
    private val buildingDao = db.buildingDao()
    private val personDao = db.personDao()

    val buildings = buildingDao.observeAll()
    fun building(id: Long) = buildingDao.observe(id)
    fun persons(buildingId: Long) = personDao.observeByBuilding(buildingId)
    fun person(id: Long) = personDao.observe(id)

    /** Ajoute une personne tirée au hasard (nom, avatar, personnalité, expertise). */
    suspend fun addPerson(buildingId: Long): Long =
        personDao.insert(randomPerson(buildingId, Expertises.all.random().id))

    suspend fun updatePerson(person: Person) = personDao.update(person)
    suspend fun deletePerson(id: Long) = personDao.delete(id)

    suspend fun isEmpty() = buildingDao.count() == 0

    /** Ajoute un bâtiment du catalogue avec sa personne par défaut. */
    suspend fun addBuilding(type: BuildingType, slot: Int): Long = db.withTransaction {
        val id = buildingDao.insert(
            Building(
                slotIndex = slot,
                typeId = type.id,
                name = type.defaultName,
                instructions = type.defaultInstructions,
            )
        )
        personDao.insert(randomPerson(id, type.defaultExpertiseId))
        id
    }

    suspend fun removeBuilding(id: Long) = buildingDao.delete(id)

    private fun randomPerson(buildingId: Long, expertiseId: String) = Person(
        buildingId = buildingId,
        name = PersonNames.all.random(),
        avatarSeed = UUID.randomUUID().toString(),
        expertiseId = expertiseId,
        personalityId = Personalities.all.random().id,
    )
}

fun Person.randomized() = copy(
    name = PersonNames.all.random(),
    avatarSeed = UUID.randomUUID().toString(),
    expertiseId = Expertises.all.random().id,
    customExpertise = null,
    personalityId = Personalities.all.random().id,
)
