package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class SuperTest {

    @Test
    public void testHl() throws IOException, ParseException, InvalidTokenOffsetsException {
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

        // 初始化高亮工具
        Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
        Scorer scorer = new QueryScorer(query);
        // 创建高亮对象
        Highlighter highlighter = new Highlighter(formatter, scorer);

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
            String hltitle = highlighter.getBestFragment(new IKAnalyzer(), "title", document.get("title"));
            System.out.println("文档的title：" + hltitle);
        }
    }

    @Test
    public void testSort() throws IOException, ParseException, InvalidTokenOffsetsException {
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

        Sort sort = new Sort();
        sort.setSort(new SortField("price", SortField.Type.LONG, true));
        // 执行查询，获取查询结果集
        TopDocs topDocs = indexSearcher.search(query, 10, sort);
        System.out.println("一共命中：" + topDocs.totalHits + "条数据");
        // 获取得分文档数组. 1-文档的得分 2-文档的编号
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的编号：" + scoreDoc.doc);
            System.out.println("该文档的分数：" + scoreDoc.score);
            Document document = indexReader.document(scoreDoc.doc);
            System.out.println("文档的id：" + document.get("id"));
            System.out.println("文档的title：" + document.get("title"));
            System.out.println("文档的price：" + document.get("price"));
        }
    }

    /**
     * 物理分页：limit
     * 逻辑分页：
     * @throws IOException
     * @throws ParseException
     * @throws InvalidTokenOffsetsException
     */
    @Test
    public void testPage() throws IOException, ParseException, InvalidTokenOffsetsException {
        // 初始化模拟数据
        Integer pageNum = 2;
        Integer pageSize = 2;
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

        Sort sort = new Sort();
        sort.setSort(new SortField("price", SortField.Type.LONG, true));
        // 执行查询，获取查询结果集
        TopDocs topDocs = indexSearcher.search(query, pageNum * pageSize, sort);
        System.out.println("一共命中：" + topDocs.totalHits + "条数据");
        // 获取得分文档数组. 1-文档的得分 2-文档的编号
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (int i = (pageNum -1)*pageSize; i< pageNum * pageSize; i++) {
            System.out.println("文档的编号：" + scoreDocs[i].doc);
            System.out.println("该文档的分数：" + scoreDocs[i].score);
            Document document = indexReader.document(scoreDocs[i].doc);
            System.out.println("文档的id：" + document.get("id"));
            System.out.println("文档的title：" + document.get("title"));
            System.out.println("文档的price：" + document.get("price"));
        }
    }
}
