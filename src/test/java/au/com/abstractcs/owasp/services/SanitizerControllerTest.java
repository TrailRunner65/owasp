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
        final String src = "<body>";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody().getResponse());
        assertEquals(1, response.getBody().getRemovedItems().size());
        assertEquals("body", response.getBody().getRemovedItems().get(0));
    }

    @Test
    public void shouldAllowAndReturnOriginalText() throws Exception {
        final String src = "a non problematic string";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(src, response.getBody().getResponse());
        assertEquals(0, response.getBody().getRemovedItems().size());
    }

    @Test
    public void shouldAllowAndReturnOriginalTextAndStripHTML() throws Exception {
        final String src = "a string with <p>html";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("a string with html", response.getBody().getResponse());
        assertEquals(1, response.getBody().getRemovedItems().size());
        assertEquals("p", response.getBody().getRemovedItems().get(0));
    }

    @Test
    public void shouldAllowAndReturnOriginalTextAndOnlyClosingHTML() throws Exception {
        final String src = "a string with </p>html";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("a string with </p>html", response.getBody().getResponse());
        assertEquals(0, response.getBody().getRemovedItems().size());
    }

    @Test
    public void shouldStripMultipleHTML() throws Exception {
        final String src = "a <b>string</b> with <p>html in many places<span/>";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("a string with html in many places", response.getBody().getResponse());
        assertEquals(3, response.getBody().getRemovedItems().size());
        assertEquals("b", response.getBody().getRemovedItems().get(0));
        assertEquals("p", response.getBody().getRemovedItems().get(1));
        assertEquals("span", response.getBody().getRemovedItems().get(2));
    }

    @Test
    public void shouldStripCustomTags() throws Exception {
        final String src = "a <wibble>string with a custom tag";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("a string with a custom tag", response.getBody().getResponse());
        assertEquals(1, response.getBody().getRemovedItems().size());
        assertEquals("wibble", response.getBody().getRemovedItems().get(0));
    }

    @Test
    public void shouldStripAllTextBetweenStartAndEndTags() throws Exception {
        final String src = "here is<script>some javascript</script> code";

        String url = "http://localhost:" + port + "/sanitize?src=" + src;
        ResponseEntity<Sanitize> response = this.restTemplate.getForEntity(url, Sanitize.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("here is code", response.getBody().getResponse());
        assertEquals(1, response.getBody().getRemovedItems().size());
        assertEquals("script", response.getBody().getRemovedItems().get(0));
    }
}