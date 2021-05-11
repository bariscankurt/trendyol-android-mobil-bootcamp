package school.cactus.succulentshop.product.detail

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import school.cactus.succulentshop.R

class RelatedProductDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spacing = view.context.resources.getDimensionPixelSize(R.dimen.item_product_spacing)
        val position =parent.getChildAdapterPosition(view)
        val spanIndex =(view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

        val isAtRight = spanIndex == 1
        val isAtFirstLine = position < 2


        outRect.right = spacing * 2
    }
}