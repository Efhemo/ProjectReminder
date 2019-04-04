package com.efhems.newinsideproject.ui.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efhems.newinsideproject.AppExecutors;
import com.efhems.newinsideproject.R;
import com.efhems.newinsideproject.data.ProjectReminderDatabase;
import com.efhems.newinsideproject.data.local.entities.Project;
import com.efhems.newinsideproject.ui.activities.CloudStorageActivity;
import com.efhems.newinsideproject.ui.activities.addTask.AddTaskActivity;
import com.efhems.newinsideproject.ui.activities.projectList.MyProjectActivity;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import okhttp3.OkHttpClient;

import static com.efhems.newinsideproject.ui.activities.projectList.MyProjectActivity.CURRENT_PROJECT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AddProjectFragment.OnClickOkButtonListener {

    private static final String TAG = "MainActivity";
    public static final int RC_SIGN_IN = 19057;

    //START declare_auth
    private FirebaseAuth mAuth;

    GoogleApiClient mGoogleApiClient;

    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView text;
    private AddProjectFragment addProjectFragment;
    private TextView tvUserName;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Stetho.initializeWithDefaults(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        // Init Views
        LinearLayout myProjects_container = findViewById(R.id.my_projects_container);
        LinearLayout newProjects_container = findViewById(R.id.new_projects_container);
        LinearLayout settingsContainer = findViewById(R.id.settings_container);
        LinearLayout authContainer = findViewById(R.id.auth_container);
        tvUserName = findViewById(R.id.user_name);
        text = findViewById(R.id.login);

        // Apply OnclickListener
        myProjects_container.setOnClickListener(this);
        newProjects_container.setOnClickListener(this);
        settingsContainer.setOnClickListener(this);
        authContainer.setOnClickListener(this);


        //START initialize_auth
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();


        //If the user is already sign in to firebase, go to other activity
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user !=null){

                    updateUI(user);
                }
            }
        };

        GoogleSignInOptions gso = new SetUpGoogleSign().invoke();
        setupGoogleClient(gso);
    }


    private void setupGoogleClient(GoogleSignInOptions gso) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    // START onactivityresult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed");
                updateUI(null);

            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //START auth_with_google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //showProgressDialog();

        //Get Google api Client credentials
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            updateUI(null);
                        }

                        //hideProgressDialog();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();

        if (user != null) { //suceesful sign user

            text.setText(getString(R.string.remote_task));
            tvUserName.setText(user.getDisplayName());

        } else { //not succesful
            Toast.makeText(MainActivity.this, R.string.auth_fail , Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onClickOkButton(EditText editTextProjectName, EditText editTextProjectDesc) {
        makeProject(editTextProjectName, editTextProjectDesc);
    }


    private class SetUpGoogleSign {
        public GoogleSignInOptions invoke() {
            return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
        }
    }

    private void goToCloud(String textOnTv){

        if(textOnTv.equals(getString(R.string.remote_task))){
            startActivity(new Intent(this, CloudStorageActivity.class));
        }else{
            signIn();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_projects_container:
                goToMyProjectsActivity();
                break;

            case R.id.new_projects_container:
                openAddNewProjectDialog();
                break;

            case R.id.settings_container:
                openSettingsPreferenceDialog();
                break;

                case R.id.auth_container:
                    goToCloud(text.getText().toString());
                //signIn();
                break;

            default:
        }
    }


    private void openAddNewProjectDialog() {
        addProjectFragment = AddProjectFragment.newInstance(this);
        addProjectFragment.setCancelable(true);
        addProjectFragment.show(getSupportFragmentManager(), "AddProjectFragment");
    }

    private void goToMyProjectsActivity() {
        startActivity(new Intent(MainActivity.this, MyProjectActivity.class));
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    private void openSettingsPreferenceDialog() {
        SettingsDialog dialog = new SettingsDialog();
        dialog.show(getSupportFragmentManager(), "preferenceDialog");
    }

    private void makeProject(EditText editTextProjectName, EditText editTextProjectDesc){

        final String name = editTextProjectName.getText().toString();
        final String desc = editTextProjectDesc.getText().toString();

        if(name.isEmpty()){
            Toast.makeText(this, R.string.invalid_project_name, Toast.LENGTH_SHORT).show();
            return;
        }

        if(desc.isEmpty()){
            Toast.makeText(this, R.string.invalid_project_description, Toast.LENGTH_SHORT).show();
            return;
        }



        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {


                final Project project = new Project(name, desc);
                final long rowId = ProjectReminderDatabase.getInstance(getApplicationContext()).projectTaskDao()
                        .insertProject(project);

                if(rowId != -1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Long l= new Long(rowId);
                            int i=l.intValue();
                            project.setId(i);
                            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                            intent.putExtra(CURRENT_PROJECT, project);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                            addProjectFragment.dismiss();


                        }
                    });

                }
            }
        });


    }

}
