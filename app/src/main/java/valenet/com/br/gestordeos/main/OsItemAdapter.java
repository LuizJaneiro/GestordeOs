package valenet.com.br.gestordeos.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.application.GestorDeOsApplication;
import valenet.com.br.gestordeos.client.ClientActivity;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.google_distance.Example;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class OsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Os> osList;
    private final Context context;
    private final Activity activity;
    private final String sortOsBy;
    private final HashMap<Integer, Integer> osDistanceHashmap;
    private Location myLocation;
    private GestorDeOsApplication application;

    public OsItemAdapter(List<Os> osList, Context context, Activity activity, Location myLocation, String sortOsBy, HashMap<Integer, Integer> osDistanceHashmap) {
        this.osList = osList;
        this.context = context;
        this.activity = activity;
        this.myLocation = myLocation;
        this.sortOsBy = sortOsBy;
        this.osDistanceHashmap = osDistanceHashmap;
        if(this.osList == null && this.osList.size() > 0 && this.osDistanceHashmap != null) {
            for (int i = 0; i < osList.size(); i++) {
                if(this.osDistanceHashmap.get(this.osList.get(i).getOsid()) == null)
                    this.osList.get(i).setDistance(null);
                else
                    this.osList.get(i).setDistance(this.osDistanceHashmap.get(this.osList.get(i).getOsid()).doubleValue());
            }
        }
        if (sortOsBy != null)
            sortOsList(sortOsBy);
    }

    private void sortOsList(String sortOsBy) {
        if (osList != null) {
            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_NAME)) {
                Collections.sort(osList, new Comparator<Os>() {
                    @Override
                    public int compare(Os o1, Os o2) {
                        return o1.getCliente().compareTo(o2.getCliente());
                    }
                });
            }

            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_DISTANCE)) {
                Collections.sort(osList, new Comparator<Os>() {
                    @Override
                    public int compare(Os o1, Os o2) {
                        Double distance1, distance2;
                        if (o1.getLongitude() == null || o1.getLatitude() == null)
                            distance1 = Double.MAX_VALUE;
                        else {
                            Location location1 = new Location("");
                            location1.setLatitude(o1.getLatitude());
                            location1.setLongitude(o1.getLongitude());
                            distance1 = (double) myLocation.distanceTo(location1);
                        }

                        if (o2.getLongitude() == null || o2.getLatitude() == null)
                            distance2 = Double.MAX_VALUE;
                        else {
                            Location location2 = new Location("");
                            location2.setLatitude(o2.getLatitude());
                            location2.setLongitude(o2.getLongitude());
                            distance2 = (double) myLocation.distanceTo(location2);
                        }

                        return distance1.compareTo(distance2);
                    }
                });
            }

            if (sortOsBy.equals(ValenetUtils.SHARED_PREF_KEY_OS_TIME)) {
                Collections.sort(osList, new Comparator<Os>() {
                    @Override
                    public int compare(Os o1, Os o2) {
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
        final Os item = osList.get(position);
        String clientName;
        String osType;
        String distance;
        String dateString = "";

        if (item.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(item.getCliente());

        if (item.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = item.getTipoAtividade();

        Integer osDistance = null;
        if(osDistanceHashmap != null)
            osDistance = osDistanceHashmap.get(item.getOsid());

        if (item.getLatitude() == null || item.getLongitude() == null || item.getDistance() == null || myLocation == null
                || osDistance == null)
            distance = "-";
        else {
            double distanceDouble = osDistance.doubleValue() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if(distanceDouble >= 100)
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
        final ImageView imageViewStatusOs;
        final ViewGroup osItemView;

        MViewHolder(View container) {
            super(container);
            this.textViewClientName = container.findViewById(R.id.text_view_client_name_toolbar);
            this.textViewType = container.findViewById(R.id.text_view_os_type_toolbar);
            this.textViewDate = container.findViewById(R.id.text_view_os_date_toolbar);
            this.textViewDistance = container.findViewById(R.id.text_view_distance_toolbar);
            this.imageViewStatusOs = container.findViewById(R.id.image_view_status_os);
            this.osItemView = container.findViewById(R.id.os_item_view);
        }
    }

}
