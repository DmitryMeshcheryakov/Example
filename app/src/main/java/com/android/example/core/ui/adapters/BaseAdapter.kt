package com.android.example.core.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.android.example.R
import com.android.example.core.model.BaseModel
import com.android.example.core.model.items.EmptyItem
import com.android.example.core.model.items.HeaderItem
import com.android.example.core.model.items.LoadingItem
import com.android.example.core.repository.LocalRepository
import com.android.example.core.ui.listeners.BaseItemClickListener
import com.android.example.core.ui.viewholders.BaseViewHolder
import com.android.example.core.ui.viewholders.EmptyViewHolder
import com.android.example.core.ui.viewholders.HeaderViewHolder
import com.android.example.core.ui.viewholders.LoadingViewHolder
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseAdapter<L : BaseItemClickListener>(context: Context?, listener: L?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @LayoutRes
    protected open var emptyLayoutId: Int? = null
    @LayoutRes
    protected open var headerLayoutId: Int? = null
    @LayoutRes
    protected open var loadingLayoutId: Int? = null
    @LayoutRes
    protected open var loadingLargeLayoutId: Int? = null

    protected var emptyItem: BaseModel = EmptyItem(context?.getString(R.string.empty).orEmpty())
    protected var itemClickListener: L? = listener
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    public var items: List<BaseModel> = ArrayList()
        get() {
            return if (dataSource?.get().isNullOrEmpty()) field
            else dataSource?.get().orEmpty()
        }

    protected var dataSource: LocalRepository<List<BaseModel>>? = null

    open fun setDataSource(source: LocalRepository<List<BaseModel>>, notify: Boolean = true) {
        dataSource = source
        if (source.get().isNullOrEmpty()) showEmpty(notify = notify)
        else if (notify) notifyDataSetChanged()
    }

    open fun setItems(list: List<BaseModel>, notify: Boolean = true) {
        items = list
        if (items.isEmpty()) showEmpty(notify = notify)
        else if (notify) notifyDataSetChanged()
    }

    open fun addItems(list: List<BaseModel>, notify: Boolean = true) {
        val count = items.size
        items = items.plus(list)
        if (notify) notifyItemRangeInserted(count, list.size)
    }

    open fun clearItems(notify: Boolean = true) {
        items = ArrayList()
        if (notify) notifyDataSetChanged()
    }

    open fun moveItem(item: BaseModel, toPos: Int, notify: Boolean = true) {
        moveItem(item.id, toPos, notify)
    }

    open fun moveItem(itemId: String, toPos: Int, notify: Boolean = true) {
        val fromPos = items.indexOfFirst { it.id == itemId }
        if (fromPos != -1)
            moveItem(fromPos, toPos, notify)
    }

    open fun moveItem(fromPos: Int, toPos: Int, notify: Boolean = true) {
        val item = items[fromPos]
        val arr = ArrayList(items)
        arr.remove(item)
        arr.add(toPos, item)
        items = arr
        if (notify) notifyItemMoved(fromPos, toPos)
    }

    open fun swapItems(fromPos: Int, toPos: Int, notify: Boolean = true) {
        Collections.swap(items, fromPos, toPos)
        if (notify) notifyItemMoved(fromPos, toPos)
    }

    open fun swapItems(item1Id: String, item2Id: String, notify: Boolean = true) {
        val posFrom = items.indexOfFirst { it.id == item1Id }
        val posTo = items.indexOfFirst { it.id == item2Id }

        if (posFrom != -1 && posTo != -1)
            swapItems(posFrom, posTo, notify)
    }

    open fun swapItems(item1: BaseModel, item2: BaseModel, notify: Boolean = true) {
        swapItems(item1.id, item2.id, notify)
    }

    open fun addItem(item: BaseModel, notify: Boolean = true) {
        addItem(item, items.size, notify)
    }

    open fun addItem(item: BaseModel, position: Int, notify: Boolean = true) {
        val arr = ArrayList(items)
        arr.add(position, item)
        items = arr
        if (notify) notifyItemInserted(position)
    }

    open fun showEmpty(notify: Boolean = true) {
        items = arrayListOf(emptyItem)
        if (notify) notifyDataSetChanged()
    }

    open fun updateItems(list: List<String>) {
        list.toSet().forEach {
            updateItem(it)
        }
    }

    open fun updateItem(id: String) {
        items.firstOrNull { it.id == id }?.let { notifyItemChanged(items.indexOf(it)) }
    }

    open fun updateItem(item: BaseModel) {
        val pos = items.indexOfFirst { it.id == item.id }
        if (pos != -1) {
            val arr = ArrayList(items)
            arr[pos] = item
            items = arr
            notifyItemChanged(pos)
        }
    }

    open fun removeItem(id: String?) {
        val pos = items.indexOfFirst { it.id == id }
        if (pos != -1) {
            items = items.filterNot { it.id == id }
            notifyItemRemoved(pos)
        }
    }

    open fun removeItem(item: BaseModel) {
        removeItem(item.id)
    }

    fun findItemById(id: String): BaseModel? {
        return items.firstOrNull { it.id == id }
    }

    fun findItemPosById(id: String): Int? {
        val pos = items.indexOfFirst { it.id == id }
        return if (pos == -1) null else pos
    }

    protected fun createView(parent: ViewGroup, @LayoutRes layoutId: Int): View {
        return inflater.inflate(layoutId, parent, false)
    }

    abstract fun createVH(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            HeaderItem.VIEW_TYPE -> HeaderViewHolder(
                createView(parent, headerLayoutId ?: R.layout.item_base_header),
                itemClickListener
            )
            EmptyItem.VIEW_TYPE -> EmptyViewHolder(
                createView(parent, emptyLayoutId ?: R.layout.item_base_empty),
                itemClickListener
            )
            LoadingItem.VIEW_TYPE -> LoadingViewHolder(
                createView(parent, loadingLayoutId ?: R.layout.item_base_loading),
                itemClickListener
            )
            LoadingItem.VIEW_TYPE_LARGE -> LoadingViewHolder(
                createView(parent, loadingLargeLayoutId ?: R.layout.item_base_loading_large),
                itemClickListener
            )
            else -> createVH(parent, viewType)
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = (holder as? BaseViewHolder<BaseModel, *>)
        vh?.isFirst = position <= 0
        vh?.isSecond = position == 1
        vh?.isLast = position >= items.size - 1
        vh?.isPreLast = position == items.size - 2
        vh?.bind(items[position])
    }
}