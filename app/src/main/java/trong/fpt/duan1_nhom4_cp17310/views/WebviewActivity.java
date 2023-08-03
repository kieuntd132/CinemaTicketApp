package trong.fpt.duan1_nhom4_cp17310.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import trong.fpt.duan1_nhom4_cp17310.R;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = findViewById(R.id.wv_tintuc);
        Intent intent = getIntent();
        String link = intent.getStringExtra("linkWeb");
        webView.loadUrl(link);
        //lick vào link vẫn ở trong app mà k mở trình duyệt
        webView.setWebViewClient(new WebViewClient());
    }
}