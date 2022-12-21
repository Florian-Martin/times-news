package fr.martinflorian.timesnews.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import fr.martinflorian.timesnews.data.State
import fr.martinflorian.timesnews.databinding.FragmentCategorizedArticlesBinding
import fr.martinflorian.timesnews.model.ArticleCategory
import fr.martinflorian.timesnews.ui.ArticlesAdapter
import fr.martinflorian.timesnews.utils.showLoadingSpinners
import fr.martinflorian.timesnews.utils.showToast
import fr.martinflorian.timesnews.utils.showView
import kotlinx.coroutines.launch


class CategorizedArticlesFragment : Fragment() {

    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentCategorizedArticlesBinding? = null
    val binding get() = _binding!!

    private lateinit var categorizedArticlesAdapter: ArticlesAdapter

    private val categoriesList = listOf(
        ArticleCategory("Technology"),
        ArticleCategory("Health"),
        ArticleCategory("Food"),
        ArticleCategory("Sports"),
        ArticleCategory("New York"),
        ArticleCategory("U.S"),
        ArticleCategory("Movies"),
        ArticleCategory("Style"),
        ArticleCategory("Arts")
    )

    private val viewModel: CategorizedArticlesViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            CategorizedArticlesViewModel.Factory(activity.application)
        )[CategorizedArticlesViewModel::class.java]
    }


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorizedArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArticles()

        categorizedArticlesAdapter = ArticlesAdapter {
            val direction =
                CategorizedArticlesFragmentDirections.actionCategoriesFragmentToArticleDetailFragment(
                    it.id
                )
            findNavController().navigate(direction)
        }

        val categoriesAdapter = CategoriesAdapter(categoriesList) {
            viewModel.switchCategory(it.name)
        }

        binding.apply {
            rvCategories.adapter = categoriesAdapter
            rvCategorizedArticles.adapter = categorizedArticlesAdapter
            swipeToRefreshCategorizedArticles.setOnRefreshListener { viewModel.getArticles() }
        }
    }

    override fun onStart() {
        viewModel.getArticles()
        super.onStart()
    }


    /**************************************
     * FUNCTIONS
     *************************************/
    private fun getArticles() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articles.collect { state ->
                    when (state) {
                        is State.Loading -> {
                            showView(binding.categorizedDataStatusImg, true)
                        }
                        is State.Success -> {
                            if (state.data.isNotEmpty()) {
                                categorizedArticlesAdapter.submitList(state.data)
                                binding.apply {
                                    showLoadingSpinners(
                                        swipeToRefreshCategorizedArticles,
                                        categorizedDataStatusImg,
                                        false
                                    )
                                }
                            }
                        }
                        is State.Error -> {
                            showToast(requireContext(), state.message)
                            binding.apply {
                                showLoadingSpinners(
                                    swipeToRefreshCategorizedArticles,
                                    categorizedDataStatusImg,
                                    false
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}