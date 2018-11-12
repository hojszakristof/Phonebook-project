package phonebook;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.collections.ObservableList;


public class PDFGeneration {
    public void pdfGeneration(String fileName, ObservableList<Person> data){
        Document document = new Document();                             //Ez maga a fájl, tehát a pdf
        try {
            //Logó
            PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));       //fájlnev.pdj kiterjesztés elmentés
            document.open();                                                               //dokumentum megnyitása
            Image image1 = Image.getInstance(getClass().getResource("/register.png"));
            image1.scaleToFit(200,86);                                                     //méretezés
            image1.setAbsolutePosition(200f, 750f);                                        //pozíció
            document.add(image1);
            
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n" + text, FontFactory.getFont("betutipus", BaseFont.IDENTITY_H, BaseFont.EMBEDDED))); // /n új sor
            document.add(new Paragraph("\n\n\n\n\n\n\n\n"));
            
            //Táblázat
            float[] columnWidth = {2, 4, 4, 5};                //ebbe tároljuk, hogy az oszlopok mennyire legyenek szélesek
            PdfPTable table = new PdfPTable(columnWidth);   //szélességet átadjuk paraméterként
            table.setWidthPercentage(100);                  //100%-os szélesség
            PdfPCell cell = new PdfPCell(new Phrase("Kontaktlista")); //header cella
            cell.setBackgroundColor(GrayColor.GRAYWHITE);            //háttérszín
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);       //vízszintes pozícionálás középre igazítva
            cell.setColspan(4);                                      //4 oszlop széles legyen
            table.addCell(cell);                                     //átadjuk a táblának
            
            
            table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));        //alapértelmezett celle minden cellának a színét beállítja
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);    //
            table.addCell("Sorszám");
            table.addCell("Vezetéknév");
            table.addCell("Keresztnév");
            table.addCell("E-mail cím");
            table.setHeaderRows(1);                                                //
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
            for(int i=1; i<=data.size(); i++){
                Person actualPerson = data.get(i-1);
                table.addCell("" + i);
                table.addCell(actualPerson.getLastName());
                table.addCell(actualPerson.getFirstName());
                table.addCell(actualPerson.getEmail());
            }
            
            document.add(table);
            
            
            //Aláírás
            Chunk signature = new Chunk("\n\n Generálva a Nyilvántartás alkalmazás segítségével");
            Paragraph base = new Paragraph(signature);
            document.add(base);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        document.close();
    }

    

}
