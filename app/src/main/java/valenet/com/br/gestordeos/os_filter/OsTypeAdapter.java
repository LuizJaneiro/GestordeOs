package valenet.com.br.gestordeos.os_filter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OsFilter.OsFilterView.selectedFiltersListener {

    private List<OsTypeModel> typesList;
    private HashMap<String, Boolean> isSelectedFilterList;
    private List<Os> osList;
    private final Context context;

    public OsTypeAdapter(Context context, List<OsTypeModel> typesList, List<Os> osList, int osType){
        this.context = context;
        SharedPreferences sharedPref = this.context.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);

        this.typesList = new ArrayList<>();
        this.isSelectedFilterList = new HashMap<>();
        this.osList = osList;
        if(typesList != null && typesList.size() > 0){
            for(OsTypeModel model : typesList){
                if(osType == ValenetUtils.GROUP_OS_MERCANTIL && model.getTipoMercantil()){
                    this.typesList.add(model);
                    this.isSelectedFilterList.put(model.getDescricao(),
                                                sharedPref.getBoolean(model.getDescricao(), true));
                }else if(osType == ValenetUtils.GROUP_OS_CORRETIVA && !model.getTipoMercantil()){
                    this.typesList.add(model);
                    this.isSelectedFilterList.put(model.getDescricao(),
                            sharedPref.getBoolean(model.getDescricao(), true));
                }
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

        ((MViewHolder) holder).btnOsFilter.setText(item.getDescricao());
        renderButton(item, ((MViewHolder) holder).btnOsFilter);

        ((MViewHolder) holder).btnOsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSelected = !isSelectedFilterList.get(item.getDescricao());
                isSelectedFilterList.put(item.getDescricao(), isSelected);

                SharedPreferences sharedPref = context.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean(item.getDescricao(), isSelected);
                editor.apply();
                renderButton(item, ((MViewHolder) holder).btnOsFilter);
                notifyDataSetChanged();
            }
        });
        ClickGuard.guard(((MViewHolder) holder).btnOsFilter);
    }

    @Override
    public int getItemCount() {
        if (this.typesList != null) {
            return typesList.size();
        }
        return 0;
    }

    private void renderButton(OsTypeModel osTypeModel, AppCompatButton button){
        Boolean isSelected = this.isSelectedFilterList.get(osTypeModel.getDescricao());
        final int sdk = Build.VERSION.SDK_INT;
        if (isSelected) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN)
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_btn_blue_fill));
            else
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_blue_fill));
            button.setTextColor(context.getResources().getColor(R.color.btn_filter_text_color_selected));
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN)
                button.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.background_btn_blue));
            else
                button.setBackground(ContextCompat.getDrawable(context, R.drawable.background_btn_blue));
            button.setTextColor(context.getResources().getColor(R.color.text_btn));
        }
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        final AppCompatButton btnOsFilter;

        MViewHolder(View container) {
            super(container);
            this.btnOsFilter = container.findViewById(R.id.btn_filter_item);
        }
    }

    @Override
    public List<Os> filterList() {
        List<Os> newOsList = new ArrayList<>();
        Set<String> keys = isSelectedFilterList.keySet();

        for(String key : keys){
            boolean isSelected = isSelectedFilterList.get(key);
            if(isSelected){
                for(Os os : osList){
                    if(os.getTipoAtividade().toUpperCase().equals(key.toUpperCase())){
                        newOsList.add(os);
                    }
                }
            }
        }

        return newOsList;
    }
}
