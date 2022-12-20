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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.data.State
import fr.martinflorian.timesnews.databinding.FragmentBookmarksBinding
import fr.martinflorian.timesnews.ui.ArticlesAdapter
import fr.martinflorian.timesnews.utils.SwipeToDeleteCallback
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
        swipeToDeleteHandling()
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

    private fun swipeToDeleteHandling() {
        val swipeTodDeleteCallback =
            object : SwipeToDeleteCallback(requireContext().applicationContext) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition
                    val bookmarkToDelete = bookmarksRvAdapter.currentList[position]
                    if (direction == ItemTouchHelper.LEFT) {
                        try {
                            viewModel.deleteBookmark(bookmarkToDelete)
                            Snackbar.make(
                                binding.root,
                                getString(R.string.delete_bookmark_snackbar_msg),
                                BaseTransientBottomBar.LENGTH_LONG
                            )
                                .setAction(
                                    getString(R.string.undo)
                                ) {
                                    viewModel.restoreBookmark(bookmarkToDelete)
                                }
                                .show()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeTodDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvBookmarks)
    }
}