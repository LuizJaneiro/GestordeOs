package valenet.com.br.gestordeos.client;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.end_os.EndOsActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.refuse_os.RefuseOsActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ClientActivity extends AppCompatActivity {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar_basic)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.text_view_client_name_toolbar)
    TextView textViewClientNameToolbar;
    @BindView(R.id.text_view_os_type_toolbar)
    TextView textViewOsTypeToolbar;
    @BindView(R.id.text_view_os_date_toolbar)
    TextView textViewOsDateToolbar;
    @BindView(R.id.text_view_distance_toolbar)
    TextView textViewDistanceToolbar;
    @BindView(R.id.footer_layout)
    RelativeLayout footerLayout;
    @BindView(R.id.text_view_os_status_toolbar)
    TextView textViewOsStatusToolbar;
    @BindView(R.id.btn_checkout)
    ImageButton btnCheckout;
    @BindView(R.id.btn_checkin)
    ImageButton btnCheckin;
    @BindView(R.id.btn_call)
    ImageButton btnCall;
    @BindView(R.id.btn_confirm)
    ImageButton btnConfirm;
    @BindView(R.id.btn_nav)
    ImageButton btnNav;

    private PagerAdapter pagerAdapter;
    private Os os;
    private Boolean cameFromHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        os = getIntent().getParcelableExtra(ValenetUtils.KEY_OS);
        cameFromHistory = getIntent().getBooleanExtra(ValenetUtils.KEY_CAME_FROM_OS_HISTORY, false);

        Integer codigoOs = os.getOsid();

        if (codigoOs == null)
            textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_client));
        else
            textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_client) + " - " + codigoOs);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String clientName;
        String osType;
        String distance;
        String dateString = "";
        String city;
        String address;

        if (os.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(os.getCliente());

        if (os.getCidade() == null)
            city = "Cidade Indefinida";
        else
            city = os.getCidade();

        address = ValenetUtils.buildOsAddress(os.getTpLogradouro(), os.getLogradouro(), os.getComplemento(), os.getNumero(), os.getAndar(), os.getBairro());

        if (os.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = os.getTipoAtividade();

        if (os.getDistance() == null)
            distance = "?";
        else {
            double distanceDouble = os.getDistance() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if (distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if (os.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(os.getDataAgendamento()) + " - " + ValenetUtils.convertJsonToStringHour(os.getDataAgendamento());
        }

        textViewClientNameToolbar.setText(clientName);
        textViewDistanceToolbar.setText(distance + " KM");
        textViewOsTypeToolbar.setText(osType);
        textViewOsDateToolbar.setText(dateString);
/*        textViewOsAddressToolbar.setText(address);
        textViewOsAddressToolbar.setSelected(true);*/

        if (cameFromHistory) {
            textViewDistanceToolbar.setVisibility(View.GONE);
            if (os.getStatusOs() != null) {
                textViewOsStatusToolbar.setText(os.getStatusOs());
                textViewOsStatusToolbar.setVisibility(View.VISIBLE);
            }
        }

        tabLayout.addTab(tabLayout.newTab().setText("OS"));
        tabLayout.addTab(tabLayout.newTab().setText("Cliente"));
        tabLayout.addTab(tabLayout.newTab().setText("Observações"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (os.getTelefoneCliente() == null) {
            btnCall.setEnabled(false);
            btnNav.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_call_transparent));
        }

        if (os.getLatitude() == null || os.getLongitude() == null) {
            btnNav.setEnabled(false);
            btnNav.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_nav_transparent));
        }

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), os, tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!cameFromHistory) {
            inflater.inflate(R.menu.menu_os_options, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem os) {
        switch (os.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_os_refuse:
                navigateToRefuseOsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(os);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ValenetUtils.REQUEST_CODE_CLIENT) {
            if (resultCode == Activity.RESULT_OK) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    public void navigateToRefuseOsActivity() {
        Intent intent = new Intent(this, RefuseOsActivity.class);
        intent.putExtra(ValenetUtils.KEY_OS_ID, os.getAgendaEventoID());
        startActivityForResult(intent, ValenetUtils.REQUEST_CODE_CLIENT);
    }

    public void navigateToEndOsActivity() {
        Intent intent = new Intent(this, EndOsActivity.class);
        intent.putExtra(ValenetUtils.KEY_OS_ID, os.getOsid());
        startActivity(intent);
    }

    public void callPhone(final String phone) {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar a ligação?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent;
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                // intent.setData(Uri.parse("tel:" + "03131975107000"));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (ContextCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(ClientActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                    }
                else
                    startActivity(intent);

            }
        });
        builderCad.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

    public void navigateToMap() {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar a navegação?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent;
                Uri gmmIntentUri = Uri.parse("geo:" + os.getLatitude()
                        + "," + os.getLongitude()
                        + "?q=" + os.getLatitude()
                        + "," + os.getLongitude() + "(" + os.getCliente() + ")");
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    if (ContextCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (intent.resolveActivity(getPackageManager()) != null)
                            startActivity(intent);
                    } else {
                        ActivityCompat.requestPermissions(ClientActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    }
                else if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);

            }
        });
        builderCad.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @OnClick({R.id.btn_checkout, R.id.btn_checkin, R.id.btn_call, R.id.btn_confirm, R.id.btn_nav})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                //TODO: Logica do Checkout
                break;
            case R.id.btn_checkin:
                //TODO: Logica do Checkin
                break;
            case R.id.btn_call:
                this.callPhone(this.os.getTelefoneCliente() + "");
                break;
            case R.id.btn_confirm:
                navigateToEndOsActivity();
                break;
            case R.id.btn_nav:
                navigateToMap();
                break;
        }
    }
}
