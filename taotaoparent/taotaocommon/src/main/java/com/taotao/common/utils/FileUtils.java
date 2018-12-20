package com.taotao.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 写文件的工具类,输出有格式的文件
 *
 * |- TXT
 * |- JSON
 * |- CSV
 */
public class FileUtils {

    // 缓冲区大小
    private final static int buffer_size = 1024;

    // 日志格式工具
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    // 小数的格式化工具,设置最大小数位为10
    private final static NumberFormat numFormatter = NumberFormat.getNumberInstance();
    static {
        numFormatter.setMaximumFractionDigits(10);
    }

    // 换行符
    @SuppressWarnings("restriction")
    private final static String lineSeparator = java.security.AccessController
            .doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));

    /**
     * 以指定编码格式写入多行多列到TXT文件
     *
     * @param rows                要输出的数据
     * @param filePath            TXT文件的具体路径
     * @param charsetName        UTF-8
     * @throws Exception
     */
    public static void writeRows2TxtFile(List<Object[]> rows, String filePath, String charsetName) throws Exception {
        // TXT内容
        StringBuffer txtStr = new StringBuffer();

        // 拼接每一行
        for (int i = 0; i < rows.size(); i++) {
            // 拼接一行数据成字符串
            StringBuffer line = new StringBuffer();

            Object[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                String field = FileUtils.formatField(row[j]);

                if (j == row.length - 1)
                    line.append(field);
                else
                    line.append(field).append("\t");
            }

            // 将一行数据的字符串添加到结果集中
            if (i == rows.size() - 1) { // 最后一行,不用换行
                txtStr.append(line);
            } else {
                txtStr.append(line).append(lineSeparator);
            }
        }

        // 将拼接后的完整内容写入文件
        FileUtils.writeString2SimpleFile(txtStr.toString(), filePath, charsetName);
    }

    /**
     * 以指定编码格式写入多行多列到CSV文件
     *
     * @param rows                要输出的数据
     * @param filePath            CSV文件的具体路径
     * @param charsetName        GB2312
     * @throws Exception
     */
    public static void writeRows2CsvFile(List<Object[]> rows, String filePath, String charsetName) throws Exception {
        // CSV内容
        StringBuffer csvStr = new StringBuffer();

        // 拼接每一行
        for (int i = 0; i < rows.size(); i++) {
            // 拼接一行数据成字符串
            StringBuffer line = new StringBuffer();

            Object[] row = rows.get(i);
            for (int j = 0; j < row.length; j++) {
                String field = FileUtils.formatField(row[j]);

                if (j == row.length - 1)
                    line.append(String.format("\"%s\"", field));
                else
                    line.append(String.format("\"%s\",", field));
            }

            // 将一行数据的字符串添加到结果集中
            if (i == rows.size() - 1) { // 最后一行,不用换行
                csvStr.append(line);
            } else {
                csvStr.append(line).append(lineSeparator);
            }
        }

        // 将拼接后的完整内容写入文件
        FileUtils.writeString2SimpleFile(csvStr.toString(), filePath, charsetName);
    }

    /**
     * 以指定编码格式写入JSON字符串到JSON文件
     *
     * @param jsonStr        要输出的JSON字符串
     * @param filePath        JSON文件的具体路径
     * @param charsetName    UTF-8
     * @throws Exception
     */
    public static void writeJsonStr2JsonFile(String jsonStr, String filePath, String charsetName) throws Exception {
        // JSON字符串格式化
        jsonStr = JsonFormat.formatJson(jsonStr);

        // 将格式化后的JSON字符串以指定编码写入文件
        FileUtils.writeString2SimpleFile(jsonStr, filePath, charsetName);

    }

    /**
     * 以指定编码格式写入字符串到CSV文件
     *
     * @param csvStr        要输出的字符串(有CSV格式)
     * @param filePath        CSV文件的具体路径
     * @param charsetName    GB2312
     * @throws Exception
     */
    public static void writeCsvStr2CsvFile(String csvStr, String filePath, String charsetName) throws Exception {
        FileUtils.writeString2SimpleFile(csvStr, filePath, charsetName);
    }

    /**
     * 以指定编码格式写入字符串到简单文件
     *
     * @param str            要输出的字符串
     * @param filePath        简单文件的具体路径
     * @param charsetName    UTF-8 | GB2312
     * @throws Exception
     */
    public static void writeString2SimpleFile(String str, String filePath, String charsetName) throws Exception {

        BufferedWriter out = null;
        try {
            File file = new File(filePath);

            createNewFileIfNotExists(file);

            OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), charsetName);
            out = new BufferedWriter(os, FileUtils.buffer_size);

            out.write(str);

            out.flush();
        } finally {
            FileUtils.close(out);
        }

    }

    /**
     * 格式化一个field成标准格式字符串 
     *
     * 支持接收的参数类型（8中基本类型及包装类、String、Date）
     * 其他引用类型返回JSON字符串
     */
    private static String formatField(Object field) {
        // null时给一个空格占位
        if (null == field) {
            return " ";
        }

        @SuppressWarnings("rawtypes")
        Class clazz = field.getClass();

        // byte、short、integer、long
        if (clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class
                || clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class) {
            return String.valueOf(field);
        }

        // float、double
        if (clazz == float.class || clazz == double.class || clazz == Float.class || clazz == Double.class) {
            return numFormatter.format(field);
        }

        // boolean、char、String
        if (clazz == boolean.class || clazz == Boolean.class || clazz == char.class || clazz == Character.class
                || clazz == String.class) {
            return String.valueOf(field);
        }

        // Date
        if (clazz == Date.class) {
            return dateFormat.format(field);
        }

        return JSON.toJSONString(field);
    }

    /**
     * 如果文件不存在,创建一个新文件
     */
    private static void createNewFileIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            // 创建目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 创建文件
            file.createNewFile();
        }
    }

    /**
     * 关闭输出流
     */
    private static void close(Writer out) {
        if (null != out) {
            try {
                out.close();
            } catch (IOException e) {
                // e.printStackTrace();
            }
        }
    }

    /**
     * JSON字符串的格式化工具
     */
    static class JsonFormat {

        // 返回格式化JSON字符串。
        public static String formatJson(String json) {
            StringBuffer result = new StringBuffer();

            int length = json.length();
            int number = 0;
            char key = 0;

            for (int i = 0; i < length; i++) {
                key = json.charAt(i);

                if ((key == '[') || (key == '{')) {
                    if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                        result.append('\n');
                        result.append(indent(number));
                    }

                    result.append(key);

                    result.append('\n');

                    number++;
                    result.append(indent(number));

                    continue;
                }

                if ((key == ']') || (key == '}')) {
                    result.append('\n');

                    number--;
                    result.append(indent(number));

                    result.append(key);

                    if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                        result.append('\n');
                    }

                    continue;
                }

                if ((key == ',')) {
                    result.append(key);
                    result.append('\n');
                    result.append(indent(number));
                    continue;
                }

                result.append(key);
            }

            return result.toString();
        }

        // 单位缩进字符串,3个空格。
        private static String SPACE = "   ";

        // 返回指定次数(number)的缩进字符串。每一次缩进一个单个的SPACE。
        private static String indent(int number) {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < number; i++) {
                result.append(SPACE);
            }
            return result.toString();
        }
    }

    //利用工具类写json文件 utf-8格式
    public static void main(String[] args) throws Exception {
        String json = "category.getDataService({\"data\":[{\"u\":\"/products/1.html\",\"n\":\"<a href='/products/1.html'>图书、音像、电子书刊</a>\",\"i\":[{\"u\":\"/products/2.html\",\"n\":\"电子书刊\",\"i\":[\"/products/3.html|电子书\",\"/products/4.html|网络原创\",\"/products/5.html|数字杂志\",\"/products/6.html|多媒体图书\"]},{\"u\":\"/products/7.html\",\"n\":\"音像\",\"i\":[\"/products/8.html|音乐\",\"/products/9.html|影视\",\"/products/10.html|教育音像\"]},{\"u\":\"/products/11.html\",\"n\":\"英文原版\",\"i\":[\"/products/12.html|少儿\",\"/products/13.html|商务投资\",\"/products/14.html|英语学习与考试\",\"/products/15.html|小说\",\"/products/16.html|传记\",\"/products/17.html|励志\"]},{\"u\":\"/products/18.html\",\"n\":\"文艺\",\"i\":[\"/products/19.html|小说\",\"/products/20.html|文学\",\"/products/21.html|青春文学\",\"/products/22.html|传记\",\"/products/23.html|艺术\"]},{\"u\":\"/products/24.html\",\"n\":\"少儿\",\"i\":[\"/products/25.html|少儿\",\"/products/26.html|0-2岁\",\"/products/27.html|3-6岁\",\"/products/28.html|7-10岁\",\"/products/29.html|11-14岁\"]},{\"u\":\"/products/30.html\",\"n\":\"人文社科\",\"i\":[\"/products/31.html|历史\",\"/products/32.html|哲学\",\"/products/33.html|国学\",\"/products/34.html|政治/军事\",\"/products/35.html|法律\",\"/products/36.html|宗教\",\"/products/37.html|心理学\",\"/products/38.html|文化\",\"/products/39.html|社会科学\"]},{\"u\":\"/products/40.html\",\"n\":\"经管励志\",\"i\":[\"/products/41.html|经济\",\"/products/42.html|金融与投资\",\"/products/43.html|管理\",\"/products/44.html|励志与成功\"]},{\"u\":\"/products/45.html\",\"n\":\"生活\",\"i\":[\"/products/46.html|生活\",\"/products/47.html|健身与保健\",\"/products/48.html|家庭与育儿\",\"/products/49.html|旅游\",\"/products/50.html|动漫/幽默\"]},{\"u\":\"/products/51.html\",\"n\":\"科技\",\"i\":[\"/products/52.html|科技\",\"/products/53.html|工程\",\"/products/54.html|建筑\",\"/products/55.html|医学\",\"/products/56.html|科学与自然\",\"/products/57.html|计算机与互联网\",\"/products/58.html|体育/运动\"]},{\"u\":\"/products/59.html\",\"n\":\"教育\",\"i\":[\"/products/60.html|教材教辅\",\"/products/61.html|教育与考试\",\"/products/62.html|外语学习\",\"/products/63.html|新闻出版\",\"/products/64.html|语言文字\"]},{\"u\":\"/products/65.html\",\"n\":\"港台图书\",\"i\":[\"/products/66.html|艺术/设计/收藏\",\"/products/67.html|经济管理\",\"/products/68.html|文化/学术\",\"/products/69.html|少儿文学/国学\"]},{\"u\":\"/products/70.html\",\"n\":\"其它\",\"i\":[\"/products/71.html|工具书\",\"/products/72.html|影印版\",\"/products/73.html|套装书\"]}]},{\"u\":\"/products/74.html\",\"n\":\"<a href='/products/74.html'>家用电器</a>\",\"i\":[{\"u\":\"/products/75.html\",\"n\":\"大 家 电\",\"i\":[\"/products/76.html|平板电视\",\"/products/77.html|空调\",\"/products/78.html|冰箱\",\"/products/79.html|洗衣机\",\"/products/80.html|家庭影院\",\"/products/81.html|DVD播放机\",\"/products/82.html|迷你音响\",\"/products/83.html|烟机/灶具\",\"/products/84.html|热水器\",\"/products/85.html|消毒柜/洗碗机\",\"/products/86.html|酒柜/冰吧/冷柜\",\"/products/87.html|家电配件\",\"/products/88.html|家电下乡\"]},{\"u\":\"/products/89.html\",\"n\":\"生活电器\",\"i\":[\"/products/90.html|电风扇\",\"/products/91.html|冷风扇\",\"/products/92.html|净化器\",\"/products/93.html|饮水机\",\"/products/94.html|净水设备\",\"/products/95.html|挂烫机/熨斗\",\"/products/96.html|吸尘器\",\"/products/97.html|电话机\",\"/products/98.html|插座\",\"/products/99.html|收录/音机\",\"/products/100.html|清洁机\",\"/products/101.html|加湿器\",\"/products/102.html|除湿机\",\"/products/103.html|取暖电器\",\"/products/104.html|其它生活电器\",\"/products/105.html|扫地机器人\",\"/products/106.html|干衣机\",\"/products/107.html|生活电器配件\"]},{\"u\":\"/products/108.html\",\"n\":\"厨房电器\",\"i\":[\"/products/109.html|料理/榨汁机\",\"/products/110.html|豆浆机\",\"/products/111.html|电饭煲\",\"/products/112.html|电压力锅\",\"/products/113.html|面包机\",\"/products/114.html|咖啡机\",\"/products/115.html|微波炉\",\"/products/116.html|电烤箱\",\"/products/117.html|电磁炉\",\"/products/118.html|电饼铛/烧烤盘\",\"/products/119.html|煮蛋器\",\"/products/120.html|酸奶机\",\"/products/121.html|电炖锅\",\"/products/122.html|电水壶/热水瓶\",\"/products/123.html|多用途锅\",\"/products/124.html|果蔬解毒机\",\"/products/125.html|其它厨房电器\"]},{\"u\":\"/products/126.html\",\"n\":\"个护健康\",\"i\":[\"/products/127.html|剃须刀\",\"/products/128.html|剃/脱毛器\",\"/products/129.html|口腔护理\",\"/products/130.html|电吹风\",\"/products/131.html|美容器\",\"/products/132.html|美发器\",\"/products/133.html|按摩椅\",\"/products/134.html|按摩器\",\"/products/135.html|足浴盆\",\"/products/136.html|血压计\",\"/products/137.html|健康秤/厨房秤\",\"/products/138.html|血糖仪\",\"/products/139.html|体温计\",\"/products/140.html|计步器/脂肪检测仪\",\"/products/141.html|其它健康电器\"]},{\"u\":\"/products/142.html\",\"n\":\"五金家装\",\"i\":[\"/products/143.html|电动工具\",\"/products/144.html|手动工具\",\"/products/145.html|仪器仪表\",\"/products/146.html|浴霸/排气扇\",\"/products/147.html|灯具\",\"/products/148.html|LED灯\",\"/products/149.html|洁身器\",\"/products/150.html|水槽\",\"/products/151.html|龙头\",\"/products/152.html|淋浴花洒\",\"/products/153.html|厨卫五金\",\"/products/154.html|家具五金\",\"/products/155.html|门铃\",\"/products/156.html|电气开关\",\"/products/157.html|插座\",\"/products/158.html|电工电料\",\"/products/159.html|监控安防\",\"/products/160.html|电线/线缆\"]}]},{\"u\":\"/products/161.html\",\"n\":\"<a href='/products/161.html'>电脑、办公</a>\",\"i\":[{\"u\":\"/products/162.html\",\"n\":\"电脑整机\",\"i\":[\"/products/163.html|笔记本\",\"/products/164.html|超极本\",\"/products/165.html|游戏本\",\"/products/166.html|平板电脑\",\"/products/167.html|平板电脑配件\",\"/products/168.html|台式机\",\"/products/169.html|服务器/工作站\",\"/products/170.html|笔记本配件\"]},{\"u\":\"/products/171.html\",\"n\":\"电脑配件\",\"i\":[\"/products/172.html|CPU\",\"/products/173.html|主板\",\"/products/174.html|显卡\",\"/products/175.html|硬盘\",\"/products/176.html|SSD固态硬盘\",\"/products/177.html|内存\",\"/products/178.html|机箱\",\"/products/179.html|电源\",\"/products/180.html|显示器\",\"/products/181.html|刻录机/光驱\",\"/products/182.html|散热器\",\"/products/183.html|声卡/扩展卡\",\"/products/184.html|装机配件\",\"/products/185.html|组装电脑\"]},{\"u\":\"/products/186.html\",\"n\":\"外设产品\",\"i\":[\"/products/187.html|移动硬盘\",\"/products/188.html|U盘\",\"/products/189.html|鼠标\",\"/products/190.html|键盘\",\"/products/191.html|鼠标垫\",\"/products/192.html|摄像头\",\"/products/193.html|手写板\",\"/products/194.html|外置盒\",\"/products/195.html|插座\",\"/products/196.html|线缆\",\"/products/197.html|UPS电源\",\"/products/198.html|电脑工具\",\"/products/199.html|游戏设备\",\"/products/200.html|电玩\",\"/products/201.html|电脑清洁\"]},{\"u\":\"/products/202.html\",\"n\":\"网络产品\",\"i\":[\"/products/203.html|路由器\",\"/products/204.html|网卡\",\"/products/205.html|交换机\",\"/products/206.html|网络存储\",\"/products/207.html|4G/3G上网\",\"/products/208.html|网络盒子\",\"/products/209.html|网络配件\"]},{\"u\":\"/products/210.html\",\"n\":\"办公设备\",\"i\":[\"/products/211.html|投影机\",\"/products/212.html|投影配件\",\"/products/213.html|多功能一体机\",\"/products/214.html|打印机\",\"/products/215.html|传真设备\",\"/products/216.html|验钞/点钞机\",\"/products/217.html|扫描设备\",\"/products/218.html|复合机\",\"/products/219.html|碎纸机\",\"/products/220.html|考勤机\",\"/products/221.html|墨粉\",\"/products/222.html|收款/POS机\",\"/products/223.html|会议音频视频\",\"/products/224.html|保险柜\",\"/products/225.html|装订/封装机\",\"/products/226.html|安防监控\",\"/products/227.html|办公家具\",\"/products/228.html|白板\"]},{\"u\":\"/products/229.html\",\"n\":\"文具/耗材\",\"i\":[\"/products/230.html|硒鼓/墨粉\",\"/products/231.html|墨盒\",\"/products/232.html|色带\",\"/products/233.html|纸类\",\"/products/234.html|办公文具\",\"/products/235.html|学生文具\",\"/products/236.html|文件管理\",\"/products/237.html|财会用品\",\"/products/238.html|本册/便签\",\"/products/239.html|计算器\",\"/products/240.html|激光笔\",\"/products/241.html|笔类\",\"/products/242.html|画具画材\",\"/products/243.html|刻录碟片/附件\"]},{\"u\":\"/products/244.html\",\"n\":\"服务产品\",\"i\":[\"/products/245.html|上门服务\",\"/products/246.html|远程服务\",\"/products/247.html|电脑软件\",\"/products/248.html|京东服务\"]}]},{\"u\":\"/products/249.html\",\"n\":\"<a href='/products/249.html'>个护化妆</a>\",\"i\":[{\"u\":\"/products/250.html\",\"n\":\"面部护肤\",\"i\":[\"/products/251.html|清洁\",\"/products/252.html|护肤\",\"/products/253.html|面膜\",\"/products/254.html|剃须\",\"/products/255.html|套装\"]},{\"u\":\"/products/256.html\",\"n\":\"身体护肤\",\"i\":[\"/products/257.html|沐浴\",\"/products/258.html|润肤\",\"/products/259.html|颈部\",\"/products/260.html|手足\",\"/products/261.html|纤体塑形\",\"/products/262.html|美胸\",\"/products/263.html|套装\"]},{\"u\":\"/products/264.html\",\"n\":\"口腔护理\",\"i\":[\"/products/265.html|牙膏/牙粉\",\"/products/266.html|牙刷/牙线\",\"/products/267.html|漱口水\",\"/products/268.html|套装\"]},{\"u\":\"/products/269.html\",\"n\":\"女性护理\",\"i\":[\"/products/270.html|卫生巾\",\"/products/271.html|卫生护垫\",\"/products/272.html|私密护理\",\"/products/273.html|脱毛膏\"]},{\"u\":\"/products/274.html\",\"n\":\"洗发护发\",\"i\":[\"/products/275.html|洗发\",\"/products/276.html|护发\",\"/products/277.html|染发\",\"/products/278.html|造型\",\"/products/279.html|假发\",\"/products/280.html|套装\"]},{\"u\":\"/products/281.html\",\"n\":\"香水彩妆\",\"i\":[\"/products/282.html|香水\",\"/products/283.html|底妆\",\"/products/284.html|腮红\",\"/products/285.html|眼部\",\"/products/286.html|唇部\",\"/products/287.html|美甲\",\"/products/288.html|美容工具\",\"/products/289.html|套装\"]}]},{\"u\":\"/products/290.html\",\"n\":\"<a href='/products/290.html'>钟表</a>\",\"i\":[{\"u\":\"/products/291.html\",\"n\":\"钟表\",\"i\":[\"/products/292.html|男表\",\"/products/293.html|女表\",\"/products/294.html|儿童手表\",\"/products/295.html|座钟挂钟\"]}]},{\"u\":\"/products/296.html\",\"n\":\"<a href='/products/296.html'>母婴</a>\",\"i\":[{\"u\":\"/products/297.html\",\"n\":\"奶粉\",\"i\":[\"/products/298.html|婴幼奶粉\",\"/products/299.html|成人奶粉\"]},{\"u\":\"/products/300.html\",\"n\":\"营养辅食\",\"i\":[\"/products/301.html|益生菌/初乳\",\"/products/302.html|米粉/菜粉\",\"/products/303.html|果泥/果汁\",\"/products/304.html|DHA\",\"/products/305.html|宝宝零食\",\"/products/306.html|钙铁锌/维生素\",\"/products/307.html|清火/开胃\",\"/products/308.html|面条/粥\"]},{\"u\":\"/products/309.html\",\"n\":\"尿裤湿巾\",\"i\":[\"/products/310.html|婴儿尿裤\",\"/products/311.html|拉拉裤\",\"/products/312.html|湿巾\",\"/products/313.html|成人尿裤\"]},{\"u\":\"/products/314.html\",\"n\":\"喂养用品\",\"i\":[\"/products/315.html|奶瓶奶嘴\",\"/products/316.html|吸奶器\",\"/products/317.html|暖奶消毒\",\"/products/318.html|碗盘叉勺\",\"/products/319.html|水壶/水杯\",\"/products/320.html|牙胶安抚\",\"/products/321.html|辅食料理机\"]},{\"u\":\"/products/322.html\",\"n\":\"洗护用品\",\"i\":[\"/products/323.html|宝宝护肤\",\"/products/324.html|宝宝洗浴\",\"/products/325.html|奶瓶清洗\",\"/products/326.html|驱蚊防蚊\",\"/products/327.html|理发器\",\"/products/328.html|洗衣液/皂\",\"/products/329.html|日常护理\",\"/products/330.html|座便器\"]},{\"u\":\"/products/331.html\",\"n\":\"童车童床\",\"i\":[\"/products/332.html|婴儿推车\",\"/products/333.html|餐椅摇椅\",\"/products/334.html|婴儿床\",\"/products/335.html|学步车\",\"/products/336.html|三轮车\",\"/products/337.html|自行车\",\"/products/338.html|电动车\",\"/products/339.html|扭扭车\",\"/products/340.html|滑板车\"]},{\"u\":\"/products/341.html\",\"n\":\"寝居服饰\",\"i\":[\"/products/342.html|婴儿外出服\",\"/products/343.html|婴儿内衣\",\"/products/344.html|婴儿礼盒\",\"/products/345.html|婴儿鞋帽袜\",\"/products/346.html|安全防护\",\"/products/347.html|家居床品\"]},{\"u\":\"/products/348.html\",\"n\":\"妈妈专区\",\"i\":[\"/products/349.html|妈咪包/背婴带\",\"/products/350.html|产后塑身\",\"/products/351.html|文胸/内裤\",\"/products/352.html|防辐射服\",\"/products/353.html|孕妇装\",\"/products/354.html|孕期营养\",\"/products/355.html|孕妈美容\",\"/products/356.html|待产/新生\",\"/products/357.html|月子装\"]},{\"u\":\"/products/358.html\",\"n\":\"童装童鞋\",\"i\":[\"/products/359.html|套装\",\"/products/360.html|上衣\",\"/products/361.html|裤子\",\"/products/362.html|裙子\",\"/products/363.html|内衣/家居服\",\"/products/364.html|羽绒服/棉服\",\"/products/365.html|亲子装\",\"/products/366.html|儿童配饰\",\"/products/367.html|礼服/演出服\",\"/products/368.html|运动鞋\",\"/products/369.html|皮鞋/帆布鞋\",\"/products/370.html|靴子\",\"/products/371.html|凉鞋\",\"/products/372.html|功能鞋\",\"/products/373.html|户外/运动服\"]},{\"u\":\"/products/374.html\",\"n\":\"安全座椅\",\"i\":[\"/products/375.html|提篮式\",\"/products/376.html|安全座椅\",\"/products/377.html|增高垫\"]}]},{\"u\":\"/products/378.html\",\"n\":\"<a href='/products/378.html'>食品饮料、保健食品</a>\",\"i\":[{\"u\":\"/products/379.html\",\"n\":\"进口食品\",\"i\":[\"/products/380.html|饼干蛋糕\",\"/products/381.html|糖果/巧克力\",\"/products/382.html|休闲零食\",\"/products/383.html|冲调饮品\",\"/products/384.html|粮油调味\",\"/products/385.html|牛奶\"]},{\"u\":\"/products/386.html\",\"n\":\"地方特产\",\"i\":[\"/products/387.html|其他特产\",\"/products/388.html|新疆\",\"/products/389.html|北京\",\"/products/390.html|山西\",\"/products/391.html|内蒙古\",\"/products/392.html|福建\",\"/products/393.html|湖南\",\"/products/394.html|四川\",\"/products/395.html|云南\",\"/products/396.html|东北\"]},{\"u\":\"/products/397.html\",\"n\":\"休闲食品\",\"i\":[\"/products/398.html|休闲零食\",\"/products/399.html|坚果炒货\",\"/products/400.html|肉干肉脯\",\"/products/401.html|蜜饯果干\",\"/products/402.html|糖果/巧克力\",\"/products/403.html|饼干蛋糕\",\"/products/404.html|无糖食品\"]},{\"u\":\"/products/405.html\",\"n\":\"粮油调味\",\"i\":[\"/products/406.html|米面杂粮\",\"/products/407.html|食用油\",\"/products/408.html|调味品\",\"/products/409.html|南北干货\",\"/products/410.html|方便食品\",\"/products/411.html|有机食品\"]},{\"u\":\"/products/412.html\",\"n\":\"饮料冲调\",\"i\":[\"/products/413.html|饮用水\",\"/products/414.html|饮料\",\"/products/415.html|牛奶乳品\",\"/products/416.html|咖啡/奶茶\",\"/products/417.html|冲饮谷物\",\"/products/418.html|蜂蜜/柚子茶\",\"/products/419.html|成人奶粉\"]},{\"u\":\"/products/420.html\",\"n\":\"食品礼券\",\"i\":[\"/products/421.html|月饼\",\"/products/422.html|大闸蟹\",\"/products/423.html|粽子\",\"/products/424.html|卡券\"]},{\"u\":\"/products/425.html\",\"n\":\"茗茶\",\"i\":[\"/products/426.html|铁观音\",\"/products/427.html|普洱\",\"/products/428.html|龙井\",\"/products/429.html|绿茶\",\"/products/430.html|红茶\",\"/products/431.html|乌龙茶\",\"/products/432.html|花草茶\",\"/products/433.html|花果茶\",\"/products/434.html|养生茶\",\"/products/435.html|黑茶\",\"/products/436.html|白茶\",\"/products/437.html|其它茶\"]}]},{\"u\":\"/products/438.html\",\"n\":\"<a href='/products/438.html'>汽车用品</a>\",\"i\":[{\"u\":\"/products/439.html\",\"n\":\"维修保养\",\"i\":[\"/products/440.html|润滑油\",\"/products/441.html|添加剂\",\"/products/442.html|防冻液\",\"/products/443.html|滤清器\",\"/products/444.html|火花塞\",\"/products/445.html|雨刷\",\"/products/446.html|车灯\",\"/products/447.html|后视镜\",\"/products/448.html|轮胎\",\"/products/449.html|轮毂\",\"/products/450.html|刹车片/盘\",\"/products/451.html|喇叭/皮带\",\"/products/452.html|蓄电池\",\"/products/453.html|底盘装甲/护板\",\"/products/454.html|贴膜\",\"/products/455.html|汽修工具\"]},{\"u\":\"/products/456.html\",\"n\":\"车载电器\",\"i\":[\"/products/457.html|导航仪\",\"/products/458.html|安全预警仪\",\"/products/459.html|行车记录仪\",\"/products/460.html|倒车雷达\",\"/products/461.html|蓝牙设备\",\"/products/462.html|时尚影音\",\"/products/463.html|净化器\",\"/products/464.html|电源\",\"/products/465.html|冰箱\",\"/products/466.html|吸尘器\"]},{\"u\":\"/products/467.html\",\"n\":\"美容清洗\",\"i\":[\"/products/468.html|车蜡\",\"/products/469.html|补漆笔\",\"/products/470.html|玻璃水\",\"/products/471.html|清洁剂\",\"/products/472.html|洗车工具\",\"/products/473.html|洗车配件\"]},{\"u\":\"/products/474.html\",\"n\":\"汽车装饰\",\"i\":[\"/products/475.html|脚垫\",\"/products/476.html|座垫\",\"/products/477.html|座套\",\"/products/478.html|后备箱垫\",\"/products/479.html|头枕腰靠\",\"/products/480.html|香水\",\"/products/481.html|空气净化\",\"/products/482.html|车内饰品\",\"/products/483.html|功能小件\",\"/products/484.html|车身装饰件\",\"/products/485.html|车衣\"]},{\"u\":\"/products/486.html\",\"n\":\"安全自驾\",\"i\":[\"/products/487.html|安全座椅\",\"/products/488.html|胎压充气\",\"/products/489.html|防盗设备\",\"/products/490.html|应急救援\",\"/products/491.html|保温箱\",\"/products/492.html|储物箱\",\"/products/493.html|自驾野营\",\"/products/494.html|摩托车装备\"]}]},{\"u\":\"/products/495.html\",\"n\":\"<a href='/products/495.html'>玩具乐器</a>\",\"i\":[{\"u\":\"/products/496.html\",\"n\":\"适用年龄\",\"i\":[\"/products/497.html|0-6个月\",\"/products/498.html|6-12个月\",\"/products/499.html|1-3岁\",\"/products/500.html|3-6岁\",\"/products/501.html|6-14岁\",\"/products/502.html|14岁以上\"]},{\"u\":\"/products/503.html\",\"n\":\"遥控/电动\",\"i\":[\"/products/504.html|遥控车\",\"/products/505.html|遥控飞机\",\"/products/506.html|遥控船\",\"/products/507.html|机器人/电动\",\"/products/508.html|轨道/助力\"]},{\"u\":\"/products/509.html\",\"n\":\"毛绒布艺\",\"i\":[\"/products/510.html|毛绒/布艺\",\"/products/511.html|靠垫/抱枕\"]},{\"u\":\"/products/512.html\",\"n\":\"娃娃玩具\",\"i\":[\"/products/513.html|芭比娃娃\",\"/products/514.html|卡通娃娃\",\"/products/515.html|智能娃娃\"]},{\"u\":\"/products/516.html\",\"n\":\"模型玩具\",\"i\":[\"/products/517.html|仿真模型\",\"/products/518.html|拼插模型\",\"/products/519.html|收藏爱好\"]},{\"u\":\"/products/520.html\",\"n\":\"健身玩具\",\"i\":[\"/products/521.html|炫舞毯\",\"/products/522.html|爬行垫/毯\",\"/products/523.html|户外玩具\",\"/products/524.html|戏水玩具\"]},{\"u\":\"/products/525.html\",\"n\":\"动漫玩具\",\"i\":[\"/products/526.html|电影周边\",\"/products/527.html|卡通周边\",\"/products/528.html|网游周边\"]},{\"u\":\"/products/529.html\",\"n\":\"益智玩具\",\"i\":[\"/products/530.html|摇铃/床铃\",\"/products/531.html|健身架\",\"/products/532.html|早教启智\",\"/products/533.html|拖拉玩具\"]},{\"u\":\"/products/534.html\",\"n\":\"积木拼插\",\"i\":[\"/products/535.html|积木\",\"/products/536.html|拼图\",\"/products/537.html|磁力棒\",\"/products/538.html|立体拼插\"]},{\"u\":\"/products/539.html\",\"n\":\"DIY玩具\",\"i\":[\"/products/540.html|手工彩泥\",\"/products/541.html|绘画工具\",\"/products/542.html|情景玩具\"]},{\"u\":\"/products/543.html\",\"n\":\"创意减压\",\"i\":[\"/products/544.html|减压玩具\",\"/products/545.html|创意玩具\"]},{\"u\":\"/products/546.html\",\"n\":\"乐器相关\",\"i\":[\"/products/547.html|钢琴\",\"/products/548.html|电子琴\",\"/products/549.html|手风琴\",\"/products/550.html|吉他/贝斯\",\"/products/551.html|民族管弦乐器\",\"/products/552.html|西洋管弦乐\",\"/products/553.html|口琴/口风琴/竖笛\",\"/products/554.html|西洋打击乐器\",\"/products/555.html|各式乐器配件\",\"/products/556.html|电脑音乐\",\"/products/557.html|工艺礼品乐器\"]}]},{\"u\":\"/products/558.html\",\"n\":\"<a href='/products/558.html'>手机</a>\",\"i\":[{\"u\":\"/products/559.html\",\"n\":\"手机通讯\",\"i\":[\"/products/560.html|手机\",\"/products/561.html|对讲机\"]},{\"u\":\"/products/562.html\",\"n\":\"运营商\",\"i\":[\"/products/563.html|购机送费\",\"/products/564.html|“0”元购机\",\"/products/565.html|选号中心\",\"/products/566.html|选号入网\"]},{\"u\":\"/products/567.html\",\"n\":\"手机配件\",\"i\":[\"/products/568.html|手机电池\",\"/products/569.html|蓝牙耳机\",\"/products/570.html|充电器/数据线\",\"/products/571.html|手机耳机\",\"/products/572.html|手机贴膜\",\"/products/573.html|手机存储卡\",\"/products/574.html|手机保护套\",\"/products/575.html|车载配件\",\"/products/576.html|iPhone 配件\",\"/products/577.html|创意配件\",\"/products/578.html|便携/无线音响\",\"/products/579.html|手机饰品\"]}]},{\"u\":\"/products/580.html\",\"n\":\"<a href='/products/580.html'>数码</a>\",\"i\":[{\"u\":\"/products/581.html\",\"n\":\"摄影摄像\",\"i\":[\"/products/582.html|数码相机\",\"/products/583.html|单电/微单相机\",\"/products/584.html|单反相机\",\"/products/585.html|摄像机\",\"/products/586.html|拍立得\",\"/products/587.html|运动相机\",\"/products/588.html|镜头\",\"/products/589.html|户外器材\",\"/products/590.html|影棚器材\"]},{\"u\":\"/products/591.html\",\"n\":\"数码配件\",\"i\":[\"/products/592.html|存储卡\",\"/products/593.html|读卡器\",\"/products/594.html|滤镜\",\"/products/595.html|闪光灯/手柄\",\"/products/596.html|相机包\",\"/products/597.html|三脚架/云台\",\"/products/598.html|相机清洁\",\"/products/599.html|相机贴膜\",\"/products/600.html|机身附件\",\"/products/601.html|镜头附件\",\"/products/602.html|电池/充电器\",\"/products/603.html|移动电源\"]},{\"u\":\"/products/604.html\",\"n\":\"智能设备\",\"i\":[\"/products/605.html|智能手环\",\"/products/606.html|智能手表\",\"/products/607.html|智能眼镜\",\"/products/608.html|运动跟踪器\",\"/products/609.html|健康监测\",\"/products/610.html|智能配饰\",\"/products/611.html|智能家居\",\"/products/612.html|体感车\",\"/products/613.html|其他配件\"]},{\"u\":\"/products/614.html\",\"n\":\"时尚影音\",\"i\":[\"/products/615.html|MP3/MP4\",\"/products/616.html|智能设备\",\"/products/617.html|耳机/耳麦\",\"/products/618.html|音箱\",\"/products/619.html|高清播放器\",\"/products/620.html|MP3/MP4配件\",\"/products/621.html|麦克风\",\"/products/622.html|专业音频\",\"/products/623.html|数码相框\",\"/products/624.html|苹果配件\"]},{\"u\":\"/products/625.html\",\"n\":\"电子教育\",\"i\":[\"/products/626.html|电子词典\",\"/products/627.html|电纸书\",\"/products/628.html|录音笔\",\"/products/629.html|复读机\",\"/products/630.html|点读机/笔\",\"/products/631.html|学生平板\",\"/products/632.html|早教机\"]}]},{\"u\":\"/products/633.html\",\"n\":\"<a href='/products/633.html'>家居家装</a>\",\"i\":[{\"u\":\"/products/634.html\",\"n\":\"家纺\",\"i\":[\"/products/635.html|床品套件\",\"/products/636.html|被子\",\"/products/637.html|枕芯\",\"/products/638.html|床单被罩\",\"/products/639.html|毯子\",\"/products/640.html|床垫/床褥\",\"/products/641.html|蚊帐\",\"/products/642.html|抱枕靠垫\",\"/products/643.html|毛巾浴巾\",\"/products/644.html|电热毯\",\"/products/645.html|窗帘/窗纱\",\"/products/646.html|布艺软饰\",\"/products/647.html|凉席\"]},{\"u\":\"/products/648.html\",\"n\":\"灯具\",\"i\":[\"/products/649.html|台灯\",\"/products/650.html|节能灯\",\"/products/651.html|装饰灯\",\"/products/652.html|落地灯\",\"/products/653.html|应急灯/手电\",\"/products/654.html|LED灯\",\"/products/655.html|吸顶灯\",\"/products/656.html|五金电器\",\"/products/657.html|筒灯射灯\",\"/products/658.html|吊灯\",\"/products/659.html|氛围照明\"]},{\"u\":\"/products/660.html\",\"n\":\"生活日用\",\"i\":[\"/products/661.html|收纳用品\",\"/products/662.html|雨伞雨具\",\"/products/663.html|浴室用品\",\"/products/664.html|缝纫/针织用品\",\"/products/665.html|洗晒/熨烫\",\"/products/666.html|净化除味\"]},{\"u\":\"/products/667.html\",\"n\":\"家装软饰\",\"i\":[\"/products/668.html|桌布/罩件\",\"/products/669.html|地毯地垫\",\"/products/670.html|沙发垫套/椅垫\",\"/products/671.html|相框/照片墙\",\"/products/672.html|装饰字画\",\"/products/673.html|节庆饰品\",\"/products/674.html|手工/十字绣\",\"/products/675.html|装饰摆件\",\"/products/676.html|保暖防护\",\"/products/677.html|帘艺隔断\",\"/products/678.html|墙贴/装饰贴\",\"/products/679.html|钟饰\",\"/products/680.html|花瓶花艺\",\"/products/681.html|香薰蜡烛\",\"/products/682.html|创意家居\"]},{\"u\":\"/products/683.html\",\"n\":\"清洁用品\",\"i\":[\"/products/684.html|纸品湿巾\",\"/products/685.html|衣物清洁\",\"/products/686.html|清洁工具\",\"/products/687.html|驱虫用品\",\"/products/688.html|家庭清洁\",\"/products/689.html|皮具护理\",\"/products/690.html|一次性用品\"]},{\"u\":\"/products/691.html\",\"n\":\"宠物生活\",\"i\":[\"/products/692.html|宠物主粮\",\"/products/693.html|宠物零食\",\"/products/694.html|医疗保健\",\"/products/695.html|家居日用\",\"/products/696.html|宠物玩具\",\"/products/697.html|出行装备\",\"/products/698.html|洗护美容\"]}]},{\"u\":\"/products/699.html\",\"n\":\"<a href='/products/699.html'>厨具</a>\",\"i\":[{\"u\":\"/products/700.html\",\"n\":\"烹饪锅具\",\"i\":[\"/products/701.html|炒锅\",\"/products/702.html|煎锅\",\"/products/703.html|压力锅\",\"/products/704.html|蒸锅\",\"/products/705.html|汤锅\",\"/products/706.html|奶锅\",\"/products/707.html|锅具套装\",\"/products/708.html|煲类\",\"/products/709.html|水壶\",\"/products/710.html|火锅\"]},{\"u\":\"/products/711.html\",\"n\":\"刀剪菜板\",\"i\":[\"/products/712.html|菜刀\",\"/products/713.html|剪刀\",\"/products/714.html|刀具套装\",\"/products/715.html|砧板\",\"/products/716.html|瓜果刀/刨\",\"/products/717.html|多功能刀\"]},{\"u\":\"/products/718.html\",\"n\":\"厨房配件\",\"i\":[\"/products/719.html|保鲜盒\",\"/products/720.html|烘焙/烧烤\",\"/products/721.html|饭盒/提锅\",\"/products/722.html|储物/置物架\",\"/products/723.html|厨房DIY/小工具\"]},{\"u\":\"/products/724.html\",\"n\":\"水具酒具\",\"i\":[\"/products/725.html|塑料杯\",\"/products/726.html|运动水壶\",\"/products/727.html|玻璃杯\",\"/products/728.html|陶瓷/马克杯\",\"/products/729.html|保温杯\",\"/products/730.html|保温壶\",\"/products/731.html|酒杯/酒具\",\"/products/732.html|杯具套装\"]},{\"u\":\"/products/733.html\",\"n\":\"餐具\",\"i\":[\"/products/734.html|餐具套装\",\"/products/735.html|碗/碟/盘\",\"/products/736.html|筷勺/刀叉\",\"/products/737.html|一次性用品\",\"/products/738.html|果盘/果篮\"]},{\"u\":\"/products/739.html\",\"n\":\"茶具/咖啡具\",\"i\":[\"/products/740.html|整套茶具\",\"/products/741.html|茶杯\",\"/products/742.html|茶壶\",\"/products/743.html|茶盘茶托\",\"/products/744.html|茶叶罐\",\"/products/745.html|茶具配件\",\"/products/746.html|茶宠摆件\",\"/products/747.html|咖啡具\",\"/products/748.html|其他\"]}]},{\"u\":\"/products/749.html\",\"n\":\"<a href='/products/749.html'>服饰内衣</a>\",\"i\":[{\"u\":\"/products/750.html\",\"n\":\"女装\",\"i\":[\"/products/751.html|T恤\",\"/products/752.html|衬衫\",\"/products/753.html|针织衫\",\"/products/754.html|雪纺衫\",\"/products/755.html|卫衣\",\"/products/756.html|马甲\",\"/products/757.html|连衣裙\",\"/products/758.html|半身裙\",\"/products/759.html|牛仔裤\",\"/products/760.html|休闲裤\",\"/products/761.html|打底裤\",\"/products/762.html|正装裤\",\"/products/763.html|小西装\",\"/products/764.html|短外套\",\"/products/765.html|风衣\",\"/products/766.html|毛呢大衣\",\"/products/767.html|真皮皮衣\",\"/products/768.html|棉服\",\"/products/769.html|羽绒服\",\"/products/770.html|大码女装\",\"/products/771.html|中老年女装\",\"/products/772.html|婚纱\",\"/products/773.html|打底衫\",\"/products/774.html|旗袍/唐装\",\"/products/775.html|加绒裤\",\"/products/776.html|吊带/背心\",\"/products/777.html|羊绒衫\",\"/products/778.html|短裤\",\"/products/779.html|皮草\",\"/products/780.html|礼服\",\"/products/781.html|仿皮皮衣\",\"/products/782.html|羊毛衫\",\"/products/783.html|设计师/潮牌\"]},{\"u\":\"/products/784.html\",\"n\":\"男装\",\"i\":[\"/products/785.html|衬衫\",\"/products/786.html|T恤\",\"/products/787.html|POLO衫\",\"/products/788.html|针织衫\",\"/products/789.html|羊绒衫\",\"/products/790.html|卫衣\",\"/products/791.html|马甲/背心\",\"/products/792.html|夹克\",\"/products/793.html|风衣\",\"/products/794.html|毛呢大衣\",\"/products/795.html|仿皮皮衣\",\"/products/796.html|西服\",\"/products/797.html|棉服\",\"/products/798.html|羽绒服\",\"/products/799.html|牛仔裤\",\"/products/800.html|休闲裤\",\"/products/801.html|西裤\",\"/products/802.html|西服套装\",\"/products/803.html|大码男装\",\"/products/804.html|中老年男装\",\"/products/805.html|唐装/中山装\",\"/products/806.html|工装\",\"/products/807.html|真皮皮衣\",\"/products/808.html|加绒裤\",\"/products/809.html|卫裤/运动裤\",\"/products/810.html|短裤\",\"/products/811.html|设计师/潮牌\",\"/products/812.html|羊毛衫\"]},{\"u\":\"/products/813.html\",\"n\":\"内衣\",\"i\":[\"/products/814.html|文胸\",\"/products/815.html|女式内裤\",\"/products/816.html|男式内裤\",\"/products/817.html|睡衣/家居服\",\"/products/818.html|塑身美体\",\"/products/819.html|泳衣\",\"/products/820.html|吊带/背心\",\"/products/821.html|抹胸\",\"/products/822.html|连裤袜/丝袜\",\"/products/823.html|美腿袜\",\"/products/824.html|商务男袜\",\"/products/825.html|保暖内衣\",\"/products/826.html|情侣睡衣\",\"/products/827.html|文胸套装\",\"/products/828.html|少女文胸\",\"/products/829.html|休闲棉袜\",\"/products/830.html|大码内衣\",\"/products/831.html|内衣配件\",\"/products/832.html|打底裤袜\",\"/products/833.html|打底衫\",\"/products/834.html|秋衣秋裤\",\"/products/835.html|情趣内衣\"]},{\"u\":\"/products/836.html\",\"n\":\"服饰配件\",\"i\":[\"/products/837.html|太阳镜\",\"/products/838.html|光学镜架/镜片\",\"/products/839.html|围巾/手套/帽子套装\",\"/products/840.html|袖扣\",\"/products/841.html|棒球帽\",\"/products/842.html|毛线帽\",\"/products/843.html|遮阳帽\",\"/products/844.html|老花镜\",\"/products/845.html|装饰眼镜\",\"/products/846.html|防辐射眼镜\",\"/products/847.html|游泳镜\",\"/products/848.html|女士丝巾/围巾/披肩\",\"/products/849.html|男士丝巾/围巾\",\"/products/850.html|鸭舌帽\",\"/products/851.html|贝雷帽\",\"/products/852.html|礼帽\",\"/products/853.html|真皮手套\",\"/products/854.html|毛线手套\",\"/products/855.html|防晒手套\",\"/products/856.html|男士腰带/礼盒\",\"/products/857.html|女士腰带/礼盒\",\"/products/858.html|钥匙扣\",\"/products/859.html|遮阳伞/雨伞\",\"/products/860.html|口罩\",\"/products/861.html|耳罩/耳包\",\"/products/862.html|假领\",\"/products/863.html|毛线/布面料\",\"/products/864.html|领带/领结/领带夹\"]}]}]}";
        FileUtils.writeString2SimpleFile(json, "/home/ios666/Desktop/category.json", "utf-8");
    }

}