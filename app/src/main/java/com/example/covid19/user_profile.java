package com.example.covid19;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.covid19.model.helper_user;
import com.example.covid19.model.user_profmodel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.covid19.MainActivity.reff;
import java.util.List;

public class user_profile extends Fragment{

    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    EditText tname,tdob,tloc,taddress,tpin,tcity;
    RadioButton tm,tf;
    Button save;
    ImageView photo;
    TextView pname,mobno;
    private FirebaseStorage firebaseStorage;
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.user_profile, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        pname=view.findViewById(R.id.name);
        mobno= view.findViewById(R.id.mobno);
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                helper_user user = dataSnapshot.getValue(helper_user.class);
                String value=user.full_name;
                pname.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                helper_user user = dataSnapshot.getValue(helper_user.class);
                String value=user.mobile_no;
                mobno.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        photo=view.findViewById(R.id.profile_image);
        tname=view.findViewById(R.id.euname);
        taddress=view.findViewById(R.id.eadd);
        tloc=view.findViewById(R.id.elocation);
        tdob=view.findViewById(R.id.edob);
        tcity=view.findViewById(R.id.ecity);
        tpin=view.findViewById(R.id.epin);
        tm=view.findViewById(R.id.radia_id1);
        tf=view.findViewById(R.id.radia_id2);
        save=view.findViewById(R.id.save);
        databaseReference=reff.child("user_profile");
        //databaseReference=databaseReference.getRef()
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_profmodel user=dataSnapshot.getValue(user_profmodel.class);
                int cnt= (int) dataSnapshot.getChildrenCount();
                if(user!=null){
                    for (int i=0;i<cnt;i++) {
                       //String n=(ds.child("name").getValue().toString());
                        tname.setText(user.name);
                        taddress.setText(user.address);
                        tloc.setText(user.loc);
                        tdob.setText(user.dob);
                        tcity.setText(user.city);
                        tpin.setText(user.pin);
                        if (user.gender == "Male") {
                            tm.setChecked(true);
                        //    tf.setEnabled(false);
                        } else {
                        //    tm.setEnabled(false);
                            tf.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference();
        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
  /*      storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                // ".fit().centerInside()" fits the entire image into the specified area.
                // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                Picasso.get().load(uri).fit().centerInside().into(photo);
            }
        });*/
        if (firebaseAuth.getCurrentUser() == null){
          //  finish();
            startActivity(new Intent(getContext(),login.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();

        
    }

    private void uploadToFirebase() {
        String pname=tname.getText().toString();
        String padd=taddress.getText().toString();
        String ploc=tloc.getText().toString();
        String ppin=tpin.getText().toString();
        String pdob=tdob.getText().toString();
        String pcity=tcity.getText().toString();
        String gender;

        if(tm.isChecked()){
            gender="Male";
        }
        else{
            gender="Female";
        }
        if(TextUtils.isEmpty(pname)|| TextUtils.isEmpty(padd)|| TextUtils.isEmpty(ploc)|| TextUtils.isEmpty(ppin)||TextUtils.isEmpty(pdob)||TextUtils.isEmpty(gender)||TextUtils.isEmpty(pcity)){
            Toast.makeText(getContext(),"Empty fields Required",Toast.LENGTH_SHORT).show();
            return;
        }
        user_profmodel user=new user_profmodel(pname,pdob,gender,ploc,padd,pcity,ppin);
        reff.child("user_profile").setValue(user);
        Toast.makeText(getContext(),"Data saved!",Toast.LENGTH_SHORT).show();
    }
}
