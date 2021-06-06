package school.cactus.succulentshop.imageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentImageViewerBinding
import school.cactus.succulentshop.infra.BaseFragment

class ImageViewerFragment : BaseFragment() {

    private val args: ImageViewerFragmentArgs by navArgs()

    private var _binding: FragmentImageViewerBinding? = null

    private val binding get() = _binding!!

    override val viewModel: ImageViewerViewModel by viewModels {
        ImageViewerViewModelFactory(args.originalResImageUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageViewerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}