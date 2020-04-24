package au.com.abstractcs.owasp.services;

import au.com.abstractcs.owasp.response.Sanitize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

//import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SanitizerControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldIdentifyAndRejectBasicHTML() throws Exception {
        final String src = "<BODY>";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody().getResponse());
    }

    @Test
    public void shouldAllowAndReturnOriginalText() throws Exception {
        final String src = "a non problematic string";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(src, response.getBody().getResponse());
    }

    @Test
    public void shouldAllowAndReturnOriginalTextAndStripHTML() throws Exception {
        final String src = "a string with <p>html";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("a string with html", response.getBody().getResponse());
    }
}