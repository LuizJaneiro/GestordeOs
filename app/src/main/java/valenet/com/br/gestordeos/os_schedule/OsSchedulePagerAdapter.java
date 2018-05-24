package valenet.com.br.gestordeos.os_schedule;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsSchedulePagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;
    private Location myLocation;
    private ArrayList<OsTypeModel> modelArrayList;
    private ArrayList<Os> osScheduleArrayList;
    private HashMap<String, Boolean> orderFilters;
    private HashMap<String, Boolean> selectedFilters;
    private Integer osType;
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public OsSchedulePagerAdapter(FragmentManager fm, Location myLocation,
                              HashMap<String, Boolean> orderFilters, HashMap<String, Boolean> selectedFilters,
                              ArrayList<OsTypeModel> modelArrayList, ArrayList<Os> osScheduleArrayList, Integer osType, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.myLocation = myLocation;
        this.orderFilters = orderFilters;
        this.selectedFilters = selectedFilters;
        this.osScheduleArrayList = osScheduleArrayList;
        this.modelArrayList = modelArrayList;
        this.osType = osType;
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ValenetUtils.KEY_USER_LOCATION, myLocation);
        bundle.putSerializable(ValenetUtils.KEY_ORDER_FILTERS, orderFilters);
        bundle.putSerializable(ValenetUtils.KEY_FILTERS, selectedFilters);
        bundle.putSerializable(ValenetUtils.KEY_OS_TYPE_LIST, modelArrayList);
        bundle.putSerializable(ValenetUtils.KEY_OS_LIST, osScheduleArrayList);
        bundle.putInt(ValenetUtils.KEY_OS_TYPE, osType);
        switch (position) {
            case 0:
                OsScheduleTodayFragment tab1 = new OsScheduleTodayFragment();
                tab1.setArguments(bundle);
                return tab1;
            case 1:
                OsScheduleTomorrowFragment tab2 = new OsScheduleTomorrowFragment();
                tab2.setArguments(bundle);
                return tab2;
            case 2:
                OsScheduleNextDaysFragment tab3 = new OsScheduleNextDaysFragment();
                tab3.setArguments(bundle);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }
}
