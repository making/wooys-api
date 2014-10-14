package wooys.api.item;

import jqiita.QiitaClient;
import jqiita.item.Item;
import jqiita.item.ItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooys.api.ResponseEntites;
import wooys.domain.model.UserModel;
import wooys.domain.service.item.ItemService;

import java.util.List;

@RestController
@RequestMapping("api/" + QiitaClient.API_VERSION + "/items")
public class ItemsRestController {
    @Autowired
    ItemService itemService;

    @RequestMapping(method = RequestMethod.GET)
    List<Item> list() {
        return itemService.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    ResponseEntity<?> get(@PathVariable String id) {
        return ResponseEntites.okIfPresent(itemService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Item> create(@RequestBody ItemRequest request) {
        UserModel sample = new UserModel();
        sample.setId("sample");
        sample.setProfileImageUrl("http://example.com");

        Item created = itemService.create(request, sample.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> delete(@PathVariable String id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
