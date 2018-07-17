package valenet.com.br.gestordeos.end_os;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class EndOsActivity extends AppCompatActivity {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar_basic)
    Toolbar toolbar;
    @BindView(R.id.web_view_progressBar)
    ProgressBar webViewProgressBar;
    @BindView(R.id.webview)
    WebView webview;

    private int osId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_os);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_end_os));

        osId = getIntent().getIntExtra(ValenetUtils.KEY_OS_ID, 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        webViewProgressBar.setVisibility(View.VISIBLE);
        webview.setVisibility(View.INVISIBLE);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webview.loadUrl("https://os.valenet.com.br/csc/os/?id=" + osId + "&coduser=" + LoginLocal.getInstance().getCurrentUser().getCoduser());
        webViewProgressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("onPageFinished", url);

                webViewProgressBar.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                android.app.AlertDialog.Builder builderCad;
                builderCad = new android.app.AlertDialog.Builder(EndOsActivity.this);
                builderCad.setTitle("Atenção");
                builderCad.setMessage("Navegação insegura, deseja continuar?");
                builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handler.proceed();
                    }
                });
                builderCad.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        handler.cancel();
                    }
                });
                final android.app.AlertDialog dialog = builderCad.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.btn_negative_dialog));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.btn_positive_dialog));
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem os) {
        switch (os.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(os);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
