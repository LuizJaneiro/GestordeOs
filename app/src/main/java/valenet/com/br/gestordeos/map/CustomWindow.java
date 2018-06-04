package valenet.com.br.gestordeos.map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import valenet.com.br.gestordeos.R;
import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.google_distance.OsDistanceAndPoints;
import valenet.com.br.gestordeos.utils.ClickGuard;
import valenet.com.br.gestordeos.utils.ValenetUtils;

public class CustomWindow implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private Context context;
    private Location myLocation;
    private int osType;
    private Polyline line;
    private HashMap<Integer, OsDistanceAndPoints> osDistanceHashMap = null;
    private GoogleMap map;

    public CustomWindow(Context context, Location myLocation, HashMap<Integer, OsDistanceAndPoints> osDistanceHashMap, GoogleMap map) {
        this.context = context;
        this.myLocation = myLocation;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myContentsView = inflater.inflate(R.layout.custom_info_marker, null);
        if (osDistanceHashMap != null)
            this.osDistanceHashMap = osDistanceHashMap;
        if(map != null)
            this.map = map;
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

        if (item.getCliente() == null)
            clientName = "Nome Indefinido";
        else
            clientName = ValenetUtils.firstAndLastWord(item.getCliente());

        if (item.getTipoAtividade() == null)
            osType = "Tipo Indefinido";
        else
            osType = item.getTipoAtividade();

        if (item.getLatitude() == null || item.getLongitude() == null || myLocation == null || osDistanceHashMap == null || osDistanceHashMap.get(item.getOsid()) == null
                || osDistanceHashMap.get(item.getOsid()).getDistance() == null)
            distance = "-";
        else {
            double distanceDouble = osDistanceHashMap.get(item.getOsid()).getDistance().doubleValue() / 1000.0;
            distanceDouble = ValenetUtils.round(distanceDouble, 1);
            if (distanceDouble >= 100)
                distance = ">100";
            else
                distance = String.valueOf(distanceDouble);
        }

        if (item.getDataAgendamento() == null)
            dateString = "Data Indefinida";
        else {
            dateString = ValenetUtils.convertJsonToStringDate(item.getDataAgendamento()) + " - " + ValenetUtils.convertJsonToStringHour(item.getDataAgendamento());
        }

        if (item.getStatusOs() == null)
            infoMarkerImageViewStatusOs.setVisibility(View.GONE);
        else {
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("AGUARDANDO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_awaiting_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CONCLUIDO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_closed_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("CANCELADO"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_refused_os));
            if (ValenetUtils.removeAccent(item.getStatusOs().toUpperCase()).equals("BLOQUEADA"))
                infoMarkerImageViewStatusOs.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_blocked_os));
        }

        infoMarkerName.setText(clientName);
        infoMarkerDistance.setText(distance + " KM");

        infoMarkerOsTypeAndDate.setText(osType + " " + dateString);

        if(line != null){
            line.remove();
        }

        if (map != null) {
            if(this.osDistanceHashMap != null && this.osDistanceHashMap.get(item.getOsid()) != null){
                String encodedPoints = this.osDistanceHashMap.get(item.getOsid()).getEncodedStringPoints();
                if(encodedPoints != null){
                    List<LatLng> list = decodePoly(encodedPoints);
                    line = map.addPolyline(new PolylineOptions()
                        .addAll(list)
                        .width(10)
                        .color(Color.parseColor("#0fb4f2"))
                        .geodesic(true)
                    );
                }
            }
        }

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

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }
}
