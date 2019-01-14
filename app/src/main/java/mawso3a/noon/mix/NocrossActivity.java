package mawso3a.noon.mix;
import java.io.File;
import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import android.net.Uri;
import java.io.*;

import android.webkit.JavascriptInterface;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.app.Activity;
        import android.content.Intent;
        import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.content.res.Configuration;
        import android.content.SharedPreferences.Editor;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.webkit.WebChromeClient;
        import android.view.View;
        import android.webkit.WebSettings;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


import android.util.Log;
import android.webkit.JavascriptInterface;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebViewClient;
import org.xwalk.core.XWalkInitializer;
import org.xwalk.core.XWalkUpdater;
import org.xwalk.core.XWalkView;
import org.xwalk.core.XWalkPreferences;
import android.content.Intent;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkResourceClient;
import android.content.res.Configuration;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;
import android.content.Context;
import android.text.Html;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;
import java.util.regex.*;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.List;
import android.text.TextUtils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.UUID;
import java.util.concurrent.*;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;

public class NocrossActivity extends AppCompatActivity {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nocross);


        wv = (WebView) findViewById(R.id.wv);
        wv.setInitialScale(1);
        wv.getSettings().setAllowFileAccess(true);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setDomStorageEnabled(true);//هنا الحل
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setDefaultTextEncodingName("utf-8");
        //wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //wv.loadUrl("file:///android_asset/index.html");

        wv.addJavascriptInterface(new WebAppInterface(), "Android");
        if (savedInstanceState == null) {
            wv.loadUrl(getUrl());
        }


        wv.setWebChromeClient(new WebChromeClient());

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String temp  = wv.getUrl().toString();

                if(temp.contains("android_asset")) {
                    saveUrl(temp);
                }
                if(url.contains("android_asset")){

                    return false;
                } else {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String temp2  = wv.getUrl().toString();
                // if(url.contains("android_asset")) {

                saveUrl(url);

                // }

                // Log.d("WebView", "your current url when webpage loading.." + url);
            }

        });


    }




    public void saveUrl(String url){
        SharedPreferences sp = getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString("SAVED_URL", url);
        editor.commit();
    }


    public String getUrl(){

        SharedPreferences sp = getSharedPreferences("SP_WEBVIEW_PREFS", MODE_PRIVATE);
        //If you haven't saved the url before, the default value will be google's page
        return sp.getString("SAVED_URL", "file:///android_asset/index.html");

    }

    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if(wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        wv.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wv.restoreState(savedInstanceState);
    }






    public class WebAppInterface implements NocrossActivity.AsyncResponse {

        @JavascriptInterface
        public String callFunction(String value, String count) {
            NocrossActivity.MyAsyncTask asyncTask = new NocrossActivity.MyAsyncTask();
            String[] obj = new String[2];
            obj[0] = value;
            obj[1] = count;

            asyncTask.delegate = this;
            asyncTask.execute(obj);

            return value;

        }

        @Override
        public void processFinish(String[] output) {


            if (output != null) {



                Log.d("data",output[0]);

                StringBuilder buf = new StringBuilder(String.format("javascript:document.getElementById('desc_%1$s').innerHTML='%2$s'", output[1], output[0]));
//%2$s

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    // mXWalkView.load("javascript:" + "document.getElementById('cool').innerHTML ="+output , null);
                    wv.evaluateJavascript(buf.toString(), null);
                    //wv.loadDataWithBaseURL("ile:///android_asset/", (String) output, "text/html", "UTF-8", null);

                } else {

                    wv.loadUrl(buf.toString());
                }


            }
        }

    }



    public interface AsyncResponse {
        void processFinish(String[] output);
    }




    public class MyAsyncTask extends AsyncTask<String, Void, String[]> {
        public NocrossActivity.AsyncResponse delegate = null;


        @Override
        public String[] doInBackground(String... params) {


            try {



                Uri uri = Uri.parse(params[0]);

                String Value = params[0];
               // String rhhlterm = uri.getQueryParameter("rhhlterm").replaceAll("^\"|\"$", "");
                String Str = getUrlWithoutParameters(uri.toString().replace("file:///android_asset/",""));

                InputStream is = getAssets().open(Str);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final int bufSize = 4096;
                byte[] buf = new byte[bufSize];
                int read;
                while ((read = is.read(buf)) > 0) {
                    baos.write(buf, 0, read);
                }
                String dataStr = new String(baos.toByteArray(), "UTF-8");


               // dataStr=  stringFromJNInoCross(dataStr);//forget about this in xmarain

                dataStr ="this is no crosswalk view, this came form java here will be heavey taxt proccseing";

                String [] obj = new String[2];
                obj[0] = dataStr;
                obj[1] = params[1];

                return obj;

            } catch (Exception e) {
                e.printStackTrace();
                //Log.d("data","gggggggg");
                return null;
            }



        }

        @Override
        public void onPostExecute(String[] result) {
            delegate.processFinish(result);
        }
    }



    public native String stringFromJNInoCross(String html);




    private String getUrlWithoutParameters(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return new URI(uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null, // Ignore the query part of the input url
                uri.getFragment()).toString();
    }


}


