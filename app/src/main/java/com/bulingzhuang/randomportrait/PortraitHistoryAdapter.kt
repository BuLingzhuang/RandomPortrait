package com.bulingzhuang.randomportrait

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by bulingzhuang
 * on 2017/6/30
 * E-mail:bulingzhuang@foxmail.com
 */
class PortraitHistoryAdapter(private val mContext: Context, private val mCallback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDataList: MutableList<String>

    init {
        mDataList = ArrayList<String>()
    }

    interface Callback {
        fun notifyChange(code: String)
    }

    fun add(str: String) {
        mDataList.add(0,str)
//        mDataList.add(str)
//        notifyItemInserted(itemCount)
        notifyDataSetChanged()
    }

    fun addAll(collection: Collection<String>){
        mDataList.addAll(0,collection)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val viewHolder = holder as PortraitHistoryAdapterViewHolder
        val itemCode = mDataList[position]
        viewHolder.mRpv.setCode(itemCode)
        viewHolder.itemView.setOnClickListener {
            mCallback.notifyChange(itemCode)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(mContext).inflate(R.layout.item_portrait_history, parent, false)
        return PortraitHistoryAdapterViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    private inner class PortraitHistoryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mRpv: RandomPortraitView = itemView.findViewById(R.id.rpv)
    }
}