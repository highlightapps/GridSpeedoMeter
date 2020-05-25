package com.jasper.pdfwriter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class GenerateReportService {

	private static final Logger logger = LoggerFactory.getLogger(GenerateReportService.class);

	public static void main(String[] args) {
		generatePdfReport();
	}

	// This method generates a PDF report
	public static void generatePdfReport() {
		try {
			File initialFile = new File("src/main/resources/data.jrxml");
			InputStream jrxmlInput = new FileInputStream(initialFile);
			JasperDesign design = JRXmlLoader.load(jrxmlInput);
			JasperReport jasperReport = JasperCompileManager.compileReport(design);
			logger.info("Report compiled");
			String data = readDataFromFile();
			logger.info("data = " + data);
			// It is possible to generate Jasper reports from a JSON source.
			JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(data.getBytes()));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), jsonDataSource);
			logger.info("JasperPrint" + jasperPrint);

			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();

			FileOutputStream fout1 = new FileOutputStream("src/main/resources/data1.pdf");
			pdfReportStream.writeTo(fout1);
			pdfReportStream.flush();
			pdfReportStream.close();
		} catch (Exception e) {
			logger.info("Error: ", e);
		}
	}

	private static String readDataFromFile() throws Exception {
		InputStream is = new FileInputStream("src/main/resources/data.json");
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));

		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();

		while (line != null) {
			sb.append(line).append("\n");
			line = buf.readLine();
		}

		return sb.toString();
	}

	private static String invokeService() {
		String url = "http://e1255fbaf8cb8.cloud.wavemakeronline.com/RestJasper/services/hrdb/Department?sort=location&r=json";
		String response = "";

		try {
			HttpClient httpClient = new HttpClient();
			HttpMethod httpMethod = new GetMethod(url);
			logger.info(" Invoking service : " + url);
			httpClient.executeMethod(httpMethod);
			response = httpMethod.getResponseBodyAsString();
		} catch (IOException e) {
			response = "Error encountered while invoking service at url: " + url + "Error message: " + e.toString();
			logger.error(response);
			return response;
		}
		logger.info(" Rest response : " + response);
		return response;
	}

}
