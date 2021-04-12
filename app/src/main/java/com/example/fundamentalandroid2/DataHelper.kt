package com.example.fundamentalandroid2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundamentalandroid2.MainActivity.Companion.GITHUB_TOKEN
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DataHelper: ViewModel() {

    val listUsers = MutableLiveData<ArrayList<ModelData>>()

    fun setUser(username: String) {
        val listUser = ArrayList<ModelData>()
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d(MainActivity.Item,result)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val itemUser = ModelData()
                        itemUser.avatar = item.getString("avatar_url")
                        itemUser.username = item.getString("login")
                        itemUser.userid = item.getString("id")
                        itemUser.type = item.getString("type")

                        listUser.add(itemUser)
                    }
                    listUsers.postValue(listUser)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<ModelData>> {
        return listUsers
    }
}