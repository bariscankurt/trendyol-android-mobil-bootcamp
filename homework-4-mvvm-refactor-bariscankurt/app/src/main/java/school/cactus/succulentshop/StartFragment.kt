package school.cactus.succulentshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import school.cactus.succulentshop.auth.JwtStore

class StartFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (JwtStore(requireContext()).loadJwt() == null) {
            findNavController().navigate(R.id.startToLogin)
        } else {
            findNavController().navigate(R.id.startToProductList)
        }
    }
}