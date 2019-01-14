package mawso3a.noon.mix;

import android.os.AsyncTask;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class CrossActivity extends Activity implements
        XWalkInitializer.XWalkInitListener,
        XWalkUpdater.XWalkUpdateListener {

    private XWalkInitializer mXWalkInitializer;
    private XWalkUpdater mXWalkUpdater;
    private XWalkView mXWalkView;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Must call initAsync() before anything that involves the embedding
        // API, including invoking setContentView() with the layout which
        // holds the XWalkView object.

        mXWalkInitializer = new XWalkInitializer(this, this);
        mXWalkInitializer.initAsync();

        // Until onXWalkInitCompleted() is invoked, you should do nothing with the
        // embedding API except the following:
        // 1. Instantiate the XWalkView object
        // 2. Call XWalkPreferences.setValue()
        // 3. Call mXWalkView.setXXClient(), e.g., setUIClient
        // 4. Call mXWalkView.setXXListener(), e.g., setDownloadListener
        // 5. Call mXWalkView.addJavascriptInterface()

        // XWalkPreferences.setValue("enable-javascript", true);
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        setContentView(R.layout.activity_cross);
        mXWalkView = (XWalkView) findViewById(R.id.xwalkview);
        // mXWalkView.getSettings().setJavaScriptEnabled(true);


        //XWalkSettings webSettings = mXWalkView.getSettings();

        //webSettings.setJavaScriptEnabled(true);
        //XWalkPreferences.setValue("enable-javascript", true);
        //mXWalkView.addJavascriptInterface(new JsInterface(), "NativeInterface");

        // XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);
        //XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);

       /* mXWalkView.setUIClient(new XWalkUIClient(mXWalkView){
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {

                if(!url.contains("android_asset")){

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(browserIntent);
                    view.stopLoading();
                }

            }
        });*/


        mXWalkView.setResourceClient(new XWalkResourceClient(mXWalkView) {

            @Override
            public boolean shouldOverrideUrlLoading(XWalkView view, String url) {

                String temp  = view.getUrl().toString();

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
            public void onLoadFinished(XWalkView view, String url) {
                super.onLoadFinished(view, url);
                String temp2  = mXWalkView.getUrl().toString();
                saveUrl(temp2);
            }

            /*public void onPageLoadStarted(XWalkView view, String url) {
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(browserIntent);
                view.stopLoading();*/
               /* String temp2  = mXWalkView.getUrl().toString();
            saveUrl(temp2);
        }*/



        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        // Try to initialize again when the user completed updating and
        // returned to current activity. The initAsync() will do nothing if
        // the initialization is proceeding or has already been completed.

        mXWalkInitializer.initAsync();
    }

    @Override
    public void onXWalkInitStarted() {
    }

    @Override
    public void onXWalkInitCancelled() {
        // Perform error handling here

        finish();
    }

    @Override
    public void onXWalkInitFailed() {
        if (mXWalkUpdater == null) {
            mXWalkUpdater = new XWalkUpdater(this, this);
        }
        mXWalkUpdater.updateXWalkRuntime();

    }

    @Override
    public void onXWalkInitCompleted() {
        // Do anyting with the embedding API

        if (mXWalkUpdater != null) {
            mXWalkUpdater.dismissDialog();
        }

        mXWalkView.addJavascriptInterface(new WebAppInterface(), "Android");
        mXWalkView.load(getUrl(), null);

        MainActivity.fa.finish();



    }

    @Override
    public void onXWalkUpdateCancelled() {
        // Perform error handling here

        finish();
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

    /*public class JsInterface {
        public JsInterface() {
        }

        @JavascriptInterface
        public String sayHello() {
            return "Hello World!";
        }
    }*/

    /*public class WebAppInterface {
        @org.xwalk.core.JavascriptInterface
        public String callFunction(){
            return "Hello World!";
        }
    }*/












    public class WebAppInterface implements AsyncResponse{

        @org.xwalk.core.JavascriptInterface
        public String callFunction(String value, String count) {
            MyAsyncTask asyncTask = new MyAsyncTask();
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

                StringBuilder buf = new StringBuilder(String.format("javascript:document.getElementById('desc_%1$s').innerHTML='%2$s';", output[1], output[0]));


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    // mXWalkView.load("javascript:" + "document.getElementById('cool').innerHTML ="+output , null);
                    mXWalkView.evaluateJavascript(buf.toString(), null);
                    //wv.loadDataWithBaseURL("ile:///android_asset/", (String) output, "text/html", "UTF-8", null);

                } else {

                    mXWalkView.loadUrl(buf.toString());
                }


            }
        }

    }



    public interface AsyncResponse {
        void processFinish(String[] output);
    }




    public class MyAsyncTask extends AsyncTask<String, Void, String[]> {
        public AsyncResponse delegate = null;


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


                //forget about this in xmarain // dataStr=  stringFromJNICross(dataStr);

                dataStr ="this cross walk view this came form java here will be heavey taxt proccseing";
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







    public native String stringFromJNICross(String html);



    private String getUrlWithoutParameters(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return new URI(uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                null, // Ignore the query part of the input url
                uri.getFragment()).toString();
    }


}
