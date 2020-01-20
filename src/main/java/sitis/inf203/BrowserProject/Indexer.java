package sitis.inf203.BrowserProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

		static final String INDEX_PATH = "resource";
		public static void buildIndex() throws Exception
		{
			String csvFile = "C:/Users/gregl/Desktop/JavaProjects/BrowserProject/data/list.csv";
			BufferedReader br = null;
	        String line = "";

	           	
	           	IndexWriter writer = createWriter();
				List<Document> documents = new ArrayList();
				br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {
	               
	                String[]identify = line.split(";");
	                String num = identify[0];
	                String morphCIM = identify[1];
	                String topCIM = identify[2];
	                

	            
			Document document = createDocument(num, morphCIM, topCIM);
			documents.add(document);
	            }
	            System.out.println("Index crée");
			

			//Let's clean everything first
			writer.deleteAll();

			writer.addDocuments(documents);
			writer.commit();
			writer.close();
		}

		private static Document createDocument(String id, String morpho, String topo)
		{
			Document document = new Document();
			document.add(new StringField("id", id , Field.Store.YES));
			document.add(new TextField("morpho", morpho , Field.Store.YES));
			document.add(new TextField("topo", topo , Field.Store.YES));
		
			return document;
		}
		
		
		private static IndexWriter createWriter() throws IOException
		{
			FSDirectory dir = FSDirectory.open(Paths.get(INDEX_PATH));
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			IndexWriter writer = new IndexWriter(dir, config);
			return writer;
		}
	}
