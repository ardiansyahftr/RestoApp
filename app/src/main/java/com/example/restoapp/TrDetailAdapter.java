package com.example.restoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TrDetailAdapter extends RecyclerView.Adapter<TrDetailAdapter.ViewHolder> {
    Context context;
    List<TrDetailModel> listDetail;
    AlertDialog a;
    public TrDetailAdapter(Context context, List<TrDetailModel> listDetail) {
        this.context = context;
        this.listDetail = listDetail;
    }
    @NonNull
    @Override
    public TrDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_list_cart, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listDetail.get(position),context, position);
    }

    @Override
    public int getItemCount() {
        return listDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameCart, txtAmountCart, txtPriceCart, txtDiscPriceCart, txtDiscCart, txtSubCart;
        Button btAddCart, btMinCart, btCloseCart;
        EditText edDescCart;
        ImageView ivPhotoCart;
        LinearLayout lyDiscCart;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameCart = itemView.findViewById(R.id.txtNameCart);
            txtPriceCart = itemView.findViewById(R.id.txtPriceCart);
            txtDiscCart = itemView.findViewById(R.id.txtDiscCart);
            txtDiscPriceCart = itemView.findViewById(R.id.txtDiscPriceCart);
            txtAmountCart = itemView.findViewById(R.id.txtAmountCart);
            btAddCart = itemView.findViewById(R.id.btAddCart);
            btMinCart = itemView.findViewById(R.id.btMinCart);
            btCloseCart = itemView.findViewById(R.id.btCloseCart);
            edDescCart = itemView.findViewById(R.id.edDescCart);
            lyDiscCart = itemView.findViewById(R.id.lyDiscCart);
            txtSubCart = itemView.findViewById(R.id.txtSubCart);
        }

        public void bind(final TrDetailModel trDetailModel, final Context context, final int pos) {
            int disco;
            final int price;
            final int[] sub_price = new int[1];

            final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            txtNameCart.setText(trDetailModel.getName());
            txtPriceCart.setText(String.valueOf(formatRupiah.format(trDetailModel.getPrice())));
            txtAmountCart.setText(String.valueOf(trDetailModel.getAmount()));

            if (trDetailModel.getDiscount() > 0 && trDetailModel.getDiscount() <= 100){
                lyDiscCart.setVisibility(View.VISIBLE);
                txtDiscCart.setText("Disc. "+String.valueOf(trDetailModel.getDiscount())+"%");
                disco = trDetailModel.getPrice() - (trDetailModel.getPrice() * trDetailModel.getDiscount()/100);
                txtDiscPriceCart.setText(String.valueOf(formatRupiah.format(disco)));
                price = disco;
            } else {
                lyDiscCart.setVisibility(View.GONE);
                price = trDetailModel.getPrice();
            }
            sub_price[0] = price * trDetailModel.getAmount();
            txtSubCart.setText(String.valueOf(formatRupiah.format(sub_price[0])));
            Toast.makeText(context,String.valueOf(sub_price[0]) , Toast.LENGTH_SHORT).show();
            btAddCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    trDetailModel.setAmount(trDetailModel.getAmount()+1);
                    txtAmountCart.setText(String.valueOf(trDetailModel.getAmount()));
                    sub_price[0] = price * trDetailModel.getAmount();
                    txtSubCart.setText(String.valueOf(formatRupiah.format(sub_price[0])));
                    MainActivity.hitungTotal();
                }
            });
            btMinCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (trDetailModel.getAmount() == 1){
                        final AlertDialog.Builder alertPut = new AlertDialog.Builder(context);
                        alertPut.setMessage("Hapus Dari Keranjang?");
                        alertPut.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listDetail.remove(pos);
                                MainActivity.rvListCart.getAdapter().notifyDataSetChanged();
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
                    } else {
                        trDetailModel.setAmount(trDetailModel.getAmount()-1);
                        txtAmountCart.setText(String.valueOf(trDetailModel.getAmount()));
                        sub_price[0] = price * trDetailModel.getAmount();
                        txtSubCart.setText(String.valueOf(formatRupiah.format(sub_price[0])));
                        MainActivity.hitungTotal();
                    }
                }
            });
            edDescCart.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    trDetailModel.setDescription(edDescCart.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            btCloseCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertPut = new AlertDialog.Builder(context);
                    alertPut.setMessage("Masukkan Keranjang?");
                    alertPut.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            listDetail.remove(pos);
                            MainActivity.rvListCart.getAdapter().notifyDataSetChanged();
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
