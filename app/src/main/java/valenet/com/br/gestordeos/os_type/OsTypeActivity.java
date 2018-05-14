package valenet.com.br.gestordeos.os_type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.login.LoginActivity;
import valenet.com.br.gestordeos.map.MapsActivity;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsTypeActivity extends AppCompatActivity implements OsType.OsTypeView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_mercantil)
    AppCompatButton btnMercantil;
    @BindView(R.id.btn_corretiva)
    AppCompatButton btnCorretiva;
    @BindView(R.id.btn_sair)
    AppCompatButton btnSair;
    @BindView(R.id.os_type_view)
    ViewGroup osTypeView;
    @BindView(R.id.loading_view)
    ViewGroup loadingView;

    private OsType.OsTypePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_type);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewToolbarTitle.setText(getString(R.string.title_activity_os_type));

        this.presenter = new OsTypePresenterImp(this);

        ClickGuard.guard(btnCorretiva);
        ClickGuard.guard(btnMercantil);
        ClickGuard.guard(btnSair);
    }

    @OnClick({R.id.btn_mercantil, R.id.btn_corretiva, R.id.btn_sair})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mercantil:
                navigateToOsList(ValenetUtils.GROUP_OS_MERCANTIL);
                break;
            case R.id.btn_corretiva:
                navigateToOsList(ValenetUtils.GROUP_OS_CORRETIVA);
                break;
            case R.id.btn_sair:
                presenter.logout();
                break;
        }
    }

    @Override
    public void navigateToOsList(int osType) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(ValenetUtils.KEY_OS_TYPE, osType);
        startActivity(intent);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        if(loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOsListView() {
        if(osTypeView != null)
            osTypeView.setVisibility(View.GONE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
