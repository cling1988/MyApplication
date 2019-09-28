package com.kopicat.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kopicat.myapplication.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<List<Product>> productList;


    private MutableLiveData<Product> product;

    public MutableLiveData<List<Product>> getProductList() {
        if(null==productList){
            Log.i("Init","Init list.....");
            productList = new MutableLiveData<>();
            productList.setValue(initProductList());
        }
        return productList;
    }

    public void updateProductList(Product p){
        productList.getValue().set(p.id-1,p);
    }


    public LiveData<Product> getProduct() {
        return product;
    }

    public void setProduct(Product p){
        product = new MutableLiveData<>();
        product.setValue(p);
    }

    private List<Product> initProductList(){
        List<Product> tempList = new ArrayList<>();
        tempList.add(new Product(1, "Item 1", new Double(1), new Double(7)));
        tempList.add(new Product(2, "Item 2",new Double(2),new Double(8)));
        tempList.add(new Product(3, "Item 3",new Double(3),new Double(9)));
        tempList.add(new Product(4, "Item 4",new Double(4),new Double(10)));
        tempList.add(new Product(5, "Item 5",new Double(5),new Double(11)));
        tempList.add(new Product(6, "Item 6",new Double(6),new Double(12)));
        return tempList;

    }



}
