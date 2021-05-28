package school.cactus.succulentshop.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentImageViewerBinding

class ImageViewerFragment : Fragment() {

    private val args: ImageViewerFragmentArgs by navArgs()

    private var _binding: FragmentImageViewerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

        Glide.with(binding.root)
            .load(args.originalResImageUrl)
            .centerInside()
            .into(binding.photoView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}