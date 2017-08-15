package nagihan.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import nagihan.myapp.models.BaseServiceResponse;
import nagihan.myapp.models.ResponseListener;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class Singup extends AppCompatActivity {

    private static final String URL = "http://35.163.166.250:9902/rest/user/register";
    private static final String UPLOAD_URL = " http://35.163.166.250:9902/rest/user/uploadImage";
    EditText password,email;
    Button singup;
    ImageView circleView;
    public static int IMAGE_GALERY_REQUEST = 1005;
    public static int STORAGE_PERMISSION_RC = 101;

    private RequestQueue requestQueue;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        singup = (Button) findViewById(R.id.singup);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        circleView = (ImageView) findViewById(R.id.circleView);


        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.circularimage);
        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);

        circleView.setImageBitmap(circularBitmap);

        requestQueue = Volley.newRequestQueue(this);

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK);
                File file = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String filePath = file.getPath();
                Uri data = Uri.parse(filePath);
                i.setDataAndType(data, "image/*");
                startActivityForResult(i, IMAGE_GALERY_REQUEST);

            }
        });

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("email", email.getText().toString());
                hashMap.put("password", password.getText().toString());


                final JSONObject obj = new JSONObject(hashMap);
                request = new JsonObjectRequest(Request.Method.POST, URL, obj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BaseServiceResponse result = new Gson().fromJson(response.toString(), (new BaseServiceResponse()).getClass());
                        if (result.getIsSuccess())
                            startActivity(new Intent(Singup.this, MainActivity.class));

                        else{
                            Toast.makeText(Singup.this, "Bilgilerinizi tekrar kontrol edin", Toast.LENGTH_SHORT).show();

                        }
                        Log.d("Request",obj.toString());
                        Log.d("Register",response.toString());

                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Singup.this, "error1", Toast.LENGTH_SHORT).show();

                    }

                });

                requestQueue.add(request);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_RC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted  start reading
            } else {
                Toast.makeText(this, "No permission to read external storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ActivityCompat.checkSelfPermission(Singup.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Singup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
            return;
        }

        if (resultCode == RESULT_OK && data!=null) {
            if (requestCode == IMAGE_GALERY_REQUEST) {

                        Uri selectedImage = data.getData();

                        InputStream inputStream;
                        try {

                            inputStream = getContentResolver().openInputStream(selectedImage);
                            Bitmap image = BitmapFactory.decodeStream(inputStream);
                            circleView.setImageBitmap(image);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

    public static void uploadUserImage(Activity activity, String filePath, String userId, final ResponseListener<String> responseListener) {

        HashMap<String, String> headers = new HashMap<>();

        UploadFileTask task = new UploadFileTask(activity, UPLOAD_URL, responseListener, headers, filePath, userId);
        task.execute();

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    static class ImageConverter {

        public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }


    }

}