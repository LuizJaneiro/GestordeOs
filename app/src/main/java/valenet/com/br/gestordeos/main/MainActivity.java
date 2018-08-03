package valenet.com.br.gestordeos.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.application.LocationListener;
import valenet.com.br.gestordeos.application.LocationService;
import valenet.com.br.gestordeos.login.LoginActivity;
import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.model.realm.OsListLocal;
import valenet.com.br.gestordeos.os_filter.OsFilterActivity;
import valenet.com.br.gestordeos.os_history.OsHistoryFragment;
import valenet.com.br.gestordeos.os_next.OsNextFragment;
import valenet.com.br.gestordeos.os_schedule.OsScheduleNextDaysFragment;
import valenet.com.br.gestordeos.os_schedule.OsSchedulePagerAdapter;
import valenet.com.br.gestordeos.os_schedule.OsScheduleTodayFragment;
import valenet.com.br.gestordeos.os_schedule.OsScheduleTomorrowFragment;
import valenet.com.br.gestordeos.search.SearchActivity;
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
    @BindView(R.id.text_view_loading)
    TextView textViewLoading;

    //tutorial variables
    @BindView(R.id.tutorial_text_view_os_id)
    TextView tutorialTextViewOsId;
    @BindView(R.id.tutorial_text_view_distance_toolbar)
    TextView tutorialTextViewDistanceToolbar;
    @BindView(R.id.tutorial_image_view_status_os)
    ImageView tutorialImageViewStatusOs;
    @BindView(R.id.tutorial_image_view_fished_os)
    ImageView tutorialImageViewFishedOs;
    @BindView(R.id.tutorial_content_os_details)
    RelativeLayout tutorialContentOsDetails;
    @BindView(R.id.tutorial_text_view_client_name_toolbar)
    TextView tutorialTextViewClientNameToolbar;
    @BindView(R.id.tutorial_text_view_os_type_toolbar)
    TextView tutorialTextViewOsTypeToolbar;
    @BindView(R.id.tutorial_text_view_os_date_toolbar)
    TextView tutorialTextViewOsDateToolbar;
    @BindView(R.id.tutorial_text_view_os_city_toolbar)
    TextView tutorialTextViewOsCityToolbar;
    @BindView(R.id.tutorial_text_view_os_address_toolbar)
    TextView tutorialTextViewOsAddressToolbar;
    @BindView(R.id.tutorial_content_text_view)
    RelativeLayout tutorialContentTextView;
    @BindView(R.id.tutorial_content_os_view)
    RelativeLayout tutorialContentOsView;
    @BindView(R.id.tutorial_os_item_view)
    RelativeLayout tutorialOsItemView;
    @BindView(R.id.tutorial_screen)
    LinearLayout tutorialScreen;


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
    private ArrayList<OrdemDeServico> ordemDeServicoScheduleArrayList = null;
    private ArrayList<OrdemDeServico> ordemDeServicoNextArrayList = null;

    private GestorDeOsApplication application;


    //Location
    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;

    private Location myLocation;
    private Integer osType = 0;
    private boolean isHistory = false;

    private TelephonyManager tm;

    private boolean showTutorial = false;
    private DownloadManager downloadManager;
    public static Intent myService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tm = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        setupScheduleToolbar();
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Até Hoje"));
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Amanhã"));
        tabLayoutToolbarSearchable.addTab(tabLayoutToolbarSearchable.newTab().setText("Próx. Dias"));
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

        LoginLocal loginLocal = LoginLocal.getInstance();
        if (loginLocal != null) {
            sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_TUTORIAL, Context.MODE_PRIVATE);
            showTutorial = sharedPref.getBoolean(loginLocal.getCurrentUser().getCoduser().toString(), true);
        }


        if (!showTutorial) {
            initMainActivity();
        } else {
            showTutorial();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SEARCH) {
            if (resultCode == RESULT_CODE_BACK_SEARCH) {
                if (searchViewContainer != null)
                    searchViewContainer.collapse();
            }
            if (resultCode == Activity.RESULT_OK) {
                if (searchViewContainer != null)
                    searchViewContainer.collapse();
                selectDrawerItem(navView.getMenu().findItem(R.id.nav_item_schedule));
                presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        false, osType, false);
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

        if (requestCode == ValenetUtils.REQUEST_CODE_CLIENT) {
            if (resultCode == Activity.RESULT_OK) {
                selectDrawerItem(navView.getMenu().findItem(R.id.nav_item_schedule));
                presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        false, osType, false);
            }
        }

        if (requestCode == CODE_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                selectDrawerItem(navView.getMenu().findItem(R.id.nav_item_schedule));
                presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), LoginLocal.getInstance().getCurrentUser().getCoduser(),
                        false, osType, false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_os_list, menu);
        if (isHistory) {
            menu.findItem(R.id.menu_map).setVisible(false);
            menu.findItem(R.id.menu_filter).setVisible(false);
        }
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

    private void downloadManual() {
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar o download do manual?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://api3.valenet.com.br/manual.pdf");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
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
            case R.id.nav_item_tutorial:
                isHistory = false;
                setupScheduleToolbar();
                hideContainer();
                showPager();
                isSchedule = true;
                break;
            case R.id.nav_item_download_manual:
                isSchedule = true;
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

        if (item.getItemId() != R.id.nav_item_tutorial && item.getItemId() != R.id.nav_item_download_manual)
            item.setChecked(true);
        drawerLayout.closeDrawers();

        if (item.getItemId() == R.id.nav_item_tutorial)
            showTutorial();
        if (item.getItemId() == R.id.nav_item_download_manual)
            downloadManual();
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
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.text_open_drawer, R.string.text_close_drawer);
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
        } else {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, null);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, null);
            intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
            startActivityForResult(intent, REQ_CODE_SEARCH);
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
        if (loadingView != null && textViewLoading != null) {
            loadingView.setVisibility(View.VISIBLE);
            textViewLoading.setVisibility(View.VISIBLE);
        }
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
    public void showErrorServerView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext) {
        if (ordemDeServicoSchedule != null)
            this.ordemDeServicoScheduleArrayList = (ArrayList<OrdemDeServico>) ordemDeServicoSchedule;

        if (ordemDeServicoNext != null)
            this.ordemDeServicoNextArrayList = (ArrayList) ordemDeServicoNext;

        if (navView != null)
            selectDrawerItem(getCheckedItem(navView));
    }

    @Override
    public void showErrorConnectionView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext) {
        if (ordemDeServicoSchedule != null)
            this.ordemDeServicoScheduleArrayList = (ArrayList<OrdemDeServico>) ordemDeServicoSchedule;

        if (ordemDeServicoNext != null)
            this.ordemDeServicoNextArrayList = (ArrayList) ordemDeServicoNext;
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
    public void loadScheduleListOs(List<OrdemDeServico> ordemDeServicoList) {
        if (ordemDeServicoList != null)
            this.ordemDeServicoScheduleArrayList = (ArrayList) ordemDeServicoList;
        if (myLocation != null) {
            LoginLocal loginLocal = LoginLocal.getInstance();
            if (loginLocal != null)
                if (loginLocal.getCurrentUser().getCoduser() != null)
                    presenter.loadMainOsList(myLocation.getLatitude(), myLocation.getLongitude(), loginLocal.getCurrentUser().getCoduser(),
                            true, osType, false);
        }
        if (navView != null && myLocation == null) {
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
    public void loadNextListOs(List<OrdemDeServico> ordemDeServicoList) {
        if (ordemDeServicoList != null) {
            this.ordemDeServicoNextArrayList = (ArrayList) ordemDeServicoList;
            if (this.ordemDeServicoNextArrayList.size() > 0) {
                boolean isLast = false;
                for (int i = 0; i < ordemDeServicoNextArrayList.size(); i++) {
                    if ((this.ordemDeServicoScheduleArrayList == null || this.ordemDeServicoScheduleArrayList.size() == 0) && i == ordemDeServicoNextArrayList.size() - 1)
                        isLast = true;
                    if (myLocation == null)
                        presenter.loadOsDistance(null, null, ordemDeServicoNextArrayList.get(i), isLast);
                    else
                        presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), ordemDeServicoNextArrayList.get(i), isLast);
                }

                if (ordemDeServicoScheduleArrayList != null && ordemDeServicoScheduleArrayList.size() > 0) {
                    isLast = false;
                    for (int i = 0; i < this.ordemDeServicoScheduleArrayList.size(); i++) {
                        if (i == ordemDeServicoScheduleArrayList.size() - 1)
                            isLast = true;
                        if (myLocation == null)
                            presenter.loadOsDistance(null, null, ordemDeServicoScheduleArrayList.get(i), isLast);
                        else
                            presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), ordemDeServicoScheduleArrayList.get(i), isLast);
                    }
                }
            } else {
                if (navView != null)
                    selectDrawerItem(getCheckedItem(navView));
            }
        }
        if (navView != null && myLocation == null) {
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
        OsListLocal osListLocal = OsListLocal.getInstance();
        if (osListLocal != null) {
            ArrayList<OrdemDeServico> scheduleOrdemDeServicoList = (ArrayList) osListLocal.getScheduleOsList();
            ArrayList<OrdemDeServico> nextOrdemDeServicoList = (ArrayList) osListLocal.getNextOsList();
            if (scheduleOrdemDeServicoList != null)
                this.ordemDeServicoScheduleArrayList = (ArrayList<OrdemDeServico>) scheduleOrdemDeServicoList;

            if (nextOrdemDeServicoList != null)
                this.ordemDeServicoNextArrayList = (ArrayList) nextOrdemDeServicoList;
        }
        if (navView != null)
            selectDrawerItem(getCheckedItem(navView));
    }

    @Override
    public void showErrorServerView(ArrayList<OsTypeModel> osTypeModels) {
        if (osTypeModels == null)
            showErrorServerView();
        else {
            loadOsTypes(osTypeModels);
        }
    }

    @Override
    public void showErrorConnectionView(ArrayList<OsTypeModel> osTypeModels) {
        if (osTypeModels == null)
            showErrorConnectionView();
        else {
            loadOsTypes(osTypeModels);
        }
    }

    @Override
    public void setOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast) {

        application.osDistanceHashMap.put(ordemDeServico.getOsid(), osDistanceAndPoints);

        if (isLast)
            if (navView != null)
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

    @Override
    public void loadAppConfig(List<AppConfig> appConfigs) {
        for (AppConfig appConfig : appConfigs) {
            if (appConfig.getChave() != null && appConfig.getValor() != null) {
                Integer appConfigValue;
                DateFormat sdf = new SimpleDateFormat("hh:mm");
                Date convertedDate;
                switch(appConfig.getChave()) {
                    case "APIKey":
                        break;
                    case "IntervaloMinimoEnvioDePontos":
                        appConfigValue = Integer.parseInt(appConfig.getValor());
                        application.setIntervalSendPoints(appConfigValue);
                        break;
                    case "MinDistance":
                        appConfigValue = Integer.parseInt(appConfig.getValor());
                        LocationService.LOCATION_DISTANCE = appConfigValue;
                        break;
                    case "MinTime":
                        appConfigValue = Integer.parseInt(appConfig.getValor());
                        LocationService.LOCATION_INTERVAL = appConfigValue;
                        break;
                    case "InicioJornada":
                        convertedDate = new Date();
                        try {
                            convertedDate = sdf.parse(appConfig.getValor());
                            LocationListener.initTime = convertedDate;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "FimJornada":
                        convertedDate = new Date();
                        try {
                            convertedDate = sdf.parse(appConfig.getValor());
                            LocationListener.endTime = convertedDate;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
        LocationService.setLocationListener();
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
        searchViewContainer.setHint(getString(R.string.text_search_box));
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
        searchViewContainer.setHint(getString(R.string.text_search_box));
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
        searchViewContainer.setHint(getString(R.string.text_search_box));
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
                orderFilters, filters, osTypeModelList, ordemDeServicoScheduleArrayList, osType, tabLayoutToolbarSearchable.getTabCount());
        pager.setOffscreenPageLimit(tabLayoutToolbarSearchable.getTabCount());
        pager.setAdapter(osSchedulePagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutToolbarSearchable) {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        OsScheduleTodayFragment fragmentToday = (OsScheduleTodayFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        if (fragmentToday != null)
                            fragmentToday.setOsListNavigation();
                        break;
                    case 1:
                        OsScheduleTomorrowFragment fragmentTomorrow = (OsScheduleTomorrowFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        if (fragmentTomorrow != null)
                            fragmentTomorrow.setOsListNavigation();
                        break;
                    case 2:
                        OsScheduleNextDaysFragment fragmentNextDays = (OsScheduleNextDaysFragment) osSchedulePagerAdapter.getRegisteredFragment(position);
                        if (fragmentNextDays != null)
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

    public void setOrdemDeServicoScheduleArrayList(ArrayList<OrdemDeServico> ordemDeServicoScheduleArrayList) {
        this.ordemDeServicoScheduleArrayList = ordemDeServicoScheduleArrayList;
    }

    public void setOrdemDeServicoNextArrayList(ArrayList<OrdemDeServico> ordemDeServicoNextArrayList) {
        this.ordemDeServicoNextArrayList = ordemDeServicoNextArrayList;
    }

    public Integer getOsType() {
        return osType;
    }

    public ArrayList<OrdemDeServico> getOrdemDeServicoNextArrayList() {
        return ordemDeServicoNextArrayList;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public interface navigateInterface {
        void navigateToOsSearch();

        void navigateToOsMap();
    }

    private void showTutorial() {
        final Activity activity = this;
        android.app.AlertDialog.Builder builderCad;
        builderCad = new android.app.AlertDialog.Builder(this);
        builderCad.setTitle("Atenção");
        builderCad.setMessage("Deseja realizar o tutorial de inicialização?");
        builderCad.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                hidePager();
                hideContainer();
                hideEmptyListView();
                hideErrorConnectionView();
                hideErrorServerView();
                hideLoading();
                if (tutorialScreen != null) {
                    tutorialScreen.setVisibility(View.VISIBLE);
                    toolbarSearchable.inflateMenu(R.menu.menu_os_list);
                    new TapTargetSequence(activity)
                            .targets(
                                    TapTarget.forToolbarNavigationIcon(toolbarSearchable, "Este é o Menu", "Nele você pode navegar entre as telas do Gestor de OS e realizar funções como visualização da sua agenda, visualizar Oss próximas a você e visualizar o seu histórico.\n" +
                                            "Nele você ainda pode baixar o manual do aplicativo e visualizar este tutorial sempre que quiser.")
                                            .targetCircleColor(R.color.white)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(1),
                                    TapTarget.forToolbarMenuItem(toolbarSearchable, R.id.menu_map, "Este é o Botão de Mapa", "Sempre que visualizar um botão como esse na barra superior direita" +
                                            " você pode alternar entre a visualização de lista e a de mapa das suas OSs.")
                                            .targetCircleColor(R.color.white)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(2),
                                    TapTarget.forToolbarMenuItem(toolbarSearchable, R.id.menu_filter, "Este é o Botão de Filtro", "Este botão te direciona para a tela de filtragem onde você pode " +
                                            "filtrar as OSs mostradas por tipo de serviço e ordenar a lista de acordo com sua necessidade (distância, nome do cliente e hora).")
                                            .targetCircleColor(R.color.white)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(3),
                                    TapTarget.forView(searchViewContainer, "Esta é a Barra de Busca", "Por ela você pode buscar por OSs dentro das listas por código, tipo de serviço" +
                                            " ou nome do cliente.")
                                            .targetCircleColor(R.color.white)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(4),
                                    TapTarget.forView(tutorialOsItemView, "Este card representa uma OS", "No card está contido algumas informações como ...")
                                            .targetCircleColor(R.color.white)
                                            .targetRadius(150)
                                            .drawShadow(false)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(6),
                                    TapTarget.forView(tutorialTextViewOsId, "... Código da OS ...")
                                            .targetCircleColor(R.color.white)
                                            .targetRadius(30)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(7),
                                    TapTarget.forView(tutorialTextViewDistanceToolbar, "... Distância estimada entre sua localização e a OS ...")
                                            .targetCircleColor(R.color.white)
                                            .targetRadius(30)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(8),
                                    TapTarget.forView(tutorialImageViewStatusOs, "... E finalmente o Status da OS", "Cada status é representado por um ícone diferente, qualquer dúvida" +
                                            " você pode sempre consultar o manual disponível para download no menu lateral.")
                                            .targetRadius(20)
                                            .targetCircleColor(R.color.white)
                                            .drawShadow(true)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(13),
                                    TapTarget.forView(tutorialOsItemView, "Card da OS", "Ao clicar no card da OS você é direcionado para uma outra tela onde" +
                                            " pode consultar mais detalhes da OS, realizar checkin e checkout, navegação até o local da OS, ligar para o cliente, entre outras funcionalidades.")
                                            .targetCircleColor(R.color.white)
                                            .targetRadius(150)
                                            .drawShadow(false)
                                            .cancelable(false)
                                            .transparentTarget(true)
                                            .id(14))
                            .listener(new TapTargetSequence.Listener() {
                                @Override
                                public void onSequenceFinish() {
                                    if (tutorialScreen != null)
                                        tutorialScreen.setVisibility(View.GONE);
                                    LoginLocal loginLocal = LoginLocal.getInstance();
                                    if (loginLocal != null) {
                                        SharedPreferences sharedPref = activity.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_TUTORIAL, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean(loginLocal.getCurrentUser().getCoduser().toString(), false);
                                        editor.commit();
                                    }
                                    initMainActivity();
                                    selectDrawerItem(navView.getMenu().findItem(R.id.nav_item_schedule));
                                }

                                @Override
                                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                                }

                                @Override
                                public void onSequenceCanceled(TapTarget lastTarget) {

                                }
                            })
                            .start();
                }
            }
        });
        builderCad.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginLocal loginLocal = LoginLocal.getInstance();
                if (loginLocal != null) {
                    SharedPreferences sharedPref = activity.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_TUTORIAL, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(loginLocal.getCurrentUser().getCoduser().toString(), false);
                    editor.commit();
                }
                initMainActivity();
                selectDrawerItem(navView.getMenu().findItem(R.id.nav_item_schedule));
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

    private void initMainActivity() {
        this.showLoading();
        this.presenter.getAppConfig();
        RxPermissions.getInstance(MainActivity.this)
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE)
                .map(new Func1<Boolean, Object>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            myService = new Intent(getApplicationContext(), LocationService.class);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                application.imei = tm.getImei();
                                startForegroundService(myService);

                            } else {
                                application.imei = tm.getDeviceId();
                                startService(myService);
                            }

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
}
