package com.jeuxdevelopers.wakreadmin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeuxdevelopers.wakreadmin.databinding.ItemTaxTransactionBinding;
import com.jeuxdevelopers.wakreadmin.models.TransactionModel;
import com.jeuxdevelopers.wakreadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<TransactionsListAdapter.TransactionViewHolder> implements Filterable {
    private List<TransactionModel> data;
    private Context context;
    private TransactionClickListener clickListener;
    private List<TransactionModel> filteredData;
    private String filterQuery;

    public TransactionsListAdapter(List<TransactionModel> data, Context context, TransactionClickListener clickListener) {
        this.data = data;
        this.context = context;
        this.clickListener = clickListener;
        this.filteredData = data;
        filterQuery = "";
    }

    public void setData(List<TransactionModel> data) {
        this.data = data;
        this.filteredData = data;
        filterQuery = "";
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(ItemTaxTransactionBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(filteredData.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    class TransactionViewHolder extends RecyclerView.ViewHolder {
        private ItemTaxTransactionBinding binding;

        public TransactionViewHolder(@NonNull ItemTaxTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
        void bind(TransactionModel model) {
            binding.tvId.setText(model.getTransactionId());

            binding.chipTax.setText(model.getTax() + "$");

            binding.tvDate.setText(Utils.getDateFromMillies(model.getDate()));

            binding.getRoot().setOnClickListener(v -> clickListener.onTransactionClick(model));

        }


    }

    public interface TransactionClickListener {
        void onTransactionClick(TransactionModel model);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                if (query.isEmpty()) {
                    filteredData = data;
                } else {
                    List<TransactionModel> filteredList = new ArrayList<>();
                    for (TransactionModel model : data) {
                        if (model.getSender().getPhone().contains(query)) {
                            filteredList.add(model);
                        }
                    }
                    filteredData = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<TransactionModel>) results.values;
                filterQuery = constraint.toString();
                notifyDataSetChanged();
            }
        };
    }
}
