package com.codenly.practice.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "owners")
data class OwnerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phoneNumber: String,
    val email: String
)

@Entity(tableName = "types")
data class TypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val preference: String
)

@Entity(tableName = "realtors")
data class RealtorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val login: String,
    val password: String,
    val phoneNumber: String
)

@Entity(tableName = "properties",
    foreignKeys = [
        ForeignKey(
            entity = OwnerEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["typeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("ownerId"),
        Index("typeId")
    ]
)
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: Long,
    val typeId: Long?,
    val address: String,
    val city: String,
    val district: String,
    val area: Double,
    val rooms: Int,
    val floor: Int,
    val floorsTotal: Int,
    val price: Long,
    val status: String,
    val date: Long,
    val description: String
)

@Entity(tableName = "views",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RealtorEntity::class,
            parentColumns = ["id"],
            childColumns = ["realtorId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index("propertyId"),
        Index("clientId"),
        Index("realtorId")
    ]
)
data class ViewEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val clientId: Long?,
    val realtorId: Long?,
    val date: Long,
    val result: String
)

@Entity(tableName = "deals",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RealtorEntity::class,
            parentColumns = ["id"],
            childColumns = ["realtorId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index("propertyId"),
        Index("clientId"),
        Index("realtorId")
    ]
)
data class DealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val clientId: Long?,
    val realtorId: Long?,
    val date: Long,
    val type: String,
    val finalPrice: Long
)
