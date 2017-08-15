package nagihan.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import nagihan.myapp.models.ResponseListener;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Profil extends AppCompatActivity {

    private static final int IMAGE_GALERY_REQUEST = 1004;
    public static int STORAGE_PERMISSION_RC = 101;
    private static final String UPLOAD_URL = " http://35.163.166.250:9902/rest/user/uploadImage";
    ImageView imageProfil;
    TextView tvlearning, tvlearn2, tvbolge, tvnative;
    Spinner spNative, spLearning, spBolge, spLearn2;
    EditText editYas,editAd,editSoyad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        imageProfil = (ImageView) findViewById(R.id.imageProfil);
        spNative = (Spinner) findViewById(R.id.spNative);
        editAd = (EditText) findViewById(R.id.editAd);
        editSoyad = (EditText) findViewById(R.id.editSoyad);


        imageProfil.setOnClickListener(new View.OnClickListener() {
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
    }

        //Spinner json

      /*  ArrayList<String> items = getNative("native.json");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txt, items);
        spNative.setAdapter(adapter);

    }

    public ArrayList<String> getNative(String fileName) {
        JSONArray jsonArray = null;
        ArrayList<String> nList = new ArrayList<String>();
        try {
            InputStream inputStream = getResources().getAssets().open(fileName);
            int size = inputStream.available();
            byte[] data = new byte[size];
            inputStream.read(data);
            inputStream.close();
            String json = new String(data, "UTF-8");
            jsonArray = new JSONArray(json);
            if (jsonArray != null) ;

            for (int i = 0; i < jsonArray.length(); i++) {

                nList.add((String) jsonArray.getJSONObject(i).get("nname"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return nList;
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (ActivityCompat.checkSelfPermission(Profil.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Profil.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_RC);
            return;
        }


        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IMAGE_GALERY_REQUEST) {

                Uri selectedImage = data.getData();

                InputStream inputStream;
                try {

                    inputStream = getContentResolver().openInputStream(selectedImage);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    imageProfil.setImageBitmap(image);

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


}

