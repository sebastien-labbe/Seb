package ca.villageia.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "building", indices = [Index(value = ["slotIndex"], unique = true)])
data class Building(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val slotIndex: Int,                 // 0..23
    val typeId: String?,                // null si image perso
    val customImageUri: String? = null,
    val name: String,
    val instructions: String,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "building_note",
    foreignKeys = [ForeignKey(Building::class, ["id"], ["buildingId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("buildingId")],
)
data class BuildingNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val buildingId: Long,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "person",
    foreignKeys = [ForeignKey(Building::class, ["id"], ["buildingId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("buildingId")],
)
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val buildingId: Long,
    val name: String,
    val avatarSeed: String,
    val avatarStyle: String = "default",
    val expertiseId: String,
    val customExpertise: String? = null,
    val personalityId: String,
    val personalityNotes: String = "",
    val voiceId: String? = null,
    val tier: String = "FAST",          // FAST | BALANCED | EXPERT
    val createdAt: Long = System.currentTimeMillis(),
)

@Entity(
    tableName = "message",
    foreignKeys = [ForeignKey(Person::class, ["id"], ["personId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("personId")],
)
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val role: String,                   // USER | ASSISTANT
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
    val tokensIn: Int? = null,
    val tokensOut: Int? = null,
)
