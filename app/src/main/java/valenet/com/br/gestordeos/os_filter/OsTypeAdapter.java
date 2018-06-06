package valenet.com.br.gestordeos.os_filter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OsTypeModel> typesList;
    private HashMap<String, Boolean> isSelectedFilterList;
    private final Context context;

    public OsTypeAdapter(Context context, List<OsTypeModel> typesList) {
        this.context = context;
        SharedPreferences sharedPref = this.context.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.typesList = new ArrayList<>();
        this.isSelectedFilterList = new HashMap<>();
        if (typesList != null && typesList.size() > 0) {
            for (OsTypeModel model : typesList) {
                this.typesList.add(model);
                this.isSelectedFilterList.put(model.getDescricao(),
                        sharedPref.getBoolean(model.getDescricao(), true));

            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        v = inflater.inflate(R.layout.os_filter_item, parent, false);
        viewHolder = new MViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final OsTypeModel item = typesList.get(position);

        ((MViewHolder) holder).textViewItemFilter.setText(item.getDescricao());
        renderButton(item, ((MViewHolder) holder).checkBoxItemFilter);

        ((MViewHolder) holder).layoutItemFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSelected = !isSelectedFilterList.get(item.getDescricao());
                ((MViewHolder) holder).checkBoxItemFilter.setChecked(isSelected);
            }
        });

        ((MViewHolder) holder).checkBoxItemFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean isSelected = !isSelectedFilterList.get(item.getDescricao());
                isSelectedFilterList.put(item.getDescricao(), isSelected);

                SharedPreferences sharedPref = context.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(item.getDescricao(), isSelected);
                editor.apply();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.typesList != null) {
            return typesList.size();
        }
        return 0;
    }

    private void renderButton(OsTypeModel osTypeModel, AppCompatCheckBox checkBox) {
        Boolean isSelected = this.isSelectedFilterList.get(osTypeModel.getDescricao());
        final int sdk = Build.VERSION.SDK_INT;
        if (isSelected) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        final AppCompatCheckBox checkBoxItemFilter;
        final TextView textViewItemFilter;
        final ViewGroup layoutItemFilter;

        MViewHolder(View container) {
            super(container);
            this.checkBoxItemFilter = container.findViewById(R.id.check_box_item_filter);
            this.textViewItemFilter = container.findViewById(R.id.text_view_item_filter);
            this.layoutItemFilter = container.findViewById(R.id.layout_item_filter);
        }
    }

/*    @Override
    public List<Os> filterNextOsList() {
        List<Os> newOsList = null;

        if (isSelectedFilterList != null && nextOsList != null) {
            newOsList = new ArrayList<>();

            Set<String> keys = isSelectedFilterList.keySet();
            for (String key : keys) {
                boolean isSelected = isSelectedFilterList.get(key);
                if (isSelected) {
                    for (Os os : nextOsList) {
                        String osTipoAtividade = ValenetUtils.removeAccent(os.getTipoAtividade()).toUpperCase();
                        String keyTratada = ValenetUtils.removeAccent(key).toUpperCase();
                        if (osTipoAtividade.equals(keyTratada)) {
                            newOsList.add(os);
                        }
                    }
                }
            }
        }
        return newOsList;
    }

    @Override
    public List<Os> filterScheduleOsList() {
        List<Os> newOsList = null;

        if (isSelectedFilterList != null && scheduleOsList != null) {
            newOsList = new ArrayList<>();
            Set<String> keys = isSelectedFilterList.keySet();

            for (String key : keys) {
                boolean isSelected = isSelectedFilterList.get(key);
                if (isSelected) {
                    for (Os os : scheduleOsList) {
                        String osTipoAtividade = ValenetUtils.removeAccent(os.getTipoAtividade()).toUpperCase();
                        String keyTratada = ValenetUtils.removeAccent(key).toUpperCase();
                        if (osTipoAtividade.equals(keyTratada)) {
                            newOsList.add(os);
                        }
                    }
                }
            }
        }
        return newOsList;
    }*/
}
