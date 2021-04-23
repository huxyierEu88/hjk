package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;

public class QueryLucene {

    @Test
    public void testQuery() throws IOException, ParseException {
        // 创建索引库对象
        Directory directory = FSDirectory.open(new File("C:\\tmp\\indexDir"));
        // 创建索引读取工具
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建索引搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 创建查询解析器对象，1-查询字段，2-分词器
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"title", "id"}, new IKAnalyzer());
        // 获取查询对象
        Query query = parser.parse("谷歌地图");
        // 执行查询，获取查询结果集
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("一共命中：" + topDocs.totalHits + "条数据");
        // 获取得分文档数组. 1-文档的得分 2-文档的编号
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的编号：" + scoreDoc.doc);
            System.out.println("该文档的分数：" + scoreDoc.score);
            Document document = indexReader.document(scoreDoc.doc);
            System.out.println("文档的id：" + document.get("id"));
            System.out.println("文档的title：" + document.get("title"));
        }
    }
}
