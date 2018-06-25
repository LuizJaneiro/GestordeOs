package valenet.com.br.gestordeos.refuse_os;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import valenet.com.br.gestordeos.R;

public class SpinnerAdapterOsReasons extends ArrayAdapter<String> {
    private ArrayList mData;
    private LayoutInflater mInflater;

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);

    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        mData = (ArrayList) objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SpinnerAdapterOsReasons(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.refuse_os_spinner_layout, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinnerTarget);
        label.setText(mData.get(position).toString());


        //Set meta data here and later we can access these values from OnItemSelected Event Of Spinner
        row.setTag(R.string.text_meta_position, Integer.toString(position));
        row.setTag(R.string.text_meta_title, mData.get(position).toString());

        return row;
    }

    @Override
    public void add(@Nullable String object) {
        super.add(object);
        mData.add(object);
    }

    @Override
    public int getCount() {
        int count = super.getCount();

        return count>0 ? count : count ;
    }
}
