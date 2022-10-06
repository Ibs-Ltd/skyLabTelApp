package tel.skylab.skylabtel.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import tel.skylab.skylabtel.R;

public class PrivacyPolicyWebActivity extends AppCompatActivity {

    private WebView wvLoadItem;
    private ProgressBar pbLoadTerms;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_web);

        String mWebUrl = "https://skylab.tel/terms.php";
        pbLoadTerms = (ProgressBar) findViewById(R.id.pv_load_terms);
        wvLoadItem = (WebView) findViewById(R.id.wv_load_item);
        back_btn = findViewById(R.id.back_btn);

        WebSettings webSettings = wvLoadItem.getSettings();
        //        wvLoadItem.getSettings().setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        wvLoadItem.setWebViewClient(new WebClient());
        wvLoadItem.loadUrl(mWebUrl);

        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }

    public class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            wvLoadItem.setVisibility(View.GONE);
            pbLoadTerms.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            try {
                pbLoadTerms.setVisibility(View.GONE);
                wvLoadItem.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedError(
                WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            wvLoadItem.setVisibility(View.GONE);
            Toast.makeText(
                            PrivacyPolicyWebActivity.this,
                            "Something went wrong",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
