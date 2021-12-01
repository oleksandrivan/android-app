package edu.pmdm.sharemybike;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.pmdm.sharemybike.bikes.BikesContent;
import edu.pmdm.sharemybike.databinding.ActivityBikeBinding;

public class BikeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityBikeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        BikesContent.loadBikesFromJSON(getApplicationContext());
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Intent inputIntent = getIntent();
        String name = getString(R.string.name);
        String email = getString(R.string.email);
        sharedPref
                .edit()
                .putString(name, inputIntent.getStringExtra(name))
                .putString(email, inputIntent.getStringExtra(email))
                .apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_bike);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void sendEmail(String ownerMail, String ownerName, String location, String city) {
        Intent intent = new Intent();
        Intent.createChooser(intent, "Choose the app to send the email with your order");
        intent.setAction(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ownerMail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Couch App: I'd like to book your bike");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String date = sharedPref.getString(getString(R.string.chosen_date), "");
        String emailText = String.format(getString(R.string.email_body), ownerName, location, city,
                date);
        intent.putExtra(Intent.EXTRA_TEXT, emailText);
        intent.setType("message/rfc822");
        startActivity(intent);
    }
}