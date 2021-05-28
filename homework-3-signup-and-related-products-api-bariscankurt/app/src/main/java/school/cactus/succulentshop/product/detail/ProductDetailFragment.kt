package school.cactus.succulentshop.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.api.product.RelatedProducts
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.toProductItem
import school.cactus.succulentshop.product.toProductItemList


class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!

    private val adapter = RelatedProductAdapter()

    private val args: ProductDetailFragmentArgs by navArgs()

    private var originalResImageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

        fetchProduct()

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, HORIZONTAL)
            recyclerView.addItemDecoration(RelatedProductDecoration())
        }

        adapter.itemClickListener = {
            val action = ProductDetailFragmentDirections.productDetailToSelf(it.id)
            findNavController().navigate(action)
        }

        binding.imageView.setOnClickListener {
            if (!originalResImageUrl.isNullOrEmpty()) {
                val action = ProductDetailFragmentDirections
                    .productDetailToImageViewer(originalResImageUrl)
                findNavController().navigate(action)
            }
        }
    }

    private fun fetchProduct() {
        api.getProductById(args.productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                when (response.code()) {
                    200 -> {
                        onProductSuccess(response.body()!!)
                        fetchRelatedProducts()
                    }
                    401 -> onTokenExpired()
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Snackbar.make(
                    binding.root,
                    R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        fetchProduct()
                    }
                    .show()
            }
        })
    }

    private fun fetchRelatedProducts() {
        api.getRelatedProducts(args.productId).enqueue(object : Callback<RelatedProducts> {
            override fun onResponse(
                call: Call<RelatedProducts>,
                response: Response<RelatedProducts>
            ) {
                when (response.code()) {
                    200 -> onRelatedProductSuccess(response.body()!!)
                    401 -> onTokenExpired()
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<RelatedProducts>, t: Throwable) {
                Snackbar.make(
                    binding.root,
                    R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        fetchRelatedProducts()
                    }
                    .show()
            }
        })
    }

    private fun onProductSuccess(product: Product) {
        val productItem = product.toProductItem()

        originalResImageUrl = productItem.originalResImageUrl

        binding.apply {
            titleText.text = productItem.title
            priceText.text = productItem.price
            descriptionText.text = productItem.description

            Glide.with(root)
                .load(productItem.highResImageUrl)
                .centerInside()
                .into(imageView)
        }
    }

    private fun onRelatedProductSuccess(body: RelatedProducts) {
        if (body.products.isEmpty().not()) {
            binding.relateds.visibility = View.VISIBLE
            body.products.toProductItemList()
            adapter.submitList(body.products.toProductItemList())
        }

        binding.group.visibility = View.VISIBLE
        binding.progressIndicator.visibility = View.INVISIBLE
    }

    private fun onUnexpectedError() {
        Snackbar.make(requireView(), R.string.unexpected_error_occured, Snackbar.LENGTH_LONG)
    }

    private fun onTokenExpired() {
        Snackbar.make(
            binding.root,
            R.string.your_session_is_expired,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.log_in) {
                navigateToLogin()
            }
            .show()
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.toLoginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}