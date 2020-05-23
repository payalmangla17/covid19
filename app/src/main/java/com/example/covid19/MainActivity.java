package com.example.covid19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button signout_btn;
    BottomNavigationView navView;
    NavigationView sideNavigationView;
    static DatabaseReference reff;
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int var = getIntent().getIntExtra("choice", 1);

        final String mmobile = getIntent().getStringExtra("mmobile");
        final String ft=getIntent().getStringExtra("firsttime");
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new About()).commit();
     /*   if(reff.child("user_profile")==null){
            Toast.makeText(MainActivity.this,"Please update profile first.",Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new user_profile()).commit();
           // finish();
        }*/
    //   toolbar = (Toolbar) findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      //  getSupportActionBar().hide();
        Toast.makeText(MainActivity.this,"If profile is not updated, please update profile first.",Toast.LENGTH_LONG).show();

       drawerLayout=findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sideNavigationView=findViewById(R.id.nav_drawer);
        Menu menu = sideNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.drhelp);
        MenuItem menuItem1=menu.findItem(R.id.dchelp);

       View actionView =menuItem.getActionView();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Need help",Toast.LENGTH_SHORT).show();
            //    else   Toast.makeText(MainActivity.this,"Do not Need help",Toast.LENGTH_SHORT).show();


            }
        });
        View ActionView=menuItem1.getActionView();
        ActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"can help",Toast.LENGTH_SHORT).show();

            }
        });

        sideNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.myprofile:
                       Fragment f= new user_profile();
                       getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                       break;
                   case R.id.Emergency_contacts:
                       Fragment f1= new EmergencyDials();
                       getSupportFragmentManager().beginTransaction().replace(R.id.frame,f1).commit();
                       break;
                   case R.id.dnotifications:
                       Fragment f2= new Notifications();
                       getSupportFragmentManager().beginTransaction().replace(R.id.frame,f2).commit();
                       break;

                   case R.id.dfamily_details:
                       Toast.makeText(MainActivity.this,"family details",Toast.LENGTH_SHORT).show();
                       break;
                   case R.id.settings:
                       Toast.makeText(MainActivity.this,"Settings",Toast.LENGTH_SHORT).show();
                       break;
                   case R.id.dsign_out:
                       Toast.makeText(MainActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
                       FirebaseAuth.getInstance().signOut();

                       finish();
                       Intent intent = new Intent(MainActivity.this, login.class);

                       startActivity(intent);
                       break;


               }
               drawerLayout.closeDrawers();
               return true;
           }
       });
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
                        selectedFragment=new help_desk();
                        break;
                    case R.id.Corona:
                        //  Toast.makeText(MainActivity.this, "updates", Toast.LENGTH_SHORT).show();
                        selectedFragment = new CoronaUpdates();
                        break;
                    case R.id.Notifications:
                        // Toast.makeText(MainActivity.this, "help", Toast.LENGTH_SHORT).show();
                       selectedFragment = new user_profile();
                     //   startActivity(new Intent(MainActivity.this,user_profile.class));
                       // return true;
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedFragment).commit();
                return true;
            }
        });

        signout_btn = findViewById(R.id.signOut_btn);
     /*  signout_btn.setOnClickListener(new View.OnClickListener() {
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
                      //  Toast.makeText(MainActivity.this, "Welcome !!", Toast.LENGTH_SHORT).show();
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

        if(ft=="0"){
            Toast.makeText(MainActivity.this,"Please update profile first.",Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,new user_profile()).commit();
            //  finish();
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
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

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
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}
