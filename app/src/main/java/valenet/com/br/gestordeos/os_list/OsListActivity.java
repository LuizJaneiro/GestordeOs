package valenet.com.br.gestordeos.os_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.Os;
import valenet.com.br.gestordeos.search.SearchActivity;
import valenet.com.br.gestordeos.utils.ValenetUtils;
import xyz.sahildave.widget.SearchViewLayout;

public class OsListActivity extends AppCompatActivity implements OsList.OsListView{

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_os)
    RecyclerView recyclerViewOs;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.search_view_container)
    SearchViewLayout searchViewContainer;

    private ArrayList<Os> filtredList;
    private ArrayList<Os> osList;

    private OsItemAdapter adapter;

    private final int REQ_CODE_SEARCH = 200;
    private final int RESULT_CODE_BACK_SEARCH = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_list));

        searchViewContainer.handleToolbarAnimation(toolbar);
        searchViewContainer.setHint("Buscar por nome");
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

        filtredList = new ArrayList<>();

        osList = new ArrayList<>();
        Date date = new Date();
        date.setDate(16);
        date.setMonth(10);
        date.setYear(2018);
        for (int i = 0; i < 30; i++)
            osList.add(new Os(2.5, date, "Corretiva Física", "Maria Lurdes"));
        osList.add(new Os(2.5, date, "Corretiva Física", "Luiz Janeiro"));

        adapter = new OsItemAdapter(osList, this);
        recyclerViewOs.setAdapter(adapter);
        recyclerViewOs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOs.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void navigateToSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(ValenetUtils.KEY_FILTERED_LIST, filtredList);
        startActivityForResult(intent, REQ_CODE_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_CODE_SEARCH) {
            if (resultCode == RESULT_CODE_BACK_SEARCH) {
                if (filtredList == null || filtredList.size() == 0)
                    adapter = new OsItemAdapter(osList, this);
                else
                    adapter = new OsItemAdapter(filtredList, this);
                this.recyclerViewOs.setAdapter(adapter);

                searchViewContainer.collapse();
            }
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
