package com.bseb.a10thobjectivequestions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener {

    private String webUrl = "https://bsebtarget.com//BSEB/Class%2010th/Home.php";
    // WebView
    String url = "";
    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;

    Loading loading;

    // Drawer Menu
    ImageView imageBtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FirebaseRemoteConfig firebaseRemoteConfig;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        swipeRefreshLayout = findViewById(R.id.swipe);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        imageBtn = findViewById(R.id.menu_btn);
        loading = new Loading(this);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadWebView();
        navigationDrawer();


        sharedPreferences = this.getSharedPreferences("MyMain", MODE_PRIVATE);
        HashMap<String, Object> map = new HashMap<>();
        map.put(RemoteUtil.a10th_Objective_Questions, BuildConfig.VERSION_CODE);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(map);
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        DialogShow();
                    }
                });



    }

    private void DialogShow() {

        if (firebaseRemoteConfig.getLong(RemoteUtil.a10th_Objective_Questions) <= BuildConfig.VERSION_CODE) return;

        CustomUpdateDialog dialog = new CustomUpdateDialog(MainActivity.this, firebaseRemoteConfig);
        dialog.show();

    }


    private void loadWebView() {

        webView.loadUrl(webUrl);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
             /*   contentView.setVisibility(View.GONE);
                nocontent.setVisibility(View.VISIBLE);*/
                super.onReceivedError(view, request, error);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                //    Toast.makeText(MainActivity.this, consoleMessage.message(), Toast.LENGTH_SHORT).show();


                if (url.equals("true")) {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    intent.putExtra("links", consoleMessage.message());
                    url = "false";
                    startActivity(intent);


                }

                if (consoleMessage.message().equals("url")) {
                    url = "true";
                }
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    loading.show();
                }
                if (newProgress==100)
                {
                    loading.dismiss();
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

    }


    @Override
    public void onRefresh() {
        webView.reload();
    }


    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //  navigationView.setCheckedItem(R.id.share);

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }



    // Navigation Drawer item click
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.more:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=8534749914765371345"));
                startActivity(intent);
                break;

            case R.id.share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_TEXT,"Download this app using this link..\n\n https://play.google.com/store/apps/details?id="+ getPackageName());
                startActivity(intent1);
                break;

            case R.id.rate:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));

                }catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }

                break;

            case R.id.privacy:
                Intent intent2 = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id"));
                startActivity(intent2);
                break;

            case R.id.exit:
                finishAffinity();
                break;

        }

        return true;
    }

}