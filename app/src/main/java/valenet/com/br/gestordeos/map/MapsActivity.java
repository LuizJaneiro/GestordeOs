package valenet.com.br.gestordeos.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import java.util.Locale;

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
import valenet.com.br.gestordeos.os_list.OsListActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class MapsActivity extends AppCompatActivity implements Maps.MapsView {

    private final int CODE_MAP = 1000;

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_adress_search)
    ViewGroup search;
    @BindView(R.id.text_view_search)
    TextView textViewSearch;
    @BindView(R.id.layout_maps)
    FrameLayout layoutMaps;
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

    private GoogleMap mMap;
    private float zoom = 13.5649395f;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private Subscription locationSubscription;
    private Location myLocation;

    int osType;

    private final static int REQUEST_CHECK_SETTINGS = 0;
    private boolean alreadyLoadedOsList = false;

    private Maps.MapsPresenter presenter;

    private ArrayList<Os> osArrayList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;

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

        osType = getIntent().getIntExtra(ValenetUtils.KEY_OS_TYPE, 0);

        textViewToolbarTitle.setText(getString(R.string.title_activity_map));

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
                search.setClickable(true);
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent =
                                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                            .build(MapsActivity.this);
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();

                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                        }
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
                                                        mMap.setInfoWindowAdapter(new CustomWindow(MapsActivity.this, myLocation));
                                                        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom));
                                                        if (!alreadyLoadedOsList)
                                                            presenter.loadOsList(location.getLatitude(), location.getLongitude(),
                                                                    LoginLocal.getInstance().getCurrentUser().getCoduser(), osType);
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
        if (item.getItemId() == R.id.menu_list) {
            Intent i = new Intent(this, OsListActivity.class);
            i.putExtra(ValenetUtils.KEY_OS_TYPE, osType);
            startActivityForResult(i, CODE_MAP);
            return true;
        }

        if (item.getItemId() == R.id.menu_filter) {
            //TODO: NAVIGATE TO FILTER
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MapsActivity.this, data);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), zoom));
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            }
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(MapsActivity.this, data);

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
    }

    @Override
    public void loadOsTypesList(ArrayList<OsTypeModel> osTypeModels) {
        this.osTypeModelArrayList = new ArrayList<>();
        this.osTypeModelArrayList = osTypeModels;
    }

    @Override
    public void addedOsMarkers(ArrayList<Os> osArrayList) {
        alreadyLoadedOsList = true;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker);
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

    @OnClick({R.id.btn_try_again_server_error, R.id.layout_error_conection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again_server_error:
                break;
            case R.id.layout_error_conection:
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
