package fr.martinflorian.timesnews.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import fr.martinflorian.timesnews.R

abstract class SwipeToDeleteCallback(private val context: Context?) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val paint = Paint()

            if (dX < 0) {
                val icon = BitmapFactory.decodeResource(
                    context?.resources, R.mipmap.ic_delete
                )
                paint.color = Color.parseColor("#CC1A1A")

                c.drawRect(
                    itemView.right.toFloat() + dX, itemView.top.toFloat(),
                    itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                )

                c.drawBitmap(
                    icon,
                    itemView.right.toFloat() - icon.width,
                    itemView.top.toFloat() + (
                            itemView.bottom.toFloat() -
                                    itemView.top.toFloat() - icon.height.toFloat()
                            ) / 2,
                    paint
                )
            }
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = 0.75f
}