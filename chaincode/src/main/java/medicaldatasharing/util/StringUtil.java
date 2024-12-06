package medicaldatasharing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {

    public static String getIdPart(String id) {
        return id.substring(id.lastIndexOf("/") + 1);
    }

    public static String generateMembershipOrganizationId(int orgCount) {
        Random random = new Random();
        int orgNum = random.nextInt(orgCount) + 1;

        return "org" + orgNum;
    }

    public static String parseDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (date == null) return "";
        return simpleDateFormat.format(date);
    }

    public static Date createDate(String stringDate) throws ParseException {
        if (stringDate.isEmpty() || stringDate.equals("X")) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(stringDate);
    }

    public static String anonymizeWord(String input, String searchWord, String replacement) {
        String searchPattern = "(?i)" + searchWord;
        return input.replaceAll(searchPattern, replacement);
    }

    public static String anonymizePatientData(String input, String fullName, String lastName) {
        input = anonymizeWord(input, fullName, "X");
        return anonymizeWord(input, lastName, "Y");
    }

}

