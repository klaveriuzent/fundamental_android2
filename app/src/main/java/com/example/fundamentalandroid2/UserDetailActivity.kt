package com.example.fundamentalandroid2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fundamentalandroid2.MainActivity.Companion.GITHUB_TOKEN
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        val Item = UserDetailActivity::class.java.simpleName
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val actionBar = supportActionBar

        supportActionBar?.title = "Detail User"
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        val user = intent.getParcelableExtra<ModelData>(EXTRA_USER) as ModelData

        getDetailUser(user.username)

        Glide.with(this)
                .load(user.avatar)
                .apply(RequestOptions())
                .into(img_avatar)

        val sectionsPagerAdapter = DataAdapterSupport(this, supportFragmentManager)
        sectionsPagerAdapter.username = user?.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun getDetailUser(username: String?) {

        val dataList = ArrayList<ModelData>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")

        progressBarDetail.visibility = View.VISIBLE

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBarDetail.visibility = View.INVISIBLE

                try {
                    val result = String(responseBody)
                    Log.d(Item,result)

                    val item = JSONObject(result)

                    val getItem = ModelData()
                    getItem.avatar = item.getString("avatar_url")
                    getItem.username = item.getString("login")
                    getItem.name = item.getString("name")
                    getItem.location = item.getString("location")
                    getItem.company = item.getString("company")
                    getItem.repository = item.getString("public_repos")
                    getItem.followers = item.getString("followers")
                    getItem.following = item.getString("following")

                    dataList.add(getItem)

                    tv_username.text = getItem.username.toString()
                    tv_name.text = getItem.name.toString()
                    tv_location.text = getItem.location.toString()
                    tv_company.text = getItem.company.toString()
                    tv_repository.text = getItem.repository.toString()
                    tv_followers.text = getItem.followers.toString()
                    tv_following.text = getItem.following.toString()

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
                progressBarDetail.visibility = View.INVISIBLE
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}