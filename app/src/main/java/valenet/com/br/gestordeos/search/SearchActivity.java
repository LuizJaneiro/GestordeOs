package valenet.com.br.gestordeos.search;

import android.content.Context;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.os_list.OsItemAdapter;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class SearchActivity extends AppCompatActivity {

    private final int RESULT_CODE_BACK = 1;
    private final int RESULT_CODE_BACK_SEARCH = 201;

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @BindView(R.id.recycler_view_search)
    RecyclerView recyclerViewSearch;

    private ImageButton searchBackBtn;
    private EditText searchEditText;
    private ArrayList<Os> filtredList;
    private ArrayList<Os> osList;
    private OsItemAdapter adapter;
    private MenuItem item;
    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_os_list));
        searchView.setHint("Buscar por Cliente");

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));

        filtredList = new ArrayList<>();
        osList = new ArrayList<>();

        filtredList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_FILTERED_LIST);
        osList = getIntent().getParcelableArrayListExtra(ValenetUtils.KEY_OS_LIST);

/*        if(filtredList == null || filtredList.size() == 0)
            //busca do banco a lista de os
            else*/

        if(filtredList == null || filtredList.size() == 0){
            setAdapter(osList);
        }else
            setAdapter(filtredList);

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
                        // TODO: handle exception
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
                    if(filtredList == null || filtredList.size() == 0)
                        setAdapter(osList);
                    else
                        setAdapter(filtredList);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!newText.isEmpty()) {
                    filter(newText, false);
                } else {
                    if(filtredList == null || filtredList.size() == 0)
                        setAdapter(osList);
                    else
                        setAdapter(filtredList);
                }
                return true;
            }
        });
    }

    public void setAdapter(ArrayList<Os> list) {
        adapter = new OsItemAdapter(list, this, this, myLocation);
        this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        this.recyclerViewSearch.setAdapter(adapter);
    }

    public void filter(String s, boolean submit) {
        ArrayList<Os> filteredList = new ArrayList<>();
        ArrayList<Os> osListArray = new ArrayList<>();
        if(filtredList != null && filtredList.size() > 0)
            osListArray = filtredList;
        else
            osListArray = osList;

        if (osListArray != null) {
            for (int i = 0; i < osListArray.size(); i++) {
                Os os = osListArray.get(i);
                String name = ValenetUtils.firstAndLastWord(os.getCliente()).toUpperCase();

                name = ValenetUtils.removeAccent(name).toUpperCase();
                s = ValenetUtils.removeAccent(s).toUpperCase();
                if (name.contains(s.toUpperCase()))
                    filteredList.add(os);
            }
        }

        if (filteredList.isEmpty()) {
            if (submit)
                Toasty.error(this, "Não há resultados para o termo pesquisado.", Toast.LENGTH_SHORT, true).show();
            adapter = new OsItemAdapter(osListArray, this, this, myLocation);
            this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
            this.recyclerViewSearch.setAdapter(adapter);
        } else {
            adapter = new OsItemAdapter(filteredList, this, this, myLocation);
            this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
            this.recyclerViewSearch.setAdapter(adapter);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
