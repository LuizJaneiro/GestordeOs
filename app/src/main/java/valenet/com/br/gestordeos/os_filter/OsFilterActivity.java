package valenet.com.br.gestordeos.os_filter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsFilterActivity extends AppCompatActivity implements OsFilter.OsFilterView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_distance)
    AppCompatButton btnDistance;
    @BindView(R.id.btn_name)
    AppCompatButton btnName;
    @BindView(R.id.btn_date)
    AppCompatButton btnDate;
    @BindView(R.id.layout_os_filter)
    ViewGroup layoutOsFilter;
    @BindView(R.id.btn_reload)
    AppCompatButton btnReload;
    @BindView(R.id.layout_empty_list)
    RelativeLayout layoutEmptyList;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    RelativeLayout layoutErrorServer;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.recycler_btn_filters)
    RecyclerView recyclerBtnFilters;

    private final static int REQUEST_CHECK_SETTINGS = 0;
    ReactiveLocationProvider locationProvider;

    private Subscription locationSubscription;

    private HashMap<String, Boolean> selectedButtons;
    private HashMap<String, AppCompatButton> myButtons;
    private int osType;

    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;

    private ArrayList<Os> filtredList;
    private ArrayList<Os> osList;
    private ArrayList<OsTypeModel> osTypeModelList;
    private Location myLocation;
    private OsFilter.OsFilterPresenter presenter;

    private OsFilter.OsFilterView.selectedFiltersListener selectedFiltersListener;

    private boolean loadOsList = false;

    private OsTypeAdapter osTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_filter);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.presenter = new OsFilterPresenterImp(this);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_filter));

        this.selectedButtons = new HashMap<>();
        this.myButtons = new HashMap<>();

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true));
        this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));
        this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false));

        this.myButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, btnDistance);
        this.myButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME, btnName);
        this.myButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE, btnDate);

        renderButtons(this.selectedButtons, this.myButtons);

        filtredList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST);
        osList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST);
        myLocation = getIntent().getParcelableExtra(ValenetUtils.KEY_USER_LOCATION);
        osType = getIntent().getIntExtra(ValenetUtils.KEY_OS_TYPE, 0);

        this.osTypeModelList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerBtnFilters.setLayoutManager(gridLayoutManager);
        if(osTypeModelList != null && osTypeModelList.size() > 0){
            osTypeAdapter = new OsTypeAdapter(this, osTypeModelList, this.osList, osType);
            recyclerBtnFilters.setAdapter(osTypeAdapter);
            this.selectedFiltersListener = osTypeAdapter;
        }

        //dff

        if (osList == null || osList.size() == 0 || osTypeModelList == null || osTypeModelList.size() == 0)
            loadOsList = true;

        this.hideFilterView();
        this.showLoading();

        if(myLocation == null) {
            RxPermissions.getInstance(OsFilterActivity.this)
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .map(new Func1<Boolean, Object>() {
                        @Override
                        public Object call(Boolean aBoolean) {
                            if (aBoolean) {
                                final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(OsFilterActivity.this);
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
                                                        status.startResolutionForResult(OsFilterActivity.this, REQUEST_CHECK_SETTINGS);
                                                    } catch (IntentSender.SendIntentException th) {
                                                        Log.e("MainActivity", "Error opening settings activity.", th);
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
                                                    presenter.loadOsListAndOsTypes(myLocation.getLatitude(), myLocation.getLongitude(),
                                                            LoginLocal.getInstance().getCurrentUser().getCoduser(),
                                                            true,
                                                            osType, loadOsList, false);
                                                    return true;
                                                } else {
                                                    presenter.loadOsListAndOsTypes(1.1, 1.1,
                                                            LoginLocal.getInstance().getCurrentUser().getCoduser(),
                                                            false,
                                                            osType, loadOsList, false);
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
                                Toasty.error(OsFilterActivity.this, "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                            }
                            return null;
                        }
                    }).subscribe();
        }

        if (loadOsList) {
            if (myLocation != null) {
                presenter.loadOsListAndOsTypes(myLocation.getLatitude(), myLocation.getLongitude(),
                        LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        true,
                        osType, loadOsList, false);
            } else {
                presenter.loadOsListAndOsTypes(1.1, 1.1,
                        LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        false,
                        osType, loadOsList, false);
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    private void renderButtons(HashMap<String, Boolean> selectedButtons, HashMap<String, AppCompatButton> buttons) {
        Set<String> keys = selectedButtons.keySet();
        for (String key : keys) {
            if (key != null) {
                Boolean isSelected = selectedButtons.get(key);
                AppCompatButton button = buttons.get(key);
                final int sdk = Build.VERSION.SDK_INT;
                if (isSelected) {
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_btn_blue_fill));
                    else
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.background_btn_blue_fill));
                    button.setTextColor(getResources().getColor(R.color.btn_filter_text_color_selected));
                } else {
                    if (sdk < Build.VERSION_CODES.JELLY_BEAN)
                        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_btn_blue));
                    else
                        button.setBackground(ContextCompat.getDrawable(this, R.drawable.background_btn_blue));
                    button.setTextColor(getResources().getColor(R.color.text_btn));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        Set<String> keys = selectedButtons.keySet();
        for (String key : keys) {
            if (key != null) {
                Boolean isSelected = selectedButtons.get(key);
                final int sdk = Build.VERSION.SDK_INT;
                if (isSelected) {
                    orderBySelectedFilter(key);
                }
            }
        }
        resultIntent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, filtredList);
        setResult(REQ_CODE_BACK_FILTER, resultIntent);
        finish();
    }

    @OnClick({R.id.btn_distance, R.id.btn_name, R.id.btn_date})
    public void onViewClicked(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (view.getId()) {
            case R.id.btn_distance:
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);
                editor.apply();

                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);

                renderButtons(this.selectedButtons, this.myButtons);
                this.orderBySelectedFilter(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE);
                break;
            case R.id.btn_name:
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, true);
                editor.apply();

                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME, true);

                renderButtons(this.selectedButtons, this.myButtons);
                orderBySelectedFilter(ValenetUtils.SHARED_PREF_KEY_OS_NAME);
                break;
            case R.id.btn_date:
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, true);
                editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);
                editor.apply();

                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE, true);
                this.selectedButtons.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);

                renderButtons(this.selectedButtons, this.myButtons);
                orderBySelectedFilter(ValenetUtils.SHARED_PREF_KEY_OS_DATE);
                break;
        }
    }

    @Override
    public void hideFilterView() {
        this.layoutOsFilter.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorConectionView() {
        this.layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorServerView() {
        this.layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyListView() {
        this.layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFilterView() {
        this.layoutOsFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorConectionView() {
        this.layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorServerView() {
        this.layoutErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyListView() {
        this.layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void loadListOs(List<Os> list) {
        this.osList = (ArrayList) list;
    }

    @Override
    public void loadOsTypesList(List<OsTypeModel> osTypes) {
        this.osTypeModelList = (ArrayList) osTypes;
        osTypeAdapter = new OsTypeAdapter(this, osTypeModelList, this.osList, osType);
        recyclerBtnFilters.setAdapter(osTypeAdapter);
        this.selectedFiltersListener = osTypeAdapter;
    }

    private void orderBySelectedFilter(String selectedFilter) {
        if (filtredList == null || filtredList.size() == 0)
            filtredList = new ArrayList<>();

        this.filtredList = (ArrayList) this.selectedFiltersListener.filterList();

        if (selectedFilter.equals(ValenetUtils.SHARED_PREF_KEY_OS_NAME)) {
            Collections.sort(filtredList, new Comparator<Os>() {
                @Override
                public int compare(Os o1, Os o2) {
                    return o1.getCliente().compareTo(o2.getCliente());
                }
            });
        }

        if (selectedFilter.equals(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE)) {
            Collections.sort(filtredList, new Comparator<Os>() {
                @Override
                public int compare(Os o1, Os o2) {
                    Double distance1, distance2;
                    if (o1.getLongitude() == null || o1.getLatitude() == null)
                        distance1 = Double.MAX_VALUE;
                    else {
                        Location location1 = new Location("");
                        location1.setLatitude(o1.getLatitude());
                        location1.setLongitude(o1.getLongitude());
                        distance1 = (double) myLocation.distanceTo(location1);
                    }

                    if (o2.getLongitude() == null || o2.getLatitude() == null)
                        distance2 = Double.MAX_VALUE;
                    else {
                        Location location2 = new Location("");
                        location2.setLatitude(o2.getLatitude());
                        location2.setLongitude(o2.getLongitude());
                        distance2 = (double) myLocation.distanceTo(location2);
                    }

                    return distance1.compareTo(distance2);
                }
            });
        }

        if (selectedFilter.equals(ValenetUtils.SHARED_PREF_KEY_OS_DATE)) {
            Collections.sort(filtredList, new Comparator<Os>() {
                @Override
                public int compare(Os o1, Os o2) {
                    Date date1, date2;
                    if (o1.getDataAgendamento() == null)
                        date1 = new Date(Long.MAX_VALUE);
                    else {
                        String dateString = ValenetUtils.convertJsonToStringDate(o1.getDataAgendamento());
                        date1 = ValenetUtils.convertStringToDate(dateString);
                    }

                    if (o2.getDataAgendamento() == null)
                        date2 = new Date(Long.MAX_VALUE);
                    else {
                        String dateString = ValenetUtils.convertJsonToStringDate(o2.getDataAgendamento());
                        date2 = ValenetUtils.convertStringToDate(dateString);
                    }

                    return date1.compareTo(date2);
                }
            });
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
