package com.example.gozlebrowser.presentation.fragment

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gozlebrowser.R
import com.example.gozlebrowser.databinding.FragmentSearchBinding
import com.example.gozlebrowser.util.DetectConnection.checkInternetConnection
import com.example.gozlebrowser.util.KeyboardHelper


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding == null")

    var baseUrl = ""
    val yandexMapUrl = "maps.yandex"
    val yandexMapUrl2 = "yandex.ru/maps"


    var uploadMessage: ValueCallback<Array<Uri>>? = null

    private var mUploadMessage: ValueCallback<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        baseUrl = binding.etSearch.text.toString()
        changeFocus()

        binding.apply {
            etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loadUrl()
                    return@OnEditorActionListener true
                }
                false
            })

            btnHome.setOnClickListener{
                etSearch.setText("")
                loadUrl()
            }
            btnRefresh.setOnClickListener{
                loadUrl()
            }
            btnQrCode.setOnClickListener{
                launchFragment(QRFragment())
            }
            btnMic.setOnClickListener{
                // do something
            }
        }



        if (!checkInternetConnection(requireActivity())) {
            //show some error connection message
            binding.webView.visibility = View.INVISIBLE
        } else {
            //remove error connection message
            binding.webView.apply {
                visibility = View.VISIBLE
                setBackgroundColor(resources.getColor(R.color.my_primary));
                webChromeClient = WebChromeClient()
                webViewClient = GozleWebViewClient()
                settings.domStorageEnabled = true
                settings.allowContentAccess = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = true
                settings.displayZoomControls = false
                webChromeClient = MyWebChromeClient()

                when {
                    URLUtil.isValidUrl(baseUrl) -> loadUrl(baseUrl)
                    baseUrl.contains(".com", ignoreCase = true) -> loadUrl(baseUrl)
                    else -> loadUrl("https://www.google.com/search?q=$baseUrl")
                }

                requireActivity().title = baseUrl;
                loadUrl(baseUrl)
                with(settings) {
                    @SuppressLint("SetJavaScriptEnabled")
                    javaScriptEnabled = true
                    databaseEnabled = true
                    allowFileAccess = true
                }
                setDownloadListener { url, userAgent, contentDescription, mimeType, length ->
                    Log.d(
                        "DOWNLOAD_LOG", "url: $url \n userAgent: $userAgent \n" +
                                " contentDescription: $contentDescription \n" +
                                " mimeType: $mimeType \n" +
                                " length: $length"
                    )

                    val request = DownloadManager.Request(Uri.parse(url)).apply {
                        val fileName = URLUtil.guessFileName(url, contentDescription, mimeType)
                        requireActivity().title = fileName
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    }

                    (requireActivity().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE)
                            as DownloadManager).enqueue(request)
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.downloading_dots),
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }

        }
    }

    private fun loadUrl() {
        baseUrl = binding.etSearch.text.toString()
        binding.webView.apply {
            when {
                URLUtil.isValidUrl(baseUrl) ->
                    loadUrl(baseUrl)

                baseUrl.contains(".com", ignoreCase = true) ->
                    loadUrl(baseUrl)

                else ->
                    loadUrl("https://www.google.com/search?q=$baseUrl")
            }
            changeFocus()
        }
    }

    private fun changeFocus() {
        binding.apply {
            etSearch.clearFocus();

            etSearch.setOnFocusChangeListener { view, _ ->
                KeyboardHelper.hideSoftKeyboard(requireActivity(), view)
                if (!view.isFocused && etSearch.text.toString().isNotBlank()) {
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.lock_without_margin,
                        0,
                        0,
                        0
                    )
                    llContainer.background = null
                    btnMic.visibility = View.GONE
                    btnQrCode.visibility = View.GONE
                    btnHome.visibility = View.VISIBLE
                    btnRefresh.visibility = View.VISIBLE

                } else {
                    etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    llContainer.background =
                        resources.getDrawable(R.drawable.edit_text_rounded_corner)
                    btnMic.visibility = View.VISIBLE
                    btnQrCode.visibility = View.VISIBLE
                    btnHome.visibility = View.GONE
                    btnRefresh.visibility = View.GONE
                }
            }
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
    inner class GozleWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val urlStr = request?.url?.toString() ?: ""
                if (urlStr.contains(baseUrl)) {
                    return false
                } else {
                    /*                    val address = Uri.parse(urlStr)
                    val openlink = Intent(Intent.ACTION_VIEW, address)
                    startActivity(Intent.createChooser(openlink, "Browser"))*/
                    // открывает сразу в браузере по умолчанию
                    if (urlStr.contains(yandexMapUrl) || urlStr.contains(yandexMapUrl2)) {
                        try {
                            val appMapIntent = Intent(Intent.ACTION_VIEW)
                            appMapIntent.data = Uri.parse(urlStr)
                            startActivity(appMapIntent)
                        } catch (e: Exception) {
                            Log.d("logUrl", " e = $e")
                            val split = urlStr.split("browser_fallback_url=")
                            val url = split[split.lastIndex]
                            val webIntent = Intent(Intent.ACTION_VIEW)
                            webIntent.data = Uri.parse(url)
                            startActivity(webIntent)
                        }
                        return true
                    } else {
                        val webIntent = Intent(Intent.ACTION_VIEW)
                        webIntent.data = Uri.parse(urlStr)
                        startActivity(webIntent)
                        return true
                    }
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return if (url?.contains(baseUrl) == true) {
                false
            } else {
                // открывает сразу в браузере по умолчанию
                if (url?.contains(yandexMapUrl) == true || url?.contains(yandexMapUrl2) == true) {
                    try {
                        val appMapIntent = Intent(Intent.ACTION_VIEW)
                        appMapIntent.data = Uri.parse(url)
                        startActivity(appMapIntent)
                    } catch (e: Exception) {
                        val split = url.split("browser_fallback_url=")
                        val urlCur = split[split.lastIndex]
                        val webIntent = Intent(Intent.ACTION_VIEW)
                        webIntent.data = Uri.parse(urlCur)
                        startActivity(webIntent)
                    }
                    return true
                } else {
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.data = Uri.parse(url)
                    startActivity(webIntent)
                    return true
                }
            }
        }
    }

    internal inner class MyWebChromeClient : WebChromeClient() {
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
            mUploadMessage = uploadMsg
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "*/*"
            startActivityForResult(
                Intent.createChooser(i, "File Browser"),
                FILE_CHOOSER_RESULT_CODE
            )
        }

        // For Lollipop 5.0+ Devices
        override fun onShowFileChooser(
            mWebView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: WebChromeClient.FileChooserParams
        ): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (uploadMessage != null) {
                    uploadMessage?.onReceiveValue(null)
                    uploadMessage = null
                }
                uploadMessage = filePathCallback
                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(
                        requireActivity(),
                        "Cannot Open File Chooser",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                return true
            } else {
                return false
            }
        }

        //For Android 4.1 only
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            mUploadMessage = uploadMsg
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(
                Intent.createChooser(intent, "File Browser"),
                FILE_CHOOSER_RESULT_CODE
            )
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
            mUploadMessage = uploadMsg
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "*/*"
            startActivityForResult(
                Intent.createChooser(i, "File Browser"),
                FILE_CHOOSER_RESULT_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (uploadMessage == null)
                return
        }
        uploadMessage?.onReceiveValue(
            WebChromeClient.FileChooserParams.parseResult(
                resultCode,
                data
            )
        )
        uploadMessage = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val REQUEST_SELECT_FILE = 100
        private const val FILE_CHOOSER_RESULT_CODE = 1
    }

}