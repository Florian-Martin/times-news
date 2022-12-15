package fr.martinflorian.timesnews.ui.newswire

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.martinflorian.timesnews.databinding.FragmentNewswireBinding


class NewswireFragment : Fragment() {
    /**************************************
     * PROPERTIES
     *************************************/
    private var _binding: FragmentNewswireBinding? = null
    private val binding get() = _binding!!


    /**************************************
     * LIFECYCLE
     *************************************/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewswireBinding.inflate(inflater, container, false)
        return binding.root
    }
}