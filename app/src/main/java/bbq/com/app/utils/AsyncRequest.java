package bbq.com.app.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncRequest extends AsyncTask<String, Integer, String> {

    OnAsyncRequestComplete caller;
    Context context;
    String method = "GET";
    ProgressDialog pDialog = null;
    String label;
    String _preloaderString = "Loading data...";
    public static final String app_url = "http://webquiz.brainbout.in/";
    //public static final String app_url = "http://bbq.theuniquemedia.in/rest/";

    public AsyncRequest(Activity a, String m, String l) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
        label = l;
    }

    public AsyncRequest(Activity a, String m) {
        caller = (OnAsyncRequestComplete) a;
        context = a;
        method = m;
    }
    public AsyncRequest(OnAsyncRequestComplete al, Activity a, String m, String l) {
        caller = al;
        context = a;
        method = m;
        label = l;
    }

    public AsyncRequest(OnAsyncRequestComplete al, Activity a, String m) {
        caller = al;
        context = a;
        method = m;
    }


    public String getPreloaderString() {
        return _preloaderString;
    }

    public void setPreloaderString(String _preloaderString) {
        this._preloaderString = _preloaderString;
    }
    // Interface to be implemented by calling activity
    public interface OnAsyncRequestComplete {
        public void asyncResponse(String response, String label);
    }

    public String doInBackground(String... params) {
        // get url pointing to entry point of API

        if (method == "POST") {
            String address = params[0].toString();
            String payload = params[1].toString();
            return post(address, payload);
        }

        if (method == "GET") {
            String address = params[0].toString();
            return get(address);
        }

        return null;
    }

    public void onPreExecute() {
        if (!DetectConnection.checkInternetConnection(context)) {
            Toast.makeText(context,
                    "You do not have Internet Connection",
                    Toast.LENGTH_LONG).show();
        }
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(_preloaderString); // typically you will define such
        // strings in a remote file.
        if(!_preloaderString.equals(""))
        pDialog.show();
    }

    public void onProgressUpdate(Integer... progress) {
        // you can implement some progressBar and update it in this record
        // setProgressPercent(progress[0]);
    }

    public void onPostExecute(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response, label);
    }

    protected void onCancelled(String response) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        caller.asyncResponse(response, label);
    }

    @SuppressWarnings("deprecation")
    private String get(String address) {
        String jsonString = new String();

        try {
            HttpURLConnection urlConnection = null;

            URL url = new URL(app_url + address);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(20000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private String post(String uri, String json) {

        HttpURLConnection urlConnection;
        String JsonResponse = null;
        String data = json;
        String result = null;
        try {
            //Connect
            URL url = new URL(app_url + uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");

            //send
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(data);
            writer.close();


            InputStream inputStream = urlConnection.getInputStream();
            //input stream
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();

            return JsonResponse;


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}