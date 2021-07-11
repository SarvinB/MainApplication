package com.example.mainapplication.pages.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mainapplication.R;
import com.example.mainapplication.adapter.ProductListAdaptor;
import com.example.mainapplication.data.repository.Repository;
import com.example.mainapplication.objects.CommodityObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment {
    RecyclerView rv;

    public List<CommodityObject> makeFakeProducts() {
        List<CommodityObject> list = new ArrayList<>();

            CommodityObject obj1 = new CommodityObject();
            obj1.setPrice(20000);
            obj1.setName("niloofar");
            obj1.setImage(String.valueOf(R.drawable.pngtreefull_color_mandala_element_5306633__1_));
            list.add(obj1);
            list.add(obj1);
            list.add(obj1);
            list.add(obj1);

        return list;
    }

    public ProductListFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);

        rv = v.findViewById(R.id.productList);
        rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        ProductListAdaptor mAdapter = new ProductListAdaptor();
        mAdapter.setmProducts(makeFakeProducts(),getContext());

        rv.setAdapter(mAdapter);
        return v;
    }
}