package school.cactus.succulentshop.helpers

import android.view.View
import android.widget.ProgressBar

fun showProgressIndicator(progressBar: ProgressBar, view: View, showProgInd: Boolean) {
    if (!showProgInd) {
        progressBar.visibility = View.INVISIBLE
        view.visibility = View.VISIBLE
    } else {
        progressBar.visibility = View.VISIBLE
        view.visibility = View.INVISIBLE
    }
}