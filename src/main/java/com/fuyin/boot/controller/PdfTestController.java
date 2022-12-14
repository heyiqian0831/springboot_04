package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.utils.MyXMLWorkerHelper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: pdfTestController
 * @Author: 何义祈安
 * @Description: pdf测试接口
 * @Date: 2022/11/27 9:49
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/pdf")
public class PdfTestController {
    @RequestMapping(value = "/test")
    public ResponseResult test(){
        return new ResponseResult(200,"测试成功,Welcome SpringBoot");
    }

    @GetMapping("/createPdfSaveFile")
    public ResponseResult createPdfSaveFile(){
//        G:\NetClassAll\SpringBootProject
        String path = "d:\\upload\\textffff.pdf";
        try {
            // 判断路径中文件夹是否存在,不存在则自动创建,防止因为文件夹不存在而报错
            creatNewFile(path);
            // 创建文档
            Document document = new Document(PageSize.A4,60,60,15,40);
            // 创建输入流
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // 制作大文本数据
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= 100; i++){
                stringBuilder.append("如果说荷兰是橙色的，那阿姆斯特丹就是缤纷的彩色。");
            }
            // 添加数据
            String pocketDescription = "<html><body><p class=\"\" style=\"\">如果说荷兰是橙色的，那阿姆斯特丹就是缤纷的彩色。"+stringBuilder.toString()+"</p>  <p class=\"\" style=\"\"><strong>游玩建议</strong><br>游玩整个阿姆斯特丹大约需2-3天</p></body></html>";
            Paragraph context = new Paragraph();
            // html 的处理
            ElementList elementList = MyXMLWorkerHelper.parseToElementList(pocketDescription, null);

            // 写入到 段落 Paragraph
            for (Element element : elementList) {
                context.add(element);
            }
            context.setSpacingBefore(10f);
            document.add(context);
            document.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return new ResponseResult(200,"测试成功","testok");
    }

    @GetMapping("/createPdfDownload")
    public String createPdfToDownload(HttpServletResponse response) throws IOException {

        // 生成 pdf 名称
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = simpleDateFormat.format(date);
        String datetime = str.replace("-", "").replace(" ", "").replace(":","");
        String filename = "pdf_"+datetime+"_content.pdf";

        // 创建PDF
        Document document = new Document(PageSize.A4,60,60,15,40);
        try {
            // 设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            //常用的有paragraph段落、phrase语句块、chunk最小单位块
            OutputStream out = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            // 打开文档
            document.open();
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            for(int n = 0; n<2; n++){
                // 添加文字水印
                PdfContentByte cb = writer.getDirectContent();
                cb.beginText(); // 开始
                // 设置透明度
                PdfGState gs = new PdfGState();
                gs.setFillOpacity(0.1f);
                cb.setGState(gs);
                cb.setFontAndSize(bfChinese,100);
                cb.showTextAligned(Element.ALIGN_CENTER, "富银集团", 340, 410 , 60);
                cb.endText(); // 结束

                // 添加标题
                //通过Font去设置字体的基本属性：大小，加粗等等
                Font font = new Font(bfChinese, 15, Font.NORMAL, BaseColor.BLACK);
                // 创建段落
                Paragraph title = new Paragraph("xxx企业债务重组方案", font);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingBefore(40f);
                document.add(title);

                // 制作大文本数据
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= 100; i++){
                    stringBuilder.append("(一)以现金清偿债务\n" +
                            "\n" +
                            "债务人应当将重组债务的账面价值与支付的现金之间的差额确认为债务重组利得，作为营业外收入，计入当期损益。其中，相关重组债务应当在满足金融负债终止确认条件时予以终止确认。\n" +
                            "\n" +
                            "债权人应当将重组债权的账面余额与收到的现金之间的差额确认为债务重组损失，作为营业外支出，计入当期损益。其中，相关重组债权应当在满足金融资产终止确认条件时予以终止确认。重组债权已经计提减值准备的，应当先将差额冲减已计提的减值准备，冲减后仍有损失的，计入营业外支出(债务重组损失);冲减后减值准备仍有余额的，应予转回并抵减当期资产减值损失。\n" +
                            "\n" +
                            "(二)以非现金资产清偿某项债务\n" +
                            "\n" +
                            "债务人应当将重组债务的账面价值与转让的非现金资产的公允价值之间的差额确认为债务重组利得，作为营业外收入，计入当期损益。其中，相关重组债务应当在满足金融负债终止确认条件时予以终止确认。转让的非现金资产的公允价值与其账面价值的差额作为转让资产损益，计入当期损益。\n" +
                            "\n" +
                            "债务人在转让非现金资产的过程中发生的一些税费，如资产评估费、运杂费等，直接计入转让资产损益。对于增值税应税项目，如债权人不向债务人另行支付增值税，则债务重组利得应为转让非现金资产的公允价值和该非现金资产的增值税销项税额与重组债务账面价值的差额;如债权人向债务人另行支付增值税，则债务重组利得应为转让非现金资产的公允价值与重组债务账面价值的差额。\n" +
                            "\n" +
                            "债权人应当对受让的非现金资产按其公允价值入账，重组债权的账面余额与受让的非现金资产的公允价值之间的差额，确认为债务重组损失，作为营业外支出，计入当期损益。其中，相关重组债权应当在满足金融资产终止确认条件时予以终止确认。重组债权已经计提减值准备的，应当先将差额冲减已计提的减值准备，冲减后仍有损失的，计入营业外支出(债务重组损失);冲减后减值准备仍有余额的，应予转回并抵减当期资产减值损失。对于增值税应税项目，如债权人不向债务人另行支付增值税，则增值税进项税额可以作为冲减重组债权的账面余额处理;如债权人向债务人另行支付增值税，则增值税进项税额不能作为重组债权的账面余额处理。\n" +
                            "\n" +
                            "债权人收到非现金资产时发生的有关运杂费等，应当计入相关资产的价值。\n" +
                            "\n" +
                            "1、以库存材料、商品产品抵偿债务\n" +
                            "\n" +
                            "债务人应视同销售进行核算。企业可将该项业务分为两部分，一是将库存材料、商品产品出售给债权人，取得货款。出售库存材料、商品产品业务与企业正常的销售业务处理相同，其发生的损益计入当期损益。二是以取得的货币清偿债务。但在这项业务中实际上并没有发生相应的货币流入与流出。\n" +
                            "\n" +
                            "2、以固定资产抵偿债务\n" +
                            "\n" +
                            "债务人应将固定资产的公允价值与该项固定资产账面价值和清理费用的差额作为转让固定资产的损益处理。同时，将固定资产的公允价值与应付债务的账面价值的差额，作为债务重组利得，计入营业外收入。债权人收到的固定资产按公允价值计量。\n" +
                            "\n" +
                            "3、以股票、债券等金融资产抵偿债务\n" +
                            "\n" +
                            "债务人应按相关金融资产的公允价值与其账面价值的差额，作为转让金融资产的利得或损失处理;相关金融资产的公允价值与重组债务的账面价值的差额，作为债务重组利得。债权人收到的相关金融资产按公允价值计量。");
                }
                Font font1  = new Font(bfChinese, 10, Font.NORMAL, BaseColor.BLACK);
                Paragraph context = new Paragraph(stringBuilder.toString(), font1);
                context.setFirstLineIndent(20);
                context.setLeading(12);
                context.setSpacingBefore(10f);
                document.add(context);

                // 开启新的一页
                document.newPage();
                //显示空内容的页
                writer.setPageEmpty(false);
            }
            // 关闭流
            document.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return "ok";
    }


    /**
     * 检查是否存在文件夹并创建
     *
     * @param path
     * @throws IOException
     */
    public static File creatNewFile(String path) throws IOException {
        File file = new File(path);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        return file;
    }

}
