package capstone.jakdu.ocrtest;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


public class ocrexample {

    PDFTextStripper reader = new PDFTextStripper() {
        boolean startOfLine = true;

        @Override
        protected void startPage(PDPage page) throws IOException {
            startOfLine = true;
            super.startPage(page);
        }

        @Override
        protected void writeLineSeparator() throws IOException {
            startOfLine = true;
            super.writeLineSeparator();
        }

        @Override
        protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
            System.out.println("text = " + text);
            if (startOfLine)
            {

                TextPosition firstPosition = textPositions.get(0);

                // writeString(String.format("[%s %s %s]", firstPosition.getFontSizeInPt(),firstPosition.getXDirAdj(), firstPosition.getYDirAdj()));
                startOfLine = false;
            }
            super.writeString(text, textPositions);
        }

    };

    public ocrexample() throws IOException {
    }

    @Test
    public void getAllPageExtractPDF() throws IOException {
        String fileName = "example-페이지-4.pdf";
        String filePath = System.getProperty("user.dir") + "/"+ fileName;
        File source = new File(filePath);
        PDDocument pdfDoc = PDDocument.load(source);
        //reader.setSortByPosition(true);

        String text = reader.getText(pdfDoc);
        System.out.println(text);

    }
    
    @Test
    public void getSomePageExtractPDF() throws IOException {

        String fileName = "수학의힘알파중1-1.pdf";
        File source = new File(fileName);
        PDDocument pdfDoc = PDDocument.load(source);
        int i=4; // page no.
       // PDFTextStripper reader = new PDFTextStripper();
       // reader.set
      //  reader.setWordSeparator(" ");
       // reader.setLineSeparator("\n");

        System.out.println("separate:" + reader.getLineSeparator());

        reader.setStartPage(i);
        reader.setEndPage(i);
      //  reader.setSortByPosition(true);
        String pageText = reader.getText(pdfDoc);
        System.out.println(pageText);
    }

    @Test
    public void pdfMaker() throws IOException{
        //새로운 pdf 생성
        PDDocument doc = new PDDocument();
        //페이지 생성
        PDPage blankpage = new PDPage(PDRectangle.A4);
        doc.addPage(blankpage);
        //
        String fileName = "example-페이지-4.pdf";
        File source = new File(fileName);
        PDDocument pdfDoc = PDDocument.load(source);
        //int i=4; // page no.
        reader.getLineSeparator();
        //reader.setStartPage(i);
        //reader.setEndPage(i);
        //  reader.setSortByPosition(true);
        String pageText = reader.getText(pdfDoc);
        System.out.println(pageText);

        String []text = pageText.split("\n");
        final List<String> list = new ArrayList<String>();
        Collections.addAll(list, text);
        list.remove("\n");


        PDPageContentStream contentStream = new PDPageContentStream(doc,blankpage);

        contentStream.beginText();

        contentStream.setFont(PDType1Font.COURIER,10);
        contentStream.newLineAtOffset(20,0);
        for(int i=0; i<text.length;i++){
            // x, y 좌표 설정
            contentStream.newLineAtOffset(0,10);
            contentStream.showText(text[text.length-i-1]);
        }

        contentStream.endText();

        contentStream.close();

        doc.save("./sample.pdf");
        doc.close();
    }


    @Test
    public void tesseractTest() throws TesseractException, IOException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("kor");
        String fileName = "일등급수학I.pdf";
        String filePath = System.getProperty("user.dir") + "/"+ fileName;
        File source = new File(filePath);
        PDDocument pdfDoc = PDDocument.load(source);

        PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
        for (int page = 3; page < 4; page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 1000, ImageType.GRAY);

            String str = tesseract.doOCR(bufferedImage);
            System.out.println(str);

        }

        //String text = tesseract.doOCR(source);
        //System.out.print(text);
    }
}
