package school.cactus.succulentshop.product.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.product.toProductItemList

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null

    private val binding get() = _binding!!

    private val adapter = ProductAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        val view = binding.root
        setHasOptionsMenu(true);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            recyclerView.addItemDecoration(ProductDecoration())
        }

        fetchProducts()

        adapter.itemClickListener = {
            val action = ProductListFragmentDirections.productListToProductDetail(it.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                JwtStore(requireContext()).deleteJwt()
                findNavController().navigate(R.id.toLoginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchProducts() {
        api.listAllProducts().enqueue(object : retrofit2.Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                when (response.code()) {
                    200 -> onSuccess(response.body()!!)
                    401 -> onTokenExpired()
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Snackbar.make(
                    binding.root,
                    R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(R.string.retry) {
                        fetchProducts()
                    }
                    .show()
            }
        })
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.toLoginFragment)
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
            .setAction(R.string.retry) {
                navigateToLogin()
            }
            .show()
    }

    private fun onSuccess(products: List<Product>) {
        binding.apply {
            progressIndicator.visibility = View.INVISIBLE
            recyclerView.visibility = View.VISIBLE
        }
        adapter.submitList(products.toProductItemList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}