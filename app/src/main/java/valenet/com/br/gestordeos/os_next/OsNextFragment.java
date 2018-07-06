package valenet.com.br.gestordeos.os_next;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.Main;
import valenet.com.br.gestordeos.main.MainActivity;
import valenet.com.br.gestordeos.main.MainPresenterImp;
import valenet.com.br.gestordeos.main.OsItemAdapter;
import valenet.com.br.gestordeos.map.MapsActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.DateUtils;
import valenet.com.br.gestordeos.utils.ValenetUtils;


public class OsNextFragment extends Fragment implements MainActivity.navigateInterface, Main.MainView {


    @BindView(R.id.recycler_view_schedule_os)
    RecyclerView recyclerViewScheduleOs;
    @BindView(R.id.refresh_layout_schedule_os)
    SwipeRefreshLayout refreshLayoutScheduleOs;
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
    @BindView(R.id.text_view_error_empty_list)
    TextView textViewErrorEmptyList;
    @BindView(R.id.btn_reload)
    AppCompatButton btnReload;
    @BindView(R.id.layout_empty_list)
    RelativeLayout layoutEmptyList;
    Unbinder unbinder;
    @BindView(R.id.text_view_loading_maps)
    TextView textViewLoadingMaps;

    private Main.MainPresenter presenter;

    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;
    private final int CODE_MAP = 1000;
    private ArrayList<Os> osList;
    private ArrayList<Os> filtredList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;

    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private Location myLocation;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;
    private HashMap<Integer, OsDistanceAndPoints> osDistanceHashMap = null;

    private OsItemAdapter adapter;
    Integer osType = null;

    public OsNextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) this.getActivity()).setNavigateInterface(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_os_next, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new MainPresenterImp(this);

        this.myLocation = ((MainActivity) this.getActivity()).getMyLocation();
        this.osTypeModelArrayList = ((MainActivity) this.getActivity()).getOsTypeModelList();
        this.orderFilters = ((MainActivity) this.getActivity()).getOrderFilters();
        this.selectedFilters = ((MainActivity) this.getActivity()).getFilters();
        this.osList = ((MainActivity) this.getActivity()).getOsNextArrayList();
        this.osType = ((MainActivity) this.getActivity()).getOsType();
        this.osDistanceHashMap = ((MainActivity) this.getActivity()).getOsDistanceHashMap();

/*        refreshLayoutScheduleOs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myLocation != null)
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), true, osType, true);
                else {
                    loadOsListWithoutLocation();
                }
            }
        });

        if (this.osList == null || this.osList.size() == 0) {
            if (myLocation == null)
                loadOsListWithoutLocation();
            else
                presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                        LoginLocal.getInstance().getCurrentUser().getCoduser(), true, osType, false);
        } else {
            hideContainer();
            hideErrorConnectionView();
            hideLoading();
            hideErrorServerView();
            hidePager();
            hideEmptyListView();
            showLoading();
            loadNextListOs(osList);
        }*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.clear();
        super.onSaveInstanceState(outState);
    }

    public void setOsListNavigation() {
        Activity activity = this.getActivity();
        ((MainActivity) this.getActivity()).setNavigateInterface(this);
    }

    @Override
    public void showLoading() {
        if (loadingView != null && textViewLoadingMaps != null) {
            loadingView.setVisibility(View.VISIBLE);
            textViewLoadingMaps.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (refreshLayoutScheduleOs != null && refreshLayoutScheduleOs.isRefreshing())
            refreshLayoutScheduleOs.setRefreshing(false);

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
    public void showEmptyListView() {
        if (layoutEmptyList != null) {
            textViewErrorEmptyList.setText("Não há OSs agendadas para amanhã!");
            layoutEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListView() {
        if (layoutEmptyList != null)
            layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void showPager() {
        if (refreshLayoutScheduleOs != null)
            refreshLayoutScheduleOs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePager() {
        if (refreshLayoutScheduleOs != null)
            refreshLayoutScheduleOs.setVisibility(View.GONE);
    }

    @Override
    public void showErrorServerView(List<Os> osSchedule, List<Os> osNext) {
        if (osNext == null) {
            showErrorServerView();
        } else {
            loadNextListOs(osNext);
            if (this.getActivity() != null)
                Toasty.error(this.getActivity(), "Não foi possível carregar a lista de OSs próximas, tente novamente!", Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void showErrorConnectionView(List<Os> osSchedule, List<Os> osNext) {
        if (osNext == null) {
            showErrorConnectionView();
        } else {
            loadNextListOs(osNext);
            if (this.getActivity() != null)
                Toasty.error(this.getActivity(), "Não foi possível carregar a lista de OSs próximas, verifique sua conexão e tente novamente!", Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void loadNextListOs(List<Os> osList) {
        if (this.getActivity() != null) {
            this.osList = selectTodayOs((ArrayList) osList);
            ((MainActivity) this.getActivity()).setOsNextArrayList((ArrayList) osList);
            this.filtredList = ValenetUtils.filterList(this.osList, selectedFilters, this.getContext());

            if (((MainActivity) this.getActivity()).getOsDistanceHashMap() == null)
                osDistanceHashMap = new HashMap<>();
            else
                osDistanceHashMap = ((MainActivity) this.getActivity()).getOsDistanceHashMap();
            boolean isLast = false;
            for (int i = 0; i < osList.size(); i++) {
                if (i == osList.size() - 1)
                    isLast = true;
                Os os = osList.get(i);
                if (os.getLatitude() == null || os.getLongitude() == null || myLocation == null)
                    presenter.loadOsDistance(null, null, os, isLast);
                else
                    presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), os, isLast);
            }
        }
    }

    @Override
    public void setOsDistance(OsDistanceAndPoints osDistanceAndPoints, Os os, boolean isLast) {

        if (osDistanceHashMap == null)
            osDistanceHashMap = new HashMap<>();

        osDistanceHashMap.put(os.getOsid(), osDistanceAndPoints);

        if (isLast) {
            if (this.getActivity() != null) {
                ((MainActivity) this.getActivity()).setOsDistanceHashMap(osDistanceHashMap);
            }
            if (this.filtredList == null || this.filtredList.size() == 0) {
                this.hideLoading();
                this.hidePager();
                this.showEmptyListView();
            } else {
                if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, osDistanceHashMap);
                else if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME, osDistanceHashMap);
                else
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_TIME, osDistanceHashMap);

                if (recyclerViewScheduleOs != null) {
                    recyclerViewScheduleOs.setAdapter(adapter);
                    recyclerViewScheduleOs.setLayoutManager(new LinearLayoutManager(this.getContext()));
                    recyclerViewScheduleOs.setItemAnimator(new DefaultItemAnimator());
                }
                this.hideLoading();
                this.hideEmptyListView();
                this.showPager();
            }
        }
    }

    // region useless functions interface
    @Override
    public void navigateToLogin() {

    }

    @Override
    public void navigateToSearch() {

    }

    @Override
    public void showContainer() {

    }

    @Override
    public void hideContainer() {

    }

    @Override
    public void loadOsTypes(List<OsTypeModel> osList) {
    }

    @Override
    public void loadScheduleListOs(List<Os> osList) {

    }

    @Override
    public void showErrorMainService() {

    }

    @Override
    public void showErrorServerView(ArrayList<OsTypeModel> osTypeModels) {

    }

    @Override
    public void showErrorConnectionView(ArrayList<OsTypeModel> osTypeModels) {

    }

    // end region useless functions interface

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                if (myLocation != null) {
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), true, osType, false);
                } else {
                    loadOsListWithoutLocation();
                }
                break;
        }
    }

    //navigate interface
    @Override
    public void navigateToOsSearch() {
        if (this.getActivity() != null) {
            Intent intent = new Intent(this.getActivity(), SearchActivity.class);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, filtredList);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, osTypeModelArrayList);
            intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
            intent.putExtra(ValenetUtils.KEY_OS_DISTANCE_HASHMAP, osDistanceHashMap);
            this.getActivity().startActivityForResult(intent, REQ_CODE_SEARCH);
        }
    }

    @Override
    public void navigateToOsMap() {
        if (this.getActivity() != null) {
            Intent intent = new Intent(this.getActivity(), MapsActivity.class);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, this.osTypeModelArrayList);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST, this.osList);
            intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
            intent.putExtra(ValenetUtils.KEY_OS_DISTANCE_HASHMAP, osDistanceHashMap);
            this.getActivity().startActivityForResult(intent, CODE_MAP);
        }
    }

    private void loadOsListWithoutLocation() {
        this.showLoading();
        RxPermissions.getInstance(this.getActivity())
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .map(new Func1<Boolean, Object>() {
                    @Override
                    public Object call(Boolean aBoolean) {
                        if (aBoolean) {
                            final ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getActivity());
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
                                                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(), true, osType, false);
                                                return true;
                                            } else {
                                                presenter.loadOsList(1.1, 1.1,
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(), true, osType, false);
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
                            Toasty.error(getActivity(), "Erro ao conseguir permissões!", Toast.LENGTH_LONG, true).show();
                        }
                        return null;
                    }
                }).subscribe();
    }

    private ArrayList<Os> selectTodayOs(ArrayList<Os> osArrayList) {
        if (osArrayList != null && osArrayList.size() > 0) {
            ArrayList<Os> todayOsList = new ArrayList<>();
            Date currentDate = Calendar.getInstance().getTime();
            for (int i = 0; i < osArrayList.size(); i++) {
                Os os = osArrayList.get(i);
                if (os.getDataAgendamento() != null) {
                    Date osDate = DateUtils.parseDate(os.getDataAgendamento());
                    if (DateUtils.isDateNextDaysWithTomorrow(currentDate, osDate))
                        todayOsList.add(os);
                }
            }
            return todayOsList;
        } else
            return null;
    }

}
