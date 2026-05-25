package com.example.networkapi;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.networkapi.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Meal> meals = new ArrayList<>();
    private RecyclerViewAdapter mAdapter;
    private boolean isGridView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle("FoodApp");

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            loadDataFromApi();
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    binding.swipeRefreshLayout.setRefreshing(false), 1500);
        });

        setupRecyclerView();
        loadDataFromApi();

        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_grid) {
                isGridView = !isGridView;

                item.setIcon(isGridView ? R.drawable.baseline_view_list_24 : R.drawable.baseline_grid_view_24);
                
                TransitionManager.beginDelayedTransition(binding.recyclerView, new AutoTransition());
                
                updateLayoutManager();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        mAdapter = new RecyclerViewAdapter(this, meals);
        updateLayoutManager();
        binding.recyclerView.setAdapter(mAdapter);
    }

    private void updateLayoutManager() {
        if (isGridView) {
            int spanCount = (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_PORTRAIT) ? 2 : 4;
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        } else {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        mAdapter.setViewType(isGridView);
    }

    public void loadDataFromApi() {
        binding.progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("meals");
                        ArrayList<Meal> allMeals = new ArrayList<>();

                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String id = data.getString("idMeal").trim();
                                String mealName = data.getString("strMeal").trim();
                                String photo = data.getString("strMealThumb").trim();
                                String area = data.optString("strArea", "Unknown").trim();

                                allMeals.add(new Meal(id, mealName, photo, area));
                            }

                            Collections.shuffle(allMeals);

                            meals.clear();
                            int limit = Math.min(allMeals.size(), 10);
                            for (int i = 0; i < limit; i++) {
                                meals.add(allMeals.get(i));
                            }

                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Gagal memuat data!", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(jsObjRequest);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isGridView) {
            int spanCount = (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) ? 2 : 4;
            if (binding.recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                ((GridLayoutManager) binding.recyclerView.getLayoutManager()).setSpanCount(spanCount);
            }
        }
    }
}