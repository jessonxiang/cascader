package com.bh.proprietor.core.tools.util;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chandler on 2017/9/28 0028.
 */
public class POIUtil {

    private static Logger LOG = LoggerFactory.getLogger(PageUtil.class);

    private static Pattern word03 = Pattern.compile("\\.doc^");
    private static Pattern word07 = Pattern.compile("\\.docx^");

    /**
     * 根据模板后缀选择POI 操作对象
     *
     * @param templatename
     * @param keyvalue
     * @param output
     */
    public static void printWordfromTemplate(String templatename, Map<String, String> keyvalue, OutputStream output) {
        Matcher m03 = word03.matcher(templatename);
        Matcher m07 = word07.matcher(templatename);
        if (m03.find()) {

        } else if (m07.find()) {

        }
    }


    /**
     * 针对word2007文件的打印
     *
     * @param keyvalue
     * @param document
     */
    public static void printWordDoc(Map<String, String> keyvalue, XWPFDocument document, OutputStream output) throws Exception {
        List<XWPFParagraph> allParagraph = document.getParagraphs();
        for (XWPFParagraph paragraph : allParagraph) {
            for (Map.Entry<String, String> kv : keyvalue.entrySet()) {
                String find = kv.getKey();
                String repl = kv.getValue();
                TextSegement found = paragraph.searchText(find, new PositionInParagraph());
                if (found != null && repl != null) {
                    replaceText(found, paragraph.getRuns(), find, repl);
                }
            }
        }
        document.write(output);
    }

    /**
     * 针对word2003文件的打印
     *
     * @param keyvalue
     * @param document
     * @param output
     * @throws Exception
     */
    public static void printWordDoc(Map<String, String> keyvalue, HWPFDocument document, OutputStream output) throws Exception {
        Range range = document.getRange();
        //文档段落数目
        int paragraphCount = range.numParagraphs();
        for (int i = 0 ; i < paragraphCount ; i++) {
            Paragraph pph = range.getParagraph(i);
            for (Map.Entry<String, String> kv : keyvalue.entrySet()) {
                String find = kv.getKey();
                String repl = kv.getValue();
                pph.replaceText(find, repl);
            }
        }
        document.write(output);
    }


    /**
     * 替换占位符为内容
     *
     * @param found 查找到的语句块
     * @param runs  段落中的所有片段
     * @param find  寻找的字符串
     * @param repl  替换的值
     */
    public static void replaceText(TextSegement found, List<XWPFRun> runs,
                                   String find, Object repl) {
        if (found.getBeginRun() == found.getEndRun()) {
            // whole search string is in one Run
            XWPFRun run = runs.get(found.getBeginRun());
            String runText = run.getText(run.getTextPosition());
            String replaced = runText.replace(find, repl.toString());
            run.setText(replaced, 0);
        } else {
            // The search string spans over more than one Run
            // Put the Strings together
            StringBuilder b = new StringBuilder();
            for (int runPos = found.getBeginRun() ; runPos <= found
                    .getEndRun() ; runPos++) {
                XWPFRun run = runs.get(runPos);
                b.append(run.getText(run.getTextPosition()));
            }
            String connectedRuns = b.toString();
            String replaced = connectedRuns.replace(find, repl.toString());

            // The first Run receives the replaced String of all
            // connected Runs
            XWPFRun partOne = runs.get(found.getBeginRun());
            partOne.setText(replaced, 0);
            // Removing the text in the other Runs.
            for (int runPos = found.getBeginRun() + 1 ; runPos <= found
                    .getEndRun() ; runPos++) {
                XWPFRun partNext = runs.get(runPos);
                partNext.setText("", 0);
            }
        }
    }
}
