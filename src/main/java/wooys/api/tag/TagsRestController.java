package wooys.api.tag;

import jqiita.QiitaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/" + QiitaClient.API_VERSION + "/tags")
public class TagsRestController {
}
