package valenet.com.br.gestordeos.os_list;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.List;

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
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;
import xyz.sahildave.widget.SearchViewLayout;

public class OsListActivity extends AppCompatActivity implements OsList.OsListView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_os)
    RecyclerView recyclerViewOs;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.search_view_container)
    SearchViewLayout searchViewContainer;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    ViewGroup layoutErrorConection;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    ViewGroup layoutErrorServer;
    @BindView(R.id.btn_reload)
    AppCompatButton btnReload;
    @BindView(R.id.layout_empty_list)
    ViewGroup layoutEmptyList;

    private OsList.OsListPresenter presenter;

    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private Location myLocation;

    private ArrayList<Os> filtredList;
    private ArrayList<Os> osList;

    private OsItemAdapter adapter;

    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private Integer osType;
    private boolean proximidade = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_list));

        osType = getIntent().getIntExtra(ValenetUtils.KEY_OS_TYPE, 0);

        this.presenter = new OsListPresenterImp(this);

        searchViewContainer.handleToolbarAnimation(toolbar);
        searchViewContainer.setHint("Buscar por Cliente");
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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(myLocation != null)
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(),
                            proximidade,
                            osType, true);
                else {
                    presenter.loadOsList(1.1, 1.1,
                            LoginLocal.getInstance().getCurrentUser().getCoduser(),
                            proximidade,
                            osType, true);
                }
            }
        });

        filtredList = new ArrayList<>();

        osList = new ArrayList<>();

        this.showLoading();
        RxPermissions.getInstance(OsListActivity.this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(OsListActivity.this);
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
                                                    status.startResolutionForResult(OsListActivity.this, REQUEST_CHECK_SETTINGS);
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
                                                presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(),
                                                        proximidade,
                                                        osType, false);
                                                return true;
                                            } else {
                                                presenter.loadOsList(1.1, 1.1,
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(),
                                                        false,
                                                        osType, false);
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
                            Toasty.error(OsListActivity.this, "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                        }
                        return null;
                    }
                }).subscribe();
    }

    @Override
    public void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST, osList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, filtredList);
        intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
        startActivityForResult(intent, REQ_CODE_SEARCH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SEARCH) {
            if (resultCode == RESULT_CODE_BACK_SEARCH) {
                if (filtredList == null || filtredList.size() == 0)
                    adapter = new OsItemAdapter(osList, this, this, myLocation);
                else
                    adapter = new OsItemAdapter(filtredList, this, this, myLocation);
                this.recyclerViewOs.setAdapter(adapter);

                searchViewContainer.collapse();
            }
        }

    }

    @Override
    public void hideOsListView() {
        this.refreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void showOsListView() {
        this.refreshLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        else
            this.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void hideErrorServerView() {
        layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyListView() {
        layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListView() {
        layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void showListOs(List<Os> osListAdapter) {
        this.osList = (ArrayList) osListAdapter;
        adapter = new OsItemAdapter(osListAdapter, this, this, myLocation);
        recyclerViewOs.setAdapter(adapter);
        recyclerViewOs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOs.setItemAnimator(new DefaultItemAnimator());
    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                presenter.loadOsList(1.1, 1.1,
                        LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        proximidade,
                        osType, false);
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
