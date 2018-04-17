package valenet.com.br.gestordeos.utils;

import java.text.Normalizer;

public class ValenetUtils {

    public static final String BASE_URL = "https://api.valenet.com.br/api/";
    public static final String KEY_FILTERED_LIST = "KEY_FILTERED_LIST";
    public static final Integer GROUP_OS_MERCANTIL = 1;
    public static final Integer GROUP_OS_CORRETIVA = 2;

    public static String removeAccent(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String[] firstAndLastWord(String word) {
        String[] firstAndLast = new String[2];
        String firstWord, lastWord;
        if(!word.contains(" ")){
            firstAndLast[0] = word;
            firstAndLast[1] = null;
            return firstAndLast;
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
            return firstAndLast;
        }

        firstAndLast[0] = firstWord;
        firstAndLast[1] = lastWord;

        return firstAndLast;
    }
}
