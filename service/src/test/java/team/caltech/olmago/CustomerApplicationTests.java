package team.caltech.olmago;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerApplicationTests {

	@Test
	void contextLoads() {
    assertThat(1).isEqualTo(1);
	}

}
