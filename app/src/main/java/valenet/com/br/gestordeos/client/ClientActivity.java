package valenet.com.br.gestordeos.client;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class ClientActivity extends AppCompatActivity {

    @BindView(R.id.text_view_toolbar_title)
    TextView textViewToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.text_view_client_name_toolbar)
    TextView textViewClientNameToolbar;
    @BindView(R.id.text_view_os_type_toolbar)
    TextView textViewOsTypeToolbar;
    @BindView(R.id.text_view_os_date_toolbar)
    TextView textViewOsDateToolbar;
    @BindView(R.id.text_view_distance_toolbar)
    TextView textViewDistanceToolbar;
    @BindView(R.id.btn_call)
    AppCompatButton btnCall;
    @BindView(R.id.footer_layout)
    RelativeLayout footerLayout;

    private PagerAdapter pagerAdapter;
    private Os os;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        textViewToolbarTitle.setText(getResources().getString(R.string.title_activity_client));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        os = getIntent().getParcelableExtra(ValenetUtils.KEY_OS);

        String clientName;
        String osType;
        String distance;
        String dateString = "";

        if(os.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(os.getCliente());

        if(os.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = os.getTipoAtividade();

        if(os.getDistance() == null)
            distance = "?";
        else {
            double distanceDouble = os.getDistance() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if(distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if(os.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(os.getDataAgendamento());
        }

        textViewClientNameToolbar.setText(clientName);
        textViewDistanceToolbar.setText(distance + " KM");
        textViewOsTypeToolbar.setText(osType);
        textViewOsDateToolbar.setText(dateString);

        tabLayout.addTab(tabLayout.newTab().setText("OS"));
        tabLayout.addTab(tabLayout.newTab().setText("Cliente"));
        tabLayout.addTab(tabLayout.newTab().setText("Observações"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), os, tabLayout.getTabCount());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem os) {
        switch (os.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(os);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick({R.id.btn_call, R.id.footer_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_call:
                break;
            case R.id.footer_layout:
                break;
        }
    }
}