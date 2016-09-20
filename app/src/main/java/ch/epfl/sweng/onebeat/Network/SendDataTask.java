package ch.epfl.sweng.onebeat.Network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import ch.epfl.sweng.onebeat.Exceptions.NotDefinedUserInfosException;
import ch.epfl.sweng.onebeat.Exceptions.ParseException;
import ch.epfl.sweng.onebeat.Exceptions.ParserNotDefinedException;

/**
 * Created by Matthieu on 01.12.2015.
 */
public class SendDataTask extends AsyncTask<String, Void, String> {

    private DataProvider callingProvider;

    public SendDataTask(DataProvider callingProvider) {
        this.callingProvider = callingProvider;
    }

    @Override
    protected String doInBackground(String... params) {

        // params[0] is url
        // params[1] is stuff to send
        return uploadUrl(params[0], params[1]);
    }

    private String uploadUrl(String targetURL, String dataToSend) {
        URL url;
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";
        try {
            //Create urlConnection
            url = new URL(targetURL);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Charset", charset);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("request", dataToSend);
            String query = builder.build().getEncodedQuery();

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            InputStream is;

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("#SendData", "http response code is " + urlConnection.getResponseCode());
                is = urlConnection.getErrorStream();
            }
            else {

                //Get Response
                is = urlConnection.getInputStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        try {
            callingProvider.onWebDataReception(result);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ParserNotDefinedException e) {
            e.printStackTrace();
        } catch (NotDefinedUserInfosException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
