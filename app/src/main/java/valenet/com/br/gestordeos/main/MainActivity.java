package valenet.com.br.gestordeos.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
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
import valenet.com.br.gestordeos.login.LoginActivity;
import valenet.com.br.gestordeos.map.MapsActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.os_filter.OsFilterActivity;
import valenet.com.br.gestordeos.os_history.OsHistoryFragment;
import valenet.com.br.gestordeos.os_next.OsNextFragment;
import valenet.com.br.gestordeos.os_schedule.OsScheduleNextDaysFragment;
import valenet.com.br.gestordeos.os_schedule.OsSchedulePagerAdapter;
import valenet.com.br.gestordeos.os_schedule.OsScheduleTodayFragment;
import valenet.com.br.gestordeos.os_schedule.OsScheduleTomorrowFragment;
import valenet.com.br.gestordeos.utils.ValenetUtils;
import xyz.sahildave.widget.SearchViewLayout;

public class MainActivity extends AppCompatActivity implements Main.MainView {


    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    RelativeLayout layoutErrorServer;
    @BindView(R.id.pager)
    ViewPager pager;

    //Toolbar Searchable
    @BindView(R.id.text_view_toolbar_searchable_title)
    TextView textViewToolbarSearchableTitle;
    @BindView(R.id.toolbar_searchable)
    Toolbar toolbarSearchable;
    @BindView(R.id.search_view_container)
    SearchViewLayout searchViewContainer;
    @BindView(R.id.tab_layout_toolbar_searchable)
    TabLayout tabLayoutToolbarSearchable;

    ActionBarDrawerToggle drawerToggle;


    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;
    private final int CODE_MAP = 1000;

    private Main.MainPresenter presenter;

    private navigateInterface navigateInterface;
    private OsSchedulePagerAdapter osSchedulePagerAdapter;
    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> filters;

    private ArrayList<OsTypeModel> osTypeModelList;
    private ArrayList<Os> osScheduleArrayList = null;
    private ArrayList<Os> osNextArrayList = null;

    private HashMap<Integer, Integer> osDistanceHashMap = null;


    //Location
    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;

    private Location myLocation;
    private Integer osType = 1;
    private boolean isHistory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupScheduleToolbar();
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Hoje"));
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Amanhã"));
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Próximos Dias"));
        tabLayoutToolbarSearchable.setTabGravity(TabLayout.GRAVITY_FILL);

        this.presenter = new MainPresenterImp(this);

        this.orderFilters = new HashMap<>();

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_TIME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, true));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));

        this.showLoading();
        RxPermissions.getInstance(MainActivity.this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(MainActivity.this);
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
                                                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
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
                                            presenter.loadOsTypes();
                                            if (location != null) {
                                                myLocation = location;
                                                presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), LoginLocal.getInstance().getCurrentUser().getCoduser(),
                                                        false, osType, false);
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
                            Toasty.error(MainActivity.this, "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                        }
                        return null;
                    }
                }).subscribe();
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
                SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

                this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_TIME,
                        sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, true));
                this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                        sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false));
                this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                        sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));

                if (this.osTypeModelList == null || osTypeModelList.size() == 0)
                    presenter.loadOsTypes();
                else {
                    for (OsTypeModel model : osTypeModelList) {
                        this.filters.put(model.getDescricao(),
                                sharedPref.getBoolean(model.getDescricao(), true));
                    }
                }
                selectDrawerItem(getCheckedItem(navView));
                Toasty.success(getApplicationContext(), "Filtros aplicados com sucesso.", Toast.LENGTH_SHORT).show();
            }
        }

/*        if (requestCode == CODE_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    boolean result = data.getBooleanExtra("result", false);
                    if (result) {
                        finish();
                    }
                }
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_os_list, menu);
        if(isHistory)
            menu.findItem(R.id.menu_map).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_map:
                navigateToMap();
                return true;
            case R.id.menu_filter:
                navigateToFilter();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToFilter() {
        Intent intent = new Intent(this, OsFilterActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, this.osTypeModelList);
        intent.putExtra(ValenetUtils.KEY_CAME_FROM_MAPS, false);
        startActivityForResult(intent, REQ_CODE_FILTER);
    }

    private void navigateToMap() {
        if (navigateInterface != null) {
            navigateInterface.navigateToOsMap();
        }
    }

    private void setupDrawerContent() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;
        boolean isSchedule = false;

        switch (item.getItemId()) {
            case R.id.nav_item_schedule:
                isHistory = false;
                setupScheduleToolbar();
                hideContainer();
                showPager();
                isSchedule = true;
                break;
            case R.id.nav_item_map:
                fragmentClass = OsNextFragment.class;
                hidePager();
                isHistory = false;
                setupToolbarGetNextOs();
                showContainer();
                isSchedule = false;
                break;
            case R.id.nav_item_history:
                fragmentClass = OsHistoryFragment.class;
                hidePager();
                isHistory = true;
                setupToolbarOsHistory();
                showContainer();
                isSchedule = false;
                break;
            case R.id.nav_item_exit:
                isSchedule = true;
                isHistory = false;
                presenter.logout();
                break;
            default:
                isHistory = false;
                setupScheduleToolbar();
                hideContainer();
                showPager();
                isSchedule = true;
                break;
        }

        if (!isSchedule) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                if (fragment instanceof OsNextFragment)
                    ((OsNextFragment) fragment).setOsListNavigation();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();
        }

        item.setChecked(true);
        drawerLayout.closeDrawers();
    }

    private MenuItem getCheckedItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                return item;
            }
        }

        return null;
    }

    private ActionBarDrawerToggle setupDrawerToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void navigateToSearch() {
        if (navigateInterface != null) {
            navigateInterface.navigateToOsSearch();
        }
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
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
    public void showErrorServerView(List<Os> osSchedule, List<Os> osNext) {
        if(osSchedule != null)
            this.osScheduleArrayList = (ArrayList<Os>) osSchedule;

        if(osNext != null)
            this.osNextArrayList = (ArrayList) osNext;
    }

    @Override
    public void showErrorConnectionView(List<Os> osSchedule, List<Os> osNext) {
        if(osSchedule != null)
            this.osScheduleArrayList = (ArrayList<Os>) osSchedule;

        if(osNext != null)
            this.osNextArrayList = (ArrayList) osNext;
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
    public void showContainer() {
        if (container != null)
            container.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContainer() {
        if (container != null)
            container.setVisibility(View.GONE);
    }

    @Override
    public void showPager() {
        if (pager != null) {
            setOsSchedulePagerAdapter();
            pager.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hidePager() {
        if (pager != null)
            pager.setVisibility(View.GONE);
    }

    @Override
    public void loadScheduleListOs(List<Os> osList) {
        if (osList != null)
            this.osScheduleArrayList = (ArrayList) osList;
        if(myLocation != null) {
            if(LoginLocal.getInstance() != null)
                presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), LoginLocal.getInstance().getCurrentUser().getCoduser(),
                    true, osType, false);
        }
    }

    @Override
    public void loadNextListOs(List<Os> osList) {
        if (osList != null) {
            if(osDistanceHashMap == null)
                osDistanceHashMap = new HashMap<>();
            this.osNextArrayList = (ArrayList) osList;
            if(this.osNextArrayList.size() > 0){
                boolean isLast = false;
                for(int i = 0; i < osNextArrayList.size(); i++){
                    if((this.osScheduleArrayList == null || this.osScheduleArrayList.size() == 0) && i == osNextArrayList.size() - 1)
                        isLast = true;
                    if(myLocation == null)
                        presenter.loadOsDistance(null, null, osNextArrayList.get(i), isLast);
                    else
                        presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), osNextArrayList.get(i), isLast);
                }

                if(osScheduleArrayList != null && osScheduleArrayList.size() > 0) {
                    isLast = false;
                    for (int i = 0; i < this.osScheduleArrayList.size(); i++){
                        if(i == osScheduleArrayList.size() - 1)
                            isLast = true;
                        if(myLocation == null)
                            presenter.loadOsDistance(null, null, osScheduleArrayList.get(i), isLast);
                        else
                            presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), osScheduleArrayList.get(i), isLast);
                    }
                }
            }
        }
        if(navView != null && myLocation == null) {
            hideContainer();
            hideErrorConnectionView();
            hideLoading();
            hideErrorServerView();
            hidePager();
            hideEmptyListView();
            selectDrawerItem(getCheckedItem(navView));
        }
    }

    @Override
    public void showErrorMainService() {
        if(navView != null)
            selectDrawerItem(getCheckedItem(navView));
    }

    @Override
    public void showErrorServerView(ArrayList<OsTypeModel> osTypeModels) {
        if(osTypeModels == null)
            showErrorServerView();
        else{
            loadOsTypes(osTypeModels);
        }
    }

    @Override
    public void showErrorConnectionView(ArrayList<OsTypeModel> osTypeModels) {
        if(osTypeModels == null)
            showErrorConnectionView();
        else{
            loadOsTypes(osTypeModels);
        }
    }

    @Override
    public void setOsDistance(Integer osDistance, Os os, boolean isLast) {
        if(osDistanceHashMap == null)
            osDistanceHashMap = new HashMap<>();

        osDistanceHashMap.put(os.getOsid(), osDistance);

        if(isLast)
            if(navView != null)
                selectDrawerItem(getCheckedItem(navView));
    }

    //region useless function interface
    @Override
    public void showEmptyListView() {

    }

    @Override
    public void hideEmptyListView() {

    }

    //end region useless function interface

    @Override
    public void loadOsTypes(List<OsTypeModel> osList) {
        this.filters = new HashMap<>();
        this.osTypeModelList = (ArrayList) osList;

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        if (osList != null && osList.size() > 0) {
            for (OsTypeModel model : osList) {
                this.filters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }
    }

    private void setupScheduleToolbar() {
        setSupportActionBar(toolbarSearchable);
        tabLayoutToolbarSearchable.setVisibility(View.VISIBLE);
        textViewToolbarSearchableTitle.setText(getResources().getString(R.string.title_schedule));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle(toolbarSearchable);
        setupDrawerContent();

        drawerLayout.addDrawerListener(drawerToggle);

        searchViewContainer.handleToolbarAnimation(toolbarSearchable);
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
    }

    private void setupToolbarGetNextOs() {
        setSupportActionBar(toolbarSearchable);
        tabLayoutToolbarSearchable.setVisibility(View.GONE);
        textViewToolbarSearchableTitle.setText(getResources().getString(R.string.title_get_next_os));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle(toolbarSearchable);
        setupDrawerContent();

        drawerLayout.addDrawerListener(drawerToggle);

        searchViewContainer.handleToolbarAnimation(toolbarSearchable);
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
    }

    private void setupToolbarOsHistory() {
        setSupportActionBar(toolbarSearchable);
        tabLayoutToolbarSearchable.setVisibility(View.GONE);
        textViewToolbarSearchableTitle.setText(getResources().getString(R.string.title_history_os));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle(toolbarSearchable);
        setupDrawerContent();

        drawerLayout.addDrawerListener(drawerToggle);

        searchViewContainer.handleToolbarAnimation(toolbarSearchable);
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
    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
                presenter.loadOsTypes();
                break;
        }
    }

    public void setOsSchedulePagerAdapter() {
        osSchedulePagerAdapter = new OsSchedulePagerAdapter(getSupportFragmentManager(), myLocation,
                orderFilters, filters, osTypeModelList, osScheduleArrayList, osType, osDistanceHashMap, tabLayoutToolbarSearchable.getTabCount());
        pager.setOffscreenPageLimit(tabLayoutToolbarSearchable.getTabCount());
        pager.setAdapter(osSchedulePagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutToolbarSearchable) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        OsScheduleTodayFragment fragmentToday = (OsScheduleTodayFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        fragmentToday.setOsListNavigation();
                        break;
                    case 1:
                        OsScheduleTomorrowFragment fragmentTomorrow = (OsScheduleTomorrowFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        fragmentTomorrow.setOsListNavigation();
                        break;
                    case 2:
                        OsScheduleNextDaysFragment fragmentNextDays = (OsScheduleNextDaysFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        fragmentNextDays.setOsListNavigation();
                        break;
                }
            }
        });
        tabLayoutToolbarSearchable.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    public void setNavigateInterface(MainActivity.navigateInterface navigateInterface) {
        this.navigateInterface = navigateInterface;
    }

    public void setOsScheduleArrayList(ArrayList<Os> osScheduleArrayList) {
        this.osScheduleArrayList = osScheduleArrayList;
    }

    public void setOsNextArrayList(ArrayList<Os> osNextArrayList) {
        this.osNextArrayList = osNextArrayList;
    }

    public Integer getOsType() {
        return osType;
    }

    public ArrayList<Os> getOsNextArrayList() {
        return osNextArrayList;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public ArrayList<OsTypeModel> getOsTypeModelList() {
        return osTypeModelList;
    }

    public HashMap<String, Boolean> getOrderFilters() {
        return orderFilters;
    }

    public HashMap<String, Boolean> getFilters() {
        return filters;
    }

    public HashMap<Integer, Integer> getOsDistanceHashMap() {
        return osDistanceHashMap;
    }

    public void setOsDistanceHashMap(HashMap<Integer, Integer> osDistanceHashMap) {
        this.osDistanceHashMap = osDistanceHashMap;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public interface navigateInterface {
        void navigateToOsSearch();

        void navigateToOsMap();
    }
}
