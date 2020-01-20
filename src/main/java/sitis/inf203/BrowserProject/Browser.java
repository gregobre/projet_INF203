package sitis.inf203.BrowserProject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Browser {

	public static final String INDEX_PATH = "resource";
	public static void main(String[] args) throws Exception {

		System.out.println("****************************************");
		Scanner scan = new Scanner(System.in);
		System.out.println("Appuyez sur une touche pour lancer l'indexation");
		String index = scan.nextLine();
		if (index.equals("i")) {
			System.out.println("****************************************");
			Indexer.buildIndex();
			String str = "";
			IndexSearcher searcher = createSearcher();
			while(!str.equalsIgnoreCase("q")) {
				System.out.println("****************************************");
				Scanner sc = new Scanner(System.in);
				System.out.println("Saisissez votre requête (syntaxe = morpho&topo) (q=quitter) :");
				str = sc.nextLine();
				if(str.equalsIgnoreCase("q")) {
					System.out.print("A bientôt !");
					break;
				}

				TopDocs foundLines = searchBy(str, searcher);

				System.out.println("Résultats pour '"+ str + "' : " + foundLines.totalHits);

				for (ScoreDoc sd : foundLines.scoreDocs) 
				{
					Document d = searcher.doc(sd.doc);
					System.out.println(String.format(d.get("id")+" : "+ d.get("morpho") + " localisé au niveau de : " + d.get("topo")));

				}
			}
		}

	}

	private static IndexSearcher createSearcher() throws IOException 
	{
		Directory dir = FSDirectory.open(Paths.get(INDEX_PATH));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}


	private static TopDocs searchBy(String query, IndexSearcher searcher) throws Exception
	{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		String [] line = query.split("&");
		String morpho = line[0];
		String topo = line[1];
		String [] fields = new String[] {"morpho", "topo"};
		MultiFieldQueryParser mqp = new MultiFieldQueryParser(fields, analyzer);
		Query topQuery = mqp.parse(new String[] {morpho, topo} , fields, analyzer);
		TopDocs hits = searcher.search(topQuery, 10);
		return hits;
	}

}

