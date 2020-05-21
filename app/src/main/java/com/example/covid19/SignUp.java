package com.example.covid19;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.model.helper_user;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "siggnup";
    private FirebaseAuth mAuth;
    TextView loginpage, resend_otp;
    Button signup;
    TextInputEditText t_name, t_age, phoneno;
    String mobile;
    String name,age;
    Spinner spinner;
    FirebaseDatabase mDatabase;

    private boolean mVerificationInProgress = false;
    DatabaseReference ref;
    private String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    int variable = 1;
    EditText otp_text;
    String mob_no;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();
        mDatabase = FirebaseDatabase.getInstance();
        spinner = findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        loginpage=findViewById(R.id.signin);
        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUp.this,login.class));
            }
        });

        t_name = findViewById(R.id.help_name);
        t_age = findViewById(R.id.help_age);
        phoneno = findViewById(R.id.Phone1 );

        signup = findViewById(R.id.btn_signUp);
        mAuth = FirebaseAuth.getInstance();

       /* signupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.toString();
                // String value = dataSnapshot.getValue(String.cvalass);
                Log.d(TAG, "sign up value read  is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
*/

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (variable == 1) {
                    ref = mDatabase.getReference("Nomodular");
                } else {
                    ref = mDatabase.getReference("Aid_Helper");
                }

                if (!validatePhoneNumber()) {
                    return;
                }
                //  startPhoneNumberVerification(phoneno.getText().toString());
                readData(phoneno.getText().toString(),variable);

                //  registerMobile();
            }
        });
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Log.e("uid new :",""+firebaseAuth.getUid());

            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phoneno.setError("Invalid Credentials.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                otpDialog();


            }
        };

    }
    public  void readData(String phoneNum,int var){
        // final String[] phoneNum = new String[1];
        final int[] f =new int[1];
        //String uid=phoneNum.getUid();
        if(var==1) {
            ref=mDatabase.getReference("Nomodular");

            ref.orderByChild("mobile_no").equalTo(phoneNum).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        //f[0] =1;
                        startPhoneNumberVerification(phoneno.getText().toString());

                    } else
                        Toast.makeText(SignUp.this, "User already exist.\nPlease register with different phone number.", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            ref=mDatabase.getReference("Aid_Helper");
            ref.orderByChild("mobile_no").equalTo(phoneNum).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        //f[0] =1;
                        startPhoneNumberVerification(phoneno.getText().toString());

                    } else
                        Toast.makeText(SignUp.this, "User already exist.\nPlease register with different phone number.", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        //  if(f[0]==0) return true;
        //return false;
    }

    String s;
    private void startPhoneNumberVerification(String phoneNumber) {
        name = t_name.getText().toString();
        age = t_age.getText().toString();
        s="+91"+phoneNumber;
        Log.e("registerMobile", "mob_no length  :" +s.length() + " mob no: " + s);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age)) {
            Toast.makeText(SignUp.this, "empty fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        if(s.length()!=13){
            Toast.makeText(SignUp.this,"Invalid Phone Number!\n Enter valid 10 digit Phone number." ,Toast.LENGTH_LONG).show();
            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        //otpDialog();
        mVerificationInProgress = true;


    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);

        // ForceResendingToken from callbacks
    }

    private void saveDataInFirebase(String uid) {
        // ref=mDatabase.getReference("Users");

        name = t_name.getText().toString().trim();
        age = t_age.getText().toString().trim();

        mobile = phoneno.getText().toString().trim();

        helper_user user = new helper_user(name, mobile, age);


        Log.e("uid : ", "" + uid);
        uid = phoneno.getText().toString();
        ref.child(uid).setValue(user);

    }

    private void otpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

        View view = getLayoutInflater().inflate(R.layout.fragment_otp_checker, null);
        resend_otp = view.findViewById(R.id.tv_otp_resend);
        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(s, mResendToken);
            }
        });

        otp_text = view.findViewById(R.id.et_otp_dig_1);
        builder.setCancelable(false);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code=otp_text.getText().toString();
                if (code.equals("")||code.length()!=6) {
                    Toast.makeText(SignUp.this, "Cannot leave empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            saveDataInFirebase(user.getUid());

                            Intent inten = new Intent(SignUp.this, MainActivity.class);
                            inten.putExtra("mmobile", phoneno.getText().toString());
                            inten.putExtra("choice", variable);
                            inten.putExtra("firsttime","1");
                            finish();
                            startActivity(inten);
                            // ...
                        } else {

                            Toast.makeText(SignUp.this, "Wrong otp !!", Toast.LENGTH_SHORT).show();

                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(SignUp.this,"Invalid credentials",Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = phoneno.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)||phoneNumber.length()!=10) {
            // phone.setError("Invalid phone number.");
            Toast.makeText(SignUp.this,"Invalid Number",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    /* private void registerMobile() {
         mob_no = "+91" + phoneno.getText().toString();
         name = t_name.getText().toString();
         age = t_age.getText().toString();

         Log.e("registerMobile", "mob_no length  :" + mob_no.length() + " mob no: " + mob_no);

         if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age)) {
             Toast.makeText(SignUp.this, "empty fields required", Toast.LENGTH_SHORT).show();
             return;
         }

         if (mob_no.length() != 13) {
             Toast.makeText(this, "Enter a valid 10 digit number", Toast.LENGTH_SHORT).show();
             return;
         }

         Toast.makeText(this, "Please wait !!", Toast.LENGTH_SHORT).show();
         PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 mob_no,        // Phone number to verify
                 60,                 // Timeout duration
                 TimeUnit.SECONDS,   // Unit of timeout
                 this,               // Activity (for callback binding)
                 mCallbacks);        // OnVerificationStateChangedCallbacks
     }
 */
    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String firstItem = String.valueOf(spinner.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinner.getSelectedItem()))) {
                // ToDo when first item is selected
                variable = 1;
            } else {
                variable = 2;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

    }
}

