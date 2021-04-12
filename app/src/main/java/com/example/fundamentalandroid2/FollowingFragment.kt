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
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import java.lang.Exception

//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [FollowingFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
class FollowingFragment : Fragment() {

    private lateinit var adapter: DataAdapter

    private val listFollowing = ArrayList<ModelData>()

    companion object {
        private val ARG_USERNAME = "username"
        val Item = MainActivity::class.java.simpleName

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
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
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DataAdapter()
        adapter.notifyDataSetChanged()

        rvFollowing.layoutManager = LinearLayoutManager(activity)
        rvFollowing.adapter = adapter

        val username = arguments?.getString(ARG_USERNAME)
        getFollowing(username)

        adapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ModelData) {
                Toast.makeText(activity, data.username, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFollowing(username: String?) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        client.addHeader("Authorization", GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        progressBarFollowing.visibility = View.VISIBLE

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                progressBarFollowing.visibility = View.INVISIBLE

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
                        listFollowing.add(userItems)

                    }
                    adapter.setData(listFollowing)

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
                progressBarFollowing.visibility = View.INVISIBLE
                Log.d("OnFailure", error.message.toString())
            }
        })
    }
}