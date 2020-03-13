package com.jimenard.cecropiatest.views.adapter

import android.view.View

/**
 * Modelo de datos para cargar hoders en recyclerView con adapatador generico
 * @param layoutToCharge layout del item que infla el holder
 */
abstract class AdapterObject(var layoutToCharge: Int) {

    abstract var foregroundView: Int?

    /**
     * Se carga vista en el holder del recyclerView
     * @param adapterPosition posicion en el adaptador
     * @param iNotify notificacion para item
     * @param itemRecyclerView item con informacion que se carga en vista
     * @param view vista en la cual se carga la informacion
     */
    abstract fun chargeView(itemRecyclerView: ItemRecyclerView, view: View, adapterPosition: Int, iNotify: INotify?)

    /**
     * Metodo para cambiar estados de las vistas si estas son seleccionadas y se le quita la seleccion a la anterior
     * @param currentView vista actual seleccionada vista a la que se le pone seleccion
     * @param lastView ultima vista seleccionada vista a la que se quita seleccion
     */
    abstract fun changeViewState(lastView: View, currentView: View)

}