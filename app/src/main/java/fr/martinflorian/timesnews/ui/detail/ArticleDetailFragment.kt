package fr.martinflorian.timesnews.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.FragmentArticleDetailBinding
import fr.martinflorian.timesnews.model.Article
import fr.martinflorian.timesnews.utils.showView


class ArticleDetailFragment : Fragment(), MenuProvider {

    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!
    private val navArgs: ArticleDetailFragmentArgs by navArgs()
    private val viewModel: ArticleDetailViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            ArticleDetailViewModel.Factory(activity.application)
        )[ArticleDetailViewModel::class.java]
    }


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navArgs.id

        viewModel.getArticleById(id)
        viewModel.article.observe(viewLifecycleOwner) {
            it?.let { bind(it) }
        }
    }


    /**************************************
     * FUNCTIONS
     *************************************/

    /**
     * Bind each layout's views with the [Article] data
     */
    private fun bind(article: Article?) {
        binding.apply {
            article?.let {
                articleDetailCategory.text = it.section
                articleDetailTitle.text = it.title
                val imageUri = it.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
                articleDetailImage.load(imageUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.image_not_found)
                }
                articleDetailPublicationDateTv.text = resources.getString(
                    R.string.article_detail_publication_date,
                    it.publicationDate
                )
                articleDetailAuthorsTv.text = it.authors
                articleDetailRedirectionTv.text = resources.getString(
                    R.string.article_detail_redirection,
                    it.webUrl
                )
                toggleBookmarkButtons(it.isBookmarked)
            }

            // Trending or Newswire article type
            if (article?.leadParagraph.isNullOrEmpty()) {
                showView(articleDetailSnippet, false)
                articleDetailLeadParagraphTv.text = article?.snippet
            } else {
                articleDetailSnippet.text = article?.snippet
                articleDetailLeadParagraphTv.text = article?.leadParagraph
            }

            articleDetailAddToBookmarkButton.setOnClickListener {}
            articleDetailRemoveFromBookmarkButton.setOnClickListener {}
        }
    }

    /**
     * Shows button to add to or remove from bookmarks the currently displayed article
     */
    private fun toggleBookmarkButtons(isBookmarked: Boolean) {
        binding.apply {
            showView(articleDetailAddToBookmarkButton, !isBookmarked)
            showView(articleDetailRemoveFromBookmarkButton, isBookmarked)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.appbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.share_article -> shareArticle()
        }
        return false
    }

    private fun shareArticle() {
        val snippet = viewModel.article.value?.snippet
        val title = viewModel.article.value?.title
        val authors = viewModel.article.value?.authors
        val webUrl = viewModel.article.value?.webUrl
        val pubDate = viewModel.article.value?.publicationDate
        val message = getString(R.string.share_message, title, snippet, authors, pubDate, webUrl)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            putExtra(Intent.EXTRA_TEXT, message)
            putExtra(Intent.EXTRA_TITLE, title)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }
}