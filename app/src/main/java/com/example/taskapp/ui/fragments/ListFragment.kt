package com.example.taskapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.databinding.ListFragmentBinding
import com.example.taskapp.datamodel.PostEntity
import com.example.taskapp.datamodel.PostModel
import com.example.taskapp.datamodel.SampleModelViewType
import com.example.taskapp.ui.activities.DetailActivity
import com.example.taskapp.ui.activities.MainContainerActivity
import com.example.taskapp.ui.activities.OnboardActivity
import com.example.taskapp.ui.adapters.GenericRecyclerAdapter
import com.example.taskapp.ui.viewmodel.ListViewModel
import com.example.taskapp.ui.viewmodel.MainStateEvent
import com.example.taskapp.util.*
import com.example.taskapp.util.interfaces.PostRetrofit
import com.example.taskapp.util.mappers.PostMapper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ListFragment:Fragment(),ListClickListener<PostModel> , NavigationView.OnNavigationItemSelectedListener{
    private val viewModel:ListViewModel by viewModels()
    private lateinit var list:List<ViewType<*>>
    @Inject
    lateinit var retrofit: PostRetrofit
    private lateinit var adapterRecycler:GenericRecyclerAdapter<ViewType<*>>
    private val adapterRecycler2 by lazy { GenericRecyclerAdapter<ViewType<*>>(this) }
    private lateinit var binding:ListFragmentBinding
    private lateinit var toggle:ActionBarDrawerToggle
    var tracker: SelectionTracker<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding=ListFragmentBinding.inflate(LayoutInflater.from(context), null, false)
        val drawer=binding.drawer
        toggle= ActionBarDrawerToggle(activity,drawer,R.string.open,R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigation=binding.listNavInclude.navigation
        navigation.setNavigationItemSelectedListener(this)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,menu)
        val searchItem=menu.findItem(R.id.search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterRecycler.filter.filter(newText)
                adapterRecycler2.filter.filter(newText)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initView()
        subscribeObservers()
    }
    private fun initView() {
        val activity=activity as AppCompatActivity
        activity.setSupportActionBar(binding.listInclude.toolbar)
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        binding.recyclerView.layoutManager=LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rv2.layoutManager=GridLayoutManager(context, 2)
        binding.rv2.adapter=adapterRecycler2
        adapterRecycler=GenericRecyclerAdapter(this)
        binding.recyclerView.adapter=adapterRecycler
        binding.addBottomInclude.button4.setOnClickListener{
            lifecycleScope.launch {
                addPost(
                    PostEntity(
                        1, list.size, binding.addBottomInclude.addTitleEditText.text.toString(),
                        binding.addBottomInclude.addBodyEditText.text.toString()
                    )
                )
            }
        }
        tracker = SelectionTracker.Builder(
                "mySelection",
                binding.recyclerView,
                MyItemKeyProvider(adapterRecycler),
                MyItemDetailsLookup(binding.recyclerView),
                StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        val tracker2 = SelectionTracker.Builder(
                "mySelection2",
                binding.rv2,
                MyItemKeyProvider(adapterRecycler2),
                MyItemDetailsLookup(binding.rv2),
                StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()
        tracker2.addObserver(object : SelectionTracker.SelectionObserver<String>() {
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val items = tracker2.selection.size()
                Toast.makeText(context, items.toString(), Toast.LENGTH_LONG).show()

            }
        })
      adapterRecycler.tracker=tracker
        adapterRecycler2.tracker=tracker2
        binding.addToBlogBtn.setOnClickListener{
                binding.addBottomInclude.addBottomMain.isClickable=true
                val behaviour=BottomSheetBehavior.from(binding.addBottomInclude.addBottomMain)
                context?.resources?.getDimension(R.dimen.edit_field_margin)?.toInt()?.let { it1 ->
                    behaviour.setPeekHeight(
                        it1,true)
                }
                behaviour.state=BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private suspend fun addPost(model:PostEntity) {
        val postModel=retrofit.addPost(model.title,model.body,model.userId)
        val mapper=PostMapper()
        adapterRecycler.addModel(SampleModelViewType(mapper.mapFromEntity(postModel)))
        adapterRecycler.notifyDataSetChanged()
        adapterRecycler2.notifyDataSetChanged()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
    private fun subscribeObservers() {

        viewModel.setStateEvent(MainStateEvent.ListEvent)
        viewModel.getDataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is CurrentState.Success<List<ViewType<*>>> -> {
                    list=dataState.data
                    displayProgress(false)
                    loadList(dataState.data)
                }
                is CurrentState.Loading -> {
                    displayProgress(true)
                }
                is CurrentState.Error -> {
                    displayProgress(false)
                    displayError(dataState.exception.toString())
                }

            }
        })
        tracker?.addObserver(
                object : SelectionTracker.SelectionObserver<String>() {
                    override fun onSelectionChanged() {
                        super.onSelectionChanged()
                        val items = tracker?.selection!!.size()
                        Toast.makeText(context, items.toString(), Toast.LENGTH_LONG).show()
                    }
                })
    }

    private fun displayError(toString: String) {
        Log.d("ListFragment", toString)
    }

    private fun displayProgress(display: Boolean) {
        if(display)
        {
            binding.progressBar.visibility=View.VISIBLE
        }
        else{
            binding.progressBar.visibility=View.GONE
        }
    }

    private fun loadList(list: List<ViewType<*>>) {
        val list2= ArrayList<ViewType<*>>()
        for(index in 0 until 10){
            list2.add(index, list[index])
        }
        adapterRecycler.setList(list2)
        adapterRecycler.notifyDataSetChanged()
        loadList2(list)
    }

    private fun loadList2(list: List<ViewType<*>>) {
        val list3= ArrayList<ViewType<*>>()
        for(index in list.indices){
            list3.add(index, list[index])
        }
        adapterRecycler2.setList(list3)
        adapterRecycler2.notifyDataSetChanged()
    }

    override fun onListClick(model: PostModel) {
        startActivity(Intent(context, DetailActivity::class.java).putExtra("data", model).putExtra("listDetail","listDetail"))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_exchange -> startActivity(Intent(context,MainContainerActivity::class.java).putExtra("ex","Exchange"))
            R.id.menu_encrypt -> startActivity(Intent(context,MainContainerActivity::class.java).putExtra("ex","Encrypt"))
            R.id.menu_contacts -> startActivity(Intent(context,MainContainerActivity::class.java).putExtra("ex","Contacts"))
            R.id.menu_insta -> startActivity(Intent(context,MainContainerActivity::class.java).putExtra("ex","Instagram"))
            R.id.menu_onboard -> startActivity(Intent(context,OnboardActivity::class.java))
        }
        return true
    }


}