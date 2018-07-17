package valenet.com.br.gestordeos.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.client.ClientActivity;
import valenet.com.br.gestordeos.model.entity.OrdemDeServico;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrdemDeServico> ordemDeServicoList;
    private final Context context;
    private final Activity activity;
    private final String sortOsBy;
    private Location myLocation;
    private GestorDeOsApplication application;
    private boolean cameFromSchedule;

    public OsItemAdapter(List<OrdemDeServico> ordemDeServicoList, Context context, Activity activity, Location myLocation, String sortOsBy, boolean cameFromSchedule) {
        this.ordemDeServicoList = ordemDeServicoList;
        this.context = context;
        this.activity = activity;
        this.myLocation = myLocation;
        this.sortOsBy = sortOsBy;
        this.cameFromSchedule = cameFromSchedule;

        if (this.ordemDeServicoList != null && this.ordemDeServicoList.size() > 0 && application.osDistanceHashMap != null) {
            for (int i = 0; i < ordemDeServicoList.size(); i++) {
                if (application.osDistanceHashMap.get(this.ordemDeServicoList.get(i).getOsid()) == null || application.osDistanceHashMap.get(this.ordemDeServicoList.get(i).getOsid()).getDistance() == null)
                    this.ordemDeServicoList.get(i).setDistance(null);
                else
                    this.ordemDeServicoList.get(i).setDistance(application.osDistanceHashMap.get(this.ordemDeServicoList.get(i).getOsid()).getDistance());
            }
        }
        if (sortOsBy != null)
            sortOsList(sortOsBy);
    }

    private void sortOsList(String sortOsBy) {
        if (ordemDeServicoList != null) {
            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_NAME)) {
                Collections.sort(ordemDeServicoList, new Comparator<OrdemDeServico>() {
                    @Override
                    public int compare(OrdemDeServico o1, OrdemDeServico o2) {
                        return o1.getCliente().compareTo(o2.getCliente());
                    }
                });
            }

            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE)) {
                Collections.sort(ordemDeServicoList, new Comparator<OrdemDeServico>() {
                    @Override
                    public int compare(OrdemDeServico o1, OrdemDeServico o2) {
                        Double distance1, distance2;
                        if (o1.getLongitude() == null || o1.getLatitude() == null || application.osDistanceHashMap == null
                                || application.osDistanceHashMap.get(o1.getOsid()) == null || application.osDistanceHashMap.get(o1.getOsid()).getDistance() == null)
                            distance1 = Double.MAX_VALUE;
                        else {
                            distance1 = (double) application.osDistanceHashMap.get(o1.getOsid()).getDistance();
                        }

                        if (o2.getLongitude() == null || o2.getLatitude() == null || application.osDistanceHashMap== null
                                || application.osDistanceHashMap.get(o2.getOsid()) == null || application.osDistanceHashMap.get(o2.getOsid()).getDistance() == null)
                            distance2 = Double.MAX_VALUE;
                        else {
                            distance2 = (double) application.osDistanceHashMap.get(o2.getOsid()).getDistance();
                        }

                        return distance1.compareTo(distance2);
                    }
                });
            }

            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_TIME)) {
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
        final OrdemDeServico item = ordemDeServicoList.get(position);
        String clientName;
        String osType;
        String distance;
        String dateString = "";
        String city;
        String address;
        String osId;

        if (item.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(item.getCliente());

        if (item.getCidade() == null)
            city = "Cidade Indefinida";
        else
            city = item.getCidade();

        if (item.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = item.getTipoAtividade();

        Integer osDistance = null;
        if (application.osDistanceHashMap != null && application.osDistanceHashMap.get(item.getOsid()) != null)
            osDistance = application.osDistanceHashMap.get(item.getOsid()).getDistance();

        address = ValenetUtils.buildOsAddress(item.getTpLogradouro(), item.getLogradouro(), item.getComplemento(), item.getNumero(), item.getAndar(), item.getBairro());

        if (item.getLatitude() == null || item.getLongitude() == null || item.getDistance() == null || myLocation == null
                || osDistance == null) {
            distance = "-";
        } else {
            double distanceDouble = osDistance.doubleValue() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if (distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if (item.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(item.getDataAgendamento()) + " - " +
                    ValenetUtils.convertJsonToStringHour(item.getDataAgendamento());
        }

        if(item.getOsid() == null) {
            osId = "-";
        } else {
            osId = item.getOsid() + "";
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
        ((MViewHolder) holder).textViewDistance.setText(distance + " KM");
        ((MViewHolder) holder).textViewOsId.setText(osId);

        ((MViewHolder) holder).textViewType.setText(osType);
        ((MViewHolder) holder).textViewDate.setText(dateString);
        ((MViewHolder) holder).textViewCity.setText(city);
        ((MViewHolder) holder).textViewAddress.setText(address);

        ((MViewHolder) holder).osItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ClientActivity.class);
                intent.putExtra(ValenetUtils.KEY_OS, item);
                intent.putExtra(ValenetUtils.KEY_CAME_FROM_SCHEDULE, cameFromSchedule);
                activity.startActivityForResult(intent, ValenetUtils.REQUEST_CODE_CLIENT);
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
        final TextView textViewDistance;
        final TextView textViewCity;
        final TextView textViewAddress;
        final TextView textViewOsId;
        final ImageView imageViewStatusOs;
        final ViewGroup osItemView;

        MViewHolder(View container) {
            super(container);
            this.textViewClientName = container.findViewById(R.id.text_view_client_name_toolbar);
            this.textViewType = container.findViewById(R.id.text_view_os_type_toolbar);
            this.textViewDate = container.findViewById(R.id.text_view_os_date_toolbar);
            this.textViewDistance = container.findViewById(R.id.text_view_distance_toolbar);
            this.imageViewStatusOs = container.findViewById(R.id.image_view_status_os);
            this.textViewCity = container.findViewById(R.id.text_view_os_city_toolbar);
            this.textViewAddress = container.findViewById(R.id.text_view_os_address_toolbar);
            this.textViewOsId = container.findViewById(R.id.text_view_os_id);
            this.osItemView = container.findViewById(R.id.os_item_view);
        }
    }

}
