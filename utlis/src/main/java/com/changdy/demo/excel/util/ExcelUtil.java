package com.changdy.demo.excel.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Changdy on 2019/8/17.
 */
@Slf4j
public class ExcelUtil {

    private static Sheet initSheet(String name) {
        Sheet initSheet = new Sheet(1, 0);
        initSheet.setSheetName(name);
        return initSheet;
    }

    public static void writeToPath(String filePath, List<?> data, String sheetName) {
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            ExcelUtil.writeToOutputStream(outputStream, data, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("导出文件异常", e);
        }
    }

    public static void writeToResponse(HttpServletResponse response, List<?> data, String sheetName) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sheetName + ".xlsx", "UTF-8"));
            ExcelUtil.writeToOutputStream(outputStream, data, sheetName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("导出文件异常", e);
        }
    }

    public static void writeToOutputStream(OutputStream outputStream, List<?> data, String sheetName) throws IOException {
        if (data == null || data.isEmpty()) {
            throw new RuntimeException("导出数据null");
        }
        Sheet sheet = initSheet(sheetName);
        List<String> titleList = ReflectUtil.getTitleList(data.get(0));
        List<List<String>> list = new ArrayList<>();
        titleList.forEach(h -> list.add(Collections.singletonList(h)));
        sheet.setHead(list);
        List<List<String>> collect = data.stream().map(ReflectUtil::getBodyList).collect(Collectors.toList());
        ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
        writer.write0(collect, sheet);
        writer.finish();
        outputStream.flush();
    }
}