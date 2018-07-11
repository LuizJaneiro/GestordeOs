package valenet.com.br.gestordeos.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.OsItemAdapter;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.os_history.OsItemHistoryAdapter;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class SearchActivity extends AppCompatActivity {

    private final int RESULT_CODE_BACK = 1;
    private final int RESULT_CODE_BACK_SEARCH = 201;

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar_basic)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.toolbar_search_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.recycler_view_search)
    RecyclerView recyclerViewSearch;

    private ImageButton searchBackBtn;
    private EditText searchEditText;
    private ArrayList<OrdemDeServico> filtredList;
    private ArrayList<OrdemDeServico> searchList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;
    private OsItemAdapter adapter;
    private OsItemHistoryAdapter adapterHistory;
    private MenuItem item;
    private Location myLocation;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;
    private HashMap<Integer, OsDistanceAndPoints> osDistanceHashMap = null;
    private Boolean cameFromHistoryFragment;
    private boolean cameFromSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_search));
        searchView.setHint(getString(R.string.text_search_box));

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));

        filtredList = new ArrayList<>();
        searchList = new ArrayList<>();
        orderFilters = new HashMap<>();
        selectedFilters = new HashMap<>();

        SharedPreferences sharedPref = getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_TIME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_TIME, true));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, false));
        this.orderFilters.put(ValenetUtils.SHARED_PREF_KEY_OS_NAME,
                sharedPref.getBoolean(ValenetUtils.SHARED_PREF_KEY_OS_NAME, false));

        filtredList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST);
        osTypeModelArrayList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_TYPE_LIST);
        myLocation = getIntent().getParcelableExtra(ValenetUtils.KEY_USER_LOCATION);
        osDistanceHashMap = (HashMap<Integer, OsDistanceAndPoints>) getIntent().getSerializableExtra(ValenetUtils.KEY_OS_DISTANCE_HASHMAP);
        cameFromHistoryFragment = getIntent().getBooleanExtra(ValenetUtils.KEY_CAME_FROM_OS_HISTORY, false);
        cameFromSchedule = getIntent().getBooleanExtra(ValenetUtils.KEY_CAME_FROM_SCHEDULE, false);


        if(filtredList == null || osTypeModelArrayList == null) {
            //TODO carrega do banco de dados a lista de os
        } else {
            if (osTypeModelArrayList != null && osTypeModelArrayList.size() > 0) {
                for (OsTypeModel model : osTypeModelArrayList) {
                    this.selectedFilters.put(model.getDescricao(),
                            sharedPref.getBoolean(model.getDescricao(), true));
                }
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, android.R.anim.slide_out_right);
            }
        });

        searchBackBtn = searchView.findViewById(R.id.action_up_btn);
        searchBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchEditText = searchView.findViewById(R.id.searchTextView);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        filter(searchEditText.getText().toString(), true);
                    } catch (Exception e) {
                    }
                    return true;
                }
                return false;
            }
        });
        setupSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_select_item, menu);

        item = menu.findItem(R.id.menu_search);
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return false;
            }
        });
        searchView.setMenuItem(item);

        onOptionsItemSelected(item);

        item.setEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            searchView.showSearch(false);
            return true;
        }
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_CODE_BACK_SEARCH, resultIntent);
        finish();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    filter(query, true);
                } else {
                    if(searchList == null || searchList.size() == 0)
                        setAdapter(filtredList);
                    else
                        setAdapter(searchList);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.isEmpty()) {
                    filter(newText, false);
                } else {
                    if(searchList == null || searchList.size() == 0)
                        setAdapter(filtredList);
                    else
                        setAdapter(searchList);
                }
                return true;
            }
        });
    }

    public void setAdapter(ArrayList<OrdemDeServico> list) {
        if(!cameFromHistoryFragment) {
            if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
                adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE, osDistanceHashMap, cameFromSchedule);
            else if (this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
                adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME, osDistanceHashMap, cameFromSchedule);
            else
                adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_TIME, osDistanceHashMap, cameFromSchedule);
            this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
            this.recyclerViewSearch.setAdapter(adapter);
        } else {
            adapterHistory = new OsItemHistoryAdapter(list, this, this);
            this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
            this.recyclerViewSearch.setAdapter(adapterHistory);
        }
    }

    public void filter(String s, boolean submit) {
        ArrayList<OrdemDeServico> filteredList = new ArrayList<>();
        ArrayList<OrdemDeServico> ordemDeServicoListArray = new ArrayList<>();
        if(searchList != null && searchList.size() > 0)
            ordemDeServicoListArray = searchList;
        else
            ordemDeServicoListArray = filtredList;

        if (ordemDeServicoListArray != null) {
            for (int i = 0; i < ordemDeServicoListArray.size(); i++) {
                OrdemDeServico ordemDeServico = ordemDeServicoListArray.get(i);
                if(s.matches("[0-9]+")){
                    Integer id = ordemDeServico.getOsid();
                    String idString = id.toString();
                    if(idString.contains(s))
                        filteredList.add(ordemDeServico);
                } else {
                    String name = ValenetUtils.firstAndLastWord(ordemDeServico.getCliente()).toUpperCase();
                    String type = ValenetUtils.removeAccent(ordemDeServico.getTipoAtividade()).toUpperCase();

                    name = ValenetUtils.removeAccent(name).toUpperCase();
                    s = ValenetUtils.removeAccent(s).toUpperCase();
                    if (name.contains(s.toUpperCase()) || type.contains(s.toUpperCase()))
                        filteredList.add(ordemDeServico);
                }
            }
        }

        if (filteredList.isEmpty()) {
            if (submit)
                Toasty.error(this, "Não há resultados para o termo pesquisado.", Toast.LENGTH_LONG, true).show();
            setAdapter(ordemDeServicoListArray);
        } else {
            setAdapter(filteredList);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
