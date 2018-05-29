package valenet.com.br.gestordeos.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.client.ClientActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.os_filter.OsFilterActivity;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;
import xyz.sahildave.widget.SearchViewLayout;

public class MapsActivity extends AppCompatActivity implements Maps.MapsView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.layout_maps)
    ViewGroup layoutMaps;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.lottie_animation_loading)
    LottieAnimationView lottieAnimationLoading;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    RelativeLayout layoutErrorServer;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.toolbar_basic)
    Toolbar toolbar;
    @BindView(R.id.search_view_container)
    SearchViewLayout searchViewContainer;
    @BindView(R.id.toolbar_searchable_without_tabs_container)
    LinearLayout toolbarSearchableWithoutTabsContainer;

    private final int CODE_MAP = 1000;
    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;

    private GoogleMap mMap;
    private float zoom = 13.5649395f;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private Subscription locationSubscription;
    private Location myLocation;

    Integer osType = 1;

    private final static int REQUEST_CHECK_SETTINGS = 0;

    private Maps.MapsPresenter presenter;

    private ArrayList<Os> osArrayList;
    private ArrayList<Os> filtredOsArrayList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;
    private boolean loadUserLocation = false;
    private boolean loadOsArrayList = false;
    private boolean loadOsTypeModel = false;

    private HashMap<String, Boolean> filters;
    private HashMap<Integer, Integer> osDistanceHashMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.presenter = new MapsPresenterImp(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbarSearchableWithoutTabsContainer.setVisibility(View.VISIBLE);
        textViewToolbarTitle.setText(getString(R.string.title_activity_map));

        osArrayList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST);
        osTypeModelArrayList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST);
        myLocation = getIntent().getParcelableExtra(ValenetUtils.KEY_USER_LOCATION);
        osDistanceHashMap = (HashMap<Integer, Integer>) getIntent().getSerializableExtra(ValenetUtils.KEY_OS_DISTANCE_HASHMAP);

        if (osArrayList == null || osArrayList.size() == 0)
            loadOsArrayList = true;

        if (osTypeModelArrayList == null || osTypeModelArrayList.size() == 0)
            loadOsTypeModel = true;

        if (myLocation == null)
            loadUserLocation = true;

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
        this.filters = new HashMap<>();
        if (!loadOsTypeModel) {
            for (OsTypeModel model : osTypeModelArrayList) {
                this.filters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }

        searchViewContainer.handleToolbarAnimation(toolbar);
        searchViewContainer.setHint("Buscar por Os (Id, Tipo ou Cliente)");
        ColorDrawable collapsed = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary));
        ColorDrawable expanded = new ColorDrawable(ContextCompat.getColor(this, R.color.default_color_expanded));
        searchViewContainer.setTransitionDrawables(collapsed, expanded);
        searchViewContainer.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                searchViewContainer.collapse();
            }
        });

        searchViewContainer.setOnToggleAnimationListener(new SearchViewLayout.OnToggleAnimationListener() {
            @Override
            public void onStart(boolean expanded) {
                if (expanded) {
                    navigateToSearch();
                } else {
                    //fab.show();
                }
            }

            @Override
            public void onFinish(boolean expanded) {
            }
        });

        final View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 280, 180, 0);

        View zoomControls = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("1"));

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            params.addRule(RelativeLayout.BELOW, 0x2);
            params.setMargins(0, 180, 180, 0);
        }


        hideMapsView();
        showProgress();

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent i = new Intent(MapsActivity.this, ClientActivity.class);
                        i.putExtra(ValenetUtils.KEY_OS, (Os) marker.getTag());
                        startActivity(i);
                    }
                });

                mMap.getUiSettings().setZoomControlsEnabled(true);

                RxPermissions.getInstance(MapsActivity.this)
                        .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .map(new Func1<Boolean, Object>() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public Object call(Boolean granted) {
                                if (granted) {
                                    //noinspection MissingPermission
                                    mMap.setMyLocationEnabled(true);
                                    final LocationRequest locationRequest = LocationRequest.create()
                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setNumUpdates(2)
                                            .setInterval(10000);
                                    final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(MapsActivity.this);
                                    locationSubscription = locationProvider
                                            .checkLocationSettings(
                                                    new LocationSettingsRequest.Builder()
                                                            .addLocationRequest(locationRequest)
                                                            .setAlwaysShow(true)  //Refrence: http://stackoverflow.com/questions/29824408/google-play-services-locationservices-api-new-option-never
                                                            .build()
                                            )
                                            .doOnNext(new Action1<LocationSettingsResult>() {
                                                @Override
                                                public void call(LocationSettingsResult locationSettingsResult) {
                                                    Status status = locationSettingsResult.getStatus();
                                                    if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                                        try {
                                                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                                                        } catch (IntentSender.SendIntentException th) {
                                                            Log.e("MainActivity", "Error opening settings activity_select_item.", th);
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
                                                        myLocation = new Location(location);
                                                        mMap.setInfoWindowAdapter(new CustomWindow(MapsActivity.this, myLocation, osDistanceHashMap));
                                                        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom));
                                                        if (loadOsArrayList) {
                                                            presenter.loadScheduleOsList(location.getLatitude(), location.getLongitude(),
                                                                    LoginLocal.getInstance().getCurrentUser().getCoduser(), osType);
                                                        } else {
                                                            filterList(osArrayList);
                                                        }
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
                                    Toasty.error(MapsActivity.this, "Erro ao conseguir permissões", Toast.LENGTH_SHORT, true).show();
                                }
                                return null;
                            }
                        })
                        .subscribe();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_os_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.menu_filter) {
            navigateToFilter();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SEARCH) {
            if (resultCode == RESULT_CODE_BACK_SEARCH) {
                searchViewContainer.collapse();
            }
        }

        if (requestCode == REQ_CODE_FILTER) {
            if (resultCode == REQ_CODE_BACK_FILTER) {
                filterList(this.osArrayList);
                Toasty.success(getApplicationContext(), "Filtros aplicados com sucesso", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == CODE_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    boolean result = data.getBooleanExtra("result", false);
                    if (result) {
                        finish();
                    }
                }
            }
        }
    }

    @Override
    public void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, filtredOsArrayList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, osTypeModelArrayList);
        intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
        intent.putExtra(ValenetUtils.KEY_OS_DISTANCE_HASHMAP, osDistanceHashMap);
        startActivityForResult(intent, REQ_CODE_SEARCH);
    }

    @Override
    public void navigateToFilter() {
        Intent intent = new Intent(this, OsFilterActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, this.osTypeModelArrayList);
        intent.putExtra(ValenetUtils.KEY_CAME_FROM_MAPS, true);
        startActivityForResult(intent, REQ_CODE_FILTER);
    }

    @Override
    public void showProgress() {
        if (loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showMapsView() {
        if (layoutMaps != null) {
            layoutMaps.setVisibility(View.VISIBLE);
            if (mMap != null && myLocation != null) {
                LatLng point = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom));
            }
        }
    }

    @Override
    public void hideMapsView() {
        if (layoutMaps != null)
            layoutMaps.setVisibility(View.GONE);
    }

    @Override
    public void showErrorConnectionView() {
        if (layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorConnectionView() {
        if (layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void showErrorServerView() {
        if (layoutErrorServer != null)
            layoutErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorServerView() {
        if (layoutErrorServer != null)
            layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void loadOsList(ArrayList<Os> osArrayList) {
        this.osArrayList = new ArrayList<>();
        this.osArrayList = osArrayList;
        filterList(osArrayList);
    }

    @Override
    public void loadScheduleOsList(ArrayList<Os> osArrayList) {
        this.osArrayList = new ArrayList<>();
        this.osArrayList = osArrayList;
        filterList(osArrayList);
    }

    @Override
    public void loadOsTypesList(ArrayList<OsTypeModel> osTypeModels) {
        this.osTypeModelArrayList = new ArrayList<>();
        this.osTypeModelArrayList = osTypeModels;

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        if (osTypeModels != null && osTypeModels.size() > 0) {
            for (OsTypeModel model : osTypeModels) {
                this.filters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }
    }

    @Override
    public void addedOsMarkers(ArrayList<Os> osArrayList) {
        if (mMap != null)
            mMap.clear();
        loadOsArrayList = false;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
        if (osArrayList != null && osArrayList.size() > 0) {
            for (int i = 0; i < osArrayList.size(); i++) {
                Os os = osArrayList.get(i);
                Location location = new Location("");
                if (os.getLongitude() != null && os.getLatitude() != null) {
                    location.setLatitude(os.getLatitude());
                    location.setLongitude(os.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.icon(icon);
                    Marker marker = mMap.addMarker(markerOptions);
                    marker.setTag(os);
                }
            }
        }
    }

    public void filterList(ArrayList<Os> osArrayList) {
        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        if (this.osTypeModelArrayList != null && osTypeModelArrayList.size() > 0) {
            for (OsTypeModel model : this.osTypeModelArrayList) {
                this.filters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }

        this.filtredOsArrayList = new ArrayList<>();
        if (this.filters != null && osArrayList != null && osArrayList.size() > 0) {
            Set<String> keys = this.filters.keySet();
            for (String key : keys) {
                boolean isSelected = this.filters.get(key);
                if (isSelected) {
                    for (Os os : osArrayList) {
                        String osTipoAtividade = ValenetUtils.removeAccent(os.getTipoAtividade()).toUpperCase();
                        String keyTratada = ValenetUtils.removeAccent(key).toUpperCase();
                        if (osTipoAtividade.equals(keyTratada)) {
                            this.filtredOsArrayList.add(os);
                        }
                    }
                }
            }
        }

        addedOsMarkers(this.filtredOsArrayList);
        showMapsView();
    }

    @OnClick({R.id.btn_try_again_server_error, R.id.btn_try_again})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again_server_error:
            case R.id.btn_try_again:
                presenter.loadScheduleOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                        LoginLocal.getInstance().getCurrentUser().getCoduser(), osType);
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
