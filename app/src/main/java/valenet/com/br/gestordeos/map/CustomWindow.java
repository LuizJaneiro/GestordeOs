package valenet.com.br.gestordeos.map;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class CustomWindow implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private Context context;
    private Location myLocation;
    private int osType;

    public CustomWindow(Context context, Location myLocation) {
        this.context = context;
        this.myLocation = myLocation;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myContentsView = inflater.inflate(R.layout.custom_info_marker, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView infoMarkerName = myContentsView.findViewById(R.id.info_marker_name);
        TextView infoMarkerOsTypeAndDate = myContentsView.findViewById(R.id.info_marker_os_type_and_date);
        TextView infoMarkerDistance = myContentsView.findViewById(R.id.info_marker_distance);
        ImageView infoMarkerImageViewStatusOs = myContentsView.findViewById(R.id.info_marker_image_view_status_os);
        ViewGroup layout = myContentsView.findViewById(R.id.info_marker_layout);
        final Os item = (Os) marker.getTag();

        String clientName;
        String osType;
        String distance;
        String dateString;

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
            dateString = ValenetUtils.convertJsonToStringDate(item.getDataAgendamento()) + " - " + ValenetUtils.convertJsonToStringHour(item.getDataAgendamento());
        }

        if(item.getStatusOs() == null)
            infoMarkerImageViewStatusOs.setVisibility(View.GONE);
        else{
            if(ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("AGUARDANDO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_awaiting_os));
            if(ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CONCLUIDO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_closed_os));
            if(ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CANCELADO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_refused_os));
            if(ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("BLOQUEADA"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_blocked_os));
        }

        infoMarkerName.setText(clientName);
        infoMarkerDistance.setText(distance + " KM");

        infoMarkerOsTypeAndDate.setText(osType + " " + dateString);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ClickGuard.guard(layout);
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
