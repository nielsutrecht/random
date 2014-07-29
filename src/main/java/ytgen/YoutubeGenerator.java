package ytgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class YoutubeGenerator {

    public static void main(final String[] args) {
        final Map<String, String[]> listMap = new HashMap<String, String[]>();

        final String[] cutelist = new String[5];

        cutelist[0] = " https://www.youtube.com/watch?v=EdCVijVT7Wk";
        cutelist[1] = " http://youtu.be/-XCvPptsfhI?t=7s";
        cutelist[2] = " https://www.youtube.com/watch?v=-nkEPsSsH68";
        cutelist[3] = " https://www.youtube.com/watch?v=argCvDpk_KQ";
        cutelist[4] = " https://www.youtube.com/watch?v=FZ-bJFVJ2P0";

        listMap.put("cute", cutelist);

        final String[] funnylist = new String[5];

        funnylist[0] = " https://www.youtube.com/watch?v=I59MgGlh2Mg";
        funnylist[1] = " http://www.youtube.com/watch?v=HKMNKS-9ugY";
        funnylist[2] = " https://www.youtube.com/watch?v=_qKmWfED8mA";
        funnylist[3] = " https://www.youtube.com/watch?v=QDFQYKPsVOQ";
        funnylist[4] = " https://www.youtube.com/watch?v=ebv51QNm2Bk";

        listMap.put("funny", funnylist);

        final String[] wtflist = new String[5];

        wtflist[0] = " https://www.youtube.com/watch?v=UfKIoSv2YEg";
        wtflist[1] = " https://www.youtube.com/watch?v=hcGvN0iBA5s";
        wtflist[2] = " http://www.youtube.com/watch?v=vxnyqvejPjI&feature=youtu.be&t=1m37s";
        wtflist[3] = " https://www.youtube.com/watch?v=10NJnT6-sSE";
        wtflist[4] = " https://www.youtube.com/watch?v=DQeyjgSUlrk";

        listMap.put("wtf", wtflist);

        final String[] intlist = new String[5];

        intlist[0] = " https://www.youtube.com/watch?v=fYwRMEomJMM";
        intlist[1] = " https://www.youtube.com/watch?v=1PmYItnlY5M&feature=youtu.be&t=32s";
        intlist[2] = " https://www.youtube.com/watch?v=HgmnIJF07kg";
        intlist[3] = " https://www.youtube.com/watch?v=cUcoiJgEyag";
        intlist[4] = " https://www.youtube.com/watch?v=BePoF4PrwHs";

        listMap.put("int", intlist);

        final String[] doclist = new String[5];

        doclist[0] = " https://www.youtube.com/watch?v=wS_WlzdOc_A";
        doclist[1] = " https://www.youtube.com/watch?v=8n0SkIGARuo";
        doclist[2] = " https://www.youtube.com/watch?v=6LaSD8oFBZE";
        doclist[3] = " https://www.youtube.com/watch?v=zvfLdg2DN18";
        doclist[4] = " https://www.youtube.com/watch?v=8af0QPhJ22s&hd=1";

        listMap.put("doc", doclist);

        for (;;) {

            System.out.println("\n ---------Youtube Video Generator 0.001 BETA------------------ \n");

            System.out.println("\n ********* DISCLAIMER: WARNING - This program may direct you to violent, disturbing content, and/or vulgar language and is intended for a MATURE person only. ********* \n \n");

            final Scanner scan = new Scanner(System.in);

            System.out.println("What kind of video from the list would you like to watch? \n");

            System.out.println("Cute \n" + "Funny \n" + "WTF \n" + "Interesting \n" + "Documentary \n");

            System.out.print("I want to watch: ");

            final String userString = scan.next().toLowerCase();

            if (!listMap.containsKey(userString)) {
                System.out.println("No category '" + userString + "'.");
            }
            else {
                final String[] list = listMap.get(userString);
                final String random = (list[new Random().nextInt(list.length)]);
                System.out.println("Here's a " + userString + " video you can watch:" + random);
            }

            System.out.println("\n Do you want to watch another video? Enter yes or no");
            final String decision = scan.next();

            if (decision.equalsIgnoreCase("no")) {
                break;
            }
        }
    }
}
