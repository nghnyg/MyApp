package nagihan.myapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import nagihan.myapp.models.BaseServiceResponse;
import nagihan.myapp.models.ResponseListener;


public class UploadFileTask extends AsyncTask<Void, Integer, String> {

    private static final String TAG = "UploadFileTask";

    private String uploadUrl;
    private String filePath;
    private String fileName;
    private long startInMillis = 0;
    private long endInMillis = 0;
    private ResponseListener<String> responseListener;
    private HashMap<String, String> headers;
    private Activity activity;
    private String userId;

    public UploadFileTask(Activity activity, String uploadUrl, ResponseListener<String> responseListener, final HashMap<String, String> headers, String filePath, String userId) {
        super();
        this.uploadUrl = uploadUrl;
        this.filePath = filePath;
        this.responseListener = responseListener;
        this.headers = headers;
        this.activity = activity;
        this.userId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (filePath.contains("/")) {
            String[] chunks = filePath.split("/");
            fileName = chunks[chunks.length - 1];
        } else {
            fileName = filePath;
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    private String uploadFile() {

        startInMillis = System.currentTimeMillis();
        Log.d(TAG, "startInMillis: " + startInMillis);

        String iFileName = "temp" + fileName;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        HttpURLConnection conn = null;
        System.setProperty("http.keepAlive", "false");
        String result = null;
        try {
            Log.e(Tag, "Starting Http File Sending to URL");

            URL uploadURL = new URL(uploadUrl);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) uploadURL.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_3 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B329 Safari/8536.25");
            conn.setRequestProperty("Connection", "Keep-Alive");
            for (String key : headers.keySet()) {
                conn.setRequestProperty(key, headers.get(key));
            }
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file", iFileName);

            conn.setChunkedStreamingMode(-1); // use default chunk size

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("My Title");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes("My Description");
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"userId\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(userId);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");

            File sourceFile = new File(filePath);
//                uploadFileInputStream = params[0];
            if (!sourceFile.isFile()) {
                Log.e("uploadFile", "Source File not exist :" + filePath);
            }

            FileInputStream uploadFileInputStream = new FileInputStream(sourceFile);
            Log.e("UploadFileInput", "" + uploadFileInputStream.available());

            // create a buffer of maximum size
                /*int bytesAvailable = uploadFileInputStream.available();

                int maxBufferSize = 1 * 1024; // 1KB
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);*/
//                int bufferSize = 65536;
            int bufferSize = 1 * 1024; // 1KB
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
//                int bytesRead = uploadFileInputStream.read(buffer, 0, bufferSize);
            int bytesRead;

            float size = 0;
//                float readSize = 0;
            float rated = 0;
            long readTime;
            long timePast;
            long startTime = System.currentTimeMillis();
            long timing = System.currentTimeMillis();
            while ((bytesRead = uploadFileInputStream.read(buffer, 0, bufferSize)) > 0
                    && !isCancelled()) {
                dos.write(buffer, 0, bufferSize);
                size += bytesRead;
//                    Log.e("Written Size", "" + bytesRead);

                    /*bytesAvailable = uploadFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = uploadFileInputStream.read(buffer, 0, bufferSize);*/

//                    size += bytesRead;
//                    Log.e("Written Size", "" + bytesRead);
                readTime = System.currentTimeMillis();
                timePast = (readTime - timing);// / 1000;
//                        Log.e("Time Past", "" + ((readTime - timing) / 1000));
            }
            Log.e("OutPutUrl", "" + conn.getURL());

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            long endTime = System.currentTimeMillis();

            // close streams
            uploadFileInputStream.close();

            dos.flush();
            dos.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            result = sb.toString();

            Log.d("UploadImageResult",result);

            Log.e("Upload:", "File uploaded Size: " + dos.size());
            Log.e("UploadManager", "upload ended: " + ((endTime - startTime) / 1000) + " secs");

            double rate = (((size / 1024) / ((endTime - startTime) / 1000)) * 8);
            rate = Math.round(rate * 100.0) / 100.0;
            String ratevalue = String.format("%.2f", rate / 1024);
            String formattedRateValue = ratevalue.concat(" Mbps");

            Log.e("SPEED_TEST_DEBUG", "upload2 speed: " + formattedRateValue);

        } catch (MalformedURLException ex) {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
        } finally {
            conn.disconnect();
        }
        endInMillis = System.currentTimeMillis();

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int val = values[0];
        if (val >= 100)
            val = 100;
        Log.d(TAG, "upload progress: %" + val);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        long totalTimeInMillis = endInMillis - startInMillis;
        File file = new File(filePath);
        long fileSizeInBytes = file.length();
        Log.d(TAG, "totalTimeInMillis: " + totalTimeInMillis);
        Log.d(TAG, "fileSizeInBytes: " + fileSizeInBytes);

        if(response == null){
        }else{
            BaseServiceResponse result = new Gson().fromJson(response.toString(), (new BaseServiceResponse()).getClass());
            if (result.getIsSuccess()) {
                if (responseListener != null)
                    responseListener.onSuccess(null);
            }
        }


    }

}
