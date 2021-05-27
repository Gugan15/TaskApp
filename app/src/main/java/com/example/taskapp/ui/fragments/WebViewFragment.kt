package com.example.taskapp.ui.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnNextLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.WebviewFragmentBinding

class WebViewFragment: Fragment() {
    private var activityHeight = 0
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding=DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.webview_fragment,null,false) as WebviewFragmentBinding
        val w1=binding.webViewLayout
        w1.webViewClient=MyBrowser()
        val activity=activity as AppCompatActivity
        activity.setSupportActionBar(binding.webViewInclude.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.webViewInclude.toolbar.setNavigationOnClickListener{
            activity.onBackPressed()
        }
        w1.settings.loadsImagesAutomatically = true
        w1.settings.javaScriptEnabled = true
        w1.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        activity.window?.decorView?.doOnNextLayout {
            val displayFrame = Rect()
            requireActivity().window.decorView.getWindowVisibleDisplayFrame(displayFrame)
            activityHeight = displayFrame.height()
        }
        binding.webViewEditText.setOnEditorActionListener{_,id,_->
            if(id==EditorInfo.IME_ACTION_DONE){
                w1.loadUrl(binding.webViewEditText.text.toString())
                binding.webContainer.visibility=View.VISIBLE
                binding.webViewEditLayout.visibility=View.INVISIBLE
            }
            true
        }
        binding.searchWebsite.setOnClickListener{
            binding.webContainer.visibility=View.INVISIBLE
           // checkKeyboard()
            binding.webViewEditLayout.visibility=View.VISIBLE
        }
        return binding.root
    }


    inner class MyBrowser:WebViewClient()
    {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, url:String): Boolean {
            view?.loadUrl(url)
            view?.setDownloadListener { pageUrl, _, contentDisposition, mimetype, _ ->
                val request=DownloadManager.Request(Uri.parse(pageUrl))
                request.setTitle(URLUtil.guessFileName(pageUrl,contentDisposition,mimetype))
                request.setDescription("Downloading file...")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(pageUrl,contentDisposition,mimetype ))
                val  downloadManager=context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
                val onComplete=object:BroadcastReceiver(){
                    override fun onReceive(context: Context?, intent: Intent?) {
                        Toast.makeText(context,"Download Completed",Toast.LENGTH_LONG).show()
                    }

                }
                context!!.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

            }
            return true

        }
    }
}