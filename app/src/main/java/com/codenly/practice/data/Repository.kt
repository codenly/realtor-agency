package com.codenly.practice.data

import com.codenly.practice.data.local.OwnerDao
import com.codenly.practice.data.local.OwnerEntity
import com.codenly.practice.data.local.TypeDao
import com.codenly.practice.data.local.TypeEntity
import com.codenly.practice.data.local.ClientDao
import com.codenly.practice.data.local.ClientEntity
import com.codenly.practice.data.local.RealtorDao
import com.codenly.practice.data.local.RealtorEntity
import com.codenly.practice.data.local.PropertyDao
import com.codenly.practice.data.local.PropertyEntity
import com.codenly.practice.data.local.ViewDao
import com.codenly.practice.data.local.ViewEntity
import com.codenly.practice.data.local.DealDao
import com.codenly.practice.data.local.DealEntity
import kotlinx.coroutines.flow.Flow

class OwnerRepository(private val ownerDao: OwnerDao) {

    val allOwners: Flow<List<OwnerEntity>> = ownerDao.getAllOwners()

    suspend fun getOwnerById(id: Long): OwnerEntity? {
        return ownerDao.getOwnerById(id)
    }

    suspend fun addOwner(owner: OwnerEntity): Long {
        return ownerDao.insertOwner(owner)
    }

    suspend fun updateOwner(owner: OwnerEntity) {
        ownerDao.updateOwner(owner)
    }

    suspend fun deleteOwner(owner: OwnerEntity) {
        ownerDao.deleteOwner(owner)
    }
}

class TypeRepository(private val typeDao: TypeDao) {

    val allTypes: Flow<List<TypeEntity>> = typeDao.getAllTypes()

    suspend fun getTypeById(id: Long): TypeEntity? {
        return typeDao.getTypeById(id)
    }

    suspend fun addType(type: TypeEntity): Long {
        return typeDao.insertType(type)
    }

    suspend fun updateType(type: TypeEntity) {
        typeDao.updateType(type)
    }

    suspend fun deleteType(type: TypeEntity) {
        typeDao.deleteType(type)
    }
}

class ClientRepository(private val clientDao: ClientDao) {

    val allClients: Flow<List<ClientEntity>> = clientDao.getAllClients()

    suspend fun getClientById(id: Long): ClientEntity? {
        return clientDao.getClientById(id)
    }

    suspend fun addClient(client: ClientEntity): Long {
        return clientDao.insertClient(client)
    }

    suspend fun updateClient(client: ClientEntity) {
        clientDao.updateClient(client)
    }

    suspend fun deleteClient(client: ClientEntity) {
        clientDao.deleteClient(client)
    }
}

class RealtorRepository(private val realtorDao: RealtorDao) {

    val allRealtors: Flow<List<RealtorEntity>> = realtorDao.getAllRealtors()

    suspend fun getRealtorById(id: Long): RealtorEntity? {
        return realtorDao.getRealtorById(id)
    }

    suspend fun addRealtor(realtor: RealtorEntity): Long {
        return realtorDao.insertRealtor(realtor)
    }

    suspend fun updateRealtor(realtor: RealtorEntity) {
        realtorDao.updateRealtor(realtor)
    }

    suspend fun deleteRealtor(realtor: RealtorEntity) {
        realtorDao.deleteRealtor(realtor)
    }
}

class PropertyRepository(private val propertyDao: PropertyDao) {

    val allProperties: Flow<List<PropertyEntity>> = propertyDao.getAllProperties()

    suspend fun getPropertyById(id: Long): PropertyEntity? {
        return propertyDao.getPropertyById(id)
    }

    suspend fun addProperty(property: PropertyEntity): Long {
        return propertyDao.insertProperty(property)
    }

    suspend fun updateProperty(property: PropertyEntity) {
        propertyDao.updateProperty(property)
    }

    suspend fun deleteProperty(property: PropertyEntity) {
        propertyDao.deleteProperty(property)
    }
}

class ViewRepository(private val viewDao: ViewDao) {

    val allViews: Flow<List<ViewEntity>> = viewDao.getAllViews()

    suspend fun getViewById(id: Long): ViewEntity? {
        return viewDao.getViewById(id)
    }

    suspend fun addView(view: ViewEntity): Long {
        return viewDao.insertView(view)
    }

    suspend fun updateView(view: ViewEntity) {
        viewDao.updateView(view)
    }

    suspend fun deleteView(view: ViewEntity) {
        viewDao.deleteView(view)
    }
}

class DealRepository(private val dealDao: DealDao) {

    val allDeals: Flow<List<DealEntity>> = dealDao.getAllDeals()

    suspend fun getDealById(id: Long): DealEntity? {
        return dealDao.getDealById(id)
    }

    suspend fun addDeal(deal: DealEntity): Long {
        return dealDao.insertDeal(deal)
    }

    suspend fun updateDeal(deal: DealEntity) {
        dealDao.updateDeal(deal)
    }

    suspend fun deleteDeal(deal: DealEntity) {
        dealDao.deleteDeal(deal)
    }
}
