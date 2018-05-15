package valenet.com.br.gestordeos.os_schedule;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OsScheduleFragment extends Fragment implements MainActivity.pagerInterface {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.os_schedule_fragment_view)
    RelativeLayout osScheduleFragmentView;
    Unbinder unbinder;
    private OsSchedulePagerAdapter pagerAdapter;

    public OsScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_os_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setOsListPagerAdapter() {
        if(this.getActivity() != null) {
            pagerAdapter = new OsSchedulePagerAdapter(this.getActivity().getSupportFragmentManager(), ((MainActivity) this.getActivity()).getMyLocation(),
                                                    ((MainActivity) this.getActivity()).getOrderFilters(), ((MainActivity) this.getActivity()).getFilters(),
                                                    ((MainActivity) this.getActivity()).getOsTypeModelList(), ((MainActivity) this.getActivity()).getTabLayoutToolbarSearchable().getTabCount());
            pager.setAdapter(pagerAdapter);
            pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(((MainActivity) this.getActivity()).getTabLayoutToolbarSearchable()) {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch (position) {
                        case 0:
                            OsScheduleTodayFragment fragmentToday = (OsScheduleTodayFragment) pagerAdapter.getRegisteredFragment(position);
                            //fragment.setOsListNavigation();
                            break;
                        case 1:
                            OsScheduleTomorrowFragment fragmentTomorrow = (OsScheduleTomorrowFragment) pagerAdapter.getRegisteredFragment(position);
                            //fragmentSchedule.setOsListNavigation();
                            break;
                        case 2:
                            OsScheduleNextDaysFragment fragmentNextDays = (OsScheduleNextDaysFragment) pagerAdapter.getRegisteredFragment(position);
                            //fragmentSchedule.setOsListNavigation();
                            break;
                    }
                }
            });
            ((MainActivity) this.getActivity()).getTabLayoutToolbarSearchable().setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
    }

    public void setMainPagerAdapter(){
        ((MainActivity)this.getActivity()).setPagerInterface(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
