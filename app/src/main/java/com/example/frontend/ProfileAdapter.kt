package com.example.frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.frontend.databinding.ItemRecyclerBinding
import com.example.frontend.ProfileData

class ProfileAdapter(val itemList: ArrayList<ProfileData>) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false)
        return ProfileViewHolder(view)



    }


    override fun onBindViewHolder(holder: ProfileViewHolder,position: Int) {
        holder.eng.text = itemList[position].eng
        holder.kor.text = itemList[position].kor
        holder.button.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kor = itemView.findViewById<TextView>(R.id.text_kor_tv)
        val eng = itemView.findViewById<TextView>(R.id.text_eng_tv)

        val button = itemView.findViewById<ImageView>(R.id.primary)

    }

}