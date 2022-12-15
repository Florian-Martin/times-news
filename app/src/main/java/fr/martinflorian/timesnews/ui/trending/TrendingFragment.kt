package fr.martinflorian.timesnews.ui.trending

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.martinflorian.timesnews.databinding.FragmentTopStoriesBinding


class TrendingFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentTopStoriesBinding? = null
    private val binding get() = _binding!!


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }
}