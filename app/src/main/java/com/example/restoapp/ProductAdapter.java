package com.example.restoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<ProductModel> listProd;

    public ProductAdapter(Context context, List<ProductModel> listProd) {
        this.context = context;
        this.listProd = listProd;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_list_product, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.bind(listProd.get(position),context);
    }

    @Override
    public int getItemCount() {
        return listProd.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        LinearLayout lyProd;
        AlertDialog a;
        List<TrDetailModel> listCart;
        RecyclerView rvListCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            lyProd = itemView.findViewById(R.id.lyProd);
//            rvListCart = MainActivity.rvListCart;
            listCart = MainActivity.arrCart;
        }

        public void bind(final ProductModel productModel, final Context context) {
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            txtName.setText(productModel.getName());
            Log.d(String.valueOf(productModel.getPrice()), "bind: ");
            txtPrice.setText(String.valueOf(formatRupiah.format(productModel.getPrice())));
            lyProd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertPut = new AlertDialog.Builder(context);
                    alertPut.setMessage("Masukkan Keranjang?");
                    alertPut.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Boolean cek = true;
                            for (int i = 0; i < listCart.size(); i++){
                                if (productModel.getId() == listCart.get(i).getId_product()){
                                    cek = false;
                                    listCart.get(i).setAmount(listCart.get(i).getAmount()+1);
                                    MainActivity.rvListCart.getAdapter().notifyDataSetChanged();
                                    Toast.makeText(context, "Produk sudah ada di keranjang" , Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (cek){
                                TrDetailModel trModel = new TrDetailModel(productModel.getPhoto(),productModel.getName(),"","","","","",0,0,productModel.getId(),1,productModel.getPrice(), productModel.getDiscount());
                                listCart.add(trModel);
                                MainActivity.rvListCart.getAdapter().notifyDataSetChanged();
                            }
                            MainActivity.hitungTotal();
                        }
                    });
                    alertPut.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            a.dismiss();
                        }
                    });
                    a = alertPut.create();
                    a.show();
                }
            });
        }
    }
}
