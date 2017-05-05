package bbq.com.app.pages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import bbq.com.app.R;
import bbq.com.app.utils.Constants;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    String _pageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String mobileNo = getIntent().getStringExtra("mobileNo");

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(Constants.DOMAIN + mobileNo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                injectCssIntoWebView(
                        webView,
                        "page-top { display:none; }",
                        ".al-main { padding-top:10px; }"
                );
            }
        });


    }


    /**
     * Creates a CSS element in the <head> section of the Web page and assigns it
     * to a `customSheet` JS variable
     */
    private final static String CREATE_CUSTOM_SHEET =
            "if (typeof(document.head) != 'undefined' && typeof(customSheet) == 'undefined') {"
                    + "var customSheet = (function() {"
                    + "var style = document.createElement(\"style\");"
                    + "style.appendChild(document.createTextNode(\"\"));"
                    + "document.head.appendChild(style);"
                    + "return style.sheet;"
                    + "})();"
                    + "}";

    /**
     * Adds CSS properties to the loaded Web page. A <head> section should exist when this method is called.
     * The Web view should be configured with `.getSettings().setJavaScriptEnabled(true);`
     *
     * @param webView  Web view to inject into
     * @param cssRules CSS rules to inject
     */
    void injectCssIntoWebView(WebView webView, String... cssRules) {
        StringBuilder jsUrl = new StringBuilder("javascript:");
        jsUrl
                .append(CREATE_CUSTOM_SHEET)
                .append("if (typeof(customSheet) != 'undefined') {");
        int cnt = 0;
        for (String cssRule : cssRules) {
            jsUrl
                    .append("customSheet.insertRule('")
                    .append(cssRule)
                    .append("', ")
                    .append(cnt++)
                    .append(");");
        }
        jsUrl.append("}");

        webView.loadUrl(jsUrl.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
