package valenet.com.br.gestordeos.main;

import android.Manifest;
import android.annotation.SuppressLint;
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
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
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


    private Main.MainPresenter presenter;

    private navigateInterface navigateInterface;

    private OsSchedulePagerAdapter osSchedulePagerAdapter;
    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> filters;

    private ArrayList<OsTypeModel> osTypeModelList;


    //Location
    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private Location myLocation;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        Class fragmentClass;

        switch (item.getItemId()) {
            case R.id.nav_item_schedule:
                setupScheduleToolbar();
                hideContainer();
                showPager();
                break;
            case R.id.nav_item_map:
                //fragmentClass = OsScheduleFragment.class;
                break;
            case R.id.nav_item_history:
                //fragmentClass = OsScheduleFragment.class;
                break;
            case R.id.nav_item_exit:
                //fragmentClass = OsScheduleFragment.class;
                presenter.logout();
                break;
            default:
                setupScheduleToolbar();
                hideContainer();
                showPager();
                break;
        }

/*        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // Insert the fragment by replacing any existing fragment
/*        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();*/

        item.setChecked(true);
        drawerLayout.closeDrawers();
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

        textViewToolbarSearchableTitle.setText(getResources().getString(R.string.title_schedule));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle(toolbarSearchable);
        setupDrawerContent();

        drawerLayout.addDrawerListener(drawerToggle);

        searchViewContainer.handleToolbarAnimation(toolbarSearchable);
        searchViewContainer.setHint("Buscar por OS");
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

    private void setOsSchedulePagerAdapter() {
        osSchedulePagerAdapter = new OsSchedulePagerAdapter(getSupportFragmentManager(), myLocation,
                orderFilters, filters, osTypeModelList, tabLayoutToolbarSearchable.getTabCount());
        pager.setAdapter(osSchedulePagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutToolbarSearchable) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        OsScheduleTodayFragment fragmentToday = (OsScheduleTodayFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        //fragment.setOsListNavigation();
                        break;
                    case 1:
                        OsScheduleTomorrowFragment fragmentTomorrow = (OsScheduleTomorrowFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        //fragmentSchedule.setOsListNavigation();
                        break;
                    case 2:
                        OsScheduleNextDaysFragment fragmentNextDays = (OsScheduleNextDaysFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        //fragmentSchedule.setOsListNavigation();
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

    public TabLayout getTabLayoutToolbarSearchable() {
        return tabLayoutToolbarSearchable;
    }

    public HashMap<String, Boolean> getOrderFilters() {
        return orderFilters;
    }

    public HashMap<String, Boolean> getFilters() {
        return filters;
    }

    public ArrayList<OsTypeModel> getOsTypeModelList() {
        return osTypeModelList;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public interface navigateInterface {
        void navigateToOsSearch();
    }
}
