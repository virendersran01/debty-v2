package com.fabirt.debty.ui.common

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fabirt.debty.R

class SwipeItemCallback<T>(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    var adapter: ListAdapter<T, out RecyclerView.ViewHolder>? = null
    var delegate: SwipeToDeleteDelegate<T>? = null

    private val deleteIcon =
        ContextCompat.getDrawable(context, R.drawable.ic_round_delete_24)!!
    private val intrinsicWidth = deleteIcon.intrinsicWidth
    private val intrinsicHeight = deleteIcon.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter?.let { listAdapter ->
            val currentList = listAdapter.currentList
            currentList.getOrNull(viewHolder.bindingAdapterPosition)?.let { item ->
                delegate?.onSwiped(item)
            }
        }
    }

    // Draw background.
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)

        // Calculate position of delete icon
        val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val iconMargin = (itemHeight - intrinsicHeight) / 2
        val iconLeft = itemView.right - iconMargin - intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = iconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon.setTint(Color.WHITE)
        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        deleteIcon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /*
      override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
      To disable "swipe" for specific item return 0 here.
      For example:
      if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
      if (viewHolder?.adapterPosition == 0) return 0
       return super.getMovementFlags(recyclerView, viewHolder)
      }
     */

}

interface SwipeToDeleteDelegate<T> {
    /**
     * @param item The swiped item extracted from adapter list.
     */
    fun onSwiped(item: T)
}