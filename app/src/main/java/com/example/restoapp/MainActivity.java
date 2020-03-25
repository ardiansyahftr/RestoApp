package com.example.restoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public String url_static = "http://192.168.1.65/api_ranit_pos/server.php?operasi=";
    RecyclerView rvListNavCat;
    RecyclerView rvListProd;
    static RecyclerView rvListCart;
    BottomNavigationView bnCat;
    String id_cat;
    List<CategoryModel> arrCat = new ArrayList<>();
    List<ProductModel> arrProd = new ArrayList<>();
    static List<TrDetailModel> arrCart = new ArrayList<>();
    static Button btSubmit;
    String datNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date dtnow = Calendar.getInstance().getTime();
        final SimpleDateFormat dfBiasa = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        datNow = dfBiasa.format(dtnow);

        btSubmit = findViewById(R.id.btSubmit);
        id_cat = "all";
        bnCat = findViewById(R.id.bnCat);
        getBnCat();
        bnCat.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                arrProd.clear();
                AndroidNetworking.get(url_static+"list_product&id_cat=" + menuItem.getTitle())
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(url_static+"list_product&id_cat=" + id_cat, "onResponse1 ");
                                if (response.length() <= 0){
                                    Toast.makeText(MainActivity.this, "Buku tidak ada", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Log.d(String.valueOf(response.length()), "onResponse:JSON");
                                for (int lp = 0; lp < response.length(); lp++){
                                    try {
                                        JSONObject objProd = response.getJSONObject(lp);
                                        ProductModel arrModel = new ProductModel(objProd.getString("name"),objProd.getString("photo"),objProd.getString("created_by"),objProd.getString("created_at"),objProd.getString("changed_by"),objProd.getString("changed_at"),objProd.getString("is_delete"),objProd.getInt("id"),objProd.getInt("id_category"),objProd.getInt("price"),objProd.getInt("discount"));
                                        arrProd.add(arrModel);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                rvListProd.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
                return true;
            }
        });
        rvListNavCat = findViewById(R.id.rvListNavCat);
        NavCatAdapter adapterlp = new NavCatAdapter(this, arrCat);
        rvListNavCat.setAdapter(adapterlp);
        rvListNavCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        arrCat.clear();
        getNavCat();

        rvListProd = findViewById(R.id.rvListProd);
        ProductAdapter adapterProd = new ProductAdapter(this, arrProd);
        rvListProd.setAdapter(adapterProd);
        rvListProd.setLayoutManager(new GridLayoutManager(this, 4));
        arrProd.clear();
        getProd();

        arrCart.clear();
        rvListCart = findViewById(R.id.rvListCart);
        TrDetailAdapter adapterCart = new TrDetailAdapter(this, arrCart);
        rvListCart.setAdapter(adapterCart);
        rvListCart.setLayoutManager(new LinearLayoutManager(this));
        arrCart.clear();
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrCart.size() < 1) {
                    Toast.makeText(MainActivity.this, "Maaf Keranjang Pesanan Kosong", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int[] id_order = new int[1];
                AndroidNetworking.get(url_static+"top_id_order")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    id_order[0] = response.getInt("id")+1;
                                    Toast.makeText(MainActivity.this, String.valueOf(response.getInt("id")+1) , Toast.LENGTH_SHORT).show();

                                    AndroidNetworking.post(url_static+"insert_order")
                                            .addBodyParameter("id", String.valueOf(id_order[0]))
                                            .addBodyParameter("no_order","P0001")
                                            .addBodyParameter("id_waiters","1")
                                            .addBodyParameter("id_table","1")
                                            .addBodyParameter("id_member","1")
                                            .addBodyParameter("id_kasir","1")
                                            .addBodyParameter("note","-")
                                            .addBodyParameter("customer_name","Jono")
                                            .addBodyParameter("created_by","admin")
                                            .addBodyParameter("created_at", datNow)
                                            .addBodyParameter("changed_by","admin")
                                            .addBodyParameter("changed_at",datNow)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        if (response.getInt("value") == 1){
                                                            int total = 0;
                                                            for (int i = 0; i < arrCart.size(); i++) {
                                                                if (arrCart.get(i).getDiscount() > 0 && arrCart.get(i).getDiscount() <= 100){
                                                                    total += (arrCart.get(i).getPrice() - (arrCart.get(i).getPrice() * arrCart.get(i).getDiscount() / 100)) * arrCart.get(i).getAmount();
                                                                    Log.d(arrCart.get(i).getName()+total , "hitungTotal: ");
                                                                } else {
                                                                    total += arrCart.get(i).getPrice() * arrCart.get(i).getAmount();
                                                                }
                                                            }
                                                            AndroidNetworking.post(url_static+"insert_trans")
                                                                    .addBodyParameter("id","")
                                                                    .addBodyParameter("id_order", String.valueOf(id_order[0]))
                                                                    .addBodyParameter("total_price", String.valueOf(total))
                                                                    .addBodyParameter("total_pay","100000")
                                                                    .addBodyParameter("total_change","10000")
                                                                    .addBodyParameter("tax","0")
                                                                    .addBodyParameter("discount","0")
                                                                    .addBodyParameter("service","0")
                                                                    .addBodyParameter("type_of_payment","0")
                                                                    .addBodyParameter("created_by","admin")
                                                                    .addBodyParameter("created_at",datNow)
                                                                    .addBodyParameter("changed_by","admin")
                                                                    .addBodyParameter("changed_at",datNow)
                                                                    .build()
                                                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            try {
//                                                            Toast.makeText(getApplicationContext(),response.getString("message") , Toast.LENGTH_SHORT).show();
                                                                                Log.d(response.getString("message"), "onResponse: GAGALJARE");
                                                                                if (response.getInt("value") == 1){
//                                                                Toast.makeText(MainActivity.this, String.valueOf(id_order[0]), Toast.LENGTH_SHORT).show();
                                                                                    for (int j = 0; j < arrCart.size(); j++) {
                                                                                        AndroidNetworking.post(url_static+"insert_detail")
                                                                                                .addBodyParameter("id","")
                                                                                                .addBodyParameter("id_order", String.valueOf(id_order[0]))
                                                                                                .addBodyParameter("id_product", String.valueOf(arrCart.get(j).getId_product()))
                                                                                                .addBodyParameter("amount",String.valueOf(arrCart.get(j).getAmount()))
                                                                                                .addBodyParameter("description",String.valueOf(arrCart.get(j).getDescription()))
                                                                                                .addBodyParameter("created_by","admin")
                                                                                                .addBodyParameter("created_at",datNow)
                                                                                                .addBodyParameter("changed_by","admin")
                                                                                                .addBodyParameter("changed_at",datNow)
                                                                                                .build()
                                                                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                                                                    @Override
                                                                                                    public void onResponse(JSONObject response) {
                                                                                                        try {
                                                                                                            Toast.makeText(getApplicationContext(),response.getString("message") , Toast.LENGTH_SHORT).show();
                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onError(ANError anError) {
                                                                                                        Log.d("InsertError", "onError: Failed" + anError); //untuk log pada onerror
                                                                                                        Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onError(ANError anError) {

                                                                        }
                                                                    });
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onError(ANError anError) {
                                                    Log.d("InsertError", "onError: Failed" + anError); //untuk log pada onerror
                                                    Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("InsertError", "onError: Failed" + anError); //untuk log pada onerror
                                Toast.makeText(getApplicationContext(),"Data gagal ditambahkan", Toast.LENGTH_SHORT).show();
                            }
                        });

//                Toast.makeText(getApplicationContext(),String.valueOf(id_order[0])  , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getBnCat() {
        bnCat.getMenu().clear();
        bnCat.getMenu().add(Menu.NONE, 1, Menu.NONE, "ALL").setIcon(R.drawable.ic_remove_red_eye_black_24dp);
//        bnCat.getMenu().add(Menu.NONE, 1, Menu.NONE, "ALL").setIcon(Drawable.createFromPath("android.resource://com.example.restoapp/res/drawable/ic_food.xml"));
//        bnCat.getMenu().add(Menu.NONE, 1, Menu.NONE, "ALL").setIcon(Drawable.createFromPath("res/drawable/ic_food.xml"));
        AndroidNetworking.get(url_static+"list_category")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() <= 0){
                            Toast.makeText(MainActivity.this, "Produk tidak ada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d(String.valueOf(response.length()), "onResponsez");
                        for (int lp = 0; lp < response.length(); lp++){
                            try {
                                JSONObject objProd = response.getJSONObject(lp);
//                                Toast.makeText(MainActivity.this, String.valueOf(Drawable.createFromPath(objProd.getString("icon")) , Toast.LENGTH_SHORT).show();
                                bnCat.getMenu().add(Menu.NONE, 1, Menu.NONE, objProd.getString("name")).setIcon(Drawable.createFromPath(objProd.getString("icon")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getProd() {
        arrProd.clear();
        AndroidNetworking.get(url_static+"list_product&id_cat=" + id_cat)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() <= 0){
                            Toast.makeText(MainActivity.this, "Tidak ada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d(String.valueOf(response.length()), "onResponse:JSON");
                        for (int lp = 0; lp < response.length(); lp++){
                            try {
                                JSONObject objProd = response.getJSONObject(lp);
                                ProductModel arrModel = new ProductModel(objProd.getString("name"),objProd.getString("photo"),objProd.getString("created_by"),objProd.getString("created_at"),objProd.getString("changed_by"),objProd.getString("changed_at"),objProd.getString("is_delete"),objProd.getInt("id"),objProd.getInt("id_category"),objProd.getInt("price"),objProd.getInt("discount"));
                                arrProd.add(arrModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        rvListProd.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String id = (String) item.getTitle();
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        return true;
    }

    void getNavCat() {
        arrCat.clear();
        AndroidNetworking.get(url_static+"list_category")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() <= 0){
                            Toast.makeText(MainActivity.this, "Produk tidak ada", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d(String.valueOf(response.length()), "onResponse:JSON");
                        for (int lp = 0; lp < response.length(); lp++){
                            try {
                                JSONObject objProd = response.getJSONObject(lp);
                                CategoryModel arrModel = new CategoryModel(objProd.getString("name"),(objProd.getInt("id")));
                                arrCat.add(arrModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        rvListNavCat.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    public static void hitungTotal(){
        int total = 0;
        for (int i = 0; i < arrCart.size(); i++) {
            if (arrCart.get(i).getDiscount() > 0 && arrCart.get(i).getDiscount() <= 100){
                total += (arrCart.get(i).getPrice() - (arrCart.get(i).getPrice() * arrCart.get(i).getDiscount() / 100)) * arrCart.get(i).getAmount();
                Log.d(arrCart.get(i).getName()+total , "hitungTotal: ");
            } else {
                total += arrCart.get(i).getPrice() * arrCart.get(i).getAmount();
            }
        }
//        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        btSubmit.setText(String.valueOf(formatRupiah.format(total) +" - Pesan"));
    }

}
