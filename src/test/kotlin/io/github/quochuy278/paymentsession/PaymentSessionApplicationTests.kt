package io.github.quochuy278.paymentsession

import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest
@ActiveProfiles("test")
class PaymentSessionApplicationTests {

	@MockitoBean
	lateinit var dslContext: DSLContext

	@Test
	fun contextLoads() {
	}

}
