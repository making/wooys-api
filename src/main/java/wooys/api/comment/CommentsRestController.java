package wooys.api.comment;

import jqiita.QiitaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/" + QiitaClient.API_VERSION + "/comments")
public class CommentsRestController {
}
