package valenet.com.br.gestordeos.os_filter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsFilterActivity extends AppCompatActivity implements OsFilter.OsFilterView {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar_basic)
    Toolbar toolbar;
    @BindView(R.id.layout_os_filter)
    ViewGroup layoutOsFilter;
    @BindView(R.id.btn_reload)
    AppCompatButton btnReload;
    @BindView(R.id.layout_empty_list)
    RelativeLayout layoutEmptyList;
    @BindView(R.id.btn_try_again)
    AppCompatButton btnTryAgain;
    @BindView(R.id.layout_error_conection)
    RelativeLayout layoutErrorConection;
    @BindView(R.id.btn_try_again_server_error)
    AppCompatButton btnTryAgainServerError;
    @BindView(R.id.layout_error_server)
    RelativeLayout layoutErrorServer;
    @BindView(R.id.loading_view)
    RelativeLayout loadingView;
    @BindView(R.id.recycler_btn_filters)
    RecyclerView recyclerBtnFilters;
    @BindView(R.id.layout_os_filter_list)
    LinearLayout layoutOsFilterList;
    @BindView(R.id.radio_button_item_time)
    AppCompatRadioButton radioButtonItemTime;
    @BindView(R.id.radio_button_item_distance)
    AppCompatRadioButton radioButtonItemDistance;
    @BindView(R.id.radio_button_item_name)
    AppCompatRadioButton radioButtonItemName;
    @BindView(R.id.radio_group_items)
    RadioGroup radioGroupItems;

    private final int REQ_CODE_BACK_FILTER = 203;

    private boolean cameFromMaps;

    private ArrayList<OsTypeModel> osTypeModelList;
    private OsFilter.OsFilterPresenter presenter;

    private boolean loadOsModelList = false;
    private OsTypeAdapter osTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_filter);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.presenter = new OsFilterPresenterImp(this);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_filter));

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        cameFromMaps = getIntent().getBooleanExtra(ValenetUtils.KEY_CAME_FROM_MAPS, false);
        if (cameFromMaps)
            layoutOsFilterList.setVisibility(View.GONE);
        else
            layoutOsFilterList.setVisibility(View.VISIBLE);

        radioButtonItemTime.setChecked(sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, true));
        radioButtonItemDistance.setChecked(sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false));
        radioButtonItemName.setChecked(sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));

        radioGroupItems.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if(checkedId == R.id.radio_button_item_distance){
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, true);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, false);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);
                    editor.apply();
                } else if(checkedId == R.id.radio_button_item_name) {
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, false);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, true);
                    editor.apply();
                } else { //date
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, true);
                    editor.putBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false);
                    editor.apply();
                }
            }
        });

        this.osTypeModelList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST);

/*        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerBtnFilters.setLayoutManager(gridLayoutManager);*/

        recyclerBtnFilters.setLayoutManager(new LinearLayoutManager(this));
        recyclerBtnFilters.setItemAnimator(new DefaultItemAnimator());

        if (osTypeModelList != null && osTypeModelList.size() > 0) {
            osTypeAdapter = new OsTypeAdapter(this, osTypeModelList);
            recyclerBtnFilters.setAdapter(osTypeAdapter);
            recyclerBtnFilters.setFocusable(false);
            layoutOsFilterList.requestFocus();
        }

        if (osTypeModelList == null || osTypeModelList.size() == 0)
            loadOsModelList = true;

        if (loadOsModelList)
            presenter.loadOsTypes();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(REQ_CODE_BACK_FILTER, resultIntent);
        finish();
    }

    @OnClick({R.id.btn_try_again, R.id.btn_try_again_server_error, R.id.btn_reload})
    public void onViewClicked(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        switch (view.getId()) {
            case R.id.btn_try_again:
            case R.id.btn_try_again_server_error:
            case R.id.btn_reload:
                presenter.loadOsTypes();
                break;
        }
    }

    @Override
    public void hideFilterView() {
        if (layoutOsFilter != null)
            this.layoutOsFilter.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorConectionView() {
        if (layoutErrorConection != null)
            this.layoutErrorConection.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorServerView() {
        if (layoutErrorServer != null)
            this.layoutErrorServer.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyListView() {
        if (layoutEmptyList != null)
            this.layoutEmptyList.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        if (loadingView != null)
            this.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFilterView() {
        if (layoutOsFilter != null) {
            this.layoutOsFilter.setVisibility(View.VISIBLE);
            this.layoutOsFilterList.requestFocus();
        }
    }

    @Override
    public void showErrorConectionView() {
        if (layoutErrorConection != null)
            this.layoutErrorConection.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorServerView() {
        if (layoutErrorServer != null)
            this.layoutErrorServer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyListView() {
        if (layoutEmptyList != null)
            this.layoutEmptyList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (loadingView != null)
            this.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void loadOsTypesList(List<OsTypeModel> osTypes) {
        this.osTypeModelList = (ArrayList) osTypes;
        osTypeAdapter = new OsTypeAdapter(this, osTypeModelList);
        recyclerBtnFilters.setAdapter(osTypeAdapter);
        recyclerBtnFilters.setFocusable(false);
        showFilterView();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
