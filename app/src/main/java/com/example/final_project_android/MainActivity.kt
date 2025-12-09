package com.example.final_project_android

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_android.data.db.MealDatabase
import com.example.final_project_android.data.repository.MealRepository
import com.example.final_project_android.ui.MealAdapter
import com.example.final_project_android.ui.MealViewModel
import com.example.final_project_android.ui.MealViewModelProviderFactory

class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MealViewModel
    private lateinit var mealAdapter: MealAdapter

    private lateinit var etSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var rvMeals: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var rootLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()


        setupEdgeToEdge()


        setupViewModel()


        setupRecyclerView()


        loadLastSearch()


        btnSearch.setOnClickListener {
            val query = etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchMeals(query)
                saveLastSearch(query)
                hideKeyboard()
            } else {
                Toast.makeText(this, "Please enter a meal name", Toast.LENGTH_SHORT).show()
            }
        }


        observeViewModel()
    }

    private fun initViews() {

        etSearch = findViewById(R.id.et_search)
        btnSearch = findViewById(R.id.btn_search)
        rvMeals = findViewById(R.id.rv_meals)
        progressBar = findViewById(R.id.progress_bar)

        rootLayout = findViewById(R.id.main_root) ?: findViewById(android.R.id.content)
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViewModel() {
        val mealRepository = MealRepository(MealDatabase(this))
        val viewModelProviderFactory = MealViewModelProviderFactory(mealRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MealViewModel::class.java]
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter()
        rvMeals.apply {
            adapter = mealAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


        mealAdapter.setOnItemClickListener { meal ->
            viewModel.saveMeal(meal)
            Toast.makeText(this, "Saved: ${meal.name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchMeals(query: String) {
        showLoading()
        viewModel.searchMeals(query)
    }

    private fun observeViewModel() {

        viewModel.searchMealsLiveData.observe(this) { response ->
            hideLoading()
            if (response.isSuccessful) {
                response.body()?.let { mealResponse ->
                    mealAdapter.differ.submitList(mealResponse.meals)
                    if (mealResponse.meals == null) {
                        Toast.makeText(this, "No meals found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.errorLiveData.observe(this) { errorMessage ->
            hideLoading()
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }




    private fun saveLastSearch(query: String) {
        val sharedPref = getSharedPreferences("MyRecipeAppPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LAST_SEARCH_QUERY", query)
            apply()
        }
    }

    private fun loadLastSearch() {
        val sharedPref = getSharedPreferences("MyRecipeAppPrefs", Context.MODE_PRIVATE)
        val lastQuery = sharedPref.getString("LAST_SEARCH_QUERY", "")
        if (!lastQuery.isNullOrEmpty()) {
            etSearch.setText(lastQuery)

        }
    }



    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
