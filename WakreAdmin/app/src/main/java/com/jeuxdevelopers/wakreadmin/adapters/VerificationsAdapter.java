package com.jeuxdevelopers.wakreadmin.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jeuxdevelopers.wakreadmin.R;
import com.jeuxdevelopers.wakreadmin.databinding.ItemAccountVerificationBinding;
import com.jeuxdevelopers.wakreadmin.enums.AccountState;
import com.jeuxdevelopers.wakreadmin.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificationsAdapter extends RecyclerView.Adapter<VerificationsAdapter.VerificationViewHolder> implements Filterable {
    private Context context;
    private List<UserModel> data;
    private List<UserModel> filteredData;
    private VerificationListener listener;
    private String filterQuery;


    public VerificationsAdapter(Context context, List<UserModel> data, VerificationListener listener) {
        this.context = context;
        this.data = data;
        filteredData = data;
        this.listener = listener;
        filterQuery = "";
    }

    public void setData(List<UserModel> data) {
        this.data = data;
        this.filteredData = data;
        filterQuery = "";
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VerificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerificationViewHolder(ItemAccountVerificationBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerificationViewHolder holder, int position) {
        holder.bind(filteredData.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }


    class VerificationViewHolder extends RecyclerView.ViewHolder {
        ItemAccountVerificationBinding binding;

        public VerificationViewHolder(@NonNull ItemAccountVerificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UserModel model) {
            Glide.with(context).load(model.getProfileImageUrl()).into(binding.cvProfile);
            binding.tvShopName.setText(model.getShopName());
            binding.tvUserName.setText(model.getName());
            binding.tvAddress.setText(model.getAddress());
            if (filterQuery.isEmpty()) {
                binding.tvPhone.setText(model.getPhone());
            } else {
                setHighlightedName(model.getPhone());
            }


            binding.btnApprove.setOnClickListener(v -> {
                listener.onVerificationChange(AccountState.Active);
            });

            binding.btnReject.setOnClickListener(v -> {
                listener.onVerificationChange(AccountState.Declined);
            });
        }

        private void setHighlightedName(String description) {
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(description.toLowerCase());

            Matcher matcher = Pattern.compile(filterQuery.toLowerCase())
                    .matcher(description.toLowerCase());
            while (matcher.find()) {
                spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorBlue)), matcher.start(),
                        matcher.start() + filterQuery.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            binding.tvPhone.setText(spanText);
        }
    }

    public interface VerificationListener {
        void onVerificationChange(AccountState state);
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
                    List<UserModel> filteredList = new ArrayList<>();
                    for (UserModel model : data) {
                        if (model.getPhone().contains(query)) {
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
                filteredData = (List<UserModel>) results.values;
                filterQuery = constraint.toString();
                notifyDataSetChanged();
            }
        };
    }
}
