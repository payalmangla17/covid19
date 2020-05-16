package com.example.covid19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button signout_btn;
    BottomNavigationView navView;
    static DatabaseReference reff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int var = getIntent().getIntExtra("choice", 1);

        final String mmobile = getIntent().getStringExtra("mmobile");

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new About()).commit();

        navView=findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.about:
                        // Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
                        selectedFragment = new About();
                        break;
                    case R.id.Emergency_contacts:
                        // Toast.makeText(MainActivity.this, "Emergency contacts", Toast.LENGTH_SHORT).show();
                        selectedFragment = new EmergencyDials();
                        break;
                    case R.id.help:
                        //  Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
                        //     startActivity(new Intent(MainActivity.this,AutocompleteFragment.class));
                        selectedFragment=new HelpDesk();
                        break;
                    case R.id.Corona:
                        //  Toast.makeText(MainActivity.this, "updates", Toast.LENGTH_SHORT).show();
                        selectedFragment = new CoronaUpdates();
                        break;
                    case R.id.Notifications:
                        // Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
                        selectedFragment = new Notifications();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedFragment).commit();
                return true;
            }
        });
        signout_btn = findViewById(R.id.signOut_btn);
     /*   signout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();

                finish();
                Intent intent = new Intent(MainActivity.this, login.class);

                startActivity(intent);
            }
        });*/

        Log.e("TAG", "betA");


        if (var != 3) {
            if (var == 2) {
                reff = FirebaseDatabase.getInstance().getReference().child("Aid_Helper").child(mmobile);
            } else {
                reff = FirebaseDatabase.getInstance().getReference().child("Nomodular").child(mmobile);
            }

            reff.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(MainActivity.this, "Welcome !!", Toast.LENGTH_SHORT).show();
                        Log.e("bro", "code");


                        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                        editor.putString("mobile_num", mmobile);
                        editor.putInt("choice", var);
                        editor.apply();

                        //      String temp_name = dataSnapshot.child("full_name").getValue().toString();
                        //     String temp_college = dataSnapshot.child("college").getValue().toString();
                        //    String temp_email = dataSnapshot.child("email").getValue().toString();
                        //   String temp_mobile = dataSnapshot.child("mobile_no").getValue().toString();

                        // show_name.setText(temp_name);
                        //  show_college.setText(temp_college);
                        //  show_email.setText(temp_email);
                        //  show_mobile.setText(temp_mobile);
                    }
                    else {
                        Log.e("bro", "dude");
                        finish();
                        Toast.makeText(MainActivity.this, "You should first sign up and then come", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();

                        Intent goto_signup = new Intent(MainActivity.this, SignUp.class);
                        startActivity(goto_signup);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }
        else
        {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            mobile_num = prefs.getString("mobile_num", null);
            variable = prefs.getInt("choice", -1);

            if (variable != -1 && mobile_num !=null )
            {
                if(variable==2)
                    reff = FirebaseDatabase.getInstance().getReference().child("Aid_Helper").child(mobile_num);

                else
                    reff = FirebaseDatabase.getInstance().getReference().child("Nomodular").child(mobile_num);


            }
        }
    }
    String mobile_num;
    int variable;
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.signOut_btn)
        {
            Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

            finish();
            Intent intent = new Intent(MainActivity.this, login.class);

            startActivity(intent);
            return true;
        }
        else
            return false;

    }
}
