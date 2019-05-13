package com.blogspot.alexeykutovenko.app42apikotlindemo.data

import com.blogspot.alexeykutovenko.app42apikotlindemo.config.app42CollectionName
import com.blogspot.alexeykutovenko.app42apikotlindemo.config.app42DBName
import com.shephertz.app42.paas.sdk.android.App42API
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder
import com.shephertz.app42.paas.sdk.android.storage.Storage
import com.shephertz.app42.paas.sdk.android.storage.StorageService
import kotlinx.coroutines.*
import org.json.JSONObject


class App42HelperService (private val callback: App42StorageServiceListener): QueryBuilder() {
    private val storageService: StorageService = App42API.buildStorageService()

    companion object {
        fun newInstance(callback: App42StorageServiceListener) = App42HelperService(callback)
    }

    interface App42StorageServiceListener {
        fun onSuccess(response: Storage)
        fun onException(ex: Throwable)
    }

    /*
    This function stores JSON Document.
     */
    fun insertJSONDoc(
        json: JSONObject
    ) = processApp42StorageQuery { storageService.insertJSONDocument(app42DBName, app42CollectionName, json) }

    /*
    This function finds JSON Document by id.
     */
    fun findDocByDocId(
        docId: String
    ) = processApp42StorageQuery { storageService.findDocumentById(app42DBName, app42CollectionName, docId) }

    /*
    This function finds JSON Document by key and value.
     */
    fun findDocByKeyValue(
        key: String,
        value: String
    ) = processApp42StorageQuery { storageService.findDocumentByKeyValue(app42DBName, app42CollectionName, key, value) }

    /*
    This function updates JSON Document by key and value.
     */
    fun updateDocByKeyValue(
        key: String,
        value: String,
        newJsonDoc: JSONObject
    ) = processApp42StorageQuery { storageService.updateDocumentByKeyValue(app42DBName, app42CollectionName, key, value, newJsonDoc) }


    /*
    This function uses coroutines to process the query.
     */
    private fun processApp42StorageQuery(service: () -> Storage) {
        val handler = CoroutineExceptionHandler { _, ex ->
            CoroutineScope(Dispatchers.Main).launch {
                callback.onException(ex)
            }
        }

        CoroutineScope(Dispatchers.IO).launch(handler) {
            val response = async { service.invoke() }
            withContext(Dispatchers.Main) {
                callback.onSuccess(response.await())
            }
        }
    }
}