package nagihan.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.HashMap;
import nagihan.myapp.models.BaseServiceResponse;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String URL = "http://35.163.166.250:9902/rest/user/register";
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 100;
    EditText password, email;
    TextView txtsing;
    Button login;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    CallbackManager callbackManager;
    LoginButton facebook;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions singInOptions;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        SignInButton signIn = (SignInButton) findViewById(R.id.signIn);
        status = (TextView) findViewById(R.id.status);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        txtsing = (TextView) findViewById(R.id.txtsing);
        requestQueue = Volley.newRequestQueue(this);

//------------------------------------------------------------------------------------

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", email.getText().toString());
                hashMap.put("password", password.getText().toString());
                JSONObject obj = new JSONObject(hashMap);
                request = new JsonObjectRequest(Request.Method.POST, URL, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BaseServiceResponse result = new Gson().fromJson(response.toString(), (new BaseServiceResponse()).getClass());
                        if (result.getIsSuccess())

                            startActivity(new Intent(MainActivity.this, Message.class));
                        else {
                            Toast.makeText(MainActivity.this, "Kullancı adı veya Şifreniz Yanlış", Toast.LENGTH_SHORT).show();
                        }


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });

                requestQueue.add(request);

            }

        });


        txtsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Singup.class);
                startActivity(i);
            }
        });


        //facebook discover işlemleri

        facebook = (LoginButton) findViewById(R.id.facebook);

        callbackManager = CallbackManager.Factory.create();

        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                loginResult.getAccessToken().getUserId();

                if (AccessToken.getCurrentAccessToken() != null) {
                    updateWithAccessToken(AccessToken.getCurrentAccessToken());
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        });


    //gmail discover işlemleri---
    singInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, singInOptions).build();
    signIn.setScopes(singInOptions.getScopeArray());
    signIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent singInintent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(singInintent, REQUEST_CODE);


        }
    });

}


    private void updateWithAccessToken(AccessToken currentAccessToken) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        startActivity(new Intent(MainActivity.this, Discover.class));


        if (requestCode == REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            Toast.makeText(getApplicationContext(), "sucess", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Discover.class));


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }
}


//-----------------------------------------------------------------------------




