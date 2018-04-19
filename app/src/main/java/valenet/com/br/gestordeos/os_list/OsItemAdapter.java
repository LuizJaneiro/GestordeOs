package valenet.com.br.gestordeos.os_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.client.ClientActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Os> osList;
    private final Context context;
    private final Activity activity;

    public OsItemAdapter(List<Os> osList, Context context, Activity activity) {
        this.osList = osList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        v = inflater.inflate(R.layout.card_os_item, parent, false);
        viewHolder = new MViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Os item = osList.get(position);
        String clientName;
        String osType;
        String distance;
        String dateString = "";

        if(item.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(item.getCliente());

        if(item.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = item.getTipoAtividade();

        if(item.getDistance() == null)
            distance = "?";
        else {
            double distanceDouble = item.getDistance() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if(distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if(item.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(item.getDataAgendamento());
        }

        ((MViewHolder) holder).textViewClientName.setText(clientName);
        ((MViewHolder) holder).textViewDistance.setText(distance + " KM");

        ((MViewHolder) holder).textViewType.setText(osType);
        ((MViewHolder) holder).textViewDate.setText(dateString);

        ((MViewHolder) holder).osItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ClientActivity.class);
                intent.putExtra(ValenetUtils.KEY_OS, item);
                activity.startActivity(intent);
            }
        });
        ClickGuard.guard(((MViewHolder) holder).osItemView);
    }

    @Override
    public int getItemCount() {
        if (osList != null) {
            return osList.size();
        }
        return 0;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewClientName;
        final TextView textViewType;
        final TextView textViewDate;
        final TextView textViewDistance;
        final ViewGroup osItemView;

        MViewHolder(View container) {
            super(container);
            this.textViewClientName = container.findViewById(R.id.text_view_client_name_toolbar);
            this.textViewType = container.findViewById(R.id.text_view_os_type_toolbar);
            this.textViewDate = container.findViewById(R.id.text_view_os_date_toolbar);
            this.textViewDistance = container.findViewById(R.id.text_view_distance_toolbar);
            this.osItemView = container.findViewById(R.id.os_item_view);
        }
    }

}
