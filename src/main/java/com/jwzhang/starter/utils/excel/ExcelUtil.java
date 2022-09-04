package com.jwzhang.starter.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.jwzhang.starter.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Excel工具类
 *
 * @author zjw
 * @since 2022/8/19
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param datas     导出数据
     * @param sheetName excel-sheet名称
     * @param response  {@link HttpServletResponse}
     */
    public static <T> void exportExcel(List<T> datas, String sheetName, Class<T> clazz, HttpServletResponse response) {
        try {
            // 设置文本类型
            response.setContentType("application/vnd.ms-excel");
            // 设置字符编码
            response.setCharacterEncoding("utf-8");
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename=demo.xlsx");
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(datas);
        } catch (Exception e) {
            log.error("导出Excel异常：", e);
            throw new CustomException("导出Excel失败，请联系管理员！");
        }
    }

    /**
     * 导入excel，转换excel中的字段为java对象
     * @param file 导入信息
     * @param clazz 转换的类
     * @param <T> 转换泛型
     * @return 结果
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) {
        try {
            return EasyExcel.read(file.getInputStream())
                    .head(clazz)
                    .sheet()
                    .doReadSync();
        } catch (Exception e) {
            log.error("导入Excel异常：", e);
            throw new CustomException("导入Excel失败");
        }
    }
}
