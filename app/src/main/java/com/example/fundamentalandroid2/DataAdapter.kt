package com.example.fundamentalandroid2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row.view.*

class DataAdapter: RecyclerView.Adapter<DataAdapter.ViewHolder>(){

    internal var dataList = arrayListOf<ModelData>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: ViewHolder, position: Int) {
        viewholder.bind(dataList[position])
        viewholder.itemView.setOnClickListener {onItemClickCallback.onItemClicked(dataList[position])}
    }

    fun setData(items: ArrayList<ModelData>) {
        dataList.clear()
        dataList.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(itemUser : ModelData){
            with(itemView) {
                tv_name.text = itemUser.username
                tv_userid.text = itemUser.userid
                tv_type.text = itemUser.type

                Glide.with(this)
                    .load(itemUser.avatar)
                    .into(img_avatar)
            }
        }
    }


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ModelData)
    }


}