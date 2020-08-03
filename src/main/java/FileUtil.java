import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.translate.demo.TransApi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtil {
    public static List<File> fileList = new ArrayList<File>();
    private static final String APP_ID = "20200527000472899";
    private static final String SECURITY_KEY = "MzHj95TZZfYvDYYLlRQp";

    public static void listAllFile(File file) {

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    listAllFile(f);
                } else {
                    fileList.add(f);
                }
            }
        } else {
            fileList.add(file);
        }

    }

    public static void main(String[] args) throws IOException {

        readwriteFile();
    }

    public static String readwriteFile() throws IOException {
        long start = System.currentTimeMillis();
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        AtomicLong total = new AtomicLong();
        //FileUtil.listAllFile(new File("D:\\englishsrc"));
        FileUtil.listAllFile(new File("D:\\englishsrc\\java\\util\\ArrayList.java"));
        FileUtil.fileList.forEach(t -> {
            try {
                FileReader reader = new FileReader(t);
                String path = t.getPath().toString().replace("D:\\englishsrc", "D:\\chinesesrc");
                String filename = path.substring(path.lastIndexOf("\\"), path.length());
                path = path.substring(0, path.lastIndexOf("\\"));

                File outFile = new File(path);
                if (!outFile.exists()) {
                    outFile.mkdirs();
                }

                FileWriter writer = new FileWriter(new File(outFile, filename));
                BufferedReader bufferedReader = new BufferedReader(reader);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                String line = "";
                StringBuilder builder = new StringBuilder();
                StringBuilder builder1 = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    String str = line.replace("*", "").replaceAll(" ", "");

                    if ((line.trim().startsWith("*") && str.length() > 0) || line.trim().startsWith("//")) {
                        builder1.append("\n");
                        builder1.append(line);
                        total.getAndIncrement();
                    } else {


//                        if (builder1 != null && builder1.length() > 0) {
//                            String result = UnicodeUtil.toString(api.getTransResult(builder1.toString(), "auto", "zh"));
//                            Thread.sleep(1000);
//                            builder1.delete(0, builder1.length());
//                            JSONObject jsonObject = JSONObject.parseObject(result);
//                            JSONArray trans_result = jsonObject.getJSONArray("trans_result");
//                            StringBuilder chbuilder = new StringBuilder();
//
//                            for (int i = 0; i < trans_result.size(); i++) {
//                                JSONObject json = trans_result.getJSONObject(i);
//                                chbuilder.append("\r\n");
//                                chbuilder.append(json.getString("dst"));
//                            }
//                            builder.append(chbuilder.toString());
//                        }

                        builder.append("\r\n");
                        builder.append(line);
                    }

                }

                String chstr = builder.toString();
                bufferedWriter.write(chstr, 0, chstr.length());
                bufferedWriter.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        long time = end - start;
        double times = time / 1000.0;
        System.out.println(total);
        System.out.println(times + "秒");
        return "";
    }
}
