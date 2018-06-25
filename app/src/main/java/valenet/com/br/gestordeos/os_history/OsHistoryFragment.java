package valenet.com.br.gestordeos.os_history;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.MainActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.realm.LoginLocal;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsHistoryFragment extends Fragment implements OsHistory.OsHistoryView, MainActivity.navigateInterface {


    @BindView(R.id.recycler_view_history_os)
    RecyclerView recyclerViewHistoryOs;
    @BindView(R.id.refresh_layout_history_os)
    SwipeRefreshLayout refreshLayoutHistoryOs;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.text_view_error_internet_connection)
    TextView textViewErrorInternetConnection;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.text_view_error_from_server)
    TextView textViewErrorFromServer;
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
    @BindView(R.id.text_view_loading)
    TextView textViewLoading;
    Unbinder unbinder;


    private final int REQ_CODE_SEARCH = 200;

    private OsItemHistoryAdapter adapter;
    private OsHistory.OsHistoryPresenter presenter;
    private List<Os> osHistoryList;

    public OsHistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_os_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        this.presenter = new OsHistoryPresenterImp(this);
        this.osHistoryList = null;

        if (LoginLocal.getInstance() != null) {
            presenter.loadHistoryUser(LoginLocal.getInstance().getCurrentUser().getCoduser(), false);
        }
        refreshLayoutHistoryOs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (LoginLocal.getInstance() != null) {
                    presenter.loadHistoryUser(LoginLocal.getInstance().getCurrentUser().getCoduser(), true);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showLayoutHistory() {
        if (refreshLayoutHistoryOs != null)
            refreshLayoutHistoryOs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutHistory() {
        if (refreshLayoutHistoryOs != null)
            refreshLayoutHistoryOs.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        if (loadingView != null && textViewLoading != null) {
            textViewLoading.setText("Carregando seu histórico...");
            loadingView.setVisibility(View.VISIBLE);
            textViewLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (refreshLayoutHistoryOs != null && refreshLayoutHistoryOs.isRefreshing())
            refreshLayoutHistoryOs.setRefreshing(false);
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
    public void showErrorConectionView() {
        if (layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorConectionView() {
        if (layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyListView() {
        if (layoutEmptyList != null) {
            textViewErrorEmptyList.setText("Não há OSs no seu histórico no momento!");
            layoutEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListView() {
        if (layoutEmptyList != null)
            layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadOsHistoryList(List<Os> osHistoryList) {
        if (this.getActivity() != null) {
            this.osHistoryList = osHistoryList;
            this.adapter = new OsItemHistoryAdapter(osHistoryList, this.getActivity().getApplicationContext(), this.getActivity());
            if (recyclerViewHistoryOs != null) {
                recyclerViewHistoryOs.setAdapter(adapter);
                recyclerViewHistoryOs.setLayoutManager(new LinearLayoutManager(this.getContext()));
                recyclerViewHistoryOs.setItemAnimator(new DefaultItemAnimator());
                this.showLayoutHistory();
            }
        }
    }

    // begin region MainNavigate interface

    @Override
    public void navigateToOsSearch() {
        if (this.getActivity() != null) {
            Intent intent = new Intent(this.getActivity(), SearchActivity.class);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST, (ArrayList) osHistoryList);
            intent.putParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST, null);
            intent.putExtra(ValenetUtils.KEY_CAME_FROM_OS_HISTORY, true);
            this.getActivity().startActivityForResult(intent, REQ_CODE_SEARCH);
        }
    }

    @Override
    public void navigateToOsMap() {

    }

    // end region MainNavigate interface

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                if (LoginLocal.getInstance() != null) {
                    presenter.loadHistoryUser(LoginLocal.getInstance().getCurrentUser().getCoduser(), false);
                }
                break;
        }
    }
}
