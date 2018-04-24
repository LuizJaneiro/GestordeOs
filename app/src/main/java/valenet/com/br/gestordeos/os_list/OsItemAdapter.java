package valenet.com.br.gestordeos.os_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    private Location myLocation;

    public OsItemAdapter(List<Os> osList, Context context, Activity activity, Location myLocation) {
        this.osList = osList;
        this.context = context;
        this.activity = activity;
        this.myLocation = myLocation;
        if(osList != null && myLocation != null)
            sortOsList();
    }

    private void sortOsList(){
        Collections.sort(osList, new Comparator<Os>() {
            @Override
            public int compare(Os o1, Os o2) {
                Double distance1, distance2;
                if(o1.getLongitude() == null || o1.getLatitude() == null)
                    distance1 = Double.MAX_VALUE;
                else {
                    Location location1 = new Location("");
                    location1.setLatitude(o1.getLatitude());
                    location1.setLongitude(o1.getLongitude());
                    distance1 = (double) myLocation.distanceTo(location1);
                }

                if(o2.getLongitude() == null || o2.getLatitude() == null)
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        RecyclerView.ViewHolder viewHolder = null;

        v = inflater.inflate(R.layout.card_os_item, parent, false);
        viewHolder = new MViewHolder(v);
        return viewHolder;
    }

    public void reloadData(Location location) {
        this.myLocation = location;
        sortOsList();
        notifyDataSetChanged();
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

        if(item.getLatitude() == null || item.getLongitude() == null || myLocation == null)
            distance = "-";
        else {
            Location osLocation = new Location("");
            osLocation.setLatitude(item.getLatitude());
            osLocation.setLongitude(item.getLongitude());
            double distanceMeters = myLocation.distanceTo(osLocation);
            double distanceDouble = distanceMeters / 1000.0;
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
