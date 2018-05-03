package valenet.com.br.gestordeos.os_list.OsFragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.os_filter.OsFilterActivity;
import valenet.com.br.gestordeos.os_list.OsItemAdapter;
import valenet.com.br.gestordeos.os_list.OsList;
import valenet.com.br.gestordeos.os_list.OsListActivity;
import valenet.com.br.gestordeos.os_list.OsListPresenterImp;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleOsFragment extends Fragment implements OsList.OsListView, OsListActivity.navigateInterface {

    @BindView(R.id.recycler_view_schedule_os)
    RecyclerView recyclerViewNextOs;
    @BindView(R.id.refresh_layout_schedule_os)
    SwipeRefreshLayout refreshLayoutScheduleOs;
    Unbinder unbinder;
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
    @BindView(R.id.text_view_error)
    TextView textViewError;
    @BindView(R.id.btn_reload)
    AppCompatButton btnReload;
    @BindView(R.id.layout_empty_list)
    RelativeLayout layoutEmptyList;

    private Location myLocation;
    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;
    private final int REQ_CODE_FILTER = 202;
    private final int REQ_CODE_BACK_FILTER = 203;
    private ArrayList<Os> osList;
    private int osType;
    private ArrayList<Os> filtredList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;
    private OsList.OsListPresenter presenter;

    ReactiveLocationProvider locationProvider;
    private Subscription locationSubscription;
    private final static int REQUEST_CHECK_SETTINGS = 0;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;

    private OsItemAdapter adapter;


    public ScheduleOsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //((OsListActivity)this.getActivity()).setNavigateInterface(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_os, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new OsListPresenterImp(this);

        this.osType = getArguments().getInt(ValenetUtils.KEY_OS_TYPE);
        this.myLocation = getArguments().getParcelable(ValenetUtils.KEY_USER_LOCATION);
        this.osTypeModelArrayList = (ArrayList<OsTypeModel>) getArguments().getSerializable(ValenetUtils.KEY_OS_TYPE_LIST);
        this.orderFilters = (HashMap<String, Boolean>) getArguments().getSerializable(ValenetUtils.KEY_ORDER_FILTERS);
        this.selectedFilters = (HashMap<String, Boolean>) getArguments().getSerializable(ValenetUtils.KEY_FILTERS);

        refreshLayoutScheduleOs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myLocation != null)
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, true);
                else {
                    loadOsListWithouLocation();
                }
            }
        });

        if(myLocation == null)
            this.showErrorServerView();
        else
            presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                    LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);

        return view;
    }

    @Override
    public void navigateToSearch() {
        Intent intent = new Intent(this.getActivity(), SearchActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, filtredList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, osTypeModelArrayList);
        intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
        this.getActivity().startActivityForResult(intent, REQ_CODE_SEARCH);
    }

    @Override
    public void navigateToFilter() {
        Intent intent = new Intent(this.getActivity(), OsFilterActivity.class);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST, osList);
        intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, osTypeModelArrayList);
        intent.putExtra(ValenetUtils.KEY_OS_TYPE, osType);
        intent.putExtra(ValenetUtils.KEY_USER_LOCATION, myLocation);
        this.getActivity().startActivityForResult(intent, REQ_CODE_FILTER);
    }

    @Override
    public void hideOsListView() {
        this.refreshLayoutScheduleOs.setVisibility(View.GONE);
    }

    @Override
    public void showOsListView() {
        this.refreshLayoutScheduleOs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (refreshLayoutScheduleOs.isRefreshing())
            refreshLayoutScheduleOs.setRefreshing(false);
        else
            this.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorConectionView() {
        this.layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorConectionView() {
        this.layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void showErrorServerView() {
        this.layoutErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorServerView() {
        this.layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyListView() {
        this.layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyListView() {
        this.layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void showListOs(List<Os> osListAdapter) {
        this.osList = (ArrayList) osListAdapter;
        Set<String> keys = selectedFilters.keySet();
        this.filtredList = new ArrayList<>();
        for (String key : keys) {
            boolean isSelected = this.selectedFilters.get(key);
            if (isSelected) {
                for (Os os : osList) {
                    if (os.getTipoAtividade().toUpperCase().equals(key.toUpperCase())) {
                        filtredList.add(os);
                    }
                }
            }
        }

        if (this.filtredList.size() == 0) {
            this.hideOsListView();
            this.showEmptyListView();
        } else {
            if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE);
            else if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME);
            else
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DATE);

            recyclerViewNextOs.setAdapter(adapter);
            recyclerViewNextOs.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewNextOs.setItemAnimator(new DefaultItemAnimator());
            this.hideEmptyListView();
            this.showOsListView();
        }
    }

    @Override
    public void showListOsType(List<OsTypeModel> osTypes) {

    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                if(myLocation != null){
                    presenter.loadOsList(myLocation.getLatitude(), myLocation.getLongitude(),
                            LoginLocal.getInstance().getCurrentUser().getCoduser(), false, osType, false);
                } else {
                    loadOsListWithouLocation();
                }
                break;
        }
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

    private void loadOsListWithouLocation(){
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
                                            }else {
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

    @Override
    public void navigateToOsFilter() {
        this.navigateToFilter();
    }

    public void setOsListNavigation(){
        ((OsListActivity)this.getActivity()).setNavigateInterface(this);
    }

    @Override
    public void onActivityResultFilter(ArrayList<Os> filtredList) {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DATE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DATE, false));

        this.selectedFilters = new HashMap<>();

        if (this.osTypeModelArrayList != null && osTypeModelArrayList.size() > 0) {
            for (OsTypeModel model : this.osTypeModelArrayList) {
                this.selectedFilters.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));
            }
        }

        this.filtredList = filtredList;
        if (this.filtredList.size() == 0) {
            this.hideOsListView();
            this.showEmptyListView();
        } else {
            if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE);
            else if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME);
            else
                adapter = new OsItemAdapter(filtredList, this.getContext(), this.getActivity(), myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DATE);

            recyclerViewNextOs.setAdapter(adapter);
            recyclerViewNextOs.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recyclerViewNextOs.setItemAnimator(new DefaultItemAnimator());
            this.hideEmptyListView();
            this.showOsListView();
        }
    }

    @Override
    public void navigateToOsSearch() {
        this.navigateToSearch();
    }

}
