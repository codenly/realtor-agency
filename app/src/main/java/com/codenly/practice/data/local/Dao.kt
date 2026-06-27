package com.codenly.practice.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnerDao {
    @Query("SELECT * FROM owners ORDER BY fullName ASC")
    fun getAllOwners(): Flow<List<OwnerEntity>>

    @Query("SELECT * FROM owners WHERE id = :id")
    suspend fun getOwnerById(id: Long): OwnerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwner(owner: OwnerEntity): Long

    @Update
    suspend fun updateOwner(owner: OwnerEntity)

    @Delete
    suspend fun deleteOwner(owner: OwnerEntity)
}

@Dao
interface TypeDao {
    @Query("SELECT * FROM types ORDER BY name ASC")
    fun getAllTypes(): Flow<List<TypeEntity>>

    @Query("SELECT * FROM types WHERE id = :id")
    suspend fun getTypeById(id: Long): TypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertType(type: TypeEntity): Long

    @Update
    suspend fun updateType(type: TypeEntity)

    @Delete
    suspend fun deleteType(type: TypeEntity)
}

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY fullName ASC")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): ClientEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity): Long

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Delete
    suspend fun deleteClient(client: ClientEntity)
}

@Dao
interface RealtorDao {
    @Query("SELECT * FROM realtors ORDER BY fullName ASC")
    fun getAllRealtors(): Flow<List<RealtorEntity>>

    @Query("SELECT * FROM realtors WHERE id = :id")
    suspend fun getRealtorById(id: Long): RealtorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRealtor(realtor: RealtorEntity): Long

    @Update
    suspend fun updateRealtor(realtor: RealtorEntity)

    @Delete
    suspend fun deleteRealtor(realtor: RealtorEntity)
}

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY address ASC")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Long): PropertyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity): Long

    @Update
    suspend fun updateProperty(property: PropertyEntity)

    @Delete
    suspend fun deleteProperty(property: PropertyEntity)
}

@Dao
interface ViewDao {
    @Query("SELECT * FROM views ORDER BY date ASC")
    fun getAllViews(): Flow<List<ViewEntity>>

    @Query("SELECT * FROM views WHERE id = :id")
    suspend fun getViewById(id: Long): ViewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertView(view: ViewEntity): Long

    @Update
    suspend fun updateView(view: ViewEntity)

    @Delete
    suspend fun deleteView(view: ViewEntity)
}

@Dao
interface DealDao {
    @Query("SELECT * FROM deals ORDER BY date ASC")
    fun getAllDeals(): Flow<List<DealEntity>>

    @Query("SELECT * FROM deals WHERE id = :id")
    suspend fun getDealById(id: Long): DealEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeal(deal: DealEntity): Long

    @Update
    suspend fun updateDeal(deal: DealEntity)

    @Delete
    suspend fun deleteDeal(deal: DealEntity)
}
