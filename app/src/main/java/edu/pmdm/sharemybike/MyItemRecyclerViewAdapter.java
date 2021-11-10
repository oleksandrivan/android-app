package edu.pmdm.sharemybike;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.pmdm.sharemybike.bikes.BikesContent.Bike;
import edu.pmdm.sharemybike.databinding.FragmentItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Bike}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Bike> mValues;

    public MyItemRecyclerViewAdapter(List<Bike> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Bike mItem = mValues.get(position);
        holder.mItem = mItem;
        holder.mPhoto.setImageBitmap(mItem.getPhoto());
        holder.mCity.setText(mItem.getCity());
        holder.mLocation.setText(mItem.getLocation());
        holder.mOwner.setText(mItem.getOwner());
        holder.mDescription.setText(mItem.getDescription());
        holder.mBtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add mail action
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Bike mItem;

        public final ImageView mPhoto;
        public final TextView mCity;
        public final TextView mLocation;
        public final TextView mOwner;
        public final TextView mDescription;
        public final ImageButton mBtnMail;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mPhoto = binding.imgPhoto;
            mCity = binding.txtCity;
            mLocation = binding.txtLocation;
            mOwner = binding.txtOwner;
            mDescription = binding.txtDescription;
            mBtnMail = binding.btnMail;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}