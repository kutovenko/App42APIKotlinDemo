package com.blogspot.alexeykutovenko.app42apikotlindemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.alexeykutovenko.app42apikotlindemo.R
import com.blogspot.alexeykutovenko.app42apikotlindemo.config.app42ApiKey
import com.blogspot.alexeykutovenko.app42apikotlindemo.config.app42SecretKey
import com.blogspot.alexeykutovenko.app42apikotlindemo.data.App42HelperService
import com.shephertz.app42.paas.sdk.android.App42API
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder
import com.shephertz.app42.paas.sdk.android.storage.Storage
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), App42HelperService.App42StorageServiceListener {
    private var id = ""
    private val key = "name"
    private val value = "Nick"
    private val json = JSONObject()

    init{
        json.put("name","Nick")
        json.put("age",30)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App42API.initialize(this, app42ApiKey, app42SecretKey);
        val app42service = App42HelperService.newInstance(this)

        btnFindById.setOnClickListener { app42service.findDocByDocId(id) }
        btnFindByKeyValue.setOnClickListener { app42service.findDocByKeyValue(key, value) }
        btnInsert.setOnClickListener { app42service.insertJSONDoc(json) }
        btnDelete.setOnClickListener { QueryBuilder.Operator.AND }
    }

    override fun onSuccess(response: Storage) {
        id = response.jsonDocList[0].docId
        message.text = "${response.jsonDocList[0]}"
    }

    override fun onException(ex: Throwable) {
        message.text = "${ex.message}"
    }
}
