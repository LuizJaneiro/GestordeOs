package valenet.com.br.gestordeos.os_history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.client.ClientActivity;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsItemHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrdemDeServico> ordemDeServicoList;
    private final Context context;
    private final Activity activity;

    public OsItemHistoryAdapter(List<OrdemDeServico> ordemDeServicoList, Context context, Activity activity) {
        this.ordemDeServicoList = ordemDeServicoList;
        this.context = context;
        this.activity = activity;

        if (this.ordemDeServicoList != null && this.ordemDeServicoList.size() > 0) {
            Collections.sort(ordemDeServicoList, new Comparator<OrdemDeServico>() {
                @Override
                public int compare(OrdemDeServico o1, OrdemDeServico o2) {
                    Date date1, date2;
                    if (o1.getDataAgendamento() == null)
                        date1 = new Date(Long.MAX_VALUE);
                    else {
                        String dateString = ValenetUtils.convertJsonToStringDate(o1.getDataAgendamento());
                        date1 = ValenetUtils.convertStringToDate(dateString);
                    }

                    if (o2.getDataAgendamento() == null)
                        date2 = new Date(Long.MAX_VALUE);
                    else {
                        String dateString = ValenetUtils.convertJsonToStringDate(o2.getDataAgendamento());
                        date2 = ValenetUtils.convertStringToDate(dateString);
                    }

                    return date1.compareTo(date2);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        v = inflater.inflate(R.layout.card_os_history_item, parent, false);
        viewHolder = new MViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final OrdemDeServico item = ordemDeServicoList.get(position);
        String clientName;
        String osType;
        String dateString = "";

        if (item.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(item.getCliente());

        if (item.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = item.getTipoAtividade();

        if (item.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(item.getDataAgendamento()) + " - " +
                    ValenetUtils.convertJsonToStringHour(item.getDataAgendamento());
        }

        if (item.getStatusOs() == null)
            ((MViewHolder) holder).imageViewStatusOs.setVisibility(View.GONE);
        else {
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("AGUARDANDO"))
                ((MViewHolder) holder).imageViewStatusOs.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_awaiting_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CONCLUIDO"))
                ((MViewHolder) holder).imageViewStatusOs.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_closed_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CANCELADO"))
                ((MViewHolder) holder).imageViewStatusOs.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_refused_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("BLOQUEADA"))
                ((MViewHolder) holder).imageViewStatusOs.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_blocked_os));
        }

        ((MViewHolder) holder).textViewClientName.setText(clientName);

        ((MViewHolder) holder).textViewType.setText(osType);
        ((MViewHolder) holder).textViewDate.setText(dateString);

        ((MViewHolder) holder).osItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ClientActivity.class);
                intent.putExtra(ValenetUtils.KEY_OS, item);
                intent.putExtra(ValenetUtils.KEY_CAME_FROM_OS_HISTORY, true);
                activity.startActivity(intent);
            }
        });
        ClickGuard.guard(((MViewHolder) holder).osItemView);
    }

    @Override
    public int getItemCount() {
        if (ordemDeServicoList != null) {
            return ordemDeServicoList.size();
        }
        return 0;
    }

    class MViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewClientName;
        final TextView textViewType;
        final TextView textViewDate;
        final ImageView imageViewStatusOs;
        final ViewGroup osItemView;

        MViewHolder(View container) {
            super(container);
            this.textViewClientName = container.findViewById(R.id.text_view_client_name_toolbar);
            this.textViewType = container.findViewById(R.id.text_view_os_type_toolbar);
            this.textViewDate = container.findViewById(R.id.text_view_os_date_toolbar);
            this.imageViewStatusOs = container.findViewById(R.id.image_view_status_os);
            this.osItemView = container.findViewById(R.id.os_item_view);
        }
    }
}
