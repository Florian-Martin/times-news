package fr.martinflorian.timesnews.ui.bookmarks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.FragmentBookmarksBinding
import fr.martinflorian.timesnews.databinding.FragmentNewswireBinding


class BookmarksFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }
}