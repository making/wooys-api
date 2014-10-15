package wooys.api.item;

import jqiita.Qiita;
import jqiita.QiitaClient;
import jqiita.item.Item;
import jqiita.item.ItemRequest;
import jqiita.tag.TagRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import wooys.ApiServer;
import wooys.domain.model.ItemModel;
import wooys.domain.model.ItemTag;
import wooys.domain.model.TagModel;
import wooys.domain.model.UserModel;
import wooys.domain.repository.item.ItemRepository;
import wooys.domain.repository.tag.TagRepository;
import wooys.domain.repository.user.UserRepository;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApiServer.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0",
        "spring.datasource.url:jdbc:h2:mem:wooys-api;DB_CLOSE_ON_EXIT=FALSE"})
public class ItemRestControllerTest {
    @Value("${local.server.port}")
    int port;
    QiitaClient qiitaClient;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ItemRepository itemRepository;
    ItemModel itemModel1;
    ItemModel itemModel2;
    UserModel userModel1;
    TagModel tagModel1;
    OffsetDateTime now = OffsetDateTime.now();

    @Before
    public void setUp() throws Exception {
        qiitaClient = Qiita.given().log().all().host("http://localhost:" + port).client();

        // clearData
        List<ItemModel> items = itemRepository.findAll();
        items.forEach(i -> i.getTags().clear());
        itemRepository.save(items);
        itemRepository.flush();
        itemRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        itemRepository.flush();

        // test data
        userModel1 = new UserModel();
        userModel1.setId("making");
        userModel1.setProfileImageUrl("http://example.com");
        userRepository.save(Arrays.asList(userModel1));
        userRepository.flush();

        tagModel1 = new TagModel();
        tagModel1.setId("java");
        tagRepository.save(Arrays.asList(tagModel1));
        tagRepository.flush();

        itemModel1 = new ItemModel();
        itemModel1.setBody("* test data1");
        itemModel1.setTitle("Hello 1");
        itemModel1.setUser(userModel1);
        itemModel1.setTags(Arrays.asList(new ItemTag(tagModel1, null)));
        itemModel1.setCreatedAt(now);
        itemModel1.setUpdatedAt(now);

        itemModel2 = new ItemModel();
        itemModel2.setBody("* test data2");
        itemModel2.setTitle("Hello 2");
        itemModel2.setUser(userModel1);
        itemModel2.setTags(Arrays.asList(new ItemTag(tagModel1, null)));
        itemModel2.setCreatedAt(now.plusMinutes(1));
        itemModel2.setUpdatedAt(now.plusMinutes(1));
        itemRepository.save(Arrays.asList(itemModel1, itemModel2));
        itemRepository.flush();
    }

    @Test
    public void testList() throws Exception {
        List<Item> items = qiitaClient.items().list();
        assertThat(items, hasSize(2));

        assertThat(items.get(0), is(itemModel1.toItem()));
        assertThat(items.get(1), is(itemModel2.toItem()));
    }

    @Test
    public void testGet() throws Exception {
        Item item = qiitaClient.items().get(itemModel2.getId());
        assertThat(item, is(itemModel2.toItem()));
    }


    @Test
    public void testCreate() throws Exception {
        ItemRequest request = new ItemRequest(
                "new item from JQiita",
                "* hello world!",
                Arrays.asList(new TagRequest("hoge")));
        Item item = qiitaClient.items().create(request);
        assertThat(item.getBody(), is(request.getBody()));
        assertThat(item.getTitle(), is(request.getTitle()));
        assertThat(item.getUser(), is(notNullValue()));
        assertThat(item.getTags(), hasSize(1));
        assertThat(item.getTags().get(0).getName(), is(request.getTags().get(0).getName()));
        assertThat(item.getCreatedAt(), is(notNullValue()));
        assertThat(item.getUpdatedAt(), is(notNullValue()));
        assertThat(item.getId(), is(notNullValue()));
        assertThat(item.getId().length(), is(20));

        List<Item> items = qiitaClient.items().list();
        assertThat(items, hasSize(3));
        assertThat(items.get(2), is(item));
    }

    @Test
    public void testCreateTwice() throws Exception {
        Item item1;
        Item item2;
        {
            ItemRequest request = new ItemRequest(
                    "new item from JQiita",
                    "* hello world!",
                    Arrays.asList(new TagRequest("hoge")));
            Item item = qiitaClient.items().create(request);
            assertThat(item.getBody(), is(request.getBody()));
            assertThat(item.getTitle(), is(request.getTitle()));
            assertThat(item.getUser(), is(notNullValue()));
            assertThat(item.getTags(), hasSize(1));
            assertThat(item.getTags().get(0).getName(), is(request.getTags().get(0).getName()));
            assertThat(item.getCreatedAt(), is(notNullValue()));
            assertThat(item.getUpdatedAt(), is(notNullValue()));
            assertThat(item.getId(), is(notNullValue()));
            assertThat(item.getId().length(), is(20));
            item1 = item;
        }
        {
            ItemRequest request = new ItemRequest(
                    "Hello!",
                    "* This is test!",
                    Arrays.asList(new TagRequest("hoge"), new TagRequest("foo"), new TagRequest("bar")));
            Item item = qiitaClient.items().create(request);
            assertThat(item.getBody(), is(request.getBody()));
            assertThat(item.getTitle(), is(request.getTitle()));
            assertThat(item.getUser(), is(notNullValue()));
            assertThat(item.getTags(), hasSize(3));
            assertThat(item.getTags().get(0).getName(), is(request.getTags().get(0).getName()));
            assertThat(item.getTags().get(1).getName(), is(request.getTags().get(1).getName()));
            assertThat(item.getTags().get(2).getName(), is(request.getTags().get(2).getName()));
            assertThat(item.getCreatedAt(), is(notNullValue()));
            assertThat(item.getUpdatedAt(), is(notNullValue()));
            assertThat(item.getId(), is(notNullValue()));
            assertThat(item.getId().length(), is(20));
            item2 = item;
        }

        List<Item> items = qiitaClient.items().list();
        assertThat(items, hasSize(4));
        assertThat(items.get(2), is(item1));
        assertThat(items.get(3), is(item2));
    }

    @Test
    public void testDelete() throws Exception {
        qiitaClient.items().delete(itemModel2.getId());

        List<Item> items = qiitaClient.items().list();
        assertThat(items, hasSize(1));
        assertThat(items.get(0), is(itemModel1.toItem()));
    }
}