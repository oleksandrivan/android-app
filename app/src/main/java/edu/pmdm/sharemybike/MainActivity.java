package edu.pmdm.sharemybike;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity  {
    GoogleSignInClient signInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        SignInButton signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(v -> signIn());
    }

    private void signIn(){
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInAccount(task);
        }
    }

    private void handleSignInAccount(Task<GoogleSignInAccount> task) {
        GoogleSignInAccount account = task.getResult();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.name), account.getDisplayName());
        editor.putString(getString(R.string.email), account.getEmail());
        editor.apply();
        Intent i = new Intent(getApplicationContext(), BikeActivity.class);
        i.putExtra(getString(R.string.name),account.getDisplayName());
        i.putExtra(getString(R.string.email),account.getEmail());
        startActivity(i);
    }
}