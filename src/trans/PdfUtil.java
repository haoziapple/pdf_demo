package trans;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

/**
 * @className:trans.PdfToText
 * @description:处理pdf工具类
 * @version:v1.0.0
 * @date:2016-1-29 下午3:06:52
 * @author:WangHao
 */
public class PdfUtil
{

	/**
	 * 将pdf中的文本读取为String,不支持中文
	 */
	public static String getStringFromPDF(String file) throws Exception
	{
		String c = "";
		PDDocument pdfdoc = null;
		File f = new File(file);
		pdfdoc = PDDocument.load(f);
		PDFTextStripper stripper = new PDFTextStripper();
		c = stripper.getText(pdfdoc);
		pdfdoc.close();
		return c;
	}

	/**
	 * 
	 * @Description:将pdf转为jpg, 使用PDF Rendering
	 * @param input 输入pdf文件路径
	 * @param output 输出图片路径加文件名，后面会拼上page序号
	 * @param dpi 图片分辨率
	 * @throws Exception
	 * @version:v1.0
	 * @author:WangHao
	 * @date:2016-1-29 下午3:53:07
	 */
	public static int pdfToJpg(String input, String output, int dpi) throws Exception
	{
		PDDocument pdf = PDDocument.load(new File(input));
		PDFRenderer pdfRenderer = new PDFRenderer(pdf);
		BufferedImage bim = null;
		int pageCount = pdf.getNumberOfPages();
		System.out.println("pageCount is " + pageCount);
		// 初始化，从第一个page开始转化
		pageCount = 0;
		PDPageTree pageTree = pdf.getDocumentCatalog().getPages();
		for (@SuppressWarnings("unused")
		PDPage page : pageTree)
		{
			bim = pdfRenderer.renderImageWithDPI(pageCount, dpi, ImageType.RGB);
			ImageIOUtil.writeImage(bim, output + "-" + (pageCount++) + ".jpg", dpi);
		}
		pdf.close();
		return pageCount;
	}

	/**
	 * 将String写成pdf,不支持中文
	 */
	public static void createPDF(String s, String filePath) throws Exception
	{
		PDDocument doc = new PDDocument();
		PDPage page = new PDPage();
		doc.addPage(page);
		PDFont font = PDType1Font.TIMES_ROMAN;

		PDPageContentStream stream = new PDPageContentStream(doc, page);
		stream.beginText();
		// 设定字体
		stream.setFont(font, 14);
		// 设定字符开始位置
		stream.newLineAtOffset(100, 700);

		stream.showText(s);
		stream.endText();
		stream.close();

		doc.save(filePath);
		doc.close();
	}

	/**
	 * test
	 */
	public static void main(String[] args) throws Exception
	{
		// 测试pdf转String
		// String os = getContent("f:/SQL语法练习.pdf");
		// System.out.println(os);

		// 测试String转pdf
		// createPDF("test write test write 中文!", "f:/testCreate.pdf");

		// 测试pdf转jpg
		int num = pdfToJpg("C:\\Users\\Administrator\\Desktop\\申请材料\\承诺书1.pdf", "C:\\Users\\Administrator\\Desktop\\申请材料\\承诺书", 300);
		System.out.print(num);
	}

}
