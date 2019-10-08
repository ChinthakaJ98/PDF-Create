package com.blog.example.CreatePDF.Controller;

import com.blog.example.CreatePDF.dto.Details;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

@RequestMapping("/testapp")
@Controller
public class PDFCreator {

    @Autowired
    SpringTemplateEngine templateEngine;

    @RequestMapping("/getdetails")
    public @ResponseBody Details savePDF(@RequestBody Details details) throws IOException, DocumentException {
        Context context = new Context();

        context.setVariable("name",details.getName());
        context.setVariable("age",details.getAge());
        context.setVariable("country",details.getCountry());

        String htmlContentToRender = templateEngine.process("pdf-template", context);
        String xHtml = xhtmlConvert(htmlContentToRender);

        ITextRenderer renderer = new ITextRenderer();

        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "main", "resources","templates")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        OutputStream outputStream = new FileOutputStream("src//test.pdf");
        renderer.createPDF(outputStream);
        outputStream.close();

        return details;

    }

    private String xhtmlConvert(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setOutputEncoding("UTF-8");
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString("UTF-8");
    }
}
