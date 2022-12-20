package fr.martinflorian.timesnews.ui.bookmarks

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
import fr.martinflorian.timesnews.databinding.FragmentBookmarksBinding
import fr.martinflorian.timesnews.ui.ArticlesAdapter
import fr.martinflorian.timesnews.utils.showToast
import fr.martinflorian.timesnews.utils.showView
import kotlinx.coroutines.launch


class BookmarksFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentBookmarksBinding? = null
    val binding get() = _binding!!
    private lateinit var bookmarksRvAdapter: ArticlesAdapter

    private val viewModel: BookmarksViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            BookmarksViewModel.Factory(activity.application)
        )[BookmarksViewModel::class.java]
    }


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBookmarks()

        bookmarksRvAdapter = ArticlesAdapter {
            val direction =
                BookmarksFragmentDirections.actionNavigationBookmarksToNavigationArticleDetail(
                    it.id
                )
            findNavController().navigate(direction)
        }

        binding.rvBookmarks.adapter = bookmarksRvAdapter
    }

    override fun onStart() {
        viewModel.getBookmarks()
        super.onStart()
    }


    /**************************************
     * FUNCTIONS
     *************************************/
    private fun getBookmarks() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getBookmarks()
                viewModel.bookmarks.collect { state ->
                    when (state) {
                        is State.Error -> {
                            showToast(requireContext(), state.message)
                        }
                        is State.Loading -> {
                            showView(binding.bookmarksDataStatusImg, true)
                        }
                        is State.Success -> {
                            binding.apply {
                                bookmarksRvAdapter.submitList(state.data)
                                showView(bookmarksDataStatusImg, false)
                            }
                        }
                    }
                }
            }
        }
    }
}