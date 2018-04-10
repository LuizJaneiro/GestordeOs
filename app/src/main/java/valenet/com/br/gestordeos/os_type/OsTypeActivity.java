package valenet.com.br.gestordeos.os_type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.os_list.OsListActivity;

public class OsTypeActivity extends AppCompatActivity implements OsType.OsTypeView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_mercantil)
    AppCompatButton btnMercantil;
    @BindView(R.id.btn_assistencia_tecnica)
    AppCompatButton btnAssistenciaTecnica;
    @BindView(R.id.btn_sair)
    AppCompatButton btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_type);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        textViewToolbarTitle.setText(getString(R.string.title_activity_os_type));
    }

    @OnClick({R.id.btn_mercantil, R.id.btn_assistencia_tecnica, R.id.btn_sair})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_mercantil:
                navigateToOsList();
                break;
            case R.id.btn_assistencia_tecnica:
                break;
            case R.id.btn_sair:
                break;
        }
    }

    @Override
    public void navigateToOsList() {
        Intent intent = new Intent(this, OsListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
