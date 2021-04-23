package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SpecLucene {

    /**
     * 词条查询
     * 效率比较高
     * 字段是不可分词的，就可以使用它
     *
     * @throws IOException
     */
    @Test
    public void testTermQuery() throws IOException {
        // 参数：词条-最小粒度的分词
        Query query = new TermQuery(new Term("title", "地图"));
        search(query);
    }

    /**
     * 通配符查询
     * *：匹配任意多个
     * ？：匹配一个字符
     */
    @Test
    public void testWildcardQuery() throws IOException {
        // 创建通配符查询对象
        Query query = new WildcardQuery(new Term("title", "*歌*"));
        search(query);
    }

    /**
     * 模糊查询
     */
    @Test
    public void testFuzzyQuery() throws IOException {
        // 最大编辑距离：由错误的拼写到正确的拼写，需要修改的次数
        // 最大值：2
        Query query = new FuzzyQuery(new Term("title", "facabool"), 1);
        search(query);
    }

    /**
     * 数值范围查询
     *
     * @throws IOException
     */
    @Test
    public void testNumericRangeQuery() throws IOException {
        Query query = NumericRangeQuery.newLongRange("price", 200000l, 400000l, true, false);
        search(query);
    }

    /**
     * 组合查询
     * @throws IOException
     */
    @Test
    public void testBooleanQuery() throws IOException {
        BooleanQuery query = new BooleanQuery();
        Query query1 = NumericRangeQuery.newLongRange("price", 100000l, 400000l, true, false);
        Query query2 = NumericRangeQuery.newLongRange("price", 300000l, 500000l, true, false);
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.MUST_NOT);
        search(query);
    }

    public void search(Query query) throws IOException {
        // 创建索引库对象
        Directory directory = FSDirectory.open(new File("C:\\tmp\\indexDir"));
        // 创建索引读取工具
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建索引搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

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
            System.out.println("文档的price：" + document.get("price"));
        }
    }

}
