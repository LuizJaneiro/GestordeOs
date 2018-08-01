package valenet.com.br.gestordeos.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.end_os.EndOsActivity;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.refuse_os.RefuseOsActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ClientActivity extends AppCompatActivity implements Client.ClientView {

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
    @BindView(R.id.layout_client)
    LinearLayout layoutClient;
    @BindView(R.id.text_view_loading)
    TextView textViewLoading;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.layout_buttons_schedule)
    RelativeLayout layoutButtonsSchedule;
    @BindView(R.id.btn_pescar)
    ImageButton btnPescar;
    @BindView(R.id.layout_buttons_fishing)
    RelativeLayout layoutButtonsFishing;
    @BindView(R.id.btn_call_pesca)
    ImageButton btnCallPesca;

    private PagerAdapter pagerAdapter;
    private OrdemDeServico ordemDeServico;
    private Boolean cameFromHistory;

    private Client.ClientPresenter presenter;
    private boolean reloadOs = false;

    //Location
    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private boolean cameFromSchedule;

    private Location myLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.presenter = new ClientPresenterImp(this);

        Intent intent = getIntent();
        intent.setExtrasClassLoader(OrdemDeServico.class.getClassLoader());

        ordemDeServico = (OrdemDeServico) intent.getParcelableExtra(ValenetUtils.KEY_OS);
        cameFromSchedule = intent.getBooleanExtra(ValenetUtils.KEY_CAME_FROM_SCHEDULE, false);
        cameFromHistory = intent.getBooleanExtra(ValenetUtils.KEY_CAME_FROM_OS_HISTORY, false);

        if (!cameFromHistory) {
            RxPermissions.getInstance(ClientActivity.this)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .map(new Func1<Boolean, Object>() {
                        @Override
                        public Object call(Boolean aBoolean) {
                            if (aBoolean) {
                                final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(ClientActivity.this);
                                final LocationRequest locationRequest = LocationRequest.create()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setNumUpdates(1)
                                        .setInterval(10000);
                                locationSubscription = locationProvider
                                        .checkLocationSettings(
                                                new LocationSettingsRequest.Builder()
                                                        .addLocationRequest(locationRequest)
                                                        .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                                        .build()
                                        )
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .doOnNext(new Action1<LocationSettingsResult>() {
                                            @Override
                                            public void call(LocationSettingsResult locationSettingsResult) {
                                                Status status = locationSettingsResult.getStatus();
                                                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                                    try {
                                                        status.startResolutionForResult(ClientActivity.this, REQUEST_CHECK_SETTINGS);
                                                    } catch (IntentSender.SendIntentException th) {
                                                        Log.e("ClientActivity", "Error opening settings activity.", th);
                                                    }
                                                }
                                            }
                                        })
                                        .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                                            @SuppressLint("MissingPermission")
                                            @Override
                                            public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                                                //noinspection MissingPermission
                                                return locationProvider.getUpdatedLocation(locationRequest);
                                            }
                                        })
                                        .map(new Func1<Location, Boolean>() {
                                            @Override
                                            public Boolean call(Location location) {
                                                if (location != null) {
                                                    myLocation = location;
                                                    return true;
                                                } else {
                                                    return false;
                                                }
                                            }
                                        })
                                        .subscribe(new Observer<Boolean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(Boolean aBoolean) {

                                            }
                                        });
                            } else {
                                Toasty.error(ClientActivity.this, "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                            }
                            return null;
                        }
                    }).subscribe();
        }

        if (cameFromSchedule || cameFromHistory) {
            if (layoutButtonsSchedule != null && layoutButtonsFishing != null) {
                layoutButtonsFishing.setVisibility(View.GONE);
                layoutButtonsSchedule.setVisibility(View.VISIBLE);
            }
        } else {
            if (layoutButtonsSchedule != null && layoutButtonsFishing != null) {
                layoutButtonsSchedule.setVisibility(View.GONE);
                layoutButtonsFishing.setVisibility(View.VISIBLE);
            }
        }

        if (!cameFromHistory) {
            if (ordemDeServico.getDataCheckin() == null || ordemDeServico.getDataCheckin().length() == 0)
                this.showLayoutOsCanCheckin();
            else
                this.showLayoutOsCheckedIn();
        }

        Integer codigoOs = ordemDeServico.getOsid();

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

        if (ordemDeServico.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(ordemDeServico.getCliente());

        if (ordemDeServico.getCidade() == null)
            city = "Cidade Indefinida";
        else
            city = ordemDeServico.getCidade();

        address = ValenetUtils.buildOsAddress(ordemDeServico.getTpLogradouro(), ordemDeServico.getLogradouro(), ordemDeServico.getComplemento(), ordemDeServico.getNumero(), ordemDeServico.getAndar(), ordemDeServico.getBairro());

        if (ordemDeServico.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = ordemDeServico.getTipoAtividade();

        if (ordemDeServico.getDistance() == null)
            distance = "?";
        else {
            double distanceDouble = ordemDeServico.getDistance() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if (distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if (ordemDeServico.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(ordemDeServico.getDataAgendamento()) + " - " + ValenetUtils.convertJsonToStringHour(ordemDeServico.getDataAgendamento());
        }

        textViewClientNameToolbar.setText(clientName);
        textViewDistanceToolbar.setText(distance + " KM");
        textViewOsTypeToolbar.setText(osType);
        textViewOsDateToolbar.setText(dateString);
/*        textViewOsAddressToolbar.setText(address);
        textViewOsAddressToolbar.setSelected(true);*/

        if (cameFromHistory) {
            textViewDistanceToolbar.setVisibility(View.GONE);
            if (ordemDeServico.getStatusOs() != null) {
                textViewOsStatusToolbar.setText(ordemDeServico.getStatusOs());
                textViewOsStatusToolbar.setVisibility(View.VISIBLE);
            }
            if (btnCheckin != null)
                btnCheckin.setVisibility(View.GONE);
            if (btnCheckout != null) {
                btnCheckout.setVisibility(View.VISIBLE);
                btnCheckout.setEnabled(false);
                btnCheckout.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_checkout_transparent));
            }

            if (btnConfirm != null)
                btnConfirm.setVisibility(View.GONE);
        }

        tabLayout.addTab(tabLayout.newTab().setText("OS"));
        tabLayout.addTab(tabLayout.newTab().setText("Cliente"));
        tabLayout.addTab(tabLayout.newTab().setText("Observações"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        if (ordemDeServico.getTelefoneCliente() == null) {
            btnCall.setEnabled(false);
            btnCall.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_call_transparent));
            btnCallPesca.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_call_transparent));
        }

        if (ordemDeServico.getLatitude() == null || ordemDeServico.getLongitude() == null) {
            btnNav.setEnabled(false);
            btnNav.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_nav_transparent));
        }

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), ordemDeServico, cameFromSchedule, tabLayout.getTabCount());
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
        if (cameFromSchedule && !cameFromHistory) {
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
                reloadOs = true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        if (reloadOs) {
            setResult(Activity.RESULT_OK, resultIntent);
        } else
            setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    public void navigateToRefuseOsActivity() {
        Intent intent = new Intent(this, RefuseOsActivity.class);
        intent.putExtra(ValenetUtils.KEY_OS_ID, ordemDeServico.getAgendaEventoID());
        startActivityForResult(intent, ValenetUtils.REQUEST_CODE_CLIENT);
    }

    public void navigateToEndOsActivity() {
        Intent intent = new Intent(this, EndOsActivity.class);
        intent.putExtra(ValenetUtils.KEY_OS_ID, ordemDeServico.getOsid());
        startActivity(intent);
    }


    @Override
    public void showProgress() {
        if (this.loadingView != null && textViewLoading != null) {
            loadingView.setVisibility(View.VISIBLE);
            textViewLoading.setText("Aguarde um momento ...");
        }
    }

    @Override
    public void hideProgress() {
        if (this.loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLayoutClient() {
        if (this.layoutClient != null)
            layoutClient.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutClient() {
        if (this.layoutClient != null)
            layoutClient.setVisibility(View.GONE);
    }

    @Override
    public void showSuccessCheckin() {
        if (getApplicationContext() != null)
            Toasty.success(getApplicationContext(), "Check-in realizado com sucesso.", Toast.LENGTH_LONG).show();
        ordemDeServico.setDataCheckin("Checkin Realizado");
        this.showLayoutOsCheckedIn();
        reloadOs = true;
    }

    @Override
    public void showErrorCheckin() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar o Check-in, tente novamente!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorInternetCheckin() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar o Check-in, verifique sua conexão com a internet e tente novamente!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessCheckout() {
        if (getApplicationContext() != null)
            Toasty.success(getApplicationContext(), "Check-out realizado com sucesso.", Toast.LENGTH_LONG).show();
        ordemDeServico.setDataCheckout("Checkout realizado");
        this.showLayoutOsCheckedIn();
        reloadOs = true;
    }

    @Override
    public void showErrorCheckout() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar o Check-out, tente novamente!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorInternetCheckout() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar o Check-out, verifique sua conexão com a internet e tente novamente!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessFishing() {
        if (getApplicationContext() != null)
            Toasty.success(getApplicationContext(), "Pesca realizada com sucesso. Por favor verifique sua agenda.", Toast.LENGTH_LONG).show();
        reloadOs = true;
        onBackPressed();
    }

    @Override
    public void showErrorFishing() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar a pesca, tente novamente!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorInternetFishing() {
        if (getApplicationContext() != null)
            Toasty.error(getApplicationContext(), "Ocorreu um problema ao realizar a pesca, verifique sua conexão com a internet e tente novamente!", Toast.LENGTH_LONG).show();
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
                Uri gmmIntentUri = Uri.parse("geo:" + ordemDeServico.getLatitude()
                        + "," + ordemDeServico.getLongitude()
                        + "?q=" + ordemDeServico.getLatitude()
                        + "," + ordemDeServico.getLongitude() + "(" + ordemDeServico.getCliente() + ")");
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

    public void checkin() {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar o Check-in na OS?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginLocal loginLocal = LoginLocal.getInstance();
                Integer codUser = null;
                if (loginLocal != null)
                    codUser = loginLocal.getCurrentUser().getCoduser();
                if (myLocation == null)
                    getLocationAndRequestCheck(true);
                else {
                    if (ordemDeServico.getLongitude() != null && ordemDeServico.getLatitude() != null) {
                        Location osLocation = new Location("");
                        osLocation.setLatitude(osLocation.getLatitude());
                        osLocation.setLongitude(osLocation.getLongitude());
                        if (myLocation.distanceTo(osLocation) > 50)
                            Toasty.error(ClientActivity.this, "Você está muito distante da OS para realizar o check-in!", Toast.LENGTH_LONG, true).show();
                        else
                            presenter.checkin(ordemDeServico.getOsid(), codUser, myLocation.getLatitude(), myLocation.getLongitude());
                    } else     
                        presenter.checkin(ordemDeServico.getOsid(), codUser, myLocation.getLatitude(), myLocation.getLongitude());
                }
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

    public void checkout() {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar o Check-out na OS?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginLocal loginLocal = LoginLocal.getInstance();
                Integer codUser = null;
                if (loginLocal != null)
                    codUser = loginLocal.getCurrentUser().getCoduser();
                if (myLocation == null)
                    getLocationAndRequestCheck(false);
                else if (codUser != null)
                    presenter.checkout(ordemDeServico.getOsid(), codUser, myLocation.getLatitude(), myLocation.getLongitude());
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

    public void pescar() {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja pescar a OS?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginLocal loginLocal = LoginLocal.getInstance();
                if (loginLocal != null)
                    presenter.putScheduleFishEvent(ordemDeServico.getAgendaEventoID(), loginLocal.getCurrentUser().getCoduser());
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

    @OnClick({R.id.btn_checkout, R.id.btn_checkin, R.id.btn_call, R.id.btn_confirm, R.id.btn_nav, R.id.btn_pescar, R.id.btn_call_pesca})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_checkout:
                if (myLocation != null)
                    this.checkout();
                else
                    this.getLocationAndRequestCheck(false);
                break;
            case R.id.btn_checkin:
                if (myLocation != null)
                    this.checkin();
                else
                    this.getLocationAndRequestCheck(true);
                break;
            case R.id.btn_call:
            case R.id.btn_call_pesca:
                this.callPhone(this.ordemDeServico.getTelefoneCliente() + "");
                break;
            case R.id.btn_confirm:
                navigateToEndOsActivity();
                break;
            case R.id.btn_nav:
                navigateToMap();
                break;
            case R.id.btn_pescar:
                pescar();
                break;
        }
    }

    private void showLayoutOsCanCheckin() {
        if (btnCheckin != null && btnCheckout != null) {
            btnCheckout.setVisibility(View.GONE);
            btnCheckin.setVisibility(View.VISIBLE);
        }

        if (btnNav != null && btnConfirm != null) {
            btnConfirm.setVisibility(View.GONE);
            btnNav.setVisibility(View.VISIBLE);
        }
    }

    private void showLayoutOsCheckedIn() {
        if (ordemDeServico.getDataCheckout() == null || ordemDeServico.getDataCheckout().length() == 0)
            showLayoutOsCanCheckOut();
        else
            showLayoutOsCheckedOut();
    }

    private void showLayoutOsCanCheckOut() {
        if (btnCheckin != null && btnCheckout != null) {
            btnCheckin.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.VISIBLE);
        }

        if (btnNav != null && btnConfirm != null) {
            btnNav.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    private void showLayoutOsCheckedOut() {
        if (btnCheckin != null && btnCheckout != null) {
            btnCheckin.setVisibility(View.GONE);
            btnCheckout.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundTintList(getResources().getColorStateList(R.color.selector_color_btn_checkout_transparent));
        }

        if (btnNav != null && btnConfirm != null) {
            btnNav.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    private void getLocationAndRequestCheck(final boolean isCheckin) {
        RxPermissions.getInstance(ClientActivity.this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(ClientActivity.this);
                            final LocationRequest locationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setNumUpdates(1)
                                    .setInterval(10000);
                            locationSubscription = locationProvider
                                    .checkLocationSettings(
                                            new LocationSettingsRequest.Builder()
                                                    .addLocationRequest(locationRequest)
                                                    .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                                    .build()
                                    )
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Action1<LocationSettingsResult>() {
                                        @Override
                                        public void call(LocationSettingsResult locationSettingsResult) {
                                            Status status = locationSettingsResult.getStatus();
                                            if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                                try {
                                                    status.startResolutionForResult(ClientActivity.this, REQUEST_CHECK_SETTINGS);
                                                } catch (IntentSender.SendIntentException th) {
                                                    Log.e("ClientActivity", "Error opening settings activity.", th);
                                                }
                                            }
                                        }
                                    })
                                    .flatMap(new Func1<LocationSettingsResult, Observable<Location>>() {
                                        @SuppressLint("MissingPermission")
                                        @Override
                                        public Observable<Location> call(LocationSettingsResult locationSettingsResult) {
                                            //noinspection MissingPermission
                                            return locationProvider.getUpdatedLocation(locationRequest);
                                        }
                                    })
                                    .map(new Func1<Location, Boolean>() {
                                        @Override
                                        public Boolean call(Location location) {
                                            if (location != null) {
                                                LoginLocal loginLocal = LoginLocal.getInstance();
                                                Integer codUser = null;
                                                if (loginLocal != null)
                                                    codUser = loginLocal.getCurrentUser().getCoduser();
                                                myLocation = location;
                                                if (isCheckin) {
                                                    if (codUser != null)
                                                        presenter.checkin(ordemDeServico.getOsid(), codUser, myLocation.getLatitude(), myLocation.getLongitude());
                                                } else {
                                                    if (codUser != null)
                                                        presenter.checkout(ordemDeServico.getOsid(), codUser, myLocation.getLatitude(), myLocation.getLongitude());
                                                }
                                                return true;
                                            } else {
                                                Toasty.error(ClientActivity.this, "Ocorreu um problema ao tentar conseguir sua localização. Tente novamente!", Toast.LENGTH_LONG, true).show();
                                                return false;
                                            }
                                        }
                                    })
                                    .subscribe(new Observer<Boolean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Boolean aBoolean) {

                                        }
                                    });
                        } else {
                            Toasty.error(ClientActivity.this, "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                        }
                        return null;
                    }
                }).subscribe();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
