package com.jeuxdevelopers.wakreadmin.adapters;

import android.annotation.SuppressLint;
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
import com.jeuxdevelopers.wakreadmin.databinding.ItemUserBinding;
import com.jeuxdevelopers.wakreadmin.models.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> implements Filterable {
    private Context context;
    private List<UserModel> data;
    private List<UserModel> filteredData;
    private String filterQuery;
    private UserSelectListener listener;

    public UsersListAdapter(Context context, List<UserModel> data, UserSelectListener listener) {
        this.context = context;
        this.data = data;
        filteredData = data;
        this.listener = listener;
        filterQuery = "";
    }

    public void setData(List<UserModel> data) {
        filteredData = data;
        filterQuery = "";
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("DefaultLocale")
        void bind(UserModel model) {
            Glide.with(context).load(model.getProfileImageUrl()).into(binding.cvProfile);
            binding.tvName.setText(model.getName());
            binding.chipAmount.setText(String.format("%.2f$", model.getAmount()));
            if (filterQuery.isEmpty())
                binding.tvPhone.setText(model.getPhone());
            else setHighlightedName(model.getPhone());
            binding.linBack.setOnClickListener(v -> listener.onUserClick(model));
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

    public interface UserSelectListener {
        void onUserClick(UserModel model);
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
