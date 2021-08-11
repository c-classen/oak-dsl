import com.github.cclassen.oakdsl.builder.OpenApiBuilder
import org.junit.jupiter.api.Test
import java.io.StringWriter

class EndpointTest {

    @Test
    fun methodsTest() {
        val writer = StringWriter()
        OpenApiBuilder.write(writer) {
            get("getter") / "test" def {}
            get<TestObj>("getterWithResponse") def {}
        }
    }

    interface TestObj {
        val text: String
    }
}