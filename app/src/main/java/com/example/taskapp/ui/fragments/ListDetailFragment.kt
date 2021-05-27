package com.example.taskapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.taskapp.R
import com.example.taskapp.databinding.ListDetailFragmentBinding
import com.example.taskapp.datamodel.PostModel
import com.example.taskapp.util.SpeechUtil
import com.example.taskapp.util.interfaces.ImageRetrofit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File
import javax.inject.Inject
@AndroidEntryPoint
class ListDetailFragment: Fragment(){
    private lateinit var listBinding: ListDetailFragmentBinding
    private lateinit var model:PostModel
    @Inject
    lateinit var retrofit:ImageRetrofit
    private lateinit var speech:SpeechUtil
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listBinding = ListDetailFragmentBinding.inflate(LayoutInflater.from(context), null, false)
        val appActivity = activity as AppCompatActivity
        appActivity.setSupportActionBar(listBinding.detailsInclude.toolbar)
        appActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        model = appActivity.intent.getParcelableExtra("data")!!
        permissionCheck()
            speech = context?.let { it1 -> SpeechUtil(it1) }!!
            speech.initTextToSpeech()

        listBinding.detailsInclude.toolbar.setNavigationOnClickListener {
            appActivity.onBackPressed()
        }
        listBinding.bottomSheetInclude.linearLayout2.setOnClickListener {
            shareDetails()
        }
        listBinding.bottomSheetInclude.callClickLayout.setOnClickListener {
            makeCall()
        }
        listBinding.imageView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(
                Intent.createChooser(gallery, "Select a Picture"),
                43,
                null
            )
        }
        return listBinding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 43) {
            val imageUri = data?.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = imageUri?.let {
                context?.contentResolver?.query(
                    it,
                    filePathColumn, null, null, null
                )
            }
            cursor?.moveToFirst()
            val columnIndex: Int? = cursor?.getColumnIndex(filePathColumn[0])
            val picturePath = columnIndex?.let { cursor.getString(it) }
            cursor?.close()
            context?.let { Glide.with(it).load(picturePath).into(listBinding.imageView) }
            lifecycleScope.launch {
                if (picturePath != null) {
                    val file=File(picturePath)
                    val body: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file)
                    val multi=MultipartBody.Part.createFormData("image",file.name,body)
                    retrofit.postImage("58210c8bf23b47abb182beed240ab713",multi)
                }
            }
        }
    }
    private fun shareDetails() {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT,listBinding.textView5.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT,listBinding.textView6.text.toString())
        startActivity(Intent.createChooser(intent,"Share Via"))
    }

    private fun makeCall() {
        val dial="tel:8124370674"
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
    }

    private fun permissionCheck() {
        if (context?.applicationContext?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.CALL_PHONE)
            } !=
            PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.CALL_PHONE)
            } !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.CALL_PHONE), 354)
            return
        } else {
            initData(model)
        }
    }

    fun hideBottomSheetFromOutSide(event: MotionEvent) {
        val mBottomSheetBehavior= BottomSheetBehavior.from(listBinding.bottomSheetInclude.bottomSheetMain)
        if (mBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            val outRect = Rect()
            listBinding.bottomSheetInclude.bottomSheetMain.getGlobalVisibleRect(outRect)
            if (!outRect.contains(
                    event.rawX.toInt(),
                    event.rawY.toInt()
                )
            ) mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    private fun initData(model: PostModel) {
        listBinding.textView5.text=model.title
        listBinding.textView6.text=model.body
        listBinding.textView7.text=model.userId.toString()
        listBinding.bottomSheetInclude.speakClickLayout.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context?.resources?.getString(R.string.example_bottom_sheet)?.let { it1 ->
                    speech.speakText(
                        it1
                    )
                }

            }
            else
                context?.resources?.getString(R.string.example_bottom_sheet)?.let { it1 ->
                    speech.speakTextPreLollipop(
                        it1
                    )
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speech.destroyTextToSpeech()
    }

}
