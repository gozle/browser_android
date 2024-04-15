package com.example.gozlebrowser.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gozlebrowser.databinding.FragmentQrBinding
import com.example.gozlebrowser.databinding.FragmentSearchBinding

class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    private val binding: FragmentQrBinding
        get() = _binding ?: throw RuntimeException("FragmentQrBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}