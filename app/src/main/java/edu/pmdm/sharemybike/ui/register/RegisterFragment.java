package edu.pmdm.sharemybike.ui.register;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.CharSequenceTransformation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import edu.pmdm.sharemybike.Bike;
import edu.pmdm.sharemybike.MyItemRecyclerViewAdapter;
import edu.pmdm.sharemybike.R;
import edu.pmdm.sharemybike.User;
import edu.pmdm.sharemybike.databinding.FragmentRegisterBinding;
import edu.pmdm.sharemybike.ui.calendar.CalendarFragment;

public class RegisterFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "RegisterFragment";
    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;
    private FusedLocationProviderClient fusedLocationClient;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        checkPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        View root = binding.getRoot();

        binding.btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePhoto();
            }
        });

        binding.btnAddMyBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

        return root;
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    binding.txtLatitude.setText(String.valueOf(location.getLatitude()));
                    binding.txtLongitude.setText(String.valueOf(location.getLongitude()));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.toString());
            }
        });
    }

    private void handleSubmit() {
        Bitmap photo = ((BitmapDrawable)binding.imgSofa.getDrawable()).getBitmap();
        String location = binding.txtLocation.getText().toString();
        String city = binding.txtCity.getText().toString();
        String description = binding.txtDescription.getText().toString();
        String owner = User.getInstance().getName();
        String email = User.getInstance().getEmail();
        Bike newBike = new Bike(photo, owner, description, city, location, email);
        newBike.setLatitude(Double.valueOf(binding.txtLatitude.getText().toString()));
        newBike.setLongitude(Double.valueOf(binding.txtLongitude.getText().toString()));
        MyItemRecyclerViewAdapter.BIKES.add(newBike);
        clearFields();
        NavHostFragment.findNavController(RegisterFragment.this)
                .navigate(R.id.action_nav_register_to_nav_bike);
    }

    private void clearFields() {
        binding.txtLocation.setText("");
        binding.txtCity.setText("");
        binding.txtDescription.setText("");
        binding.imgSofa.setImageBitmap(BitmapFactory
                .decodeResource(getContext().getResources(), R.drawable.share_my_bike_logo));
    }

    private void handlePhoto() {
        dispatchTakePictureIntent();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            binding.imgSofa.setImageBitmap(imageBitmap);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "Can't take the picture");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}