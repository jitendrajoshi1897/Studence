package com.example.shubh.studence;

import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;

public class Create_pdf extends AppCompatActivity {

    Spinner dates,times,classs;
    SQLiteManager sqLiteManager;
    Cursor getdata;
    String [] datedata;
    String [] timedata;
    String [] classdata;
    Button genreate;

    int ii=0,j=0,k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pdf);
        dates = (Spinner)findViewById(R.id.date);
       // times = (Spinner)findViewById(R.id.timeo);
        classs = (Spinner)findViewById(R.id.clas);
        genreate = (Button)findViewById(R.id.genreatepdf);
        sqLiteManager = new SQLiteManager(this);
        getdata = sqLiteManager.getdatedata();
        datedata = new String[getdata.getCount()];
        Log.e("database","" + getdata.getCount());
        while(getdata.moveToNext())
        {
            datedata[ii]=getdata.getString(0);
            ii++;
        }
        /*getdata = sqLiteManager.gettimedata();
        timedata = new String[getdata.getCount()];
        Log.e("database","" + getdata.getCount());
        while(getdata.moveToNext())
        {
            timedata[j]=getdata.getString(0);
            j++;
        }*/
        getdata = sqLiteManager.getclasspdfdata();
        classdata = new String[getdata.getCount()];
        Log.e("database","" + getdata.getCount());
        while(getdata.moveToNext())
        {
            classdata[k]=getdata.getString(0);
            k++;
        }
        ArrayAdapter<String> dateadapter = new ArrayAdapter<>(Create_pdf.this, android.R.layout.simple_list_item_1, datedata);
        dates.setAdapter(dateadapter);
        /*ArrayAdapter<String> timeadapter = new ArrayAdapter<>(Create_pdf.this, android.R.layout.simple_list_item_1, timedata);
        times.setAdapter(timeadapter);*/
        ArrayAdapter<String> classadapter = new ArrayAdapter<>(Create_pdf.this, android.R.layout.simple_list_item_1, classdata);
        classs.setAdapter(classadapter);

        genreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String d = dates.getSelectedItem().toString();
                String c = classs.getSelectedItem().toString();
                Log.e("database","PDF called");
                File path = getExternalFilesDir("Studence_pdf");
                Log.e("database",""+ path.toString());
                createPDF(d,c);
            }
        });
    }
    public void createPDF(String date,String classs)
    {
        com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
        Log.e("database","PDF call");
        try {
            File dir = new File(String.valueOf(getExternalFilesDir("Studence_pdf")));
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, classs+"_"+date+".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Font paraFont = new Font(Font.FontFamily.TIMES_ROMAN,25.0f);
            String text = classs+" "+ date;
            Paragraph p1 = new Paragraph(text);
            p1.setFont(paraFont);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            //add paragraph to document
            doc.add(p1);
            Cursor data = sqLiteManager.getpresentdata(classs,date);
            String datasheet=null;
            int sno=1;
            while(data.moveToNext())
            {
                Log.e("database"," "+ data.getString(5));
                Log.e("database"," "+ data.getString(3));
                Log.e("database"," "+ data.getString(4));

                String t =String.valueOf(sno)+" "+ data.getString(5)+"  "+data.getString(4)+"  "+data.getString(3)+"\n";
                if(datasheet==null)
                {
                    datasheet = t;
                }
                else
                {
                    datasheet += t;
                }
                sno++;
            }
            Paragraph p2 = new Paragraph(datasheet);
            p2.setFont(paraFont);
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            //add paragraph to document
            doc.add(p2);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }
    }
}

