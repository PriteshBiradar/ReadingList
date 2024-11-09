package readinglist;

import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReadingListApplication.class)
@WebAppConfiguration
public class MockMvcWebTests {
    @Autowired
    private WebApplicationContext webContext;

    private MockMvc mockMvc;



    @Before
    public void setupMockMvc(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .build();
    }
    @Test
    public void homePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/readingList"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("readingList"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.model().attribute("books",
                        Matchers.is(Matchers.empty())));
    }


    @Test
    public void postBook() throws Exception {
        String reader = "pritesh"; // Set the reader name you want to test

        // Perform the POST request to add a new book, including the reader in the URL
        mockMvc.perform(post("/" + reader) // Include the reader in the URL
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Book Title")  // Ensure consistent parameter names
                        .param("author", "Book Author")
                        .param("isbn", "123456789")
                        .param("description", "Description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/" + reader)); // Redirects to the same reader path

        // Set up expected book details
        Book expectedBook = new Book();
        expectedBook.setId(1L);  // Ensure this matches your application's logic
        expectedBook.setReader(reader);  // This should match the reader used in the POST request
        expectedBook.setTitle("Book Title");
        expectedBook.setAuthor("Book Author");
        expectedBook.setIsbn("123456789");
        expectedBook.setDescription("Description");

        // Perform a GET request to retrieve the list of books for the specific reader
        mockMvc.perform(get("/" + reader))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));
    }
}


