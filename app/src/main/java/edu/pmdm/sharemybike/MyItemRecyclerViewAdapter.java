package edu.pmdm.sharemybike;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.pmdm.sharemybike.databinding.FragmentItemBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Bike}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyItemRecyclerViewAdapter";

    public static List<Bike> BIKES = new ArrayList<>();
    private Context context;
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;

    public MyItemRecyclerViewAdapter(Context context) {
        this.context = context;
        loadBikesList();
    }

    private void loadBikesList() {
        if(BIKES.isEmpty()) {
            mDatabase = FirebaseDatabase
                    .getInstance("https://sharemybike-4333d-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference();

            mDatabase.child("bikes_list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Bike bike = productSnapshot.getValue(Bike.class);
                        downloadPhoto(bike);
                        BIKES.add(bike);
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void downloadPhoto(Bike c) {

        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(c.getImage());
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            final File localFile = File.createTempFile("PNG_" + timeStamp, ".png");
            mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Insert the downloaded image in its right position at the ArrayList

                    String url = "gs://" + taskSnapshot.getStorage().getBucket() + "/images/" + taskSnapshot.getStorage().getName();

                    Log.d(TAG, "Loaded " + url);
                    for (Bike c : BIKES) {
                        if (c.getImage().equals(url)) {
                            c.setPhoto(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            notifyDataSetChanged();
                            Log.d(TAG, "Loaded pic " + c.getImage() + ";" + url + localFile.getAbsolutePath());
                        }
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Bike mItem = BIKES.get(position);
        holder.mItem = mItem;
        holder.mPhoto.setImageBitmap(mItem.getPhoto());
        holder.mCity.setText(mItem.getCity());
        holder.mLocation.setText(mItem.getLocation());
        holder.mOwner.setText(mItem.getOwner());
        holder.mDescription.setText(mItem.getDescription());
        holder.mBtnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked for booking a bike");
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                String date = sharedPref.getString("date", "");
                UserBooking userBooking = new UserBooking(User.getInstance().getUid(),
                        User.getInstance().getEmail(), mItem.getEmail(), mItem.getCity(), date);
                userBooking.addToDatabase();
            }
        });
    }

    @Override
    public int getItemCount() {
        return BIKES.size();
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