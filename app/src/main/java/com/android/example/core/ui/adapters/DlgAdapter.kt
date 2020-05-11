package com.android.example.core.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.android.example.core.model.items.DlgItem
import com.android.example.core.utils.containsForSearch
import com.android.example.core.utils.loadImage
import com.android.example.R

class DlgAdapter(val context: Context?, @LayoutRes var itemLayoutId: Int) :
    RecyclerView.Adapter<DlgAdapter.DlgItemViewHolder>(), Filterable {

    var enableDividers: Boolean = false
    var enableMultipleSelection = false
    var enableEmptySelection = false

    var onItemClickListener: ((item: DlgItem) -> Unit)? = null

    var items: List<DlgItem> = emptyList()
        set(value) {
            field = value
            filteredItems = value
        }

    private var filteredItems: List<DlgItem> = emptyList()

    private val inflater = LayoutInflater.from(context)

    fun filter(query: String) {
        filter.filter(query)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filteredList = items.filter {
                    StringBuilder()
                        .append(it.title)
                        .append(it.description)
                        .append(it.descriptionId?.let { id -> context?.getString(id) }.orEmpty())
                        .append(it.titleId?.let { id -> context?.getString(id) }.orEmpty())
                        .toString().containsForSearch(charSequence)
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredItems = (filterResults.values as? List<DlgItem>?).orEmpty()
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DlgItemViewHolder =
        DlgItemViewHolder(inflater.inflate(itemLayoutId, parent, false))

    override fun getItemCount(): Int = filteredItems.size

    override fun onBindViewHolder(holder: DlgItemViewHolder, pos: Int) =
        holder.bind(filteredItems[pos])

    fun getSelectedItems() = items.filter { it.checked }

    fun checkItem(id: String, check: Boolean) {
        for (pos in filteredItems.indices) {
            val item = filteredItems[pos]
            if (item.checkable) {
                val targetCheck = when {
                    !enableMultipleSelection && !enableEmptySelection -> item.id == id
                    !enableMultipleSelection && enableEmptySelection && item.id != id -> false
                    enableMultipleSelection && !enableEmptySelection && filteredItems.none { it.id != id && it.checked && it.checkable } -> item.id == id
                    item.id == id -> check
                    else -> item.checked
                }

                val needNotify = item.checked != targetCheck && item.id != id
                item.checked = targetCheck
                if (needNotify) notifyItemChanged(pos)
            }
        }
    }

    inner class DlgItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView: ImageView? =
            runCatching { itemView.findViewById<ImageView?>(R.id.imageIv) }.getOrNull()
        private val titleTv: TextView? =
            runCatching { itemView.findViewById<TextView?>(R.id.titleTv) }.getOrNull()
        private val descriptionTv: TextView? =
            runCatching { itemView.findViewById<TextView?>(R.id.descriptionTv) }.getOrNull()
        private val compoundButton: CompoundButton? =
            runCatching { itemView.findViewById<CompoundButton?>(R.id.compoundBtn) }.getOrNull()
        private val divider: View? =
            runCatching { itemView.findViewById<View?>(R.id.divider) }.getOrNull()


        init {
            itemView.setOnClickListener {
                runCatching { filteredItems[adapterPosition] }.getOrNull()?.let {
                    if (it.checkable)
                        compoundButton?.isChecked = !(compoundButton?.isChecked ?: false)
                    else
                        onItemClickListener?.invoke(it)
                }
            }

        }

        fun bind(item: DlgItem?) {
            item?.let {
                removeListeners()
                imageView?.visibility = if (it.image != null) View.VISIBLE else View.GONE
                imageView?.loadImage(it.image)

                when {
                    (it.titleId != null) -> {
                        titleTv?.setText(it.titleId!!)
                        titleTv?.visibility = View.VISIBLE
                    }
                    (it.title != null) -> {
                        titleTv?.text = it.title
                        titleTv?.visibility = View.VISIBLE
                    }
                    else -> titleTv?.visibility = View.GONE
                }

                when {
                    (it.descriptionId != null) -> {
                        descriptionTv?.setText(it.descriptionId!!)
                        descriptionTv?.visibility = View.VISIBLE
                    }
                    (it.description != null) -> {
                        descriptionTv?.text = it.description
                        descriptionTv?.visibility = View.VISIBLE
                    }
                    else -> descriptionTv?.visibility = View.GONE
                }

                val isLast: Boolean = runCatching { adapterPosition >= filteredItems.size - 1 }.getOrDefault(false)

                divider?.visibility = if (!enableDividers || isLast) View.GONE else View.VISIBLE
                compoundButton?.visibility = if (it.checkable) View.VISIBLE else View.GONE
                compoundButton?.isChecked = it.checked

                titleTv?.isEnabled = item.enabled
                descriptionTv?.isEnabled = item.enabled
                compoundButton?.isEnabled = item.enabled
                itemView.isEnabled = item.enabled

                initListeners()
            }
        }

        private fun removeListeners() {
            compoundButton?.setOnCheckedChangeListener(null)
        }

        private fun initListeners() {
            compoundButton?.setOnCheckedChangeListener { v, isChecked ->
                runCatching { filteredItems[adapterPosition] }.getOrNull()?.let {
                    checkItem(it.id, isChecked)
                    v.isChecked = it.checked
                    onItemClickListener?.invoke(it)
                }
            }
        }
    }
}