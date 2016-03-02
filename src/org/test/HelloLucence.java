package org.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class HelloLucence {

	/**
	 * 建立索引
	 */
	public void index(){
		IndexWriter writer = null;
		try {
			//1创建Directory
			//Directory directory = new RAMDirectory();//建立在内存中
			Directory  directory = FSDirectory.open(new File("F:/lucene/index01"));//创建在硬盘上
			//2创建IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			writer= new IndexWriter(directory, iwc);
			//3创建Document对象
			Document doc =null;
			//4为Document添加Field
			File f = new File("F:/lucene/example");
			for(File file:f.listFiles() ){
				doc = new Document();
				doc.add(new Field("content",new FileReader(file)));
				doc.add(new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("path",file.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				//5通过IndexWriter添加文档到索引中
				writer.addDocument(doc);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer !=null)
				try {
					writer.close();
				} catch (CorruptIndexException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	/**
	 * 搜索
	 */
	public void searcher(){
		try {
			//1、创建 Directory
			Directory directory = FSDirectory.open(new File("F:/lucene/index01"));
			//2、创建IndexReader
			IndexReader reader = IndexReader.open(directory);
			//3、根据IndexReader创建IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			//4、创建搜索的Query
				//创建parser 来确定要搜索文件的内容，第二个参数表示搜索的域
			QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
				//创建Query,表示搜索域为content中包含java 的文档
			Query query = parser.parse("prompt");
				//执行搜索
			//5、根据searcher搜索并且返回TopDocs
			TopDocs tds = searcher.search(query, 10);
			//6、根据TopDcos获取ScoreDoc对象
			ScoreDoc[] sds = tds.scoreDocs;
			for(ScoreDoc sd:sds){
				//7、根据Searcher和ScoreDoc对象获取具体的Document对象
				Document d = searcher.doc(sd.doc);
				//8、根据document对象获取需要的值
				System.out.println(d.get("filename")+"["+d.get("path")+"]");
				//9、关闭reader
				reader.clone();
			}
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
