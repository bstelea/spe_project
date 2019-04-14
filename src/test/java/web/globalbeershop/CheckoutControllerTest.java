package web.globalbeershop;

import java.io.File;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import web.globalbeershop.service.BraintreeGatewayService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = {GlobalbeershopApplication.class}
)
@WebAppConfiguration
public class CheckoutControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public CheckoutControllerTest() {
    }

    @BeforeClass
    public static void setupConfig() {
        File configFile = new File("config.properties");

        try {
            if (configFile.exists() && !configFile.isDirectory()) {
                GlobalbeershopApplication.gateway = BraintreeGatewayService.fromConfigFile(configFile);
            } else {
                GlobalbeershopApplication.gateway = BraintreeGatewayService.fromConfigMapping(System.getenv());
            }
        } catch (NullPointerException var2) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
        }

    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void checkoutReturnsOK() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/checkout", new Object[0])).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void rendersNewView() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/checkout", new Object[0])).andExpect(MockMvcResultMatchers.view().name("checkout")).andExpect(MockMvcResultMatchers.model().hasNoErrors()).andExpect(MockMvcResultMatchers.model().attributeExists(new String[]{"clientToken"}));
    }

    @Test
    public void rendersErrorsOnTransactionFailure() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/checkout", new Object[0]).param("payment_method_nonce", new String[]{"fake-valid-nonce"}).param("amount", new String[]{"2000.00"})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void rendersErrorsOnInvalidAmount() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/checkout", new Object[0]).param("payment_method_nonce", new String[]{"fake-valid-nonce"}).param("amount", new String[]{"-1.00"})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/checkout", new Object[0]).param("payment_method_nonce", new String[]{"fake-valid-nonce"}).param("amount", new String[]{"not_a_valid_amount"})).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void redirectsRootToNew() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/", new Object[0])).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
