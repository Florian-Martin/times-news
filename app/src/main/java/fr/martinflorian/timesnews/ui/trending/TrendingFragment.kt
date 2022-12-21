package fr.martinflorian.timesnews.ui.trending

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
import fr.martinflorian.timesnews.databinding.FragmentTrendingBinding
import fr.martinflorian.timesnews.ui.ArticlesAdapter
import fr.martinflorian.timesnews.utils.showLoadingSpinners
import fr.martinflorian.timesnews.utils.showToast
import fr.martinflorian.timesnews.utils.showView
import kotlinx.coroutines.launch


class TrendingFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentTrendingBinding? = null
    val binding get() = _binding!!

    private lateinit var articlesRvAdapter: ArticlesAdapter

    private val viewModel: TrendingViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            TrendingViewModel.Factory(activity.application),
        )[TrendingViewModel::class.java]
    }


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArticles()

        articlesRvAdapter = ArticlesAdapter {
            val direction =
                TrendingFragmentDirections.actionNavigationTrendingToNavigationArticleDetail(
                    it.id
                )
            findNavController().navigate(direction)
        }

        binding.apply {
            rvTrending.adapter = articlesRvAdapter
            swipeToRefreshTrending.setOnRefreshListener { viewModel.getArticles() }
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
                            showView(binding.trendingDataStatusImg, true)
                        }
                        is State.Success -> {
                            if (state.data.isNotEmpty()) {
                                articlesRvAdapter.submitList(state.data)
                                binding.apply {
                                    showLoadingSpinners(
                                        swipeToRefreshTrending,
                                        trendingDataStatusImg,
                                        false
                                    )
                                }
                            }
                        }
                        is State.Error -> {
                            showToast(requireContext(), state.message)
                            binding.apply {
                                showLoadingSpinners(
                                    swipeToRefreshTrending,
                                    trendingDataStatusImg,
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