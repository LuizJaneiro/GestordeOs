package valenet.com.br.gestordeos.os_list;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
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
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.os_filter.OsFilterActivity;
import valenet.com.br.gestordeos.os_list.OsFragments.NextOsFragment;
import valenet.com.br.gestordeos.os_list.OsFragments.ScheduleOsFragment;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;
import xyz.sahildave.widget.SearchViewLayout;

public class OsListActivity extends AppCompatActivity implements OsList.OsListView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.os_list_view)
    ViewGroup osListView;

    private OsList.OsListPresenter presenter;

    private OsListPagerAdapter pagerAdapter;

    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private Location myLocation;

    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;
    private Integer osType;
    private navigateInterface navigateInterface;
    private onActivityResultNextOs onActivityResultNextOs;
    private onActivityResultScheduleOs onActivityResultScheduleOs;
    private ArrayList<Os> scheduleOsList;
    private ArrayList<Os> nextOsList;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> filters;

    private ArrayList<OsTypeModel> osTypeModelList;

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

        tabLayout.addTab(tabLayout.newTab().setText("Próximas"));
        tabLayout.addTab(tabLayout.newTab().setText("Agendadas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        this.orderFilters = new HashMap<>();

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false));

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
                                            presenter.loadOsTypes(osType);
                                            if (location != null) {
                                                myLocation = location;
                                                return true;
                                            }else {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_os_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_map) {
            //TODO navigateToMap()
            return true;
        }

        if (item.getItemId() == R.id.menu_filter) {
            navigateToFilter();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void navigateToFilter() {
        Intent intent = new Intent(this, OsFilterActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_SCHEDULE_OS_LIST, this.scheduleOsList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_NEXT_OS_LIST, this.nextOsList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, this.osTypeModelList);
        intent.putExtra(ValenetUtils.KEY_OS_TYPE, osType);
        intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
        startActivityForResult(intent, REQ_CODE_FILTER);
    }

    @Override
    public void navigateToSearch() {
        if(navigateInterface != null){
            navigateInterface.navigateToOsSearch();
        }
    }

    public void setScheduleOsList(ArrayList<Os> scheduleOsList){
        this.scheduleOsList = scheduleOsList;
    }

    public void setNextOsList(ArrayList<Os> nextOsList){
        this.nextOsList = nextOsList;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SEARCH) {
            if (resultCode == RESULT_CODE_BACK_SEARCH) {
                searchViewContainer.collapse();
            }
        }

        if (requestCode == REQ_CODE_FILTER) {
            if (resultCode == REQ_CODE_BACK_FILTER) {
                ArrayList<Os> scheduleOsFiltredList = data.getParcelableArrayListExtra(ValenetUtils.KEY_SCHEDULE_OS_LIST);
                ArrayList<Os> nextOsFiltredList = data.getParcelableArrayListExtra(ValenetUtils.KEY_NEXT_OS_LIST);
                if(scheduleOsFiltredList != null && onActivityResultScheduleOs != null)
                    onActivityResultScheduleOs.onActivityResultFilter(scheduleOsFiltredList);
                if(nextOsFiltredList != null && onActivityResultNextOs != null)
                    onActivityResultNextOs.onActivityResultFilter(nextOsFiltredList);
            }
        }

    }

    @Override
    public void hideOsListView() {
        this.osListView.setVisibility(View.GONE);
    }

    @Override
    public void showOsListView() {
        this.osListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
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

    }

    @Override
    public void showListOsType(List<OsTypeModel> osTypes) {
        this.filters = new HashMap<>();
        this.osTypeModelList = (ArrayList) osTypes;

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        if (osTypes != null && osTypes.size() > 0) {
            for (OsTypeModel model : osTypes) {
                this.filters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }

        setOsListPagerAdapter();
    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                presenter.loadOsTypes(osType);
                break;
        }
    }

    public void setNavigateInterface(navigateInterface navigateInterface){
        this.navigateInterface = navigateInterface;
    }

    public void setOnActivityResultNextOs(onActivityResultNextOs navigateInterface){
        this.onActivityResultNextOs = navigateInterface;
    }

    public void setOnActivityResultScheduleOs(onActivityResultScheduleOs navigateInterface){
        this.onActivityResultScheduleOs = navigateInterface;
    }
    private void setOsListPagerAdapter() {
        pagerAdapter = new OsListPagerAdapter(getSupportFragmentManager(), myLocation, this.orderFilters, this.filters,
                                                this.osTypeModelList, this.osType, tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        NextOsFragment fragment = (NextOsFragment) pagerAdapter.getRegisteredFragment(position);
                        fragment.setOsListNavigation();
                        break;
                    case 1:
                        ScheduleOsFragment fragmentSchedule = (ScheduleOsFragment) pagerAdapter.getRegisteredFragment(position);
                        fragmentSchedule.setOsListNavigation();
                        break;
                }
            }
        });
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public interface navigateInterface {
        void navigateToOsSearch();
    }

    public interface onActivityResultNextOs{
        void onActivityResultFilter(ArrayList<Os> filtredList);
    }

    public interface onActivityResultScheduleOs{
        void onActivityResultFilter(ArrayList<Os> filtredList);
    }
}
