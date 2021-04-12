package com.example.fundamentalandroid2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundamentalandroid2.MainActivity.Companion.GITHUB_TOKEN
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.*
import org.json.JSONArray
import java.lang.Exception

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [FollowerFragment.newInstance] factory method to
// * create an instance of this fragment.
// */

class FollowerFragment : Fragment() {

    private lateinit var adapter: DataAdapter

    private val listFollower = ArrayList<ModelData>()

    companion object {
        private val ARG_USERNAME ="username"
        val Item = MainActivity::class.java.simpleName

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DataAdapter()
        adapter.notifyDataSetChanged()

        rvFollower.layoutManager = LinearLayoutManager(activity)
        rvFollower.adapter = adapter

        val username = arguments?.getString(ARG_USERNAME)
        getFollowers(username)

        adapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ModelData) {
                Toast.makeText(activity, data.username, Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun getFollowers(username: String?) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        client.addHeader("Authorization", GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        progressBarFollower.visibility = View.VISIBLE

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBarFollower.visibility = View.INVISIBLE

                try {
                    val result = String(responseBody)
                    Log.d(Item,result)

                    val items = JSONArray(result)

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val userItems = ModelData()
                        userItems.username = item.getString("login")
                        userItems.avatar = item.getString("avatar_url")
                        userItems.userid = item.getString("id")
                        userItems.type = item.getString("type")
                        listFollower.add(userItems)

                    }
                    adapter.setData(listFollower)

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
                progressBarFollower.visibility = View.INVISIBLE
                Log.d("OnFailure", error.message.toString())
            }
        })
    }

}