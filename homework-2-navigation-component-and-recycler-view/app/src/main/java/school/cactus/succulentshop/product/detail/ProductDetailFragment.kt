package school.cactus.succulentshop.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.HORIZONTAL
import school.cactus.succulentshop.BUNDLE_KEY_PRODUCT_ID
import school.cactus.succulentshop.R
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.Product
import school.cactus.succulentshop.product.ProductStore

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val store = ProductStore()

    private val adapter = RelatedProductAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
                _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
                return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

        val productId = requireArguments().getInt(BUNDLE_KEY_PRODUCT_ID)
        val product = store.findProduct(productId)

        binding.apply {
            imageView.setImageResource(product.imageUrl)
            titleText.text = product.title
            priceText.text = product.price
            descriptionText.text = product.description
        }

        binding.recyclerView.adapter = adapter
        //binding.recyclerView.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(1,HORIZONTAL)
        binding.recyclerView.addItemDecoration(RelatedProductDecoration())

        val randomList = getRandRelProducts(store.products,productId)

        adapter.submitList(randomList)

        adapter.itemClickListener = {
            val action = ProductDetailFragmentDirections.actionProductDetailFragmentSelf(it.id)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getRandRelProducts(list: List<Product>, id: Int) : List<Product>{
        val s: MutableSet<Product> = mutableSetOf()
        while (s.size < 5) {
            val x = (0..7).random()

            if (x == id-1) continue
            else s.add(list[x])
        }
        return s.toList()
    }
}