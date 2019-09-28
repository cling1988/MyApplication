package com.kopicat.myapplication;

import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private ProductViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof ItemFragment){
            ItemFragment itemFragment = (ItemFragment)fragment;
            itemFragment.setListFragmentInteractionListener(this);
            Log.i("MAIN ACTIVITY","Load fragment to listener");
        }

    }

    @Override
    public void onListFragmentInteraction(Product item) {
        viewModel.setProduct(item);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("edit_product_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        EditProductDialog newFragment = new EditProductDialog();
        newFragment.show(ft, "edit_product_dialog");



    }

    public void doPositiveClick(double open,double balance) {
        Log.i("PrintOBJ",viewModel.getProduct().getValue().toString());
        viewModel.getProduct().getValue().setOpening(open);
        viewModel.getProduct().getValue().setBalance(balance);
        Log.i("FragmentAlertDialog", viewModel.getProduct().getValue().toString());
//        viewModel.getProductList().
//        viewModel.getProductList().getValue().set(viewModel.getProduct().getValue().id-1,viewModel.getProduct().getValue());
        viewModel.updateProductList(viewModel.getProduct().getValue());
        Log.i("FragmentAlertDialog", "Positive click!"+open);
    }

}
