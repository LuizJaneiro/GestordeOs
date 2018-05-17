package valenet.com.br.gestordeos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.ViewGroup;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import valenet.com.br.gestordeos.model.entity.Os;
import valenet.com.br.gestordeos.model.entity.OsTypeModel;

public class ValenetUtils {

    public static final String BASE_URL = "https://api.valenet.com.br/api/";
    public static final String KEY_FILTERED_LIST = "KEY_FILTERED_LIST";
    public static final String KEY_OS_LIST = "KEY_OS_LIST";
    public static final String KEY_OS_TYPE = "KEY_OS_TYPE";
    public static final String KEY_OS_TYPE_LIST = "KEY_OS_TYPE_LIST";
    public static final String KEY_OS = "KEY_OS";
    public static final String KEY_OS_ID = "KEY_OS_ID";
    public static final String KEY_USER_LOCATION = "KEY_USER_LOCATION";
    public static final String KEY_ORDER_FILTERS = "KEY_ORDER_FILTERS";
    public static final String KEY_FILTERS = "KEY_FILTERS";
    public static final String KEY_CAME_FROM_MAPS = "KEY_CAME_FROM_MAPS";
    public static final String SHARED_PREF_KEY_EMAIL_LOGIN = "SHARED_PREF_KEY_EMAIL_LOGIN";
    public static final String SHARED_PREF_KEY_EMAIL_CLIENT = "SHARED_PREF_KEY_EMAIL_CLIENT";
    public static final String SHARED_PREF_KEY_PASSWORD_CLIENT = "SHARED_PREF_KEY_PASSWORD_CLIENT";
    public static final String SHARED_PREF_KEY_OS_FILTER = "SHARED_PREF_KEY_OS_FILTER";
    public static final String SHARED_PREF_KEY_OS_DISTANCE = "SHARED_PREF_KEY_OS_DISTANCE";
    public static final String SHARED_PREF_KEY_OS_NAME = "SHARED_PREF_KEY_OS_NAME";
    public static final String SHARED_PREF_KEY_OS_TIME = "SHARED_PREF_KEY_OS_TIME";
    public static final String SHARED_PREF_KEY_OS_NEXT = "SHARED_PREF_KEY_OS_NEXT";
    public static final Integer GROUP_OS_MERCANTIL = 1;
    public static final Integer GROUP_OS_CORRETIVA = 2;

    public static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String firstAndLastWord(String word) {
        String[] firstAndLast = new String[2];
        String firstWord, lastWord;
        if (!word.contains(" ")) {
            firstAndLast[0] = word;
            firstAndLast[1] = null;
            return firstAndLast[0];
        }

        String parts[] = word.split(" ");
        firstWord = parts[0];

        int partsLength = parts.length;

        if (parts[partsLength - 1].equals(" ")) {
            if (partsLength - 2 > 0)
                lastWord = parts[partsLength - 2];
            else
                lastWord = null;
        } else
            lastWord = parts[partsLength - 1];

        if (firstWord.equals(lastWord) || lastWord == null) {
            firstAndLast[0] = firstWord;
            firstAndLast[1] = null;
            return firstAndLast[0];
        }

        firstAndLast[0] = firstWord;
        firstAndLast[1] = lastWord;

        return firstAndLast[0] + " " + firstAndLast[1];
    }

    public static String convertJsonToStringDate(String json) {
        Date date = DateUtils.parseDate(json);
        DateTime dateTime = new DateTime(date);

        DateTimeFormatter fmt = new DateTimeFormatterBuilder().
                appendDayOfMonth(2). // 2 Digito (Valor mínimo) - Preenche com 0 se for menor que 10
                appendLiteral('/'). // Separador
                appendMonthOfYear(2). // Mes como Texto
                appendLiteral('/'). // Separador
                appendYear(2, 4).   // Numero minimo para impressao (2) | Numero maximo para parse (4)
                toFormatter();

        return fmt.print(dateTime);
    }

    public static String convertJsonToStringHour(String json) {
        Date date = DateUtils.parseDate(json);
        DateTime dateTime = new DateTime(date);

        DateTimeFormatter fmt = new DateTimeFormatterBuilder().
                appendHourOfDay(2). // 2 Digito (Valor mínimo) - Preenche com 0 se for menor que 10
                appendLiteral(':'). // Separador
                appendMinuteOfHour(2). // Mes como Texto
                toFormatter();

        return fmt.print(dateTime);
    }

    public static Date convertStringToDate(String dateString) {
        if (dateString == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    public static ArrayList<Os> filterList(ArrayList<Os> osArrayList, HashMap<String, Boolean> filters, Context context) {
        if (osArrayList == null || osArrayList.size() == 0 || filters == null || filters.size() == 0)
            return null;

        SharedPreferences sharedPref = context.getSharedPreferences(ValenetUtils.SHARED_PREF_KEY_OS_FILTER, Context.MODE_PRIVATE);


        ArrayList<Os> filtredOsArrayList = new ArrayList<>();
        Set<String> keys = filters.keySet();
        for (String key : keys) {
            boolean isSelected = filters.get(key);
            if (isSelected) {
                for (Os os : osArrayList) {
                    String osTipoAtividade = ValenetUtils.removeAccent(os.getTipoAtividade()).toUpperCase();
                    String keyTratada = ValenetUtils.removeAccent(key).toUpperCase();
                    if (osTipoAtividade.equals(keyTratada)) {
                        filtredOsArrayList.add(os);
                    }
                }
            }
        }
        return filtredOsArrayList;
    }
}
