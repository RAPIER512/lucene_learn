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
	 * ��������
	 */
	public void index(){
		IndexWriter writer = null;
		try {
			//1����Directory
			//Directory directory = new RAMDirectory();//�������ڴ���
			Directory  directory = FSDirectory.open(new File("F:/lucene/index01"));//������Ӳ����
			//2����IndexWriter
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
			writer= new IndexWriter(directory, iwc);
			//3����Document����
			Document doc =null;
			//4ΪDocument����Field
			File f = new File("F:/lucene/example");
			for(File file:f.listFiles() ){
				doc = new Document();
				doc.add(new Field("content",new FileReader(file)));
				doc.add(new Field("filename",file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				doc.add(new Field("path",file.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
				//5ͨ��IndexWriter�����ĵ���������
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
	 * ����
	 */
	public void searcher(){
		try {
			//1������ Directory
			Directory directory = FSDirectory.open(new File("F:/lucene/index01"));
			//2������IndexReader
			IndexReader reader = IndexReader.open(directory);
			//3������IndexReader����IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			//4������������Query
				//����parser ��ȷ��Ҫ�����ļ������ݣ��ڶ���������ʾ��������
			QueryParser parser = new QueryParser(Version.LUCENE_35,"content",new StandardAnalyzer(Version.LUCENE_35));
				//����Query,��ʾ������Ϊcontent�а���java ���ĵ�
			Query query = parser.parse("prompt");
				//ִ������
			//5������searcher�������ҷ���TopDocs
			TopDocs tds = searcher.search(query, 10);
			//6������TopDcos��ȡScoreDoc����
			ScoreDoc[] sds = tds.scoreDocs;
			for(ScoreDoc sd:sds){
				//7������Searcher��ScoreDoc�����ȡ�����Document����
				Document d = searcher.doc(sd.doc);
				//8������document�����ȡ��Ҫ��ֵ
				System.out.println(d.get("filename")+"["+d.get("path")+"]");
				//9���ر�reader
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