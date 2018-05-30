package valenet.com.br.gestordeos.os_history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsHistoryFragment extends Fragment implements OsHistory.OsHistoryView {


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
    Unbinder unbinder;

    public OsHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_os_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        refreshLayoutHistoryOs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

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
        if(refreshLayoutHistoryOs != null)
            refreshLayoutHistoryOs.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLayoutHistory() {
        if(refreshLayoutHistoryOs != null)
            refreshLayoutHistoryOs.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        if(loadingView != null)
            loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if(refreshLayoutHistoryOs != null && refreshLayoutHistoryOs.isRefreshing())
            refreshLayoutHistoryOs.setRefreshing(false);
        if(loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorServerView() {
        if(layoutErrorServer != null)
            layoutErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorServerView() {
        if(layoutErrorServer != null)
            layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void showErrorConectionView() {
        if(layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideErrorConectionView() {
        if(layoutErrorConection != null)
            layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyListView() {
        if(layoutEmptyList != null){
            textViewErrorEmptyList.setText("Não há OSs no histórico no momento!");
            layoutEmptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyListView() {
        if(layoutEmptyList != null)
            layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_try_again:
                break;
            case R.id.btn_try_again_server_error:
                break;
            case R.id.btn_reload:
                break;
        }
    }
}
