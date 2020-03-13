package com.jimenard.cecropiatest.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GenericRecyclerAdapter(var items: ArrayList<ItemRecyclerView>,
                             private val adapterObject: AdapterObject,
                             private val iNotify: INotify? = null)
    : RecyclerView.Adapter<GenericRecyclerAdapter.GenericRecyclerViewHolder>() {

    private var holders: ArrayList<GenericRecyclerViewHolder> = ArrayList()
    var selectedPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GenericRecyclerViewHolder {
        val holder = GenericRecyclerViewHolder(LayoutInflater.from(parent.context)
            .inflate(this.adapterObject.layoutToCharge, parent, false))
        this.holders.add(holder)
        return holder
    }

    override fun getItemCount() = this.items.size

    override fun onBindViewHolder(holder: GenericRecyclerViewHolder, position: Int) {
        holder.setParams(this.items[position])
    }

    /**
     * Se valida posicion seleccionada
     */
    fun setCurrentPosition(position: Int) {
        this.holders[position].changeState(position)
    }

    inner class GenericRecyclerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val foregroundView: View? = view.findViewById(adapterObject.foregroundView ?: -1)

        /**
         * Método encargado de recibir los parámetros cuando se renderiza la vista
         */
        fun setParams(itemRecyclerView: ItemRecyclerView) {
            adapterObject.chargeView(itemRecyclerView, this.view, this.adapterPosition, iNotify)
        }

        /**
         * Se notifica para que el item cambie de estado
         */
        fun changeState(position: Int) {
            adapterObject.changeViewState(holders[selectedPosition].view, holders[position].view)
            selectedPosition = position
        }
    }
}