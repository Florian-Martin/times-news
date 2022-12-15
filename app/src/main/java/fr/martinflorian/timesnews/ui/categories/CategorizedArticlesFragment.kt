package fr.martinflorian.timesnews.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.martinflorian.timesnews.R
import fr.martinflorian.timesnews.databinding.FragmentCategorizedArticlesBinding
import fr.martinflorian.timesnews.databinding.FragmentNewswireBinding


class CategorizedArticlesFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentCategorizedArticlesBinding? = null
    private val binding get() = _binding!!


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorizedArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }
}