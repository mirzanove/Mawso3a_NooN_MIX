package mawso3a.noon.mix;


import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.webkit.WebView;

import android.app.Activity;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private WebView wv;

    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText("gggggggggggggggg");



        int currentapiVersion = android.os.Build.VERSION.SDK_INT;


        if (currentapiVersion >= 19) {

            Intent i = new Intent(this,NocrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
            startActivity(i);
            finish();


        } else {


            Intent i = new Intent(this,CrossActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);;
            startActivity(i);
            finish();

        }


        fa = this;

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String html);














}
