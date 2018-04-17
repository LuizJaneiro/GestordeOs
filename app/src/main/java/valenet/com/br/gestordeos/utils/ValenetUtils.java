package valenet.com.br.gestordeos.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.Normalizer;
import java.util.Date;

public class ValenetUtils {

    public static final String BASE_URL = "https://api.valenet.com.br/api/";
    public static final String KEY_FILTERED_LIST = "KEY_FILTERED_LIST";
    public static final String KEY_OS_LIST = "KEY_OS_LIST";
    public static final String KEY_OS_TYPE = "KEY_OS_TYPE";
    public static final Integer GROUP_OS_MERCANTIL = 1;
    public static final Integer GROUP_OS_CORRETIVA = 2;

    public static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String firstAndLastWord(String word) {
        String[] firstAndLast = new String[2];
        String firstWord, lastWord;
        if(!word.contains(" ")){
            firstAndLast[0] = word;
            firstAndLast[1] = null;
            return firstAndLast[0];
        }

        String parts[] = word.split(" ");
        firstWord = parts[0];

        int partsLength = parts.length;

        if(parts[partsLength-1].equals(" ")){
            if(partsLength - 2 > 0)
                lastWord = parts[partsLength - 2];
            else
                lastWord = null;
        } else
            lastWord = parts[partsLength-1];

        if(firstWord.equals(lastWord) || lastWord == null){
            firstAndLast[0] = firstWord;
            firstAndLast[1] = null;
            return firstAndLast[0];
        }

        firstAndLast[0] = firstWord;
        firstAndLast[1] = lastWord;

        return firstAndLast[0] + " " + firstAndLast[1];
    }

    public static String convertJsonToStringDate(String json){
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
}
