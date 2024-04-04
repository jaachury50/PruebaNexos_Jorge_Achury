package com.example.pruebanexos.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebanexos.R
import com.example.pruebanexos.models.ListAdapterAuthorizationModel
import com.example.pruebanexos.viewmodels.HomeViewModel
import com.example.pruebanexos.viewmodels.SearchViewModel

class AuthorizationsAdapter(
    private val onItemClick: (position: Int) -> Unit,
    var listAuthorizations: List<ListAdapterAuthorizationModel>
): RecyclerView.Adapter<AuthorizationsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNameTable = itemView.findViewById(R.id.tvNameTable) as TextView
        val linearCard = itemView.findViewById(R.id.linearCard) as LinearLayout
        val tvReceiptId = itemView.findViewById(R.id.tvReceiptId) as TextView
        val tvRrn = itemView.findViewById(R.id.tvRrn) as TextView
        val tvCommerceCode = itemView.findViewById(R.id.tvCommerceCode) as TextView
        val tvTerminalCode = itemView.findViewById(R.id.tvTerminalCode) as TextView
        val tvCount = itemView.findViewById(R.id.tvCount) as TextView
        val tvCard = itemView.findViewById(R.id.tvCard) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_authorization, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val table = listAuthorizations[position]
        var title = "APROBADA"

        if(!table.approvedStatus){
            title = "ANULADA"
        }

        holder.tvNameTable.text = "TRANSACCIÓN - " + title
        holder.tvReceiptId.text = table.receiptId
        holder.tvRrn.text = table.rrn
        holder.tvCommerceCode.text = table.commerceCode
        holder.tvTerminalCode.text = table.terminalCode
        holder.tvCount.text = table.amount
        holder.tvCard.text = table.card

        holder.linearCard.setOnClickListener{
            onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return listAuthorizations.size
    }
}