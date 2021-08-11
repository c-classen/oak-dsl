package com.github.cclassen.oakdsl.type

import com.github.cclassen.oakdsl.component.ComponentResolver
import com.github.cclassen.oakdsl.model.schema.ArraySchema
import com.github.cclassen.oakdsl.model.schema.SchemaRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

class ComponentResolverTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun resolveLists() {
        val typeResolver = ComponentResolver()
        val schema = typeResolver.resolveType(typeOf<List<Recursive>>())
        assertThat(schema).isExactlyInstanceOf(ArraySchema::class.java)
        val arraySchema = schema as ArraySchema
        assertThat(arraySchema.items).isExactlyInstanceOf(SchemaRef::class.java)
        val schemaRef = arraySchema.items as SchemaRef
        assertThat(schemaRef.ref).isEqualTo("#/components/schemas/Recursive")
    }

    interface Recursive {
        val items: List<Recursive>
    }
}