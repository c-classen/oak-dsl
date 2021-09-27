package io.github.cclassen.oakdsl.component

import io.github.cclassen.oakdsl.annotation.Description
import io.github.cclassen.oakdsl.model.schema.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class TypeResolver {

    private val schemas: MutableMap<String, Pair<KClass<*>?, Schema>> = mutableMapOf()

    val customClassResolvers: MutableMap<Class<*>, (type: KClass<*>) -> Schema> = mutableMapOf()

    fun resolveType(type: KType): Schema {
        resolveList(type)?.let { return it }
        val classifier = type.classifier as? KClass<*>
            ?: throw RuntimeException("Unknown type $type")
        return resolveKClass(classifier)
    }

    fun resolveKClass(type: KClass<*>): Schema {
        customClassResolvers[type.java]?.let {
            return it(type)
        }
        resolvePrimitive(type)?.let { return it }
        resolveEnum(type)?.let { return it }
        resolveInterface(type)?.let { return it }
        throw RuntimeException("Type could not be handled: $type")
    }

    private fun resolvePrimitive(type: KClass<*>): Schema? {
        return when (type.qualifiedName) {
            Boolean::class.qualifiedName -> PrimitiveSchema("boolean")
            Byte::class.qualifiedName -> PrimitiveSchema("integer", "int8")
            Short::class.qualifiedName -> PrimitiveSchema("integer", "int16")
            Integer::class.qualifiedName -> PrimitiveSchema("integer")
            Long::class.qualifiedName -> PrimitiveSchema("integer", "int64")
            Float::class.qualifiedName -> PrimitiveSchema("number")
            Double::class.qualifiedName -> PrimitiveSchema("number", "f64")
            String::class.qualifiedName -> PrimitiveSchema("string")
            ByteArray::class.qualifiedName -> PrimitiveSchema("string", "binary")
            Base64::class.qualifiedName -> PrimitiveSchema("string", "byte")
            UUID::class.qualifiedName -> PrimitiveSchema("string", "uuid")
            else -> null
        }
    }

    private fun resolveList(type: KType): Schema? {
        val classifier = type.classifier as? KClass<*>
            ?: return null
        if (!List::class.java.isAssignableFrom(classifier.java)) {
            return null
        }
        val listParameter = type.arguments[0]
        val itemType = listParameter.type
            ?: throw RuntimeException("List item type is null. List type: $type")
        val schema = try {
            resolveType(itemType)
        } catch (ex: Exception) {
            throw RuntimeException("Failed to resolve list $type", ex)
        }
        return ArraySchema(schema)
    }

    private fun resolveEnum(type: KClass<*>): Schema? {
        if (!type.java.isEnum) {
            return null
        }
        val name = type.simpleName
            ?: throw RuntimeException("Type has no simple name: $type")
        schemas[name]?.let {
            if (it.first != type) {
                throw RuntimeException("Different types with same name \"$name\" required: ${it.first}, $type")
            }
            return SchemaRef(name)
        }
        val enumSchema = PrimitiveSchema(
            type = "string",
            enum = type.java.enumConstants.map { it.toString() }
        )
        schemas[name] = type to enumSchema
        return SchemaRef(name)
    }

    private fun resolveInterface(type: KClass<*>): Schema? {
        if (!type.java.isInterface) {
            return null
        }
        val name = type.simpleName
            ?: throw RuntimeException("Type has no simple name: $type")
        existingType(name, type)?.let { return it }
        val objectSchema = createObjectSchema(name, type)
        for (property in type.declaredMemberProperties.sortedBy { it.name }) {
            try {
                val propertyType = property.returnType
                val schema = resolveType(propertyType)
                val required = !property.returnType.isMarkedNullable
                val descriptionAnnotation = property.annotations.mapNotNull { it as? Description }.firstOrNull()
                val description = descriptionAnnotation?.value
                objectSchema.properties.add(ObjectProperty(property.name, schema, required, description))
            } catch (ex: Exception) {
                throw RuntimeException("Failed to resolve type of property \"$name\" of interface \"$type\"", ex)
            }
        }
        return SchemaRef(name)
    }

    private fun createObjectSchema(name: String, type: KClass<*>): ObjectSchema {
        val objectSchema = ObjectSchema()
        val superTypes = type.supertypes.filter { it.classifier != Any::class }
        if (superTypes.isEmpty()) {
            schemas[name] = type to objectSchema
        } else {
            val items = superTypes.map { resolveType(it) }.toMutableList()
            items.add(objectSchema)
            val allOf = AllOfSchema(items)
            schemas[name] = type to allOf
        }
        return objectSchema
    }

    private fun existingType(name: String, type: KClass<*>): SchemaRef? {
        schemas[name]?.let {
            if (it.first != type) {
                throw RuntimeException("Different types with same name \"$name\" required: ${it.first}, $type")
            }
            return SchemaRef(name)
        }
        return null
    }

    private fun resolveClass(type: KClass<*>): Schema? {
        val name = type.simpleName
            ?: throw RuntimeException("Type has no simple name: $type")
        existingType(name, type)?.let { return it }
        val constructor = type.primaryConstructor
            ?: return null
        val objectSchema = createObjectSchema(name, type)
        for (parameter in constructor.parameters.filter { it.kind == KParameter.Kind.VALUE }) {
            val propertyType = parameter.type
            val schema = resolveType(propertyType)
            val required = !propertyType.isMarkedNullable
            val descriptionAnnotation = parameter.annotations.mapNotNull { it as? Description }.firstOrNull()
            val description = descriptionAnnotation?.value
            objectSchema.properties.add(ObjectProperty(parameter.name!!, schema, required, description))
        }
        return SchemaRef(name)
    }

    fun addAllSchemasTo(schemas: MutableMap<String, Schema>) {
        val existingKey = this.schemas.keys.find { schemas.containsKey(it) }
        if (existingKey != null) {
            throw RuntimeException("Cannot add autogenerated schemas. Name $existingKey is already in use")
        }
        schemas.putAll(this.schemas.mapValues { it.value.second })
    }

    fun type(ref: String, schema: Schema): Schema {
        if (schemas.containsKey(ref)) {
            throw RuntimeException("A type named \"$ref\" is already registered: ${schemas[ref]}")
        }
        schemas[ref] = null to schema
        return SchemaRef(ref)
    }
}