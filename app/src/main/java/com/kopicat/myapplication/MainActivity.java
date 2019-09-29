package com.kopicat.myapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kopicat.myapplication.entity.Product;
import com.kopicat.myapplication.entity.ProductViewModel;
import com.kopicat.myapplication.ui.edit.EditItemDialog;
import com.kopicat.myapplication.ui.edit.EditItemsFragment;
import com.kopicat.myapplication.ui.table.TableItemsFragment;

public class MainActivity extends AppCompatActivity implements EditItemsFragment.OnListFragmentInteractionListener, TableItemsFragment.OnFragmentInteractionListener {

    private ProductViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        viewModel.initList();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_edit, R.id.navigation_result)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof EditItemsFragment){
            EditItemsFragment editItemsFragment = (EditItemsFragment)fragment;
            editItemsFragment.setListFragmentInteractionListener(this);
            Log.i("MAIN ACTIVITY","Load fragment to listener");
        }

    }

    @Override
    public void onListFragmentInteraction(Product item) {
        viewModel.setSelectedProduct(item);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("edit_product_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditItemDialog newFragment = new EditItemDialog();
        newFragment.show(ft, "edit_product_dialog");



    }

    @Override
    public void onFragmentInteraction() {

    }
}
