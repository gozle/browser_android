package com.example.gozlebrowser.presentation.homePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gozlebrowser.R
import com.example.gozlebrowser.databinding.FragmentHomeBinding
import com.example.gozlebrowser.domain.model.Recent
import com.example.gozlebrowser.presentation.qrPage.QRFragment
import com.example.gozlebrowser.presentation.searchPage.SearchFragment

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recents = mutableListOf<Recent>()
        recents.add(Recent(1, "Turkenportal", ""))
        recents.add(Recent(2, "Tmcars", ""))
        recents.add(Recent(3, "Turkenportal", ""))
        recents.add(Recent(4, "horjuntv.com.tm", ""))
        recents.add(Recent(5, "gozle.org", ""))
        recents.add(Recent(6, "horjuntv.com.tm", ""))

        val adapter = RecentAdapter(requireActivity())
        binding.rvRecents.adapter = adapter
        adapter.submitList(recents)

        binding.btnSearch.setOnClickListener {
            launchFragment(SearchFragment())
        }
        binding.btnQrCode.setOnClickListener {
            launchFragment(QRFragment())
        }

    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}