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
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.OsItemAdapter;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
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
    private ArrayList<Os> filtredList;
    private ArrayList<Os> searchList;
    private ArrayList<OsTypeModel> osTypeModelArrayList;
    private OsItemAdapter adapter;
    private MenuItem item;
    private Location myLocation;

    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_list));
        searchView.setHint("Buscar por Os (Id, Tipo ou Cliente)");

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

    public void setAdapter(ArrayList<Os> list) {
        if(this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE))
            adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE);
        else if(this.orderFilters.get(ValenetUtils.SHARED_PREF_KEY_OS_NAME))
            adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_NAME);
        else
            adapter = new OsItemAdapter(list, this, this, myLocation, ValenetUtils.SHARED_PREF_KEY_OS_TIME);
        this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        this.recyclerViewSearch.setAdapter(adapter);
    }

    public void filter(String s, boolean submit) {
        ArrayList<Os> filteredList = new ArrayList<>();
        ArrayList<Os> osListArray = new ArrayList<>();
        if(searchList != null && searchList.size() > 0)
            osListArray = searchList;
        else
            osListArray = filtredList;

        if (osListArray != null) {
            for (int i = 0; i < osListArray.size(); i++) {
                Os os = osListArray.get(i);
                if(s.matches("[0-9]+")){
                    Integer id = os.getOsid();
                    String idString = id.toString();
                    if(idString.contains(s))
                        filteredList.add(os);
                } else {
                    String name = ValenetUtils.firstAndLastWord(os.getCliente()).toUpperCase();
                    String type = ValenetUtils.removeAccent(os.getTipoAtividade()).toUpperCase();

                    name = ValenetUtils.removeAccent(name).toUpperCase();
                    s = ValenetUtils.removeAccent(s).toUpperCase();
                    if (name.contains(s.toUpperCase()) || type.contains(s.toUpperCase()))
                        filteredList.add(os);
                }
            }
        }

        if (filteredList.isEmpty()) {
            if (submit)
                Toasty.error(this, "Não há resultados para o termo pesquisado.", Toast.LENGTH_SHORT, true).show();
            setAdapter(osListArray);
        } else {
            setAdapter(filteredList);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
