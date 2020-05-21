package com.example.covid19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroCustomLayoutFragment;
import com.github.appintro.AppIntroFragment;

public class introactivity extends AppIntro {



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int check = preferences.getInt("checkk",2);

      /*      if(check == 3)
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
*/



            getSupportActionBar().hide();
        setWizardMode(true);
        setImmersive(true);


        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_page1));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_page2));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_page3));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.activity_page4));
            setIndicatorColor(R.color.colorAccent,R.color.yellow);

    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("checkk",1);
        editor.commit();
        startActivity(new Intent(getApplicationContext(),chooserActivity.class));
        finish();
    }

}
