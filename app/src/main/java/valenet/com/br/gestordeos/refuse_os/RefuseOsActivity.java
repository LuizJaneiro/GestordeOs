package valenet.com.br.gestordeos.refuse_os;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class RefuseOsActivity extends AppCompatActivity implements RefuseOs.RefuseOsView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_text_refuse_os_reason)
    AppCompatSpinner editTextRefuseOsReasonSpinner;
    @BindView(R.id.edit_text_refuse_os_observation)
    EditText editTextRefuseOsObservation;
    @BindView(R.id.btn_send_os_reasons)
    AppCompatButton btnSendOsReasons;
    @BindView(R.id.layout_refuse_os)
    NestedScrollView layoutRefuseOs;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.text_view_error_internet_connection)
    TextView textViewErrorInternetConnection;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.text_view_error_from_server)
    TextView textViewErrorFromServer;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    RelativeLayout layoutErrorServer;

    private int osId;
    private SpinnerAdapterOsReasons adapterOsReasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse_os);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        osId = getIntent().getIntExtra(ValenetUtils.KEY_OS_ID, 0);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_refuse_os));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String[] spinnerItems = new String[]{
                "Assunto",
                "Assunto 1",
                "Assunto 2",
                "Assunto 3",

        };

        adapterOsReasons = new SpinnerAdapterOsReasons(RefuseOsActivity.this, R.layout.refuse_os_spinner_layout,
                                                            new ArrayList<String>(Arrays.asList(spinnerItems)));
        adapterOsReasons.setDropDownViewResource(R.layout.refuse_os_spinner_layout);
        editTextRefuseOsReasonSpinner.setAdapter(adapterOsReasons);

        editTextRefuseOsReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(editTextRefuseOsReasonSpinner.getSelectedItem() == "Motivo")){
                    //TODO: item selecionado
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    public void hideRefuseOsView() {
        layoutRefuseOs.setVisibility(View.GONE);
    }

    @Override
    public void showRefuseOsView() {
        layoutRefuseOs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorConectionView() {
        layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorConectionView() {
        layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void showErrorServerView() {
        layoutErrorServer.setVisibility(View.VISIBLE);
        textViewErrorFromServer.setText("Ocorreu um problema no carregamento\\n da lista de motivos!");
    }

    @Override
    public void hideErrorServerView() {
        layoutErrorServer.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_send_os_reasons, R.id.btn_try_again, R.id.btn_try_again_server_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_os_reasons:
                break;
            case R.id.btn_try_again:
                break;
            case R.id.btn_try_again_server_error:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
