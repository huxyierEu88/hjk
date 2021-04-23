package cn.itcast.lucene;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateLucene {

    /**
     * 测试创建索引
     */
    @Test
    public void testCreate() throws IOException {

        // 创建集合数据
        List<Document> docs = new ArrayList<>();
        // 创建文档对象
        Document document1 = new Document();
        // 添加字段信息
        // StringField:会被创建索引，但是不会被分词
        // intField doubleFiled FloatField等这些都不会被分词，但是会创建索引
        // 字段是否保存取决于搜索列表要不要展示这个字段
        // 字段是否分词取决于：1.你要以他进行查询 2.字段比较复杂
        document1.add(new StringField("id", "1", Field.Store.YES));
        // TextField:不仅会创建索引，还会分词
        Field field = new TextField("title", "谷歌地图之父跳槽FaceBook，加入传智播客，简直碉堡了呀，蓝瘦香菇", Field.Store.YES);
        field.setBoost(1000);
        document1.add(field);
        document1.add(new LongField("price", 100000l, Field.Store.YES));
        Document document2 = new Document();
        document2.add(new StringField("id", "2", Field.Store.YES));
        document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Field.Store.YES));
        document2.add(new LongField("price", 200000l, Field.Store.YES));
        Document document3 = new Document();
        document3.add(new StringField("id", "3", Field.Store.YES));
        document3.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Field.Store.YES));
        document3.add(new LongField("price", 300000l, Field.Store.YES));
        Document document4 = new Document();
        document4.add(new StringField("id", "4", Field.Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES));
        document4.add(new LongField("price", 400000l, Field.Store.YES));
        Document document5 = new Document();
        document5.add(new StringField("id", "5", Field.Store.YES));
        document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Field.Store.YES));
        document5.add(new LongField("price", 500000l, Field.Store.YES));

        docs.add(document1);
        docs.add(document2);
        docs.add(document3);
        docs.add(document4);
        docs.add(document5);

        // 创建索引库对象。FSDirectory:硬盘。RAMDirectory：内存
        Directory directory = FSDirectory.open(new File("C:\\tmp\\indexDir"));
        // 创建索引写入器的配置对象. 1-lucene版本， 2-分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 创建文档写入工具。不仅可以创建索引，更新 删除。
        IndexWriter indexWriter = new IndexWriter(directory, config);

        // 执行写入操作
        indexWriter.addDocuments(docs);
        // 提交
        indexWriter.commit();
        // 关闭
        indexWriter.close();
    }
}
