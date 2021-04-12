package com.example.fundamentalandroid2

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val Item = MainActivity::class.java.simpleName
        const val GITHUB_TOKEN = "BuildConfig.GITHUB_TOKEN"
    }

    private lateinit var adapter: DataAdapter
    private lateinit var mainViewModel: DataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = DataAdapter()
        adapter.notifyDataSetChanged()

        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DataHelper::class.java
        )

        mainViewModel.getUser().observe(this, Observer { userItems ->
            if (userItems != null) {
                tv_description.visibility = View.GONE
                imageView4.visibility = View.GONE
                adapter.setData(userItems)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ModelData) {
                showSelectedUser(data)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showSelectedUser(data: ModelData) {
        val detailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
        detailIntent.putExtra(UserDetailActivity.EXTRA_USER, data)
        startActivity(detailIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.setUser(query)
                showLoading(true)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity((mIntent))
        }
        return super.onOptionsItemSelected(item)
    }
}