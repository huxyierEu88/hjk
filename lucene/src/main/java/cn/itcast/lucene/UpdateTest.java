package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class UpdateTest {

    /**
     * 测试更新
     * 更新的本质：先删除，再新增
     * 一定要保证更新的条件要唯一。
     */
    @Test
    public void testUpdate() throws IOException {

        // 创建索引目录
        Directory directory = FSDirectory.open(new File("C:\\tmp\\indexDir"));

        // 创建配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        // 索引库写入器
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 创建文档对象
        Document document = new Document();
        document.add(new StringField("id", "10", Field.Store.YES));
        document.add(new TextField("title", "传智播客碉堡了，学完了我要迎娶白富美", Field.Store.YES));

        // 更新数据
        indexWriter.updateDocument(new Term("id", "1"), document);

        indexWriter.commit();
        indexWriter.close();
    }
}
