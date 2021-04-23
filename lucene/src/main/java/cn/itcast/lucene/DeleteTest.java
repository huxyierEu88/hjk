package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class DeleteTest {

    /**
     * 测试删除
     */
    @Test
    public void testDelete() throws IOException, ParseException {

        // 创建索引目录
        Directory directory = FSDirectory.open(new File("C:\\tmp\\indexDir"));

        // 创建配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 索引库写入器
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 创建查询解析器对象，1-查询字段，2-分词器
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"title", "id"}, new IKAnalyzer());
        // 获取查询对象
        Query query = parser.parse("2");

        // 更新数据
        indexWriter.deleteDocuments(query);

        indexWriter.commit();
        indexWriter.close();
    }
}
