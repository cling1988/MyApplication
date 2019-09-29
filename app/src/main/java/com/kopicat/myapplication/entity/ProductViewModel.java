package com.kopicat.myapplication.entity;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kopicat.myapplication.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductViewModel extends ViewModel {


    private MutableLiveData<String> branchName;

    private MutableLiveData<List<Product>> productList;

    private MutableLiveData<Product> selectedProduct;

    public LiveData<List<Product>> getProductList() {
        return productList;
    }

    public void initList(){
        if (null == productList) {
            Log.i("Init", "Init list.....");
            productList = new MutableLiveData<>();
            productList.setValue(initProductList());
        }
        if(null==branchName){
            updateBranchName("Default branch");
        }
    }

    public LiveData<String> getBranchName() {
        return branchName;
    }

    public void updateBranchName(String name){
        branchName = new MutableLiveData<>();
        branchName.setValue(name);
    }

    public void updateProductList(Product p) {
        List<Product> tmpList = productList.getValue();
        tmpList.set(p.id-1,p);
        productList.setValue(tmpList);
    }

    public LiveData<Product> getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product p) {
        selectedProduct = new MutableLiveData<>();
        selectedProduct.setValue(p);
    }

    private List<Product> initProductList() {
        List<Product> tempList = new ArrayList<>();
        tempList.add(new Product(1, "龟苓膏 大", new Double(0), new Double(0)));
        tempList.add(new Product(2, "龟苓膏 小",new Double(0),new Double(0)));
        tempList.add(new Product(3, "王老吉",new Double(0),new Double(0)));
        tempList.add(new Product(4, "五花茶",new Double(0),new Double(0)));
        tempList.add(new Product(5, "蜜糖",new Double(0),new Double(0)));
        tempList.add(new Product(6, "送口",new Double(0),new Double(0)));
        return tempList;

    }


}
