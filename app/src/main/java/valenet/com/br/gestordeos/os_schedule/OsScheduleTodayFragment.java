package valenet.com.br.gestordeos.os_schedule;


import android.Manifest;
import android.annotation.SuppressLint;
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
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.main.Main;
import valenet.com.br.gestordeos.main.MainActivity;
import valenet.com.br.gestordeos.main.MainPresenterImp;
import valenet.com.br.gestordeos.main.OsItemAdapter;
import valenet.com.br.gestordeos.map.MapsActivity;
import valenet.com.br.gestordeos.model.entity.AppConfig;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.DateUtils;
import valenet.com.br.gestordeos.utils.ValenetUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsScheduleTodayFragment extends Fragment implements MainActivity.navigateInterface, Main.MainView {


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
    @BindView(R.id.text_view_loading)
    TextView textViewLoading;

    private Main.MainPresenter presenter;

    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;
    private final int CODE_MAP = 1000;
    private ArrayList<OrdemDeServico> ordemDeServicoList;
    private ArrayList<OrdemDeServico> filtredList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;

    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;
    private Location myLocation;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;
    private GestorDeOsApplication application;

    private OsItemAdapter adapter;
    Integer osType = null;

    public OsScheduleTodayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_os_schedule_today, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter = new MainPresenterImp(this);

        this.myLocation = getArguments().getParcelable(ValenetUtils.KEY_USER_LOCATION);
        this.osTypeModelArrayList = (ArrayList<OsTypeModel>) getArguments().getSerializable(ValenetUtils.KEY_OS_TYPE_LIST);
        this.orderFilters = (HashMap<String, Boolean>) getArguments().getSerializable(ValenetUtils.KEY_ORDER_FILTERS);
        this.selectedFilters = (HashMap<String, Boolean>) getArguments().getSerializable(ValenetUtils.KEY_FILTERS);
        this.ordemDeServicoList = (ArrayList<OrdemDeServico>) getArguments().getSerializable(ValenetUtils.KEY_OS_LIST);
        this.osType = getArguments().getInt(ValenetUtils.KEY_OS_TYPE);

        refreshLayoutScheduleOs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myLocation != null)
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
                else {
                    loadOsListWithoutLocation();
                }
            }
        });

        if (this.ordemDeServicoList == null || this.ordemDeServicoList.size() == 0) {
            if (myLocation == null)
                loadOsListWithoutLocation();
            else
                presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                        LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
        } else {
            hideContainer();
            hideErrorConnectionView();
            hideLoading();
            hideErrorServerView();
            hidePager();
            hideEmptyListView();
            showLoading();
            loadMainScheduleOs(ordemDeServicoList);
        }

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
        ((MainActivity) this.getActivity()).setNavigateInterface(this);
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
            textViewErrorEmptyList.setText("Não há OSs agendadas para hoje!");
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
    public void setOsDistance(OsDistanceAndPoints osDistanceAndPoints, OrdemDeServico ordemDeServico, boolean isLast) {
        application.osDistanceHashMap.put(ordemDeServico.getOsid(), osDistanceAndPoints);

        if (isLast) {
            if (this.getActivity() != null) {
                ((MainActivity) this.getActivity()).showPager();
            }
        }
    }

    @Override
    public void showErrorServerView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext) {
        if (ordemDeServicoSchedule == null) {
            showErrorServerView();
        } else {
            loadScheduleListOs(ordemDeServicoSchedule);
            if (this.getActivity() != null)
                Toasty.error(this.getActivity(), "Não foi possível carregar a lista de OSs agendadas, tente novamente!", Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void showErrorConnectionView(List<OrdemDeServico> ordemDeServicoSchedule, List<OrdemDeServico> ordemDeServicoNext) {
        if (ordemDeServicoSchedule == null) {
            showErrorConnectionView();
        } else {
            loadScheduleListOs(ordemDeServicoSchedule);
            if (this.getActivity() != null)
                Toasty.error(this.getActivity(), "Não foi possível carregar a lista de OSs agendadas, verifique sua conexão e tente novamente!", Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public void loadScheduleListOs(List<OrdemDeServico> ordemDeServicoList) {
        if (this.getActivity() != null && ordemDeServicoList != null) {
            ((MainActivity) this.getActivity()).setOrdemDeServicoScheduleArrayList((ArrayList) ordemDeServicoList);

            boolean isLast = false;
            for (int i = 0; i < ordemDeServicoList.size(); i++) {
                if (i == ordemDeServicoList.size() - 1)
                    isLast = true;
                OrdemDeServico ordemDeServico = ordemDeServicoList.get(i);
                if (ordemDeServico.getLatitude() == null || ordemDeServico.getLongitude() == null || myLocation == null)
                    presenter.loadOsDistance(null, null, ordemDeServico, isLast);
                else
                    presenter.loadOsDistance(myLocation.getLatitude(), myLocation.getLongitude(), ordemDeServico, isLast);
            }
        }
    }

    private void loadMainScheduleOs(List<OrdemDeServico> ordemDeServicoList) {
        if (this.getActivity() != null) {
            this.ordemDeServicoList = selectTodayOs((ArrayList) ordemDeServicoList);

            this.filtredList = ValenetUtils.filterList(this.ordemDeServicoList, selectedFilters, this.getContext());

            if (this.filtredList == null || this.filtredList.size() == 0) {
                this.hideLoading();
                this.hidePager();
                this.showEmptyListView();
            } else {
                if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true);
                else if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME, true);
                else
                    adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_TIME, true);

                recyclerViewScheduleOs.setAdapter(adapter);
                recyclerViewScheduleOs.setLayoutManager(new LinearLayoutManager(this.getContext()));
                recyclerViewScheduleOs.setItemAnimator(new DefaultItemAnimator());
                this.hideLoading();
                this.hideEmptyListView();
                this.showPager();
            }
        }
    }

    // region useless functions interface

    @Override
    public void loadAppConfig(List<AppConfig> appConfigs) {

    }

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
    public void loadNextListOs(List<OrdemDeServico> ordemDeServicoList) {

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
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
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
            intent.putExtra(ValenetUtils.KEY_CAME_FROM_SCHEDULE, true);
            this.getActivity().startActivityForResult(intent, REQ_CODE_SEARCH);
        }
    }

    @Override
    public void navigateToOsMap() {
        if (this.getActivity() != null) {
            Intent intent = new Intent(this.getActivity(), MapsActivity.class);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, this.osTypeModelArrayList);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST, this.ordemDeServicoList);
            intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
            intent.putExtra(ValenetUtils.KEY_CAME_FROM_SCHEDULE, true);
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
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
                                                return true;
                                            } else {
                                                presenter.loadOsList(1.1, 1.1,
                                                        LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
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

    private ArrayList<OrdemDeServico> selectTodayOs(ArrayList<OrdemDeServico> ordemDeServicoArrayList) {
        if (ordemDeServicoArrayList != null && ordemDeServicoArrayList.size() > 0) {
            ArrayList<OrdemDeServico> todayOrdemDeServicoList = new ArrayList<>();
            Date currentDate = Calendar.getInstance().getTime();
            for (int i = 0; i < ordemDeServicoArrayList.size(); i++) {
                OrdemDeServico ordemDeServico = ordemDeServicoArrayList.get(i);
                if (ordemDeServico.getDataAgendamento() != null) {
                    Date osDate = DateUtils.parseDate(ordemDeServico.getDataAgendamento());
                    if (DateUtils.isDateBeforeOrEqual(osDate, currentDate))
                        todayOrdemDeServicoList.add(ordemDeServico);
                }
            }
            return todayOrdemDeServicoList;
        } else
            return null;
    }
}
