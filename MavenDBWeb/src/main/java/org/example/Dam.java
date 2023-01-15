package org.example;

import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Dam {
    final static String fetchURL = "https://ckan.open-governmentdata.org/dataset/8c753941-a47e-43de-b438-30f159c6efda/resource/96b18b76-f2aa-47a8-9530-255d439a0090/download/realtimetotal.html";
    final static String ZONE = "Japan";
    //final static String debugFile = "output.txt";

    public static String main() {
        //System.out.println(getDamdata());
        return getDamdata();
    }

    public static String getDamdata() {      // 情報の取得データの成形Json化一連の流れ全部まとめる
        ArrayList<String> damData = cookStr(getContent(fetchURL));
        //ZonedDateTime timeDatafetched = makeDate(damData, ZONE);
        //damData.add(timeDatafetched.toString());
        damData.add(ZonedDateTime.now(ZoneId.of(ZONE)).toString());
        return makeJson(damData);
    }

    public static String getContent(String contentURL) {     // コンテンツ取得
        try {
            URLConnection connection = new URL(contentURL).openConnection();
            Scanner sc = new Scanner(connection.getInputStream(), "UTF-8");
            sc.useDelimiter("\\Z");
            String result = sc.next();      // サイトにGetリクエスト飛ばして内容を取得する
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static String makeJson(ArrayList<String> data) {      // Jacksonライブラリを通じてJson作成 Json型がないのでStringで返ります
        Damdata dm = new Damdata();
        dm.year = Integer.parseInt(data.get(0));
        dm.month = Integer.parseInt(data.get(1));
        dm.day = Integer.parseInt(data.get(2));
        dm.hour = Integer.parseInt(data.get(3));
        dm.waterVolume = Integer.parseInt(data.get(4).replace(",", ""));
        dm.percentage = Float.parseFloat(data.get(5));
        dm.diff = Float.parseFloat(data.get(6));
        dm.dataCreated = data.get(7);

        ObjectMapper mp = new ObjectMapper();
        String res = null;
        try {
            res = mp.writeValueAsString(dm);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ArrayList<String> cookStr(String s) {      // リクエストで取得した文字列をあれやこれやして処理するところ
        String ellimStr = "span class=\"number\">";
        String[] po = s.split("<");
        ArrayList<String> res = new ArrayList<String>();
        for (int $_ = 0; $_ < po.length; $_++) {
            if (po[$_].length() > ellimStr.length() && po[$_].indexOf("number") >= 0) {
                //System.out.println(po[$_].substring(ellimStr.length(),po[$_].length()));
                res.add(po[$_].substring(ellimStr.length(), po[$_].length()));
            }
        }
        res.remove(0);
        return res;
    }
    /*
    @WebServlet("/damData")
    public static class ServletGet extends HttpServlet {
        private HttpServletRequest request;
        private HttpServletResponse response;

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            this.request = request;
            this.response = response;

            response.setContentType("text/html; charset=Shift_JIS");
            PrintWriter out = response.getWriter();
            out.println(getDamdata());
        }
    }
    public static ZonedDateTime makeDate(ArrayList<String> baseData, String zone) {
        int[] datas = new int[4];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = Integer.parseInt(baseData.get(i));
        }
        ZoneId timezone = ZoneId.of(zone);
        ZonedDateTime res = ZonedDateTime.of(datas[0], datas[1], datas[2], datas[3], 0, 0, 0, timezone);
        return res;
    }

    public static void testFileoutput(ArrayList<String> data, String fileLocation) {  // テスト出力
        try {
            FileWriter fw = new FileWriter(fileLocation);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            for (int i = 0; i < data.size(); i++) {
                pw.println(data.get(i));
            }

            pw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}


