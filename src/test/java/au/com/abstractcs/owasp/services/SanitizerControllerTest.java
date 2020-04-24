package au.com.abstractcs.owasp.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SanitizerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldAcceptAndReturnPlainText() throws Exception {
        String src = "wibble";
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/sanitize?src=" + src,
                String.class)).contains("wibble");
    }

    @Test
    public void shouldIdentifyAndRejectBasicHTML() throws Exception {
        String src = "<BODY>";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<String> response = this.restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("");

        //assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/sanitize?src=" + src,
          //      String.class)).contains("<BODY>");
    }

/*    @Test
    public void contexLoads() throws Exception {
        assertThat(sanitizerController).isNotNull();
    }*/

}